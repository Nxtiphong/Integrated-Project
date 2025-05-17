package tt2.int221.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt2.int221.backend.dto.AddBrandDTO;
import tt2.int221.backend.dto.BrandDTO;
import tt2.int221.backend.dto.BrandResponseDTO;
import tt2.int221.backend.dto.UpdateBrandDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.services.BrandService;

import java.util.List;

@RestController
@Tag(name = "Brand API")
@RequestMapping("/v1/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all brands", description = "Returning all brand in lists")
    @GetMapping("")
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        List<BrandDTO> brandDTO = brands.stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .toList();
        return ResponseEntity.ok(brandDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> getBrand(@PathVariable Integer id) {
        BrandResponseDTO response = brandService.getBrandById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<BrandResponseDTO> addBrand(@RequestBody AddBrandDTO newBrandDTO) {
        Brand brand = modelMapper.map(newBrandDTO, Brand.class);
        BrandResponseDTO response = brandService.addBrand(brand);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> updateBrand(@PathVariable Integer id, @RequestBody UpdateBrandDTO updateBrandDTO) {
        BrandResponseDTO response = brandService.updateBrand(id, updateBrandDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}