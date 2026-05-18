package cl.volvo.usuarios.controller;

import cl.volvo.usuarios.dto.UsuarioRequestDTO;
import cl.volvo.usuarios.dto.UsuarioResponseDTO;
import cl.volvo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v0/usuarios") // Esta será la ruta principal: localhost:8081/usuarios
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para registrar un usuario: http://localhost:8085/api/v0/usuarios/crear
    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        log.info("Petición REST recibida para registrar usuario: {}", requestDTO.getUsername());
        
        UsuarioResponseDTO nuevoUsuario = usuarioService.registrarUsuario(requestDTO);
        
        // Retornamos código 201 (CREATED)
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); 
    }

    // Endpoint para obtener todos los usuarios: http://localhost:8085/api/v0/usuarios/listar
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        log.info("Petición REST recibida para listar usuarios");
        
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios); // Retornamos código 200 (OK)
    }

    // Endpoint para obtener un usuario específico por su ID: http://localhost:8085/api/v0/usuarios/buscar/1
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Petición REST recibida para buscar usuario ID: {}", id);
        
        UsuarioResponseDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario); // Retornamos código 200 (OK)
    }

    // Endpoint para actualizar un usuario: http://localhost:8085/api/v0/usuarios/actualizar/1
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        
        log.info("Petición REST recibida para actualizar el usuario ID: {}", id);
        UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuario(id, requestDTO);
        
        // Retornamos el usuario actualizado con código 200 (OK)
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Endpoint para eliminar un usuario: http://localhost:8085/api/v0/usuarios/eliminar/1
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("Petición REST recibida para eliminar el usuario ID: {}", id);
        usuarioService.eliminarUsuario(id);
        
        // Retornamos código 204 (No Content), que es el estándar ideal para Delete
        return ResponseEntity.noContent().build();
    }
    
}