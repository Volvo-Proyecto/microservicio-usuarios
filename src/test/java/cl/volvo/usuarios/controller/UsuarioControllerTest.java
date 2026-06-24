package cl.volvo.usuarios.controller;

import cl.volvo.usuarios.dto.UsuarioRequestDTO;
import cl.volvo.usuarios.dto.UsuarioResponseDTO;
import cl.volvo.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioRequestDTO requestDTO;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new UsuarioRequestDTO();
        requestDTO.setUsername("gamerPro99");
        requestDTO.setEmail("gamer99@volvo.cl");
        requestDTO.setContrasena("secreta123");

        responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsername("gamerPro99");
        responseDTO.setEmail("gamer99@volvo.cl");
        responseDTO.setFechaRegistro(LocalDate.now());
    }

    @Test
    void registrarUsuario_DebeRetornar201_CuandoDatosSonValidos() throws Exception {
        // Given
        when(usuarioService.registrarUsuario(any(UsuarioRequestDTO.class))).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("gamerPro99"));
    }

    @Test
    void registrarUsuario_DebeRetornar400_CuandoFaltanDatos() throws Exception {
        // Given (Un DTO inválido sin email ni contraseña)
        UsuarioRequestDTO requestInvalido = new UsuarioRequestDTO();
        requestInvalido.setUsername("incompleto");

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerTodos_DebeRetornar200YLista() throws Exception {
        // Given
        when(usuarioService.obtenerTodos()).thenReturn(List.of(responseDTO));

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value("gamer99@volvo.cl"));
    }

    @Test
    void obtenerPorId_DebeRetornar200_CuandoUsuarioExiste() throws Exception {
        // Given
        when(usuarioService.obtenerPorId(1L)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("gamerPro99"));
    }

    @Test
    void actualizarUsuario_DebeRetornar200_CuandoDatosSonValidos() throws Exception {
        // Given
        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioRequestDTO.class))).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void eliminarUsuario_DebeRetornar204() throws Exception {
        // Given
        doNothing().when(usuarioService).eliminarUsuario(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}