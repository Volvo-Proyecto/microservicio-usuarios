package cl.volvo.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Objeto de respuesta que devuelve los datos públicos del usuario (oculta la contraseña)")
public class UsuarioResponseDTO {

    @Schema(description = "ID único del usuario generado por la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "gamerPro99")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "gamer99@volvo.cl")
    private String email;

    @Schema(description = "Fecha en la que el usuario se registró en el sistema", example = "2026-06-23")
    private LocalDate fechaRegistro;
}