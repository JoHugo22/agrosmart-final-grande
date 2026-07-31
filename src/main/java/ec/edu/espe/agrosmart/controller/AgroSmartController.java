package ec.edu.espe.agrosmart.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.service.ProductoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AgroSmartController {

    private final ProductoService productoService;

    public AgroSmartController(
            ProductoService productoService
    ) {
        this.productoService = productoService;
    }

    /**
     * Devuelve los productos comercializables.
     */
    @GetMapping("/productos")
    public Flux<Producto> obtenerProductos() {
        return productoService.obtenerProductosComercializables();
    }

    /**
     * Busca un producto por su identificador.
     */
    @GetMapping("/productos/{id}")
    public Mono<Producto> buscarProducto(
            @PathVariable Long id
    ) {
        return productoService.buscarPorId(id);
    }

    /**
     * Genera una frase publicitaria utilizando LangChain4j.
     */
    @GetMapping(
            value = "/agrosmart/publicidad",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public Mono<String> generarPublicidad(
            @RequestParam String producto,
            @RequestParam String audiencia
    ) {
        return productoService.generarPublicidad(
                producto,
                audiencia
        );
    }
}