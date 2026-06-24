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

// IMPORTACIONES DE SWAGGER / OPENAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios") // Esta será la ruta principal: localhost:8085/api/v1/usuarios
@Tag(name = "Controlador de Usuarios", description = "Endpoints para la gestión, registro y administración de los usuarios del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // POST http://localhost:8085/api/v1/usuarios
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en la base de datos validando los datos de entrada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UsuarioResponseDTO.class),
            examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"username\": \"gamerPro99\",\n  \"email\": \"gamer99@volvo.cl\"\n}"))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        log.info("Petición REST recibida para registrar usuario: {}", requestDTO.getUsername());
        
        UsuarioResponseDTO nuevoUsuario = usuarioService.registrarUsuario(requestDTO);
        
        // Retornamos código 201 (CREATED)
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); 
    }

    // GET http://localhost:8085/api/v1/usuarios
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista completa de todos los usuarios registrados en la plataforma.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito",
            content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)),
            examples = @ExampleObject(value = "[\n  {\n    \"id\": 1,\n    \"username\": \"gamerPro99\",\n    \"email\": \"gamer99@volvo.cl\"\n  }\n]"))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios registrados", content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        log.info("Petición REST recibida para listar usuarios");
        
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retornamos código 204 (NO CONTENT)
        }
        return ResponseEntity.ok(usuarios); // Retornamos código 200 (OK)
    }

    // GET http://localhost:8085/api/v1/usuarios/{id}
    @Operation(summary = "Obtener usuario por ID", description = "Busca y retorna la información de un único usuario utilizando su identificador numérico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UsuarioResponseDTO.class),
            examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"username\": \"gamerPro99\",\n  \"email\": \"gamer99@volvo.cl\"\n}"))),
        @ApiResponse(responseCode = "404", description = "El usuario con el ID proporcionado no existe", content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Petición REST recibida para buscar usuario ID: {}", id);
        
        UsuarioResponseDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario); // Retornamos código 200 (OK)
    }

    // PUT http://localhost:8085/api/v1/usuarios/{id}
    @Operation(summary = "Actualizar un usuario existente", description = "Modifica los datos de un usuario buscando por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado de forma exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UsuarioResponseDTO.class),
            examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"username\": \"gamerPro_Actualizado\",\n  \"email\": \"nuevo_email@volvo.cl\"\n}"))),
        @ApiResponse(responseCode = "404", description = "No se pudo actualizar porque el usuario no existe", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content)
    })
    @PutMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        
        log.info("Petición REST recibida para actualizar el usuario ID: {}", id);
        UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuario(id, requestDTO);
        
        // Retornamos el usuario actualizado con código 200 (OK)
        return ResponseEntity.ok(usuarioActualizado);
    }

    // DELETE http://localhost:8085/api/v1/usuarios/{id}
    @Operation(summary = "Eliminar un usuario", description = "Elimina físicamente a un usuario de la base de datos utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente (Sin contenido)", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se pudo eliminar porque el usuario no existe", content = @Content)
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("Petición REST recibida para eliminar el usuario ID: {}", id);
        usuarioService.eliminarUsuario(id);
        
        // Retornamos código 204 (No Content), que es el estándar ideal para Delete
        return ResponseEntity.noContent().build();
    }
}