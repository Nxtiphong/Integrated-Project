package tt2.int221.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tt2.int221.backend.dto.SaleItemDTO;
import tt2.int221.backend.entities.SaleItem;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(SaleItemDTO.class, SaleItem.class)
                .addMappings(mapper -> {
                    mapper.skip(SaleItem::setId);
                    mapper.skip(SaleItem::setCreatedOn);
                    mapper.skip(SaleItem::setUpdatedOn);
                });

        return modelMapper;
    }
}
