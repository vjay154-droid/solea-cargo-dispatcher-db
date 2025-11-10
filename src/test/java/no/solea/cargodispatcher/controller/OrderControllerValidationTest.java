package no.solea.cargodispatcher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.solea.cargodispatcher.dto.OrderItemRequestDTO;
import no.solea.cargodispatcher.dto.OrderRequestDTO;
import no.solea.cargodispatcher.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unused")
    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturnCreated_whenOrderRequestIsValid() throws Exception {
        OrderItemRequestDTO item = new OrderItemRequestDTO(1L, 5);
        OrderRequestDTO validRequest = new OrderRequestDTO(1L, List.of(item));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequest_whenOrderRequestIsInvalid() throws Exception {
        OrderRequestDTO invalidRequest = new OrderRequestDTO(null, Collections.emptyList());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.path").value("/api/orders"));
    }

}
