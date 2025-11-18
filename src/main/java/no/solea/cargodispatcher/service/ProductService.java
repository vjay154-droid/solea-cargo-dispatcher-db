package no.solea.cargodispatcher.service;

import lombok.extern.slf4j.Slf4j;
import no.solea.cargodispatcher.dto.ProductRequestDTO;
import no.solea.cargodispatcher.dto.ProductResponseDTO;
import no.solea.cargodispatcher.dto.ProductUpdateRequestDTO;
import no.solea.cargodispatcher.mapper.ProductMapper;
import no.solea.cargodispatcher.entities.Product;
import no.solea.cargodispatcher.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieve all products.
     *
     * @return list of ProductResponseDTO objects
     */
    public List<ProductResponseDTO> getProducts(){
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products", products.size());
        return ProductMapper.toProductResponseDTO(products);
    }

    /**
     * Retrieve a product by its ID.
     *
     * @param id product ID
     * @return Product entity
     * @throws ResponseStatusException if product not found
     */
    public Product getProductById(long id){
        log.info("Getting product by id");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found for id "+id));
        log.info("Found product is {}",product);
        return product;
    }

    /**
     * Create a new product in the database.
     *
     * @param productRequestDTO ProductRequestDTO to save
     * @return saved ProductResponseDTO
     */
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO){
        log.info("Creating new product: {}", productRequestDTO);
        Product savedProduct = productRepository.save(
                ProductMapper.toProduct(productRequestDTO)
        );
        log.info("Product created with id: {}", savedProduct.getId());
        return ProductMapper.toProductResponseDTO(savedProduct);
    }

    /**
     * Update an existing product.
     *
     * @param id product ID
     * @param productUpdateRequestDTO ProductUpdateRequestDTO with updated values
     * @return updated Product entity
     * @throws ResponseStatusException if product does not exist
     */
    public ProductResponseDTO updateProduct(long id,
            ProductUpdateRequestDTO productUpdateRequestDTO){
        log.info("Updating product with id: {}", id);

        Product product = ProductMapper.toProduct(productUpdateRequestDTO);
        Product existingProduct = getProductById(id);

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getSize() != null) {
            existingProduct.setSize(product.getSize());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully: {}", updatedProduct);
        return ProductMapper.toProductResponseDTO(updatedProduct);
    }

    public ProductResponseDTO getProductResponseById(long id){
        return ProductMapper.toProductResponseDTO(getProductById(id));
    }

    public List<ProductResponseDTO> findByMinSize(double size){
        List<Product> products = productRepository.findBySizeGreaterThan(size);
        return ProductMapper.toProductResponseDTO(products);
    }
}
