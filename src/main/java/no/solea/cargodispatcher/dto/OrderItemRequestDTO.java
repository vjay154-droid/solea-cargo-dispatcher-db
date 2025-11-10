package no.solea.cargodispatcher.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO(
        @NotNull(message = "Product id can not be null")
        Long productId,
        @NotNull(message = "quantity can not be null")
        @Min(value = 1, message = "Quantity should be at least 1")
        Integer quantity
) {
}
