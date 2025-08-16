package tt2.int221.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.dto.*;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.services.SaleItemImageService;
import tt2.int221.backend.services.SaleItemService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@Tag(name = "Sale Items API")
@RequestMapping("")
public class SaleItemController {

    @Autowired
    private SaleItemService service;

    @Autowired
    private SaleItemImageService imageService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all sale items", description = "Return all sale items")

    @GetMapping("/v2/sale-items")
    public ResponseEntity<PageDTO<GalleryDTO>> getGalleryDTO(
            @RequestParam(required = true) int page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "") List<String> filterBrands,
            @RequestParam(required = false) Integer filterPriceLower,
            @RequestParam(required = false) Integer filterPriceUpper,
            @RequestParam(required = false) List<Integer> filterStorages,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Page<SaleItem> saleItems = service.findAll(page, size, sortField, filterBrands, filterPriceLower, filterPriceUpper, filterStorages, sortDirection);
        List<GalleryDTO> galleryDTOs = saleItems.getContent().stream()
                .map(saleItem -> modelMapper.map(saleItem, GalleryDTO.class))
                .toList();
        PageDTO<GalleryDTO> response = new PageDTO<>(
                galleryDTOs,
                sortField + ": " + sortDirection.toUpperCase(),
                saleItems.getNumber(),
                saleItems.getSize(),
                saleItems.getTotalElements(),
                saleItems.getTotalPages(),
                saleItems.isFirst(),
                saleItems.isLast()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/sale-items")
    public ResponseEntity<List<GalleryDTO>> getGalleryDTOV2() {
        List<GalleryDTO> response = service.getAllSaleItemsOrderByCreatedOnAscV2().stream()
                .map(saleItem -> modelMapper.map(saleItem, GalleryDTO.class))
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a sale item by id", description = "Return a sale item by id")
    @GetMapping("/v1/sale-items/{id}")
    public ResponseEntity<DetailDTO> findById(@PathVariable Integer id) {
        SaleItem saleItem = service.getSaleItemById(id);
        if (saleItem == null) {
            return ResponseEntity.notFound().build();
        }
        DetailDTO detailDTO = modelMapper.map(saleItem, DetailDTO.class);
        return ResponseEntity.ok(detailDTO);
    }

//    @Operation(summary = "Create new sale item", description = "Return a new sale item detail")
//    @PostMapping("/v1/sale-items")
//    public ResponseEntity<DetailDTO> createSaleItem(@RequestBody SaleItemDTO saleItem) {
//        if (saleItem == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        SaleItem createdSaleItem = service.createSaleItem(saleItem);
//        DetailDTO detailDTO = modelMapper.map(createdSaleItem, DetailDTO.class);
//        detailDTO.setBrandName(createdSaleItem.getBrand().getName());
//
//        return ResponseEntity.status(201).body(detailDTO);
//    }

    @Operation(summary = "Update exists sale item by id", description = "Return an updated sale item detail")
    @PutMapping("/v1/sale-items/{id}")
    public ResponseEntity<DetailDTO> updateSaleItem(@PathVariable Integer id, @RequestBody SaleItemDTO saleItem) {
        if (saleItem == null) {
            return ResponseEntity.badRequest().build();
        }

        SaleItem updatedSaleItem = service.updateSaleItemById(id, saleItem);
        DetailDTO detailDTO = modelMapper.map(updatedSaleItem, DetailDTO.class);
        detailDTO.setBrandName(updatedSaleItem.getBrand().getName());

        return ResponseEntity.status(200).body(detailDTO);
    }

    @Operation(summary = "Delete sale item by id", description = "Return status 204 if delete successfully")
    @DeleteMapping("/v1/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Integer id) {
        service.deleteSaleItemById(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Create new sale item w/ images")
    @PostMapping("/v2/sale-items")
    @ApiResponse(responseCode = "201", description = "Sale item created")
    @ApiResponse(responseCode = "500", description = "Sale item create failed")
    public ResponseEntity<DetailDTO> createSaleItem(@ModelAttribute SaleItemDTO saleItem,
                                                    @RequestParam(required = false) List<MultipartFile> images) throws IOException {
        if (saleItem == null) {
            return ResponseEntity.badRequest().build();
        }

        SaleItem createdSaleItem = service.createSaleItem(saleItem, images);

        DetailDTO detailDTO = modelMapper.map(createdSaleItem, DetailDTO.class);
        detailDTO.setBrandName(createdSaleItem.getBrand().getName());
        return ResponseEntity.status(201).body(detailDTO);
    }

    @Operation(summary = "Get a sale item by id", description = "Return a sale item by id with images")
    @GetMapping("/v2/sale-items/{id}")
    public ResponseEntity<DetailDTO> findByIdWithImages(@PathVariable Integer id) {
        SaleItem saleItem = service.getSaleItemById(id);
        if (saleItem == null) {
            return ResponseEntity.notFound().build();
        }
        DetailDTO detailDTO = modelMapper.map(saleItem, DetailDTO.class);
        return ResponseEntity.ok(detailDTO);
    }

    @Operation(summary = "Delete sale item by id", description = "Return status 204 if delete successfully")
    @DeleteMapping("/v2/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItemWithImages(@PathVariable Integer id) {
        service.deleteSaleItemById(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Delete individual image", description = "Return status 204 if delete successfully")
    @DeleteMapping("/v2/sale-items/images")
    public ResponseEntity<Void> deleteSaleItemWithImage(@RequestParam String fileName, @RequestParam Integer id) {
        imageService.deleteImage(id, fileName);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Update Sale item with Image", description = "Return a updated sale item with image")
    @PutMapping("/v2/sale-items/{id}")
    public ResponseEntity<DetailDTO> updateSaleItemWithImage(@PathVariable Integer id,
                                                             @ModelAttribute SaleItemWithImageDTO saleItem) throws IOException {
        if (saleItem == null) {
            return ResponseEntity.badRequest().build();
        }

        SaleItem updatedSaleItem = service.updateSaleItemWithImageById(id, saleItem);
        DetailDTO detailDTO = modelMapper.map(updatedSaleItem, DetailDTO.class);
        detailDTO.setBrandName(updatedSaleItem.getBrand().getName());

        return ResponseEntity.status(200).body(detailDTO);
    }

    @Operation(summary = "Get Sale item Image", description = "Return an Image file for sale item")
    @GetMapping("/v2/sale-items/images")
    public ResponseEntity<byte[]> getSaleItemFile(@RequestParam String fileName) throws IOException {
        if (fileName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Resource resource = service.loadFile(fileName);

        String contentType = Files.probeContentType(resource.getFile().toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        byte[] fileBytes = resource.getInputStream().readAllBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(fileBytes.length)
                .body(fileBytes);
    }
}
