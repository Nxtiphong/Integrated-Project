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
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.dto.SaleItemDTO;
import tt2.int221.backend.dto.SaleItemWithImageDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.io.IOException;
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
    @Autowired
    private SaleItemImageService imageService;

    public SaleItem getSaleItemById(Integer id) {
        return saleItemRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + id));
    }


    public Page<SaleItem> findAll(int page, int size, String sortField,
                                  List<String> filterBrands, Integer filterPriceLower, Integer filterPriceUpper,
                                  List<Integer> filterStorages, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField)
                .and(Sort.by(Sort.Direction.ASC, "id"));
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Integer> storageFilter = (filterStorages == null || filterStorages.isEmpty()) ? null : filterStorages;
        List<String> brandsFilter = (filterBrands == null || filterBrands.isEmpty()) ? null : filterBrands;

        return saleItemRepository.filterItems(brandsFilter, filterPriceLower, filterPriceUpper, storageFilter, pageable);
    }

    public List<SaleItem> getAllSaleItemsOrderByCreatedOnAscV2() {
        return saleItemRepository.findAllByOrderByCreatedOnAscIdAsc();
    }

    @Transactional
    public SaleItem createSaleItem(SaleItemDTO saleItemDTO, List<MultipartFile> images) throws IOException {
        Integer brandId = saleItemDTO.getBrand().getId();
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() ->
                        new NotfoundException("Brand not found with id: " + saleItemDTO.getBrand().getId()));

        SaleItem saleItem = modelMapper.map(saleItemDTO, SaleItem.class);
        saleItem.setId(null);
        saleItem.setBrand(brand);

        saleItemRepository.save(saleItem);

        if (images != null && !images.isEmpty()) {
            imageService.uploadImage(saleItem, images);
        }

        em.refresh(saleItem);

        return saleItemRepository.findById(saleItem.getId())
                .orElseThrow(() ->
                        new NotfoundException("Sale-Item not found with id: " + saleItem.getId()));
    }

    @Transactional
    public SaleItem updateSaleItemById(Integer id, SaleItemDTO saleItemDTO) {

        SaleItem editedSaleItem = getSaleItemById(id);

        Integer brandId = saleItemDTO.getBrand().getId();
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new NotfoundException("Brand not found with id: " + brandId));

        editedSaleItem.setModel(saleItemDTO.getModel());
        editedSaleItem.setBrand(brand);
        editedSaleItem.setDescription(saleItemDTO.getDescription());
        editedSaleItem.setPrice(saleItemDTO.getPrice());
        editedSaleItem.setQuantity(saleItemDTO.getQuantity());
        editedSaleItem.setRamGb(saleItemDTO.getRamGb());
        editedSaleItem.setScreenSizeInch(saleItemDTO.getScreenSizeInch());
        editedSaleItem.setStorageGb(saleItemDTO.getStorageGb());
        editedSaleItem.setColor(saleItemDTO.getColor());

        SaleItem updatedSaleItem = saleItemRepository.saveAndFlush(editedSaleItem);
        em.refresh(updatedSaleItem);

        return saleItemRepository.findById(updatedSaleItem.getId()).orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + updatedSaleItem.getId()));
    }

    @Transactional
    public SaleItem updateSaleItemWithImageById(Integer id, SaleItemWithImageDTO saleItemDTO) throws IOException {

        SaleItem editedSaleItem = getSaleItemById(id);

        Integer brandId = saleItemDTO.getSaleItem().getBrand().getId();
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new NotfoundException("Brand not found with id: " + brandId));

        editedSaleItem.setModel(saleItemDTO.getSaleItem().getModel());
        editedSaleItem.setBrand(brand);
        editedSaleItem.setDescription(saleItemDTO.getSaleItem().getDescription());
        editedSaleItem.setPrice(saleItemDTO.getSaleItem().getPrice());
        editedSaleItem.setQuantity(saleItemDTO.getSaleItem().getQuantity());
        editedSaleItem.setRamGb(saleItemDTO.getSaleItem().getRamGb());
        editedSaleItem.setScreenSizeInch(saleItemDTO.getSaleItem().getScreenSizeInch());
        editedSaleItem.setStorageGb(saleItemDTO.getSaleItem().getStorageGb());
        editedSaleItem.setColor(saleItemDTO.getSaleItem().getColor());

        imageService.updateSaleItemImages(editedSaleItem, saleItemDTO.getImageInfos());

        SaleItem updatedSaleItem = saleItemRepository.saveAndFlush(editedSaleItem);
        em.refresh(updatedSaleItem);

        return saleItemRepository.findById(updatedSaleItem.getId())
                .orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + updatedSaleItem.getId()));
    }

    public void deleteSaleItemById(Integer id) {
        if (!saleItemRepository.existsById(id)) {
            throw new NotfoundException("Sale-Item not found with id: " + id);
        }
        imageService.deleteImages(id);
        saleItemRepository.deleteById(id);
    }
}
