package com.hostal.api.controller;

import com.hostal.api.entity.Pedido;
import com.hostal.api.entity.Usuario;
import com.hostal.api.entity.Producto;
import com.hostal.api.repository.PedidoRepository;
import com.hostal.api.repository.UsuarioRepository;
import com.hostal.api.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public ResponseEntity<Page<Pedido>> listarPedidos(Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pedidos por usuario")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pedidos por estado")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorEstado(@PathVariable String estado) {
        Pedido.EstadoPedido estadoEnum = Pedido.EstadoPedido.valueOf(estado.toUpperCase());
        List<Pedido> pedidos = pedidoRepository.findByEstado(estadoEnum);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo pedido")
    public ResponseEntity<?> crearPedido(@Valid @RequestBody CreatePedidoRequest request) {
        // Verificar que el usuario existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        
        // Calcular total basado en los items del request
        BigDecimal total = new BigDecimal(request.getTotal());

        Pedido pedido = new Pedido(usuario, total, Pedido.MetodoPago.valueOf(request.getMetodoPago()));
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedido.setDireccionEntrega(request.getDireccionEntrega());
        pedido.setNotas(request.getNotas());
        pedido.setNumeroHuespedes(request.getNumeroHuespedes());
        pedido.setFechaInicioReserva(request.getFechaInicioReserva());
        pedido.setFechaFinReserva(request.getFechaFinReserva());

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoGuardado);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            Pedido.EstadoPedido estadoAnterior = pedido.getEstado();
            Pedido.EstadoPedido nuevoEstado = Pedido.EstadoPedido.valueOf(estado.toUpperCase());
            pedido.setEstado(nuevoEstado);
            
            Pedido pedidoActualizado = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoActualizado);
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            
            // Solo se puede cancelar si está pendiente o confirmado
            if (pedido.getEstado() == Pedido.EstadoPedido.PENDIENTE || 
                pedido.getEstado() == Pedido.EstadoPedido.CONFIRMADO) {
                pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
                
                pedidoRepository.save(pedido);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("No se puede cancelar un pedido en estado: " + pedido.getEstado().getDisplayName());
            }
        }
        
        return ResponseEntity.notFound().build();
    }

    // DTOs para requests
    public static class CreatePedidoRequest {
        private Long usuarioId;
        private String total;
        private String metodoPago;
        private String direccionEntrega;
        private String notas;
        private Integer numeroHuespedes;
        private LocalDateTime fechaInicioReserva;
        private LocalDateTime fechaFinReserva;

        // Getters y Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }
        
        public String getMetodoPago() { return metodoPago; }
        public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
        
        public String getDireccionEntrega() { return direccionEntrega; }
        public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
        
        public String getNotas() { return notas; }
        public void setNotas(String notas) { this.notas = notas; }
        
        public Integer getNumeroHuespedes() { return numeroHuespedes; }
        public void setNumeroHuespedes(Integer numeroHuespedes) { this.numeroHuespedes = numeroHuespedes; }
        
        public LocalDateTime getFechaInicioReserva() { return fechaInicioReserva; }
        public void setFechaInicioReserva(LocalDateTime fechaInicioReserva) { this.fechaInicioReserva = fechaInicioReserva; }
        
        public LocalDateTime getFechaFinReserva() { return fechaFinReserva; }
        public void setFechaFinReserva(LocalDateTime fechaFinReserva) { this.fechaFinReserva = fechaFinReserva; }
    }
}