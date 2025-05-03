package tt2.int221.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt2.int221.backend.entities.SaleItem;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.SaleItemRepository;

import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;


    public List<SaleItem> getAllSaleItems() {
        return saleItemRepository.findAll();
    }

    public SaleItem getSaleItemById(Integer id) {
        return saleItemRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Sale-Item not found with id: " + id));
    }

    public List<SaleItem> getAllSaleItemsOrderByCreatedOnAsc() {
        return saleItemRepository.findAllByOrderByCreatedOnAscIdAsc();
    }



}
