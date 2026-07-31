package ec.edu.espe.agrosmart.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.domain.ProductoFilters;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
import ec.edu.espe.agrosmart.mapper.ProductoMapper;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ProductoService {

    private static final Producto PRODUCTO_GENERICO =
            new Producto(
                    0L,
                    "SIN PRODUCTOS COMERCIALIZABLES",
                    "Cacao",
                    BigDecimal.ZERO,
                    List.of()
            );

    private final ProductoRepository productoRepository;
    private final AgroSmartAIService aiService;

    public ProductoService(
            ProductoRepository productoRepository,
            AgroSmartAIService aiService
    ) {
        this.productoRepository = productoRepository;
        this.aiService = aiService;
    }

    /**
     * Obtiene los productos desde JPA y los transforma
     * en un flujo reactivo sin bloquear el event loop.
     */
    public Flux<Producto> obtenerProductosComercializables() {

        return Mono.fromCallable(productoRepository::findAll)

                // JPA es bloqueante, por eso se ejecuta
                // en boundedElastic.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte la lista obtenida por JPA en un Flux.
                .flatMapMany(Flux::fromIterable)

                // Convierte ProductoEntity en Producto inmutable.
                .map(ProductoMapper::toDominio)

                // Crea un nuevo producto con el nombre en mayúsculas.
                .map(ProductoFilters.A_MAYUSCULAS)

                // Conserva únicamente los productos válidos.
                .filter(ProductoFilters.IS_VALID)

                // Registra cada producto sin alterar el flujo.
                .doOnNext(ProductoFilters.LOG_PRODUCTO)

                // Emite un producto genérico cuando no queda ninguno.
                .defaultIfEmpty(PRODUCTO_GENERICO);
    }

    /**
     * Busca un producto por su identificador.
     */
    public Mono<Producto> buscarPorId(Long id) {

        return Mono.fromCallable(
                        () -> productoRepository.findById(id)
                )

                // findById es bloqueante porque utiliza JPA.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte Optional<ProductoEntity> en Mono<ProductoEntity>.
                .flatMap(Mono::justOrEmpty)

                // Convierte la entidad al dominio inmutable.
                .map(ProductoMapper::toDominio)

                // Emite un error reactivo cuando no existe el producto.
                .switchIfEmpty(
                        Mono.error(
                                new ProductoNoEncontradoException(id)
                        )
                );
    }

    /**
     * Genera publicidad con LangChain4j sin bloquear
     * los hilos del servidor WebFlux.
     */
    public Mono<String> generarPublicidad(
            String producto,
            String audiencia
    ) {

        return Mono.fromCallable(
                        () -> aiService.generarPublicidad(
                                producto,
                                audiencia
                        )
                )

                // La petición HTTP al modelo de IA es bloqueante.
                .subscribeOn(Schedulers.boundedElastic())

                // Evita esperar indefinidamente al proveedor externo.
                .timeout(Duration.ofSeconds(30))

                // Un fallo del proveedor no debe tumbar el endpoint.
                .onErrorResume(error ->
                        Mono.just(
                                "Publicidad no disponible en este momento ("
                                        + error.getClass().getSimpleName()
                                        + ")"
                        )
                );
    }
}