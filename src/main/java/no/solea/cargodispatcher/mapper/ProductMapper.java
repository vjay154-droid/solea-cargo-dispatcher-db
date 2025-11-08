package no.solea.cargodispatcher.mapper;

import no.solea.cargodispatcher.dto.ProductRequestDTO;
import no.solea.cargodispatcher.dto.ProductResponseDTO;
import no.solea.cargodispatcher.dto.ProductUpdateRequestDTO;
import no.solea.cargodispatcher.entities.Product;

import java.util.List;

public class ProductMapper {

    public static ProductResponseDTO toProductResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getSize()
        );
    }

    public static Product toProduct(ProductRequestDTO productRequestDTO) {
        return Product.builder()
                .name(productRequestDTO.name())
                .size(productRequestDTO.size())
                .build();
    }

    public static Product toProduct(
            ProductUpdateRequestDTO productUpdateRequestDTO) {
        return Product.builder()
                .name(productUpdateRequestDTO.name())
                .size(productUpdateRequestDTO.size())
                .build();
    }
    public static List<ProductResponseDTO> toProductResponseDTO(List<Product> product) {
        return product.stream()
                .map(ProductMapper::toProductResponseDTO)
                .toList();
    }
}
