package tt2.int221.backend.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.dto.SaleItemImageRequestDTO;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.entities.SaleItemImage;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.SaleItemImageRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class SaleItemImageService {
    @Autowired
    private SaleItemImageRepository saleItemImageRepository;
    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private FileService fileService;

    public List<SaleItemImage> getImagesBySaleItem(Integer saleItemId) {
        return saleItemImageRepository.findBySaleItemIdOrderByImageViewOrderAsc(saleItemId);
    }

    public void uploadImage(SaleItem saleItem, List<MultipartFile> files) throws IOException {

        if (files.size() > 5) {
            throw new RuntimeException("File is too large");
        }

        SaleItem createdSaleItem = saleItemRepository.findById(saleItem.getId())
                .orElseThrow(() -> new NotfoundException("Sale item not found"));

        for (int i = 0; i < files.size(); i++) {
            int viewOrder = i + 1;

            String customFileName = fileService.buildCustomFileName(
                    createdSaleItem.getId(),
                    viewOrder,
                    files.get(i)
            );

            // Save file physically
            String savedFile = fileService.saveFile(files.get(i), customFileName);

            // Save DB
            SaleItemImage saleItemImage = new SaleItemImage();
            saleItemImage.setSaleItem(createdSaleItem);
            saleItemImage.setImageViewOrder(viewOrder);
            saleItemImage.setFileName(savedFile);

            createdSaleItem.getSaleItemImages().add(saleItemImage);
            saleItemImageRepository.save(saleItemImage);
        }
    }

    public void deleteImage(Integer saleItemId, String fileName) {
        if (!saleItemImageRepository.existsBySaleItemId(saleItemId)) {
            throw new NotfoundException("Not found images with sale item id: " + saleItemId);
        }
        List<SaleItemImage> images = getImagesBySaleItem(saleItemId);

        SaleItemImage targetImage = images.stream()
                .filter(img -> img.getFileName().equals(fileName))
                .findFirst()
                .orElseThrow(() -> new NotfoundException("Image not found with file name: " + fileName));

        int deleteOrder = targetImage.getImageViewOrder();

        saleItemImageRepository.delete(targetImage);
        fileService.removeFile(targetImage.getFileName());

        for (var image : images) {
            if (image.getImageViewOrder() > deleteOrder) {
                image.setImageViewOrder(image.getImageViewOrder() - 1);
                saleItemImageRepository.save(image);
            }
        }
    }

    public void deleteImages(Integer saleItemId) {
        List<SaleItemImage> images = getImagesBySaleItem(saleItemId);

        for (var image : images) {
            fileService.removeFile(image.getFileName());
        }

        saleItemImageRepository.deleteById(saleItemId);
    }

    public void updateSaleItemImages(SaleItem editedSaleItem,
                                     List<SaleItemImageRequestDTO> imagesInfo) throws IOException {
        if (imagesInfo == null || imagesInfo.isEmpty()) {
            return;
        }

        List<SaleItemImage> existingImages = getImagesBySaleItem(editedSaleItem.getId());

        Set<Integer> requestedOrders = imagesInfo.stream()
                .map(SaleItemImageRequestDTO::getOrder)
                .collect(Collectors.toSet());

        // Remove existing image that not on longer in request
        for (SaleItemImage existing : new ArrayList<>(existingImages)) {
            if (!requestedOrders.contains(existing.getImageViewOrder())) {
                fileService.removeFile(existing.getFileName());
                saleItemImageRepository.delete(existing);
            }
        }

        for (SaleItemImageRequestDTO imageInfo : imagesInfo) {
            SaleItemImage imageEntity = existingImages.stream()
                    .filter(img -> Objects.equals(img.getFileName(), imageInfo.getFileName())
                            || Objects.equals(img.getImageViewOrder(), imageInfo.getOrder()))
                    .findFirst()
                    .orElse(null);

            if (imageEntity == null) {
                if (existingImages.size() >= 4) {
                    throw new IllegalStateException("Cannot add more than 4 images to a sale item.");
                }
                imageEntity = new SaleItemImage();
                imageEntity.setSaleItem(editedSaleItem);
            }

            // Always update the order
            if (imageInfo.getOrder() == null) {
                throw new IllegalArgumentException("Image order must not be null.");
            }
            imageEntity.setImageViewOrder(imageInfo.getOrder());

            // User send new image
            if (imageInfo.getImageFile() != null && !imageInfo.getImageFile().isEmpty()) {

                if (imageEntity.getFileName() != null) {
                    fileService.removeFile(imageEntity.getFileName());
                }

                String customFileName = fileService.buildCustomFileName(
                        editedSaleItem.getId(),
                        imageInfo.getOrder(),
                        imageInfo.getImageFile()
                );

                String newFileName = fileService.updateImage(
                        imageInfo.getFileName(),
                        imageInfo.getImageFile(),
                        customFileName
                );
                imageEntity.setFileName(newFileName);

                // User only reorders (no new file)
            } else if (imageEntity.getFileName() == null && imageInfo.getFileName() != null) {
                imageEntity.setFileName(imageInfo.getFileName());
            }

            saleItemImageRepository.save(imageEntity);
        }
    }

    public Resource loadFile(String fileName) throws IOException {
        return fileService.loadFile(fileName);
    }
}

