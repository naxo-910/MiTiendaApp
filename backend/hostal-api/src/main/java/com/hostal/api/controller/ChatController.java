package com.hostal.api.controller;

import com.hostal.api.entity.Chat;
import com.hostal.api.entity.Mensaje;
import com.hostal.api.entity.Usuario;
import com.hostal.api.repository.ChatRepository;
import com.hostal.api.repository.MensajeRepository;
import com.hostal.api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chats", description = "Gestión de chats y mensajes")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private MensajeRepository mensajeRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // === GESTIÓN DE CHATS ===
    
    @GetMapping
    @Operation(summary = "Listar todos los chats")
    public ResponseEntity<Page<Chat>> listarChats(Pageable pageable) {
        Page<Chat> chats = chatRepository.findAll(pageable);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener chat por ID")
    public ResponseEntity<Chat> obtenerChat(@PathVariable Long id) {
        Optional<Chat> chat = chatRepository.findById(id);
        return chat.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener chats de un usuario")
    public ResponseEntity<List<Chat>> obtenerChatsPorUsuario(@PathVariable Long usuarioId) {
        List<Chat> chats = chatRepository.findByUsuario1IdOrUsuario2Id(usuarioId, usuarioId);
        return ResponseEntity.ok(chats);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo chat")
    public ResponseEntity<?> crearChat(@Valid @RequestBody CreateChatRequest request) {
        // Verificar que ambos usuarios existen
        Optional<Usuario> usuario1Opt = usuarioRepository.findById(request.getUsuario1Id());
        Optional<Usuario> usuario2Opt = usuarioRepository.findById(request.getUsuario2Id());
        
        if (!usuario1Opt.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario 1 no encontrado");
        }
        if (!usuario2Opt.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario 2 no encontrado");
        }

        // Verificar si ya existe un chat entre estos usuarios
        Optional<Chat> chatExistente = chatRepository.findByUsuario1AndUsuario2OrUsuario2AndUsuario1(
            usuario1Opt.get(), usuario2Opt.get(), usuario1Opt.get(), usuario2Opt.get());
            
        if (chatExistente.isPresent()) {
            return ResponseEntity.ok(chatExistente.get());
        }

        Chat chat = new Chat();
        chat.setUsuario1(usuario1Opt.get());
        chat.setUsuario2(usuario2Opt.get());
        chat.setFechaCreacion(LocalDateTime.now());
        chat.setFechaUltimoMensaje(LocalDateTime.now());

        Chat chatGuardado = chatRepository.save(chat);
        return ResponseEntity.ok(chatGuardado);
    }

    @PutMapping("/{id}/actividad")
    @Operation(summary = "Actualizar última actividad del chat")
    public ResponseEntity<Chat> actualizarActividad(@PathVariable Long id) {
        Optional<Chat> chatOpt = chatRepository.findById(id);
        
        if (chatOpt.isPresent()) {
            Chat chat = chatOpt.get();
            chat.setFechaUltimoMensaje(LocalDateTime.now());
            
            Chat chatActualizado = chatRepository.save(chat);
            return ResponseEntity.ok(chatActualizado);
        }
        
        return ResponseEntity.notFound().build();
    }

    // === GESTIÓN DE MENSAJES ===

    @GetMapping("/{chatId}/mensajes")
    @Operation(summary = "Obtener mensajes de un chat")
    public ResponseEntity<Page<Mensaje>> obtenerMensajesChat(@PathVariable Long chatId, Pageable pageable) {
        Page<Mensaje> mensajes = mensajeRepository.findByChatIdOrderByFechaEnvioDesc(chatId, pageable);
        return ResponseEntity.ok(mensajes);
    }

    @PostMapping("/{chatId}/mensajes")
    @Operation(summary = "Enviar nuevo mensaje")
    public ResponseEntity<?> enviarMensaje(@PathVariable Long chatId, @Valid @RequestBody CreateMensajeRequest request) {
        // Verificar que el chat existe
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        if (!chatOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Chat no encontrado");
        }

        // Verificar que el usuario existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getRemitenteId());
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Chat chat = chatOpt.get();
        Usuario emisor = usuarioOpt.get();

        // Verificar que el usuario pertenece al chat
        if (!chat.getUsuario1().getId().equals(emisor.getId()) && 
            !chat.getUsuario2().getId().equals(emisor.getId())) {
            return ResponseEntity.badRequest().body("Usuario no autorizado para este chat");
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setRemitente(emisor);
        mensaje.setContenido(request.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setLeido(false);

        // Actualizar última actividad del chat
        chat.setFechaUltimoMensaje(LocalDateTime.now());
        chatRepository.save(chat);

        Mensaje mensajeGuardado = mensajeRepository.save(mensaje);
        return ResponseEntity.ok(mensajeGuardado);
    }

    @PutMapping("/mensajes/{mensajeId}/leido")
    @Operation(summary = "Marcar mensaje como leído")
    public ResponseEntity<Mensaje> marcarComoLeido(@PathVariable Long mensajeId) {
        Optional<Mensaje> mensajeOpt = mensajeRepository.findById(mensajeId);
        
        if (mensajeOpt.isPresent()) {
            Mensaje mensaje = mensajeOpt.get();
            mensaje.setLeido(true);
            
            Mensaje mensajeActualizado = mensajeRepository.save(mensaje);
            return ResponseEntity.ok(mensajeActualizado);
        }
        
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/mensajes/no-leidos/{chatId}/usuario/{usuarioId}")
    @Operation(summary = "Obtener mensajes no leídos para un usuario en un chat específico")
    public ResponseEntity<List<Mensaje>> obtenerMensajesNoLeidos(@PathVariable Long chatId, @PathVariable Long usuarioId) {
        List<Mensaje> mensajes = mensajeRepository.findMensajesNoLeidosDeOtrosUsuarios(chatId, usuarioId, false);
        return ResponseEntity.ok(mensajes);
    }

    @DeleteMapping("/mensajes/{mensajeId}")
    @Operation(summary = "Eliminar mensaje")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long mensajeId) {
        if (mensajeRepository.existsById(mensajeId)) {
            mensajeRepository.deleteById(mensajeId);
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    // DTOs para requests
    public static class CreateChatRequest {
        private Long usuario1Id;
        private Long usuario2Id;

        // Getters y Setters
        public Long getUsuario1Id() { return usuario1Id; }
        public void setUsuario1Id(Long usuario1Id) { this.usuario1Id = usuario1Id; }
        
        public Long getUsuario2Id() { return usuario2Id; }
        public void setUsuario2Id(Long usuario2Id) { this.usuario2Id = usuario2Id; }
    }

    public static class CreateMensajeRequest {
        private Long remitenteId;
        private String contenido;

        // Getters y Setters
        public Long getRemitenteId() { return remitenteId; }
        public void setRemitenteId(Long remitenteId) { this.remitenteId = remitenteId; }
        
        public String getContenido() { return contenido; }
        public void setContenido(String contenido) { this.contenido = contenido; }
    }
}