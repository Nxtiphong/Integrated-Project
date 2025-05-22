package tt2.int221.backend.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tt2.int221.backend.dto.SaleItemDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.util.List;
import java.util.Set;

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


    public Page<SaleItem> findAll(int page, int size, String sortField, List<String> filterBrands, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        return filterBrands.isEmpty() ? saleItemRepository.findAll(pageable) : saleItemRepository.findByBrandNameIn(filterBrands, pageable);
    }


    public  List<SaleItem> getAllSaleItemsOrderByCreatedOnAscV2() {
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
        saleItemDTO.setColor(saleItemDTO.getColor() == null || saleItemDTO.getColor().trim().isEmpty() ? null : saleItemDTO.getColor().trim());

        SaleItem saleItem = modelMapper.map(saleItemDTO, SaleItem.class);

        saleItem.setBrand(brand);
        saleItem.setQuantity(saleItemDTO.getQuantity() == null || saleItem.getQuantity() < 0 ? 1 : saleItemDTO.getQuantity());
        saleItem.setModel(saleItemDTO.getModel());

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

        editedSaleItem.setModel(saleItemDTO.getModel().trim());
        editedSaleItem.setBrand(brand);
        editedSaleItem.setDescription(saleItemDTO.getDescription().trim());
        editedSaleItem.setPrice(saleItemDTO.getPrice());
        editedSaleItem.setQuantity(saleItemDTO.getQuantity() == null || saleItemDTO.getQuantity() < 0 ? 1 : saleItemDTO.getQuantity());
        editedSaleItem.setRamGb(saleItemDTO.getRamGb());
        editedSaleItem.setScreenSizeInch(saleItemDTO.getScreenSizeInch());
        editedSaleItem.setStorageGb(saleItemDTO.getStorageGb());
        editedSaleItem.setColor(saleItemDTO.getColor() == null || saleItemDTO.getColor().trim().isEmpty() ? null : saleItemDTO.getColor().trim());

        SaleItem updatedSaleItem = saleItemRepository.saveAndFlush(editedSaleItem);
        em.refresh(updatedSaleItem);

        return saleItemRepository.findById(updatedSaleItem.getId()).orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + updatedSaleItem.getId()));
    }

    public void deleteSaleItemById(Integer id) {
        if (!saleItemRepository.existsById(id)) {
            throw new NotfoundException("Sale-Item not found with id: " + id);
        }
        saleItemRepository.deleteById(id);
    }
}
