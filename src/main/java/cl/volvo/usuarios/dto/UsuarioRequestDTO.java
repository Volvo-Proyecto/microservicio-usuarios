package cl.volvo.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Objeto que representa los datos necesarios para crear o actualizar un usuario")
public class UsuarioRequestDTO {

    @Schema(description = "Nombre de usuario único", example = "gamerPro99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "gamer99@volvo.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un formato de correo válido")
    private String email;

    @Schema(description = "Contraseña de acceso al sistema", example = "MiClaveSecreta123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
}