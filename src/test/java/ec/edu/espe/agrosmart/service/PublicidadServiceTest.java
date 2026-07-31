package ec.edu.espe.agrosmart.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ec.edu.espe.agrosmart.repository.ProductoRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PublicidadServiceTest {

    @Test
    void generarPublicidad_cuandoProveedorResponde_debeEmitirTextoGenerado() {

        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        AgroSmartAIService ia =
                Mockito.mock(AgroSmartAIService.class);

        String publicidadEsperada =
                "Cacao ecuatoriano: calidad premium para Europa.";

        Mockito.when(
                ia.generarPublicidad(
                        "Cacao",
                        "exportadores europeos"
                )
        ).thenReturn(publicidadEsperada);

        ProductoService service =
                new ProductoService(repository, ia);

        // Act
        Mono<String> resultado =
                service.generarPublicidad(
                        "Cacao",
                        "exportadores europeos"
                );

        // Assert
        StepVerifier.create(resultado)
                .expectNext(publicidadEsperada)
                .verifyComplete();
    }

    @Test
    void generarPublicidad_cuandoProveedorFalla_debeEmitirMensajeDeRespaldo() {

        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        AgroSmartAIService ia =
                Mockito.mock(AgroSmartAIService.class);

        Mockito.when(
                ia.generarPublicidad(
                        "Cacao",
                        "exportadores europeos"
                )
        ).thenThrow(
                new RuntimeException("429 Too Many Requests")
        );

        ProductoService service =
                new ProductoService(repository, ia);

        // Act
        Mono<String> resultado =
                service.generarPublicidad(
                        "Cacao",
                        "exportadores europeos"
                );

        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(texto ->
                        texto.contains(
                                "Publicidad no disponible"
                        )
                        && texto.contains("RuntimeException")
                )
                .verifyComplete();
    }
}