package cl.volvo.usuarios.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDate fechaRegistro;
}
