package com.hostal.api.controller;

import com.hostal.api.entity.Review;
import com.hostal.api.entity.Usuario;
import com.hostal.api.entity.Producto;
import com.hostal.api.repository.ReviewRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Gestión de reseñas de productos")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @Operation(summary = "Listar todas las reviews")
    public ResponseEntity<Page<Review>> listarReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener review por ID")
    public ResponseEntity<Review> obtenerReview(@PathVariable Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener reviews por producto")
    public ResponseEntity<List<Review>> obtenerReviewsPorProducto(@PathVariable Long productoId) {
        List<Review> reviews = reviewRepository.findByProductoId(productoId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener reviews por usuario")
    public ResponseEntity<List<Review>> obtenerReviewsPorUsuario(@PathVariable Long usuarioId) {
        List<Review> reviews = reviewRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/calificacion/{calificacion}")
    @Operation(summary = "Obtener reviews por calificación")
    public ResponseEntity<List<Review>> obtenerReviewsPorCalificacion(@PathVariable Integer calificacion) {
        List<Review> reviews = reviewRepository.findByCalificacion(calificacion);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @Operation(summary = "Crear nueva review")
    public ResponseEntity<?> crearReview(@Valid @RequestBody CreateReviewRequest request) {
        // Verificar que el usuario existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        // Verificar que el producto existe
        Optional<Producto> productoOpt = productoRepository.findById(request.getProductoId());
        if (!productoOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Producto no encontrado");
        }

        Review review = new Review();
        review.setUsuario(usuarioOpt.get());
        review.setProducto(productoOpt.get());
        review.setCalificacion(request.getCalificacion());
        review.setComentario(request.getComentario());
        review.setFechaCreacion(LocalDateTime.now());

        Review reviewGuardada = reviewRepository.save(review);
        return ResponseEntity.ok(reviewGuardada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar review existente")
    public ResponseEntity<?> actualizarReview(@PathVariable Long id, @Valid @RequestBody UpdateReviewRequest request) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setCalificacion(request.getCalificacion());
            review.setComentario(request.getComentario());
            
            Review reviewActualizada = reviewRepository.save(review);
            return ResponseEntity.ok(reviewActualizada);
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar review")
    public ResponseEntity<Void> eliminarReview(@PathVariable Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    // DTOs para requests
    public static class CreateReviewRequest {
        private Long usuarioId;
        private Long productoId;
        private Integer calificacion;
        private String comentario;

        // Getters y Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        
        public Integer getCalificacion() { return calificacion; }
        public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
        
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }

    public static class UpdateReviewRequest {
        private Integer calificacion;
        private String comentario;

        // Getters y Setters
        public Integer getCalificacion() { return calificacion; }
        public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
        
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }
}