package tt2.int221.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt2.int221.backend.dto.BrandResponseDTO;
import tt2.int221.backend.dto.UpdateBrandDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public BrandResponseDTO getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new NotfoundException("Brand not found with id: " + id));
        return convertToDTO(brand);
    }

    public BrandResponseDTO updateBrand(Integer id, UpdateBrandDTO updateBrandDTO) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new NotfoundException("Brand not found with id: " + id));
        brand.setName(updateBrandDTO.getName());
        brand.setWebsiteUrl(updateBrandDTO.getWebsiteUrl());
        brand.setCountryOfOrigin(updateBrandDTO.getCountryOfOrigin());
        brand.setIsActive(updateBrandDTO.getIsActive());
        brand = brandRepository.save(brand);
        return convertToDTO(brand);
    }

    public BrandResponseDTO addBrand(Brand brand) {
        brand = brandRepository.save(brand);
        return convertToDTO(brand);
    }

    public void deleteBrand(Integer id) {
        if (!brandRepository.existsById(id)) {
            throw new NotfoundException("Brand not found with id: " + id);
        }
        brandRepository.deleteById(id);
    }

    private BrandResponseDTO convertToDTO(Brand brand) {
        BrandResponseDTO dto = new BrandResponseDTO();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setWebsiteUrl(brand.getWebsiteUrl());
        dto.setCountryOfOrigin(brand.getCountryOfOrigin());
        dto.setIsActive(brand.getIsActive());

        long noOfSaleItems = saleItemRepository.countByBrandId(brand.getId());
        dto.setNoOfSaleItems(String.valueOf(noOfSaleItems));

        return dto;
    }
}