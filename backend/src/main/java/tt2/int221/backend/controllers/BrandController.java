package tt2.int221.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tt2.int221.backend.dto.BrandDTO;
import tt2.int221.backend.entities.Brand;
import tt2.int221.backend.services.BrandService;

import java.util.List;

@RestController
@Tag(name="Brand API")
@RequestMapping("/v1")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all brands", description = "Returning all brand in lists")
    @GetMapping("/brands")
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        List<BrandDTO> brandDTO = brands.stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .toList();
        return ResponseEntity.ok(brandDTO);
    }
}
