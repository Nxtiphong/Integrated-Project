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
import java.util.List;
import java.util.Objects;

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
            fileService.saveFile(files.get(i), files.get(i).getOriginalFilename(), createdSaleItem.getId());

            // Save DB
            SaleItemImage saleItemImage = new SaleItemImage();
            saleItemImage.setSaleItem(createdSaleItem);
            saleItemImage.setImageViewOrder(viewOrder);
            saleItemImage.setActualFileName(files.get(i).getOriginalFilename());
            saleItemImage.setStatus("ONLINE");
            saleItemImage.setFileName(customFileName);

            createdSaleItem.getSaleItemImages().add(saleItemImage);
            saleItemImageRepository.save(saleItemImage);
        }
    }

    public void deleteImages(Integer saleItemId) {
        List<SaleItemImage> images = getImagesBySaleItem(saleItemId);

        for (var image : images) {
            fileService.removeFile(image.getActualFileName(), saleItemId);
        }

        saleItemImageRepository.deleteById(saleItemId);
    }

    public void updateSaleItemImages(SaleItem editedSaleItem,
                                     List<SaleItemImageRequestDTO> imagesInfo) throws IOException {
        if (imagesInfo == null || imagesInfo.isEmpty()) {
            List<SaleItemImage> existingImages = getImagesBySaleItem(editedSaleItem.getId());
            for (SaleItemImage saleItemImage : existingImages) {
                fileService.removeFile(saleItemImage.getFileName(), editedSaleItem.getId());
                saleItemImageRepository.delete(saleItemImage);
            }
            return;
        }

        List<SaleItemImage> existingImages = getImagesBySaleItem(editedSaleItem.getId());

        for (SaleItemImageRequestDTO imageInfo : imagesInfo) {

            SaleItemImage imageEntity = existingImages.stream()
                    .filter(img -> img.getFileName().equals(imageInfo.getFileName()))
                    .findFirst()
                    .orElse(null);

            // If not found by filename, try by order (replacement scenario)
            if (imageEntity == null) {
                imageEntity = existingImages.stream()
                        .filter(img -> Objects.equals(img.getImageViewOrder(), imageInfo.getOrder()))
                        .findFirst()
                        .orElse(null);
            }

            boolean isNew = false;
            if (imageEntity == null && imageInfo.getStatus().equals("NEW")) {
                if (existingImages.size() >= 4) {
                    throw new IllegalStateException("Cannot add more than 4 images to a sale item.");
                }
                imageEntity = new SaleItemImage();
                imageEntity.setSaleItem(editedSaleItem);
                isNew = true;
            }

            if (imageInfo.getOrder() == null && !isNew) {
                throw new IllegalArgumentException("Image order must not be null.");
            }

            switch (imageInfo.getStatus()) {
                case "ONLINE":
                    if (imageEntity != null) {
                        imageEntity.setImageViewOrder(imageInfo.getOrder());
                        imageEntity.setStatus("ONLINE");
                        saleItemImageRepository.save(imageEntity);
                    }
                    break;

                case "DELETE":
                    if (imageEntity != null) {
                        fileService.removeFile(imageEntity.getActualFileName(), editedSaleItem.getId());
                        saleItemImageRepository.delete(imageEntity);
                        existingImages.remove(imageEntity);
                    }
                    continue;

                case "MOVE":
                    if (imageEntity != null) {

                        String customFileName = fileService.buildCustomFileName(
                                editedSaleItem.getId(),
                                imageInfo.getOrder(),
                                imageInfo.getFileName());

                        imageEntity.setFileName(customFileName);
                        imageEntity.setImageViewOrder(imageInfo.getOrder());
                        imageEntity.setStatus("ONLINE");
                        saleItemImageRepository.save(imageEntity);
                    }
                    break;

                case "NEW":
                    if (imageInfo.getOrder() == null) {
                        throw new IllegalArgumentException("Order must not be null for a new image.");
                    }

                    if (imageInfo.getImageFile() == null || imageInfo.getImageFile().isEmpty()) {
                        throw new IllegalArgumentException("Image file must not be null for a new image.");
                    }

                    SaleItemImage newEntity = new SaleItemImage();
                    newEntity.setSaleItem(editedSaleItem);
                    newEntity.setImageViewOrder(imageInfo.getOrder());

                    String customFileName = fileService.buildCustomFileName(
                            editedSaleItem.getId(),
                            imageInfo.getOrder(),
                            imageInfo.getImageFile());

                    fileService.updateImage(
                            imageInfo.getImageFile().getOriginalFilename(),
                            imageInfo.getImageFile(),
                            editedSaleItem.getId());

                    newEntity.setFileName(customFileName);
                    newEntity.setActualFileName(imageInfo.getImageFile().getOriginalFilename());
                    newEntity.setStatus("ONLINE");

                    saleItemImageRepository.save(newEntity);

                    break;

                default:
                    throw new IllegalArgumentException("Unsupported status: " + imageInfo.getStatus());
            }
        }
    }

    public Resource loadFile(String fileName) {
        SaleItemImage existFile = saleItemImageRepository.findByFileName(fileName);
        return fileService.loadFile(existFile.getSaleItem().getId() + "/" + existFile.getActualFileName());
    }
}

