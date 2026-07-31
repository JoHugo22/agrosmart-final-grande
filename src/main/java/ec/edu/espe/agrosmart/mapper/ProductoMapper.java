package ec.edu.espe.agrosmart.mapper;

import java.util.Arrays;
import java.util.List;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.entity.ProductoEntity;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toDominio(
            ProductoEntity entity
    ) {

        List<String> correos;

        if (entity.getCorreosNotificacion() == null
                || entity.getCorreosNotificacion().isBlank()) {

            // Cadena vacía significa que no existen correos.
            correos = List.of();

        } else {

            correos = Arrays.stream(
                            entity
                                    .getCorreosNotificacion()
                                    .split(",")
                    )
                    .map(String::trim)
                    .filter(correo -> !correo.isBlank())
                    .toList();
        }

        return new Producto(
                entity.getIdProducto(),
                entity.getNombreProducto(),
                entity.getCategoria(),
                entity.getPrecioUsd(),
                correos
        );
    }
}