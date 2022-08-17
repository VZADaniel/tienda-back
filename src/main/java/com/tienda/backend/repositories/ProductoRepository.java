package com.tienda.backend.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tienda.backend.models.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    public List<Producto> findAllByOrderByStockDesc();

    public Optional<Producto> findByCodigo(String codigo);

    @Modifying
    @Transactional
    public Optional<Producto> deleteByCodigo(String codigo);
}
