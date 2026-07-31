package ec.edu.espe.agrosmart.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ec.edu.espe.agrosmart.entity.ProductoEntity;
import ec.edu.espe.agrosmart.repository.ProductoRepository;

@Configuration
public class DatosInicialesConfig {

    @Bean
    CommandLineRunner cargarProductosIniciales(
            ProductoRepository productoRepository
    ) {
        return args -> {

            // Evita duplicar los productos cada vez que inicia la aplicación.
            if (productoRepository.count() == 0) {

                ProductoEntity productoValido1 = new ProductoEntity(
                        "Cacao fino de aroma",
                        new BigDecimal("8.50"),
                        120,
                        "Cacao",
                        "ventas@agrosmart.com"
                );

                ProductoEntity productoValido2 = new ProductoEntity(
                        "Pasta artesanal de cacao",
                        new BigDecimal("12.75"),
                        75,
                        "Cacao",
                        "comercial@agrosmart.com"
                );

                ProductoEntity productoValido3 = new ProductoEntity(
                        "Nibs naturales de cacao",
                        new BigDecimal("6.40"),
                        95,
                        "Cacao",
                        "pedidos@agrosmart.com"
                );

                ProductoEntity productoInvalido1 = new ProductoEntity(
                        "Cacao de prueba sin precio",
                        BigDecimal.ZERO,
                        40,
                        "Cacao",
                        ""
                );

                ProductoEntity productoInvalido2 = new ProductoEntity(
                        "Producto de cacao sin contacto",
                        BigDecimal.ZERO,
                        25,
                        "Cacao",
                        ""
                );

                productoRepository.saveAll(
                        List.of(
                                productoValido1,
                                productoValido2,
                                productoValido3,
                                productoInvalido1,
                                productoInvalido2
                        )
                );
            }
        };
    }
}