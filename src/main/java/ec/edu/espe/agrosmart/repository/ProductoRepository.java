package ec.edu.espe.agrosmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.espe.agrosmart.entity.ProductoEntity;

@Repository
public interface ProductoRepository
        extends JpaRepository<ProductoEntity, Long> {
}