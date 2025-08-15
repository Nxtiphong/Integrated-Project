package tt2.int221.backend.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
            saleItemImage.setImageViewOrder(i + 1); // Order starts from 1
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

        for (var image : images) {
            if(image.getFileName().equals(fileName)) {
                saleItemImageRepository.delete(image);
                fileService.removeFile(image.getFileName());
            }
        }

        saleItemImageRepository.deleteById(saleItemId);
    }

    public void deleteImages(Integer saleItemId) {
//        if (!saleItemImageRepository.existsBySaleItemId(saleItemId)) {
//            throw new NotfoundException("Sale-Item not found with id: " + saleItemId);
//        }
        List<SaleItemImage> images = getImagesBySaleItem(saleItemId);

        for (var image : images) {
            fileService.removeFile(image.getFileName());
        }

        saleItemImageRepository.deleteById(saleItemId);
    }

}
