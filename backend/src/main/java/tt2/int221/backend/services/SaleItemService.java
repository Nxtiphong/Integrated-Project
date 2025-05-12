package tt2.int221.backend.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt2.int221.backend.dto.SaleItemDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager em;

    public SaleItem getSaleItemById(Integer id) {
        return saleItemRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + id));
    }

    public List<SaleItem> getAllSaleItemsOrderByCreatedOnAsc() {
        return saleItemRepository.findAllByOrderByCreatedOnAscIdAsc();
    }

    @Transactional
    public SaleItem createSaleItem(SaleItemDTO saleItemDTO) {
        Integer brandId = saleItemDTO.getBrand().getId();
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() ->
                        new NotfoundException("Brand not found with id: " + saleItemDTO.getBrand().getId()));

        saleItemDTO.setModel(saleItemDTO.getModel().trim());
        saleItemDTO.setDescription(saleItemDTO.getDescription().trim());
        saleItemDTO.setColor(saleItemDTO.getColor() != null ? saleItemDTO.getColor().trim() : null);

        SaleItem saleItem = modelMapper.map(saleItemDTO, SaleItem.class);
        saleItem.setBrand(brand);

        if (saleItemDTO.getQuantity() == null || saleItemDTO.getQuantity() < 0) {
            saleItem.setQuantity(1);
        }else{
            saleItem.setQuantity(saleItemDTO.getQuantity());
        }

        if (saleItemDTO.getModel().length() > 60) {
            saleItem.setModel(saleItemDTO.getModel().substring(0, 60));
        } else {
            saleItem.setModel(saleItemDTO.getModel());
        }

        SaleItem savedSaleItem = saleItemRepository.save(saleItem);
        em.refresh(savedSaleItem);

        return saleItemRepository.findById(savedSaleItem.getId())
                .orElseThrow(() ->
                        new NotfoundException("Sale-Item not found with id: " + savedSaleItem.getId()));
    }

    @Transactional
    public SaleItem updateSaleItemById(Integer id, SaleItemDTO saleItemDTO) {

        SaleItem editedSaleItem = getSaleItemById(id);

        Integer brandId = saleItemDTO.getBrand().getId();
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new NotfoundException("Brand not found with id: " + brandId));

        if (saleItemDTO.getModel() != null) {
            String trimmedModel = saleItemDTO.getModel().trim();
            editedSaleItem.setModel(trimmedModel.length() > 60 ? trimmedModel.substring(0, 60) : trimmedModel);
        }

        editedSaleItem.setBrand(brand);

        if (saleItemDTO.getDescription() != null) {
            editedSaleItem.setDescription(saleItemDTO.getDescription().trim());
        }

        editedSaleItem.setPrice(saleItemDTO.getPrice());

        if (saleItemDTO.getQuantity() != null) {
            editedSaleItem.setQuantity(saleItemDTO.getQuantity());
        }

        if (saleItemDTO.getRamGb() != null) {
            editedSaleItem.setRamGb(saleItemDTO.getRamGb());
        }

        if (saleItemDTO.getScreenSizeInch() != null) {
            editedSaleItem.setScreenSizeInch(saleItemDTO.getScreenSizeInch());
        }

        if (saleItemDTO.getStorageGb() != null) {
            editedSaleItem.setStorageGb(saleItemDTO.getStorageGb());
        }

        if (saleItemDTO.getColor() != null && !saleItemDTO.getColor().trim().isEmpty()) {
            editedSaleItem.setColor(saleItemDTO.getColor().trim());
        }

        SaleItem updatedSaleItem = saleItemRepository.saveAndFlush(editedSaleItem);

        em.refresh(updatedSaleItem);

        return saleItemRepository.findById(updatedSaleItem.getId()).orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + updatedSaleItem.getId()));
    }

    public void deleteSaleItemById(Integer id) {
        SaleItem saleItem = getSaleItemById(id);
        saleItemRepository.delete(saleItem);
    }
}
