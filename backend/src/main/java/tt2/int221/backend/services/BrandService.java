package tt2.int221.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt2.int221.backend.dto.UpdateBrandDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.BrandRepository;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
    public Brand getBrandById(Integer id) {
       return brandRepository.findById(id).orElseThrow(() ->
               new NotfoundException("Brand not found with id: " + id));
    }

    public Brand updateBrand(Integer id, UpdateBrandDTO updateBrandDTO) {
        Brand brand = getBrandById(id);
        if (updateBrandDTO.getName() != null) {
            brand.setName(updateBrandDTO.getName());
        }if (updateBrandDTO.getWebsiteUrl() != null){
            brand.setWebsiteUrl(updateBrandDTO.getWebsiteUrl());}
        if (updateBrandDTO.getCountryOfOrigin() != null){
            brand.setCountryOfOrigin(updateBrandDTO.getCountryOfOrigin());}
        if (updateBrandDTO.getIsActive() != null){
            brand.setIsActive(updateBrandDTO.getIsActive());
        }return brandRepository.save(brand);
    }
    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }
    public void deleteBrand(Integer id) {
        if (!brandRepository.existsById(id)) {
            throw new NotfoundException("Brand not found with id: " + id);
        }
        brandRepository.deleteById(id);
    }
}
