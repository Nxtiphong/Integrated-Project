package tt2.int221.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt2.int221.backend.dto.SaleItemDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.time.Instant;
import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper modelMapper;

    public SaleItem getSaleItemById(Integer id) {
        return saleItemRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + id));
    }

    public List<SaleItem> getAllSaleItemsOrderByCreatedOnAsc() {
        return saleItemRepository.findAllByOrderByCreatedOnAscIdAsc();
    }

    public SaleItem createSaleItem(SaleItemDTO saleItemDTO) {
        Integer brandId = saleItemDTO.getBrand().getId();
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() ->
                        new NotfoundException("Brand not found with id: " + saleItemDTO.getBrand().getId()));

        SaleItem saleItem = modelMapper.map(saleItemDTO, SaleItem.class);
        saleItem.setBrand(brand);

        if (saleItemDTO.getQuantity() == null || saleItemDTO.getQuantity() <= 0) {
            saleItem.setQuantity(1);
        }

        if(saleItemDTO.getModel().length() > 60){
            saleItem.setModel(saleItemDTO.getModel().substring(0, 60));
        }else{
            saleItem.setModel(saleItemDTO.getModel());
        }

        SaleItem savedSaleItem = saleItemRepository.save(saleItem);

        return saleItemRepository.findById(savedSaleItem.getId())
                .orElseThrow(() ->
                        new NotfoundException("Sale-Item not found with id: " + savedSaleItem.getId()));
    }

    public SaleItem updateSaleItemById(Integer id, SaleItemDTO saleItemDTO) {

        SaleItem editedSaleItem = getSaleItemById(id);

        Integer brandId = saleItemDTO.getBrand().getId();
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new NotfoundException("Brand not found with id: " + brandId));

        if(saleItemDTO.getModel().length() > 60){
            editedSaleItem.setModel(saleItemDTO.getModel().substring(0, 60));
        }else{
            editedSaleItem.setModel(saleItemDTO.getModel());
        }

        editedSaleItem.setBrand(brand);
        editedSaleItem.setDescription(saleItemDTO.getDescription());
        editedSaleItem.setPrice(saleItemDTO.getPrice());

        if (saleItemDTO.getQuantity() != null){
            editedSaleItem.setQuantity(saleItemDTO.getQuantity());
        }

        if (saleItemDTO.getRamGb() != null) {
            editedSaleItem.setRamGb(saleItemDTO.getRamGb());
        }

        if (saleItemDTO.getScreenSizeInch() != null) {
            editedSaleItem.setScreenSizeInch(saleItemDTO.getScreenSizeInch());
        }

        SaleItem updatedSaleItem = saleItemRepository.save(editedSaleItem);

        return saleItemRepository.findById(updatedSaleItem.getId()).orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + updatedSaleItem.getId()));
    }

    public void deleteSaleItemById(Integer id) {
        SaleItem saleItem = getSaleItemById(id);
        saleItemRepository.delete(saleItem);
    }
}
