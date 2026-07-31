package ec.edu.espe.agrosmart.service;

import java.math.BigDecimal;
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

    public ProductoService(
            ProductoRepository productoRepository
    ) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene los productos desde JPA y los transforma
     * en un flujo reactivo sin bloquear el event loop.
     */
    public Flux<Producto> obtenerProductosComercializables() {

        return Mono.fromCallable(productoRepository::findAll)

                // JPA es bloqueante, por eso se ejecuta
                // en el scheduler boundedElastic.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte la lista obtenida por JPA en un Flux.
                .flatMapMany(Flux::fromIterable)

                // Convierte ProductoEntity en Producto inmutable.
                .map(ProductoMapper::toDominio)

                // Genera un nuevo producto con el nombre en mayúsculas.
                .map(ProductoFilters.A_MAYUSCULAS)

                // Solo conserva productos con precio mayor que cero
                // y al menos un correo de notificación.
                .filter(ProductoFilters.IS_VALID)

                // Registra cada producto procesado sin modificarlo.
                .doOnNext(ProductoFilters.LOG_PRODUCTO)

                // Si ningún producto cumple las condiciones,
                // devuelve un producto genérico.
                .defaultIfEmpty(PRODUCTO_GENERICO);
    }

    /**
     * Busca un producto por su identificador utilizando JPA
     * dentro de boundedElastic.
     */
    public Mono<Producto> buscarPorId(Long id) {

        return Mono.fromCallable(
                        () -> productoRepository.findById(id)
                )

                // findById es una operación bloqueante de JPA.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte el Optional en Mono.
                .flatMap(optional ->
                        Mono.justOrEmpty(optional)
                )

                // Convierte la entidad al modelo inmutable.
                .map(ProductoMapper::toDominio)

                // Si no existe el producto, emite un error reactivo.
                .switchIfEmpty(
                        Mono.error(
                                new ProductoNoEncontradoException(id)
                        )
                );
    }
}