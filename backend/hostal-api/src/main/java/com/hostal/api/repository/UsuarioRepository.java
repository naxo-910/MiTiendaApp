package com.hostal.api.repository;

import com.hostal.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Buscar usuario por email
     * @param email Email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verificar si existe un usuario con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Buscar usuarios por rol
     * @param rol Rol del usuario
     * @return Lista de usuarios con el rol especificado
     */
    List<Usuario> findByRol(Usuario.RolUsuario rol);
    
    /**
     * Buscar usuarios activos
     * @param activo Estado activo del usuario
     * @return Lista de usuarios activos
     */
    List<Usuario> findByActivo(Boolean activo);
    
    /**
     * Buscar usuarios por nombre que contenga el texto dado
     * @param nombre Parte del nombre a buscar
     * @return Lista de usuarios que coinciden
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Contar usuarios por rol
     * @param rol Rol del usuario
     * @return Cantidad de usuarios con el rol especificado
     */
    long countByRol(Usuario.RolUsuario rol);
    
    /**
     * Buscar usuarios administradores activos
     * @return Lista de usuarios administradores activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'ADMIN' AND u.activo = true")
    List<Usuario> findAdministradoresActivos();
    
    /**
     * Buscar usuario por email y que esté activo
     * @param email Email del usuario
     * @return Optional con el usuario encontrado si está activo
     */
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.activo = true")
    Optional<Usuario> findByEmailAndActivo(@Param("email") String email);
}