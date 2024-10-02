package co.edu.uniquindio.gestorcontactos.controlador;
import co.edu.uniquindio.gestorcontactos.modelo.Contactos;
import co.edu.uniquindio.gestorcontactos.modelo.GestionContactos;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class VentanaPrincipalControlador implements Initializable{

    public Button botonEditarContacto;
    public Button botonEliminarContacto;
    public Button botonListarContactos;
    public Button botonBuscarContacto;
    public Button botonAgregarContacto;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private DatePicker txtFechaCumple;
    @FXML
    private ComboBox<String> txtFiltroNomTel;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtNomTel;
    @FXML
    private TextField txtURL;
    @FXML
    private TableView<Contactos> tablaContactos;
    @FXML
    private TableColumn<Contactos, String> colNombre;
    @FXML
    private TableColumn<Contactos, String> colApellido;
    @FXML
    private TableColumn<Contactos, String> colTelefono;
    @FXML
    private TableColumn<Contactos, String> colCumpleanos;
    @FXML
    private TableColumn<Contactos, String> colCorreo;
    @FXML
    private TableColumn<Contactos, String> colPerfil;
    private Contactos contactoSeleccionado;
    private ObservableList<Contactos> contactosObservable;
    private final GestionContactos gestionContactos; //Instancia de la clase NotaPrincipal
    public VentanaPrincipalControlador() {
        gestionContactos = new GestionContactos();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Asignar las propiedades de la nota a las columnas de la tabla
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        colCumpleanos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCumple().toString()));
        colCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));
        colPerfil.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFotoPerfil()));

        //Cargar categorias en el ComboBox
        txtFiltroNomTel.setItems(FXCollections.observableList(gestionContactos.listarOpciones()).sorted());

        //Inicializar lista observable y cargar las notas
        contactosObservable = FXCollections.observableArrayList();
        cargarContactos();

        //Evento clic en la tabla
        tablaContactos.setOnMouseClicked(e -> {
            //Obtener la nota seleccionada
            contactoSeleccionado = tablaContactos.getSelectionModel().getSelectedItem();

            if(contactoSeleccionado != null){
                txtNombre.setText(contactoSeleccionado.getNombre());
                txtApellido.setText(contactoSeleccionado.getApellido());
                txtFechaCumple.setValue(contactoSeleccionado.getCumple());
                txtTelefono.setText(contactoSeleccionado.getTelefono());
                txtCorreo.setText(contactoSeleccionado.getCorreo());
                txtURL.setText(contactoSeleccionado.getFotoPerfil());
            }
        });
    }

    //Agregar Contacto
    public void agregarContacto(){
        try {
            gestionContactos.agregarContacto(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtFechaCumple.getValue(),
                    txtCorreo.getText(),
                    txtURL.getText()
            );

            limpiarCampos();
            actualizarContactos();
            mostrarAlerta("Contacto agregado exitosamente", Alert.AlertType.INFORMATION);
            limpiarCampos();
            tablaContactos.setItems( FXCollections.observableArrayList(gestionContactos.listarContactos()) );
        }catch (Exception ex){
            mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
        }

    }

    //Carga Contacto
    private void cargarContactos() {
        contactosObservable.setAll(gestionContactos.listarContactos());
        tablaContactos.setItems(contactosObservable);
    }
    private void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    //Eliminar contacto
    public void eliminarContacto(){
        if(contactoSeleccionado != null) {
            try {
                gestionContactos.eliminarContacto(contactoSeleccionado.getId());

                limpiarCampos();
                cargarContactos();
                mostrarAlerta("Contacto eliminado exitosamente", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
            }

        }else{
            mostrarAlerta("Selecciona un contacto de la tabla de contactos", Alert.AlertType.WARNING);
        }
    }

    //Edita Contacto
    public void editarContacto(){
        if(contactoSeleccionado != null) {
            try {
                gestionContactos.editarContacto(
                        contactoSeleccionado.getId(),
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtTelefono.getText(),
                        txtFechaCumple.getValue(),
                        txtCorreo.getText(),
                        txtURL.getText()

                );
                limpiarCampos();
                cargarContactos();
                mostrarAlerta("Contacto actualizado exitosamente", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            mostrarAlerta("Selecciona un contacto de la lista de contactos", Alert.AlertType.WARNING);
        }
    }

    //Busca contacto
    public void buscarContacto() {
        String filtro = txtFiltroNomTel.getSelectionModel().getSelectedItem();  // Filtro seleccionado (Nombre o Teléfono)
        String valorBusqueda = txtNomTel.getText();  // Valor ingresado en el campo de búsqueda

        try {
            List<Contactos> contactosFiltrados = gestionContactos.buscarContactos(filtro, valorBusqueda);
            tablaContactos.setItems(FXCollections.observableArrayList(contactosFiltrados));  // Actualizar la tabla con los contactos filtrados
        } catch (Exception e) {
            // Mostrar alerta o mensaje de error en caso de excepción
            System.out.println("Error al buscar contactos: " + e.getMessage());
        }
        limpiarCampos();
    }

    //Lista contacto
    public void ListarContactos() {
        // Obtiene todos los contactos disponibles en el modelo
        List<Contactos> todosLosContactos = gestionContactos.listarContactos();

        // Actualiza la tabla con todos los contactos
        tablaContactos.setItems(FXCollections.observableArrayList(todosLosContactos));
    }

    //Actualiza contacto
    public void actualizarContactos() {
        contactosObservable.setAll(gestionContactos.listarContactos());
    }

    //Limpia los campos de texto del formulario
    private void limpiarCampos(){
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtURL.clear();
        txtFechaCumple.setValue(null);
    }
}