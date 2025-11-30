package com.hostal.api.repository;

import com.hostal.api.entity.Mensaje;
import com.hostal.api.entity.Chat;
import com.hostal.api.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos relacionadas con Mensaje
 */
@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    
    /**
     * Buscar mensajes por chat
     * @param chat Chat al que pertenecen los mensajes
     * @return Lista de mensajes del chat ordenados por fecha
     */
    List<Mensaje> findByChatOrderByFechaEnvioAsc(Chat chat);
    
    /**
     * Buscar mensajes por chat ID
     * @param chatId ID del chat
     * @return Lista de mensajes del chat ordenados por fecha
     */
    List<Mensaje> findByChatIdOrderByFechaEnvioAsc(Long chatId);
    
    /**
     * Buscar mensajes por chat ID con paginación
     * @param chatId ID del chat
     * @param pageable Información de paginación
     * @return Página de mensajes del chat ordenados por fecha descendente
     */
    Page<Mensaje> findByChatIdOrderByFechaEnvioDesc(Long chatId, Pageable pageable);
    
    /**
     * Buscar mensajes por remitente
     * @param remitente Usuario que envió los mensajes
     * @return Lista de mensajes del usuario
     */
    List<Mensaje> findByRemitente(Usuario remitente);
    
    /**
     * Buscar mensajes por remitente ID
     * @param remitenteId ID del usuario que envió los mensajes
     * @return Lista de mensajes del usuario
     */
    List<Mensaje> findByRemitenteId(Long remitenteId);
    
    /**
     * Buscar mensajes no leídos por chat
     * @param chat Chat al que pertenecen los mensajes
     * @param leido Estado de lectura del mensaje
     * @return Lista de mensajes no leídos del chat
     */
    List<Mensaje> findByChatAndLeido(Chat chat, Boolean leido);
    
    /**
     * Buscar mensajes no leídos por chat ID
     * @param chatId ID del chat
     * @param leido Estado de lectura del mensaje
     * @return Lista de mensajes no leídos del chat
     */
    List<Mensaje> findByChatIdAndLeido(Long chatId, Boolean leido);
    
    /**
     * Buscar mensajes no leídos de un usuario en un chat específico
     * @param chatId ID del chat
     * @param remitenteId ID del remitente (para excluir sus propios mensajes)
     * @param leido Estado de lectura del mensaje
     * @return Lista de mensajes no leídos de otros usuarios en el chat
     */
    @Query("SELECT m FROM Mensaje m WHERE m.chat.id = :chatId AND m.remitente.id != :remitenteId AND m.leido = :leido ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findMensajesNoLeidosDeOtrosUsuarios(@Param("chatId") Long chatId, @Param("remitenteId") Long remitenteId, @Param("leido") Boolean leido);
    
    /**
     * Buscar mensajes por rango de fechas
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de mensajes en el rango de fechas
     */
    List<Mensaje> findByFechaEnvioBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Buscar mensajes por chat y rango de fechas
     * @param chatId ID del chat
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de mensajes del chat en el rango de fechas
     */
    List<Mensaje> findByChatIdAndFechaEnvioBetween(Long chatId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Buscar mensajes que contengan texto específico
     * @param contenido Texto a buscar en el contenido
     * @return Lista de mensajes que contienen el texto
     */
    List<Mensaje> findByContenidoContainingIgnoreCase(String contenido);
    
    /**
     * Buscar mensajes de un chat que contengan texto específico
     * @param chatId ID del chat
     * @param contenido Texto a buscar en el contenido
     * @return Lista de mensajes del chat que contienen el texto
     */
    List<Mensaje> findByChatIdAndContenidoContainingIgnoreCase(Long chatId, String contenido);
    
    /**
     * Contar mensajes por chat
     * @param chat Chat del que contar los mensajes
     * @return Cantidad de mensajes en el chat
     */
    long countByChat(Chat chat);
    
    /**
     * Contar mensajes por chat ID
     * @param chatId ID del chat
     * @return Cantidad de mensajes en el chat
     */
    long countByChatId(Long chatId);
    
    /**
     * Contar mensajes no leídos por chat
     * @param chat Chat del que contar los mensajes no leídos
     * @param leido Estado de lectura del mensaje
     * @return Cantidad de mensajes no leídos en el chat
     */
    long countByChatAndLeido(Chat chat, Boolean leido);
    
    /**
     * Contar mensajes no leídos por chat ID
     * @param chatId ID del chat
     * @param leido Estado de lectura del mensaje
     * @return Cantidad de mensajes no leídos en el chat
     */
    long countByChatIdAndLeido(Long chatId, Boolean leido);
    
    /**
     * Contar mensajes no leídos de otros usuarios en un chat
     * @param chatId ID del chat
     * @param usuarioId ID del usuario (para excluir sus propios mensajes)
     * @param leido Estado de lectura del mensaje
     * @return Cantidad de mensajes no leídos de otros usuarios
     */
    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.chat.id = :chatId AND m.remitente.id != :usuarioId AND m.leido = :leido")
    long countMensajesNoLeidosDeOtrosUsuarios(@Param("chatId") Long chatId, @Param("usuarioId") Long usuarioId, @Param("leido") Boolean leido);
    
    /**
     * Obtener el último mensaje de un chat
     * @param chatId ID del chat
     * @return El último mensaje del chat
     */
    @Query("SELECT m FROM Mensaje m WHERE m.chat.id = :chatId ORDER BY m.fechaEnvio DESC LIMIT 1")
    Mensaje findUltimoMensajeByChatId(@Param("chatId") Long chatId);
    
    /**
     * Marcar mensajes como leídos por chat y usuario
     * @param chatId ID del chat
     * @param usuarioId ID del usuario que no debe ser el remitente
     */
    @Modifying
    @Transactional
    @Query("UPDATE Mensaje m SET m.leido = true, m.fechaLeido = CURRENT_TIMESTAMP WHERE m.chat.id = :chatId AND m.remitente.id != :usuarioId AND m.leido = false")
    void marcarMensajesComoLeidos(@Param("chatId") Long chatId, @Param("usuarioId") Long usuarioId);
    
    /**
     * Buscar mensajes más recientes del sistema
     * @param pageable Información de paginación
     * @return Página de mensajes más recientes
     */
    Page<Mensaje> findAllByOrderByFechaEnvioDesc(Pageable pageable);
    
    /**
     * Buscar mensajes de los chats de un usuario
     * @param usuarioId ID del usuario
     * @param pageable Información de paginación
     * @return Página de mensajes de los chats del usuario
     */
    @Query("SELECT m FROM Mensaje m WHERE m.chat.usuario1.id = :usuarioId OR m.chat.usuario2.id = :usuarioId ORDER BY m.fechaEnvio DESC")
    Page<Mensaje> findMensajesByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);
}