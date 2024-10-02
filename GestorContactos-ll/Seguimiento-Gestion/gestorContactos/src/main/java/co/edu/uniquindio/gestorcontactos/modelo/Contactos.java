package co.edu.uniquindio.gestorcontactos.modelo;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Contactos {

    @EqualsAndHashCode.Include
    private String id;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate cumple;
    private String correo;
    private String fotoPerfil;
}