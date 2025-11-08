package no.solea.cargodispatcher.service;

import no.solea.cargodispatcher.dto.ProductRequestDTO;
import no.solea.cargodispatcher.dto.ProductResponseDTO;
import no.solea.cargodispatcher.dto.ProductUpdateRequestDTO;
import no.solea.cargodispatcher.entities.Product;
import no.solea.cargodispatcher.mapper.ProductMapper;
import no.solea.cargodispatcher.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Product A", 1.0);
        product2 = new Product(2L, "Product B", 2.0);
    }

    @Test
    void getProducts_shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductResponseDTO> products = productService.getProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product A", products.get(0).name());
        assertEquals("Product B", products.get(1).name());
    }

    @Test
    void getProductById_shouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Product A", product.getName());
    }

    @Test
    void getProductById_shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productService.getProductById(999L)
        );

        assertEquals("Product not found for id 999", exception.getReason());
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO("New Product", 5.0);
        Product productToSave = ProductMapper.toProduct(requestDTO);
        Product savedProduct = new Product(3L, "New Product", 5.0);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO responseDTO = productService.createProduct(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(3L, responseDTO.id());
        assertEquals("New Product", responseDTO.name());
        assertEquals(5.0, responseDTO.size());
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        ProductUpdateRequestDTO updateDTO = new ProductUpdateRequestDTO("Updated Product", 9.0);
        Product updatedProduct = new Product(1L, "Updated Product", 9.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponseDTO responseDTO = productService.updateProduct(1L, updateDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.id());
        assertEquals("Updated Product", responseDTO.name());
        assertEquals(9.0, responseDTO.size());
    }

    @Test
    void updateProduct_shouldThrowWhenProductNotFound() {
        ProductUpdateRequestDTO updateDTO = new ProductUpdateRequestDTO("Updated Product", 9.0);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productService.updateProduct(999L, updateDTO)
        );

        assertEquals("Product not found for id 999", exception.getReason());
    }
}
