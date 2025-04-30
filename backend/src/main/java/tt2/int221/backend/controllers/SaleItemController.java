package tt2.int221.backend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt2.int221.backend.dto.response.DetailDTO;
import tt2.int221.backend.dto.response.GelleryDTO;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.services.SaleItemService;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class SaleItemController {

    @Autowired
    private SaleItemService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<SaleItem>> findAll() {
        return ResponseEntity.ok(service.getAllSaleItemsOrderByCreatedOnAsc());
    }
    @GetMapping("/sale-item")
    public ResponseEntity<List<GelleryDTO>> getGelleryDTO() {
        List<SaleItem> saleItems = service.getAllSaleItemsOrderByCreatedOnAsc();
        List<GelleryDTO> gelleryDTOs = saleItems.stream()
                .map(saleItem -> modelMapper.map(saleItem, GelleryDTO.class))
                .toList();
        return ResponseEntity.ok(gelleryDTOs);
    }

    @GetMapping("/sale-item/{id}")
    public ResponseEntity<DetailDTO> findById(@PathVariable Integer id) {
        SaleItem saleItem = service.getSaleItemById(id);
        if (saleItem == null) {
            return ResponseEntity.notFound().build();
        }
        DetailDTO detailDTO = modelMapper.map(saleItem, DetailDTO.class);
        return ResponseEntity.ok(detailDTO);
    }


}
