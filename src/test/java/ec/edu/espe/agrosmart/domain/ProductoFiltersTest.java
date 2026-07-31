package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ProductoFiltersTest {

    @Test
    void isValid_conPrecioPositivoYCorreo_debeRetornarTrue() {

        Producto producto = new Producto(
                1L,
                "Cacao fino de aroma",
                "Cacao",
                new BigDecimal("8.50"),
                List.of("ventas@agrosmart.com")
        );

        boolean resultado = ProductoFilters.IS_VALID.test(producto);

        assertTrue(resultado);
    }

    @Test
    void isValid_conPrecioCero_debeRetornarFalse() {

        Producto producto = new Producto(
                2L,
                "Cacao sin precio",
                "Cacao",
                BigDecimal.ZERO,
                List.of("control@agrosmart.com")
        );

        boolean resultado = ProductoFilters.IS_VALID.test(producto);

        assertFalse(resultado);
    }

    @Test
    void isValid_sinCorreos_debeRetornarFalse() {

        Producto producto = new Producto(
                3L,
                "Cacao sin contacto",
                "Cacao",
                new BigDecimal("5.25"),
                List.of()
        );

        boolean resultado = ProductoFilters.IS_VALID.test(producto);

        assertFalse(resultado);
    }

    @Test
    void aMayusculas_debeCrearNuevoProductoSinModificarOriginal() {

        Producto original = new Producto(
                4L,
                "Nibs naturales de cacao",
                "Cacao",
                new BigDecimal("6.40"),
                List.of("pedidos@agrosmart.com")
        );

        Producto transformado =
                ProductoFilters.A_MAYUSCULAS.apply(original);

        assertNotSame(original, transformado);

        assertEquals(
                "Nibs naturales de cacao",
                original.getNombre()
        );

        assertEquals(
                "NIBS NATURALES DE CACAO",
                transformado.getNombre()
        );

        assertEquals(original.getId(), transformado.getId());
        assertEquals(
                original.getCategoria(),
                transformado.getCategoria()
        );
        assertEquals(
                original.getPrecioUsd(),
                transformado.getPrecioUsd()
        );
        assertEquals(
                original.getCorreosNotificacion(),
                transformado.getCorreosNotificacion()
        );
    }
}