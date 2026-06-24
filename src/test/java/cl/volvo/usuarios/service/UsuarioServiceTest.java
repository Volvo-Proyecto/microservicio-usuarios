package cl.volvo.usuarios.service;

import cl.volvo.usuarios.dto.UsuarioRequestDTO;
import cl.volvo.usuarios.dto.UsuarioResponseDTO;
import cl.volvo.usuarios.model.Usuario;
import cl.volvo.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Configuramos los datos base para cada prueba
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setUsername("gamerPro99");
        usuarioMock.setEmail("gamer99@volvo.cl");
        usuarioMock.setContrasena("secreta123");
        usuarioMock.setFechaRegistro(LocalDate.now());

        requestDTO = new UsuarioRequestDTO();
        requestDTO.setUsername("gamerPro99");
        requestDTO.setEmail("gamer99@volvo.cl");
        requestDTO.setContrasena("secreta123");
    }

    @Test
    void registrarUsuario_Exitoso() {
        // Given
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByUsername(requestDTO.getUsername())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // When
        UsuarioResponseDTO response = usuarioService.registrarUsuario(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("gamerPro99", response.getUsername());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_LanzaExcepcion_SiEmailYaExiste() {
        // Given
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(requestDTO);
        });

        assertEquals("El correo ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_LanzaExcepcion_SiUsernameYaExiste() {
        // Given
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByUsername(requestDTO.getUsername())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(requestDTO);
        });

        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void obtenerPorId_Exitoso() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // When
        UsuarioResponseDTO response = usuarioService.obtenerPorId(1L);

        // Then
        assertNotNull(response);
        assertEquals("gamer99@volvo.cl", response.getEmail());
    }

    @Test
    void eliminarUsuario_Exitoso() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // When
        usuarioService.eliminarUsuario(1L);

        // Then
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}