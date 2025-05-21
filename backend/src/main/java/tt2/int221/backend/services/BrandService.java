package tt2.int221.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt2.int221.backend.dto.BrandDTO;
import tt2.int221.backend.dto.BrandResponseDTO;
import tt2.int221.backend.dto.UpdateBrandDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.exceptions.BadRequestException;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.util.List;
import java.util.Objects;

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
        Boolean existBrandName = (brandRepository.existsByName(updateBrandDTO.getName()));
        if (!Objects.equals(brand.getName(), updateBrandDTO.getName()) && existBrandName) {
            throw new RuntimeException("This Brand already exists");
        };
        brand.setName(updateBrandDTO.getName());
        brand.setWebsiteUrl(updateBrandDTO.getWebsiteUrl());
        brand.setCountryOfOrigin(updateBrandDTO.getCountryOfOrigin());
        brand.setIsActive(updateBrandDTO.getIsActive());
        brand = brandRepository.save(brand);
        return convertToDTO(brand);
    }

    public BrandResponseDTO addBrand(Brand brand) {
        if(brandRepository.existsByName(brand.getName())){
            throw new RuntimeException("Brand name " + brand.getName()+" already exists");
        }
        brand = brandRepository.save(brand);
        return convertToDTO(brand);
    }

    public void deleteBrand(Integer id) {
        if (!brandRepository.existsById(id)){
            throw new NotfoundException("Delete failed, Brand not found with ID: " + id);
        }
        if (!saleItemRepository.findAllByBrandId(id).isEmpty()) {
            BrandResponseDTO brand = getBrandById(id);
            throw new BadRequestException("There are sale item with " + brand.getName() + " brand in used." );
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

        long count = saleItemRepository.countByBrandId(brand.getId());
        dto.setNoOfSaleItems((int) count);

        return dto;
    }
}