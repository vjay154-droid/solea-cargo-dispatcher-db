package no.solea.cargodispatcher;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Solea Cargo Dispatcher(Solea Intergalactic Empire)",
        description = "A Spring Boot application for managing cargo dispatching to planets.", version = "1.0.0"))
@SpringBootApplication
public class SoleaCargoDispatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoleaCargoDispatcherApplication.class, args);
	}

}
