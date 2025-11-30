package com.hostal.api.repository;

import com.hostal.api.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con Producto
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Buscar productos activos
     * @param activo Estado activo del producto
     * @return Lista de productos activos
     */
    List<Producto> findByActivo(Boolean activo);
    
    /**
     * Buscar productos por categoría
     * @param categoria Categoría del producto
     * @return Lista de productos de la categoría especificada
     */
    List<Producto> findByCategoria(Producto.CategoriaProducto categoria);
    
    /**
     * Buscar productos activos por categoría
     * @param categoria Categoría del producto
     * @param activo Estado activo del producto
     * @return Lista de productos activos de la categoría
     */
    List<Producto> findByCategoriaAndActivo(Producto.CategoriaProducto categoria, Boolean activo);
    
    /**
     * Buscar productos por nombre que contenga el texto dado
     * @param nombre Parte del nombre a buscar
     * @return Lista de productos que coinciden
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Buscar productos activos por nombre
     * @param nombre Parte del nombre a buscar
     * @param activo Estado activo del producto
     * @return Lista de productos activos que coinciden
     */
    List<Producto> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo);
    
    /**
     * Buscar productos por rango de precios
     * @param precioMinimo Precio mínimo
     * @param precioMaximo Precio máximo
     * @return Lista de productos en el rango de precios
     */
    List<Producto> findByPrecioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo);
    
    /**
     * Buscar productos activos por rango de precios
     * @param precioMinimo Precio mínimo
     * @param precioMaximo Precio máximo
     * @param activo Estado activo del producto
     * @return Lista de productos activos en el rango de precios
     */
    List<Producto> findByPrecioBetweenAndActivo(BigDecimal precioMinimo, BigDecimal precioMaximo, Boolean activo);
    
    /**
     * Buscar productos con stock disponible
     * @param stockMinimo Stock mínimo requerido
     * @return Lista de productos con stock disponible
     */
    List<Producto> findByStockGreaterThanEqual(Integer stockMinimo);
    
    /**
     * Buscar productos activos con stock disponible
     * @param stockMinimo Stock mínimo requerido
     * @param activo Estado activo del producto
     * @return Lista de productos activos con stock disponible
     */
    List<Producto> findByStockGreaterThanEqualAndActivo(Integer stockMinimo, Boolean activo);
    
    /**
     * Buscar productos por capacidad de personas
     * @param capacidad Capacidad de personas
     * @return Lista de productos con la capacidad especificada
     */
    List<Producto> findByCapacidadPersonas(Integer capacidad);
    
    /**
     * Buscar productos activos por capacidad mínima de personas
     * @param capacidadMinima Capacidad mínima de personas
     * @param activo Estado activo del producto
     * @return Lista de productos activos con capacidad mínima
     */
    List<Producto> findByCapacidadPersonasGreaterThanEqualAndActivo(Integer capacidadMinima, Boolean activo);
    
    /**
     * Buscar habitaciones con baño privado
     * @param tieneBanoPrivado Si tiene baño privado
     * @param activo Estado activo del producto
     * @return Lista de habitaciones con/sin baño privado
     */
    List<Producto> findByTieneBanoPrivadoAndActivo(Boolean tieneBanoPrivado, Boolean activo);
    
    /**
     * Buscar habitaciones que incluyen desayuno
     * @param incluyeDesayuno Si incluye desayuno
     * @param activo Estado activo del producto
     * @return Lista de habitaciones que incluyen/no incluyen desayuno
     */
    List<Producto> findByIncluyeDesayunoAndActivo(Boolean incluyeDesayuno, Boolean activo);
    
    /**
     * Buscar productos activos con paginación
     * @param activo Estado activo del producto
     * @param pageable Información de paginación
     * @return Página de productos activos
     */
    Page<Producto> findByActivo(Boolean activo, Pageable pageable);
    
    /**
     * Buscar productos por múltiples criterios con paginación
     * @param categoria Categoría del producto (puede ser null)
     * @param precioMinimo Precio mínimo (puede ser null)
     * @param precioMaximo Precio máximo (puede ser null)
     * @param capacidadMinima Capacidad mínima de personas (puede ser null)
     * @param activo Estado activo del producto
     * @param pageable Información de paginación
     * @return Página de productos que cumplen los criterios
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "(:categoria IS NULL OR p.categoria = :categoria) AND " +
           "(:precioMinimo IS NULL OR p.precio >= :precioMinimo) AND " +
           "(:precioMaximo IS NULL OR p.precio <= :precioMaximo) AND " +
           "(:capacidadMinima IS NULL OR p.capacidadPersonas >= :capacidadMinima) AND " +
           "p.activo = :activo")
    Page<Producto> findByMultiplesCriterios(@Param("categoria") Producto.CategoriaProducto categoria,
                                          @Param("precioMinimo") BigDecimal precioMinimo,
                                          @Param("precioMaximo") BigDecimal precioMaximo,
                                          @Param("capacidadMinima") Integer capacidadMinima,
                                          @Param("activo") Boolean activo,
                                          Pageable pageable);
    
    /**
     * Buscar productos más populares (con más reviews)
     * @param limit Número máximo de productos a retornar
     * @return Lista de productos más populares
     */
    @Query("SELECT p FROM Producto p LEFT JOIN p.reviews r " +
           "WHERE p.activo = true " +
           "GROUP BY p " +
           "ORDER BY COUNT(r) DESC")
    List<Producto> findProductosMasPopulares(Pageable pageable);
    
    /**
     * Buscar productos mejor calificados
     * @param pageable Información de paginación
     * @return Lista de productos mejor calificados
     */
    @Query("SELECT p FROM Producto p LEFT JOIN p.reviews r " +
           "WHERE p.activo = true " +
           "GROUP BY p " +
           "HAVING COUNT(r) > 0 " +
           "ORDER BY AVG(CAST(r.calificacion AS double)) DESC, COUNT(r) DESC")
    List<Producto> findProductosMejorCalificados(Pageable pageable);
    
    /**
     * Contar productos por categoría
     * @param categoria Categoría del producto
     * @return Cantidad de productos en la categoría
     */
    long countByCategoria(Producto.CategoriaProducto categoria);
    
    /**
     * Contar productos activos
     * @param activo Estado activo del producto
     * @return Cantidad de productos activos
     */
    long countByActivo(Boolean activo);
}