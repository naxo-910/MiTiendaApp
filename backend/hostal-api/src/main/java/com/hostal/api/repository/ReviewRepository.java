package com.hostal.api.repository;

import com.hostal.api.entity.Review;
import com.hostal.api.entity.Producto;
import com.hostal.api.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con Review
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    /**
     * Buscar reviews por producto
     * @param producto Producto del cual obtener las reviews
     * @return Lista de reviews del producto
     */
    List<Review> findByProducto(Producto producto);
    
    /**
     * Buscar reviews activas por producto
     * @param producto Producto del cual obtener las reviews
     * @param activa Estado activo de la review
     * @return Lista de reviews activas del producto
     */
    List<Review> findByProductoAndActiva(Producto producto, Boolean activa);
    
    /**
     * Buscar reviews por producto ID
     * @param productoId ID del producto
     * @return Lista de reviews del producto
     */
    List<Review> findByProductoId(Long productoId);
    
    /**
     * Buscar reviews activas por producto ID con paginación
     * @param productoId ID del producto
     * @param activa Estado activo de la review
     * @param pageable Información de paginación
     * @return Página de reviews activas del producto
     */
    Page<Review> findByProductoIdAndActiva(Long productoId, Boolean activa, Pageable pageable);
    
    /**
     * Buscar reviews por usuario
     * @param usuario Usuario que escribió las reviews
     * @return Lista de reviews del usuario
     */
    List<Review> findByUsuario(Usuario usuario);
    
    /**
     * Buscar reviews activas por usuario
     * @param usuario Usuario que escribió las reviews
     * @param activa Estado activo de la review
     * @return Lista de reviews activas del usuario
     */
    List<Review> findByUsuarioAndActiva(Usuario usuario, Boolean activa);
    
    /**
     * Buscar reviews por usuario ID
     * @param usuarioId ID del usuario
     * @return Lista de reviews del usuario
     */
    List<Review> findByUsuarioId(Long usuarioId);
    
    /**
     * Buscar reviews por calificación
     * @param calificacion Calificación de la review
     * @return Lista de reviews con la calificación especificada
     */
    List<Review> findByCalificacion(Integer calificacion);
    
    /**
     * Buscar reviews por producto y calificación
     * @param producto Producto de la review
     * @param calificacion Calificación de la review
     * @param activa Estado activo de la review
     * @return Lista de reviews que coinciden con los criterios
     */
    List<Review> findByProductoAndCalificacionAndActiva(Producto producto, Integer calificacion, Boolean activa);
    
    /**
     * Buscar reviews por rango de calificación
     * @param calificacionMinima Calificación mínima
     * @param calificacionMaxima Calificación máxima
     * @return Lista de reviews en el rango de calificación
     */
    List<Review> findByCalificacionBetween(Integer calificacionMinima, Integer calificacionMaxima);
    
    /**
     * Verificar si un usuario ya escribió una review para un producto
     * @param usuario Usuario
     * @param producto Producto
     * @return true si ya existe una review del usuario para el producto
     */
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    /**
     * Verificar si un usuario ya escribió una review activa para un producto
     * @param usuarioId ID del usuario
     * @param productoId ID del producto
     * @param activa Estado activo de la review
     * @return true si ya existe una review activa del usuario para el producto
     */
    boolean existsByUsuarioIdAndProductoIdAndActiva(Long usuarioId, Long productoId, Boolean activa);
    
    /**
     * Obtener la review específica de un usuario para un producto
     * @param usuario Usuario
     * @param producto Producto
     * @return Optional con la review encontrada
     */
    Optional<Review> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    /**
     * Contar reviews por producto
     * @param producto Producto
     * @return Cantidad de reviews del producto
     */
    long countByProducto(Producto producto);
    
    /**
     * Contar reviews activas por producto
     * @param producto Producto
     * @param activa Estado activo de la review
     * @return Cantidad de reviews activas del producto
     */
    long countByProductoAndActiva(Producto producto, Boolean activa);
    
    /**
     * Contar reviews por producto ID
     * @param productoId ID del producto
     * @return Cantidad de reviews del producto
     */
    long countByProductoId(Long productoId);
    
    /**
     * Obtener calificación promedio por producto
     * @param productoId ID del producto
     * @return Calificación promedio del producto
     */
    @Query("SELECT AVG(CAST(r.calificacion AS double)) FROM Review r WHERE r.producto.id = :productoId AND r.activa = true")
    Double findCalificacionPromedioByProductoId(@Param("productoId") Long productoId);
    
    /**
     * Obtener estadísticas de calificaciones por producto
     * @param productoId ID del producto
     * @return Array con conteo de cada calificación [1-estrella, 2-estrellas, 3-estrellas, 4-estrellas, 5-estrellas]
     */
    @Query("SELECT r.calificacion, COUNT(r) FROM Review r WHERE r.producto.id = :productoId AND r.activa = true GROUP BY r.calificacion ORDER BY r.calificacion")
    List<Object[]> findEstadisticasCalificacionesByProductoId(@Param("productoId") Long productoId);
    
    /**
     * Buscar reviews más recientes por producto
     * @param productoId ID del producto
     * @param activa Estado activo de la review
     * @param pageable Información de paginación
     * @return Página de reviews más recientes del producto
     */
    Page<Review> findByProductoIdAndActivaOrderByFechaCreacionDesc(Long productoId, Boolean activa, Pageable pageable);
    
    /**
     * Buscar reviews más útiles (por calificación alta) por producto
     * @param productoId ID del producto
     * @param activa Estado activo de la review
     * @param pageable Información de paginación
     * @return Página de reviews mejor calificadas del producto
     */
    Page<Review> findByProductoIdAndActivaOrderByCalificacionDesc(Long productoId, Boolean activa, Pageable pageable);
    
    /**
     * Buscar todas las reviews con paginación
     * @param activa Estado activo de las reviews
     * @param pageable Información de paginación
     * @return Página de reviews
     */
    Page<Review> findByActiva(Boolean activa, Pageable pageable);
}