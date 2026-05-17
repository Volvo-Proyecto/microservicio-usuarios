package cl.volvo.usuarios.service;

import cl.volvo.usuarios.dto.UsuarioRequestDTO;
import cl.volvo.usuarios.dto.UsuarioResponseDTO;
import cl.volvo.usuarios.model.Usuario;
import cl.volvo.usuarios.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j 
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Crear / Registrar un usuario (CREATE)
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO) {
        log.info("Iniciando registro de usuario con email: {}", requestDTO.getEmail());

        // Validamos si el email ya existe en Laragon
        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("El correo {} ya se encuentra registrado", requestDTO.getEmail());
            throw new RuntimeException("El correo ya está en uso");
        }

        // Validamos si el username ya existe en Laragon
        if (usuarioRepository.existsByUsername(requestDTO.getUsername())) {
            log.warn("El nombre de usuario {} ya existe", requestDTO.getUsername());
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Convertimos el DTO en la Entidad para guardarla en la Base de Datos
        Usuario usuario = new Usuario();
        usuario.setUsername(requestDTO.getUsername());
        usuario.setEmail(requestDTO.getEmail());
        usuario.setContrasena(requestDTO.getContrasena()); 

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado con éxito con ID: {}", usuarioGuardado.getId());

        // Convertimos la Entidad guardada en el DTO de respuesta (Sin contraseña)
        return mapearADTO(usuarioGuardado);
    }

    // 2. Obtener todos los usuarios (READ)
    public List<UsuarioResponseDTO> obtenerTodos() {
        log.info("Obteniendo listado de todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // 3. Obtener un usuario por su ID
    public UsuarioResponseDTO obtenerPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con el ID: " + id);
                });
        return mapearADTO(usuario);
    }

    // 4. Actualizar un usuario existente (UPDATE)
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {
        log.info("Actualizando usuario con ID: {}", id);

        // 4.1. Buscamos si el usuario existe en la BD
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se pudo actualizar. El usuario con ID {} no existe.", id);
                    return new RuntimeException("Usuario no encontrado para actualizar");
                });

        // 4.2. Si existe, le pisamos los datos antiguos con los nuevos que vienen en el DTO
        usuarioExistente.setUsername(requestDTO.getUsername());
        usuarioExistente.setEmail(requestDTO.getEmail());
        usuarioExistente.setContrasena(requestDTO.getContrasena());

        // 4.3. Guardamos los cambios
        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario ID: {} actualizado correctamente", id);

        // 4.4. Retornamos el DTO limpio
        return mapearADTO(usuarioGuardado);
    }

    // 5. Eliminar un usuario (DELETE)
    public void eliminarUsuario(Long id) {
        log.info("Iniciando eliminación de usuario con ID: {}", id);

        // Verificamos si existe antes de intentar borrarlo
        if (!usuarioRepository.existsById(id)) {
            log.error("No se puede eliminar. El usuario con ID {} no existe.", id);
            throw new RuntimeException("Usuario no encontrado para eliminar");
        }

        usuarioRepository.deleteById(id);
        log.info("Usuario con ID: {} eliminado de la base de datos", id);
    }

    // Método auxiliar para transformar Entidad -> DTO de salida
    private UsuarioResponseDTO mapearADTO(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setEmail(usuario.getEmail());
        response.setFechaRegistro(usuario.getFechaRegistro());
        return response;
    }
}