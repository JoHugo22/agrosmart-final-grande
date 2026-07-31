package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void getters_conDatosDelConstructor_debenDevolverLosMismosValores() {

        // Arrange
        List<String> correos = List.of("ventas@agrosmart.com");

        Producto producto = new Producto(
                1L,
                "Cacao fino de aroma",
                "Cacao",
                new BigDecimal("8.50"),
                correos
        );

        // Act y Assert
        assertEquals(1L, producto.getId());
        assertEquals(
                "Cacao fino de aroma",
                producto.getNombre()
        );
        assertEquals("Cacao", producto.getCategoria());
        assertEquals(
                new BigDecimal("8.50"),
                producto.getPrecioUsd()
        );
        assertEquals(correos, producto.getCorreosNotificacion());
    }

    @Test
    void constructor_alModificarListaOriginal_noDebeCambiarElProducto() {

        // Arrange
        List<String> correos = new ArrayList<>();
        correos.add("ventas@agrosmart.com");

        Producto producto = new Producto(
                1L,
                "Cacao fino de aroma",
                "Cacao",
                new BigDecimal("8.50"),
                correos
        );

        // Act
        correos.add("intruso@correo.com");

        // Assert
        assertEquals(
                1,
                producto.getCorreosNotificacion().size()
        );
        assertEquals(
                "ventas@agrosmart.com",
                producto.getCorreosNotificacion().getFirst()
        );
    }

    @Test
    void getCorreosNotificacion_alIntentarModificar_debeSerInmodificable() {

        // Arrange
        List<String> correos = new ArrayList<>();
        correos.add("ventas@agrosmart.com");

        Producto producto = new Producto(
                1L,
                "Cacao fino de aroma",
                "Cacao",
                new BigDecimal("8.50"),
                correos
        );

        // Act
        List<String> listaDevuelta =
                producto.getCorreosNotificacion();

        // Assert
        assertNotSame(correos, listaDevuelta);

        assertThrows(
                UnsupportedOperationException.class,
                () -> listaDevuelta.add("otro@correo.com")
        );
    }
}