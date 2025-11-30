package com.hostal.api.repository;

import com.hostal.api.entity.Chat;
import com.hostal.api.entity.Usuario;
import com.hostal.api.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con Chat
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    /**
     * Buscar chats donde participa un usuario específico
     * @param usuarioId ID del usuario
     * @return Lista de chats del usuario
     */
    @Query("SELECT c FROM Chat c WHERE (c.usuario1.id = :usuarioId OR c.usuario2.id = :usuarioId) AND c.activo = true ORDER BY c.fechaUltimoMensaje DESC NULLS LAST, c.fechaCreacion DESC")
    List<Chat> findChatsByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Buscar chats activos donde participa un usuario con paginación
     * @param usuarioId ID del usuario
     * @param activo Estado activo del chat
     * @param pageable Información de paginación
     * @return Página de chats del usuario
     */
    @Query("SELECT c FROM Chat c WHERE (c.usuario1.id = :usuarioId OR c.usuario2.id = :usuarioId) AND c.activo = :activo ORDER BY c.fechaUltimoMensaje DESC NULLS LAST, c.fechaCreacion DESC")
    Page<Chat> findChatsByUsuarioIdAndActivo(@Param("usuarioId") Long usuarioId, @Param("activo") Boolean activo, Pageable pageable);
    
    /**
     * Buscar chat específico entre dos usuarios
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return Optional con el chat encontrado
     */
    @Query("SELECT c FROM Chat c WHERE ((c.usuario1.id = :usuario1Id AND c.usuario2.id = :usuario2Id) OR (c.usuario1.id = :usuario2Id AND c.usuario2.id = :usuario1Id)) AND c.activo = true")
    Optional<Chat> findChatBetweenUsuarios(@Param("usuario1Id") Long usuario1Id, @Param("usuario2Id") Long usuario2Id);
    
    /**
     * Buscar chat específico entre dos usuarios para un producto
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @param productoId ID del producto
     * @return Optional con el chat encontrado
     */
    @Query("SELECT c FROM Chat c WHERE ((c.usuario1.id = :usuario1Id AND c.usuario2.id = :usuario2Id) OR (c.usuario1.id = :usuario2Id AND c.usuario2.id = :usuario1Id)) AND c.producto.id = :productoId AND c.activo = true")
    Optional<Chat> findChatBetweenUsuariosForProducto(@Param("usuario1Id") Long usuario1Id, @Param("usuario2Id") Long usuario2Id, @Param("productoId") Long productoId);
    
    /**
     * Buscar chats por producto
     * @param producto Producto relacionado con los chats
     * @return Lista de chats relacionados con el producto
     */
    List<Chat> findByProducto(Producto producto);
    
    /**
     * Buscar chats activos por producto
     * @param producto Producto relacionado con los chats
     * @param activo Estado activo del chat
     * @return Lista de chats activos relacionados con el producto
     */
    List<Chat> findByProductoAndActivo(Producto producto, Boolean activo);
    
    /**
     * Buscar chats por producto ID
     * @param productoId ID del producto
     * @return Lista de chats relacionados con el producto
     */
    List<Chat> findByProductoId(Long productoId);
    
    /**
     * Buscar chats activos por producto ID
     * @param productoId ID del producto
     * @param activo Estado activo del chat
     * @return Lista de chats activos relacionados con el producto
     */
    List<Chat> findByProductoIdAndActivo(Long productoId, Boolean activo);
    
    /**
     * Buscar chats por usuario1
     * @param usuario1 Primer usuario del chat
     * @return Lista de chats donde el usuario es usuario1
     */
    List<Chat> findByUsuario1(Usuario usuario1);
    
    /**
     * Buscar chats por usuario2
     * @param usuario2 Segundo usuario del chat
     * @return Lista de chats donde el usuario es usuario2
     */
    List<Chat> findByUsuario2(Usuario usuario2);
    
    /**
     * Buscar chats activos
     * @param activo Estado activo del chat
     * @return Lista de chats activos
     */
    List<Chat> findByActivo(Boolean activo);
    
    /**
     * Buscar chats activos con paginación
     * @param activo Estado activo del chat
     * @param pageable Información de paginación
     * @return Página de chats activos
     */
    Page<Chat> findByActivo(Boolean activo, Pageable pageable);
    
    /**
     * Verificar si existe un chat entre dos usuarios
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return true si existe un chat entre los usuarios
     */
    @Query("SELECT COUNT(c) > 0 FROM Chat c WHERE ((c.usuario1.id = :usuario1Id AND c.usuario2.id = :usuario2Id) OR (c.usuario1.id = :usuario2Id AND c.usuario2.id = :usuario1Id)) AND c.activo = true")
    boolean existsChatBetweenUsuarios(@Param("usuario1Id") Long usuario1Id, @Param("usuario2Id") Long usuario2Id);
    
    /**
     * Contar chats por usuario
     * @param usuarioId ID del usuario
     * @return Cantidad de chats del usuario
     */
    @Query("SELECT COUNT(c) FROM Chat c WHERE (c.usuario1.id = :usuarioId OR c.usuario2.id = :usuarioId) AND c.activo = true")
    long countChatsByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Contar chats activos
     * @param activo Estado activo del chat
     * @return Cantidad de chats activos
     */
    long countByActivo(Boolean activo);
    
    /**
     * Buscar chats más recientes del sistema
     * @param pageable Información de paginación
     * @return Página de chats más recientes
     */
    Page<Chat> findAllByActivoTrueOrderByFechaCreacionDesc(Pageable pageable);
    
    /**
     * Buscar chats con actividad reciente
     * @param pageable Información de paginación
     * @return Página de chats ordenados por último mensaje
     */
    @Query("SELECT c FROM Chat c WHERE c.activo = true ORDER BY c.fechaUltimoMensaje DESC NULLS LAST, c.fechaCreacion DESC")
    Page<Chat> findChatsConActividadReciente(Pageable pageable);
    
    /**
     * Buscar chats sin mensajes (nuevos)
     * @return Lista de chats que no tienen mensajes
     */
    @Query("SELECT c FROM Chat c WHERE c.activo = true AND (c.fechaUltimoMensaje IS NULL OR SIZE(c.mensajes) = 0)")
    List<Chat> findChatsSinMensajes();
    
    // Métodos simples para compatibilidad con controladores
    List<Chat> findByUsuario1IdOrUsuario2Id(Long usuario1Id, Long usuario2Id);
    
    Optional<Chat> findByUsuario1AndUsuario2OrUsuario2AndUsuario1(
        Usuario usuario1a, Usuario usuario2a, Usuario usuario1b, Usuario usuario2b);
}