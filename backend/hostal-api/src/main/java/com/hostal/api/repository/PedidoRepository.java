package com.hostal.api.repository;

import com.hostal.api.entity.Pedido;
import com.hostal.api.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos relacionadas con Pedido
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    /**
     * Buscar pedidos por usuario
     * @param usuario Usuario propietario de los pedidos
     * @return Lista de pedidos del usuario
     */
    List<Pedido> findByUsuario(Usuario usuario);
    
    /**
     * Buscar pedidos por usuario ID
     * @param usuarioId ID del usuario
     * @return Lista de pedidos del usuario
     */
    List<Pedido> findByUsuarioId(Long usuarioId);
    
    /**
     * Buscar pedidos por usuario ID con paginación
     * @param usuarioId ID del usuario
     * @param pageable Información de paginación
     * @return Página de pedidos del usuario
     */
    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);
    
    /**
     * Buscar pedidos por estado
     * @param estado Estado del pedido
     * @return Lista de pedidos con el estado especificado
     */
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
    
    /**
     * Buscar pedidos por estado con paginación
     * @param estado Estado del pedido
     * @param pageable Información de paginación
     * @return Página de pedidos con el estado especificado
     */
    Page<Pedido> findByEstado(Pedido.EstadoPedido estado, Pageable pageable);
    
    /**
     * Buscar pedidos por usuario y estado
     * @param usuario Usuario propietario del pedido
     * @param estado Estado del pedido
     * @return Lista de pedidos del usuario con el estado especificado
     */
    List<Pedido> findByUsuarioAndEstado(Usuario usuario, Pedido.EstadoPedido estado);
    
    /**
     * Buscar pedidos por usuario ID y estado
     * @param usuarioId ID del usuario
     * @param estado Estado del pedido
     * @return Lista de pedidos del usuario con el estado especificado
     */
    List<Pedido> findByUsuarioIdAndEstado(Long usuarioId, Pedido.EstadoPedido estado);
    
    /**
     * Buscar pedidos por método de pago
     * @param metodoPago Método de pago utilizado
     * @return Lista de pedidos con el método de pago especificado
     */
    List<Pedido> findByMetodoPago(Pedido.MetodoPago metodoPago);
    
    /**
     * Buscar pedidos por rango de fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de pedidos en el rango de fechas
     */
    List<Pedido> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Buscar pedidos por rango de fechas con paginación
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @param pageable Información de paginación
     * @return Página de pedidos en el rango de fechas
     */
    Page<Pedido> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    
    /**
     * Buscar pedidos por rango de total
     * @param totalMinimo Total mínimo del pedido
     * @param totalMaximo Total máximo del pedido
     * @return Lista de pedidos en el rango de total
     */
    List<Pedido> findByTotalBetween(BigDecimal totalMinimo, BigDecimal totalMaximo);
    
    /**
     * Buscar pedidos recientes ordenados por fecha de creación
     * @param pageable Información de paginación
     * @return Página de pedidos más recientes
     */
    Page<Pedido> findAllByOrderByFechaCreacionDesc(Pageable pageable);
    
    /**
     * Buscar pedidos por usuario ordenados por fecha de creación (más recientes primero)
     * @param usuarioId ID del usuario
     * @param pageable Información de paginación
     * @return Página de pedidos del usuario ordenados por fecha
     */
    Page<Pedido> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId, Pageable pageable);
    
    /**
     * Buscar pedidos pendientes de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de pedidos pendientes del usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId AND p.estado IN ('PENDIENTE', 'CONFIRMADO', 'PROCESANDO') ORDER BY p.fechaCreacion DESC")
    List<Pedido> findPedidosPendientesByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Buscar pedidos completados de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de pedidos completados del usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId AND p.estado IN ('ENTREGADO') ORDER BY p.fechaCreacion DESC")
    List<Pedido> findPedidosCompletadosByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Contar pedidos por usuario
     * @param usuario Usuario
     * @return Cantidad de pedidos del usuario
     */
    long countByUsuario(Usuario usuario);
    
    /**
     * Contar pedidos por usuario ID
     * @param usuarioId ID del usuario
     * @return Cantidad de pedidos del usuario
     */
    long countByUsuarioId(Long usuarioId);
    
    /**
     * Contar pedidos por estado
     * @param estado Estado del pedido
     * @return Cantidad de pedidos con el estado especificado
     */
    long countByEstado(Pedido.EstadoPedido estado);
    
    /**
     * Contar pedidos por usuario y estado
     * @param usuarioId ID del usuario
     * @param estado Estado del pedido
     * @return Cantidad de pedidos del usuario con el estado especificado
     */
    long countByUsuarioIdAndEstado(Long usuarioId, Pedido.EstadoPedido estado);
    
    /**
     * Obtener total de ventas por rango de fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Total de ventas en el periodo
     */
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin AND p.estado NOT IN ('CANCELADO', 'REEMBOLSADO')")
    BigDecimal calcularTotalVentasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Obtener estadísticas de pedidos por estado
     * @return Lista con el conteo de pedidos por cada estado
     */
    @Query("SELECT p.estado, COUNT(p) FROM Pedido p GROUP BY p.estado")
    List<Object[]> obtenerEstadisticasPorEstado();
    
    /**
     * Buscar pedidos por múltiples criterios
     * @param usuarioId ID del usuario (puede ser null)
     * @param estado Estado del pedido (puede ser null)
     * @param fechaInicio Fecha de inicio (puede ser null)
     * @param fechaFin Fecha de fin (puede ser null)
     * @param pageable Información de paginación
     * @return Página de pedidos que cumplen los criterios
     */
    @Query("SELECT p FROM Pedido p WHERE " +
           "(:usuarioId IS NULL OR p.usuario.id = :usuarioId) AND " +
           "(:estado IS NULL OR p.estado = :estado) AND " +
           "(:fechaInicio IS NULL OR p.fechaCreacion >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR p.fechaCreacion <= :fechaFin) " +
           "ORDER BY p.fechaCreacion DESC")
    Page<Pedido> findByMultiplesCriterios(@Param("usuarioId") Long usuarioId,
                                        @Param("estado") Pedido.EstadoPedido estado,
                                        @Param("fechaInicio") LocalDateTime fechaInicio,
                                        @Param("fechaFin") LocalDateTime fechaFin,
                                        Pageable pageable);
}