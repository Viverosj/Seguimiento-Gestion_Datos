package co.edu.uniquindio.gestorcontactos.modelo;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@AllArgsConstructor
public class GestionContactos {

    private final List<Contactos> contactos;
    public GestionContactos() {
        contactos = new ArrayList<>();
    }

    public void agregarContacto(String nombre, String apellido, String telefono, LocalDate cumple, String correo, String fotoPerfil) throws Exception {

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || cumple == null || correo.isEmpty() || fotoPerfil.isEmpty() ) {
            throw new Exception("Todos los campos son obligatorios.");
        }
        if (!esTelefonoValido(telefono)) {
            throw new Exception("El teléfono solo puede contener números");
        }
        if (!esCorreoValido(correo)) {
            throw new Exception("El correo no es válido");
        }
        if (existeContacto(nombre, apellido, telefono)) {
            throw new Exception("Ya existe un contacto con el nombre y número ingresado");
        }

        //Creamos el Objeto
        Contactos contacto = Contactos.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre)
                .apellido(apellido)
                .telefono(telefono)
                .cumple(cumple)
                .correo(correo)
                .fotoPerfil(fotoPerfil)
                .build();

        // Se añade a la lista de contactos
        contactos.add(contacto);
    }

    // Método para actualizar
    public void editarContacto(String id, String nombre, String apellido, String telefono, LocalDate cumple, String correo, String fotoPerfil) throws Exception {
        Contactos contactos = buscarContactoPorId(id);
        if (contactos == null) {
            throw new Exception("El contacto con el id especificado no existe.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new Exception("La apellido es obligatorio.");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new Exception("La teléfono es obligatorio.");
        }
        if (cumple == null ) {
            throw new Exception("El Cumpleaños es obligatorio.");
        }
        if (correo == null || correo.isBlank()) {
            throw new Exception("El correo es obligatorio.");
        }
        if (fotoPerfil == null || fotoPerfil.isBlank()) {
            throw new Exception("El url para la foto de perfil es obligatorio.");
        }
        if (!esTelefonoValido(telefono)) {
            throw new Exception("El teléfono solo puede contener números");
        }
        if (!esCorreoValido(correo)) {
            throw new Exception("El correo no es válido");
        }

        // Actualización de campos
        contactos.setNombre(nombre);
        contactos.setApellido(apellido);
        contactos.setTelefono(telefono);
        contactos.setCumple(cumple);
        contactos.setCorreo(correo);
        contactos.setFotoPerfil(fotoPerfil);
    }

    public void eliminarContacto(String id) throws Exception {
        Contactos contacto = buscarContactoPorId(id);
        if (contacto == null) {
            throw new Exception("El contacto con el id especificado no existe.");
        }

        contactos.remove(contacto);
    }

    private Contactos buscarContactoPorId(String id) {
        for (Contactos contactos : contactos) {
            if (contactos.getId().equals(id)) {
                return contactos;
            }
        }
        return null;
    }

    public ArrayList<String> listarOpciones() {
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("NOMBRE");
        categorias.add("TELEFONO");

        return categorias;
    }

    public List<Contactos> buscarContactos(String filtro, String valorBusqueda) throws Exception {
        List<Contactos> contactosFiltrados = new ArrayList<>();

        if (valorBusqueda.isEmpty()) {
            throw new Exception("El valor de búsqueda no puede estar vacío");
        }

        for (Contactos contacto : contactos) {
            switch (filtro.toUpperCase()) {
                case "NOMBRE":
                    if (contacto.getNombre().toLowerCase().contains(valorBusqueda.toLowerCase())) {
                        contactosFiltrados.add(contacto);
                    }
                    break;
                case "TELEFONO":
                    if (contacto.getTelefono().contains(valorBusqueda)) {
                        contactosFiltrados.add(contacto);
                    }
                    break;
                default:
                    throw new Exception("Filtro no válido");
            }
        }

        return contactosFiltrados;
    }

    //Metodo para Validar el correo Electronico
    public static boolean esCorreoValido(String correo) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (correo==null) {
            return false;
        }

        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }
    private boolean esTelefonoValido(String numeroTelefono) {
        if (numeroTelefono.length() != 10) {
            return false;
        }

        // Verifica si todos los caracteres son dígitos (del 0 al 9)
        for (int i = 0; i < numeroTelefono.length(); i++) {
            char numero = numeroTelefono.charAt(i);
            if (numero < '0' || numero > '9') {
                return false;
            }
        }

        return true;
    }

    private boolean existeContacto(String nombre, String apellido, String numeroTelefono) {
        for (Contactos contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre)
                    && contacto.getApellido().equalsIgnoreCase(apellido)
                    && contacto.getTelefono().equals(numeroTelefono)) {
                return true;
            }
        }
        return false;
    }

    public void editarContacto(Contactos contactosOriginal, String nuevoNombre, String nuevoApellido, String nuevoTelefono, LocalDate nuevoCumple, String nuevoCorreo, String nuevaFotoPerfil) {
        contactosOriginal.setNombre(nuevoNombre);
        contactosOriginal.setApellido(nuevoApellido);
        contactosOriginal.setTelefono(nuevoTelefono);
        contactosOriginal.setCumple(nuevoCumple);
        contactosOriginal.setCorreo(nuevoCorreo);
        contactosOriginal.setFotoPerfil(nuevaFotoPerfil);
    }

    public void eliminarContactos(Contactos contacto) {
        contactos.remove(contacto);
    }

    public List<Contactos> listarContactos() {
        return contactos;
    }
}