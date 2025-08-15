package tt2.int221.backend.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void uploadImage(SaleItem saleItem, List<MultipartFile> file) throws IOException {

        if (file.size() > 5) {
            throw new RuntimeException("File is too large");
        }

        List<String> savedFiles = fileService.storeMultipartFile(file);

        SaleItem createdSaleItem = saleItemRepository.findById(saleItem.getId())
                .orElseThrow(() -> new NotfoundException("Sale item not found"));

        for (int i = 0; i < savedFiles.size(); i++) {
            SaleItemImage saleItemImage = new SaleItemImage();
            saleItemImage.setSaleItem(createdSaleItem);
            saleItemImage.setImageViewOrder(i + 1);
            saleItemImage.setFileName(savedFiles.get(i));
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

        long newImagesCount = imagesInfo.stream()
                .filter(dto -> dto.getImageFile() != null
                        && !dto.getImageFile().isEmpty())
                .count();

        if (existingImages.size() + newImagesCount > 4) {
            throw new IllegalArgumentException("A sale item can have a maximum of 4 images.");
        }

        for (SaleItemImageRequestDTO imageInfo : imagesInfo) {
            SaleItemImage imageEntity = null;

            if (imageInfo.getOrder() != null) {
                imageEntity = existingImages.stream()
                        .filter(img -> img.getImageViewOrder().equals(imageInfo.getOrder()))
                        .findFirst()
                        .orElse(null);
            }

            if (imageEntity == null && imageInfo.getFileName() != null) {
                imageEntity = existingImages.stream()
                        .filter(img -> img.getFileName().equals(imageInfo.getFileName()))
                        .findFirst()
                        .orElse(null);
            }

            if (imageEntity == null) {
                imageEntity = new SaleItemImage();
                imageEntity.setSaleItem(editedSaleItem);
                imageEntity.setImageViewOrder(imageInfo.getOrder());
            }

            if (imageInfo.getImageFile() != null && !imageInfo.getImageFile().isEmpty()) {
                if(imageEntity.getFileName() != null){
                    fileService.removeFile(imageEntity.getFileName());
                }
                String newFileName = fileService.updateImage(imageInfo.getFileName(), imageInfo.getImageFile());
                imageEntity.setFileName(newFileName);
            }

            saleItemImageRepository.save(imageEntity);
        }
    }
}

