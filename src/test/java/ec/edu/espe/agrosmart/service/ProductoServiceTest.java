package ec.edu.espe.agrosmart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.entity.ProductoEntity;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ProductoServiceTest {

    @Test
    void obtenerProductosComercializables_conTresValidosYDosInvalidos_debeEmitirTres() {

        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        Mockito.when(repository.findAll())
                .thenReturn(datosConTresValidosYDosInvalidos());

        ProductoService service =
                new ProductoService(repository, null);

        // Act
        Flux<Producto> flujo =
                service.obtenerProductosComercializables();

        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void obtenerProductosComercializables_conTodosInvalidos_debeEmitirProductoGenerico() {

        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        ProductoEntity precioCero = new ProductoEntity(
                "Producto sin precio",
                BigDecimal.ZERO,
                20,
                "Cacao",
                "control@agrosmart.com"
        );

        ProductoEntity sinCorreo = new ProductoEntity(
                "Producto sin correo",
                new BigDecimal("5.25"),
                15,
                "Cacao",
                ""
        );

        Mockito.when(repository.findAll())
                .thenReturn(List.of(precioCero, sinCorreo));

        ProductoService service =
                new ProductoService(repository, null);

        // Act
        Flux<Producto> flujo =
                service.obtenerProductosComercializables();

        // Assert
        StepVerifier.create(flujo)
                .expectNextMatches(producto ->
                        producto.getId().equals(0L)
                                && producto.getNombre().equals(
                                        "SIN PRODUCTOS COMERCIALIZABLES"
                                )
                                && producto.getPrecioUsd()
                                        .compareTo(BigDecimal.ZERO) == 0
                )
                .verifyComplete();
    }

    @Test
    void buscarPorId_conIdInexistente_debeEmitirProductoNoEncontradoException() {

        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        Mockito.when(repository.findById(9999L))
                .thenReturn(Optional.empty());

        ProductoService service =
                new ProductoService(repository, null);

        // Act
        Mono<Producto> resultado =
                service.buscarPorId(9999L);

        // Assert
        StepVerifier.create(resultado)
                .expectError(ProductoNoEncontradoException.class)
                .verify();
    }

    private List<ProductoEntity> datosConTresValidosYDosInvalidos() {

        ProductoEntity valido1 = new ProductoEntity(
                "Cacao fino de aroma",
                new BigDecimal("8.50"),
                120,
                "Cacao",
                "ventas@agrosmart.com"
        );

        ProductoEntity valido2 = new ProductoEntity(
                "Pasta artesanal de cacao",
                new BigDecimal("12.75"),
                75,
                "Cacao",
                "comercial@agrosmart.com"
        );

        ProductoEntity valido3 = new ProductoEntity(
                "Nibs naturales de cacao",
                new BigDecimal("6.40"),
                95,
                "Cacao",
                "pedidos@agrosmart.com"
        );

        ProductoEntity invalidoPrecio = new ProductoEntity(
                "Cacao de prueba sin precio",
                BigDecimal.ZERO,
                40,
                "Cacao",
                "control@agrosmart.com"
        );

        ProductoEntity invalidoCorreo = new ProductoEntity(
                "Producto de cacao sin contacto",
                new BigDecimal("5.25"),
                25,
                "Cacao",
                ""
        );

        return List.of(
                valido1,
                valido2,
                valido3,
                invalidoPrecio,
                invalidoCorreo
        );
    }
}
