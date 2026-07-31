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

            // Solo inserta los datos cuando la tabla está vacía.
            if (productoRepository.count() == 0) {

                // Producto válido 1:
                // precio mayor que cero y correo registrado.
                ProductoEntity productoValido1 = new ProductoEntity(
                        "Cacao fino de aroma",
                        new BigDecimal("8.50"),
                        120,
                        "Cacao",
                        "ventas@agrosmart.com"
                );

                // Producto válido 2:
                // precio mayor que cero y correo registrado.
                ProductoEntity productoValido2 = new ProductoEntity(
                        "Pasta artesanal de cacao",
                        new BigDecimal("12.75"),
                        75,
                        "Cacao",
                        "comercial@agrosmart.com"
                );

                // Producto válido 3:
                // precio mayor que cero y correo registrado.
                ProductoEntity productoValido3 = new ProductoEntity(
                        "Nibs naturales de cacao",
                        new BigDecimal("6.40"),
                        95,
                        "Cacao",
                        "pedidos@agrosmart.com"
                );

                // Producto inválido 1:
                // tiene correo, pero el precio es igual a cero.
                ProductoEntity productoInvalido1 = new ProductoEntity(
                        "Cacao de prueba sin precio",
                        BigDecimal.ZERO,
                        40,
                        "Cacao",
                        "control@agrosmart.com"
                );

                // Producto inválido 2:
                // tiene precio mayor que cero, pero no tiene correo.
                ProductoEntity productoInvalido2 = new ProductoEntity(
                        "Producto de cacao sin contacto",
                        new BigDecimal("5.25"),
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

                System.out.println(
                        "Se insertaron 5 productos iniciales en "
                                + "tbl_productos_base_60."
                );

            } else {
                System.out.println(
                        "La tabla tbl_productos_base_60 ya contiene datos. "
                                + "No se realizó una nueva inserción."
                );
            }
        };
    }
}