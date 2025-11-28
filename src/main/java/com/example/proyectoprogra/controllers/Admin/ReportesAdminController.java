package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.ReportesDao;
import com.example.proyectoprogra.models.Historial;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReportesAdminController {

    @FXML private TableView<Historial> tablaReportes;
    @FXML private TableColumn<Historial, Integer> colIdPedido;
    @FXML private TableColumn<Historial, String> colCliente;
    @FXML private TableColumn<Historial, String> colFechaPedido;
    @FXML private TableColumn<Historial, String> colFechaEntrega;
    @FXML private TableColumn<Historial, String> colEstado;
    @FXML private TableColumn<Historial, String> colPastel;
    @FXML private TableColumn<Historial, Integer> colCantidad;
    @FXML private TableColumn<Historial, Double> colPrecio;
    @FXML private TableColumn<Historial, Double> colTotal;

    @FXML
    public void initialize() {
        cargarTablaReportes();
    }

    private void cargarTablaReportes() {
        ObservableList<Historial> lista = ReportesDao.obtenerPedidosListosEntregados();

        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPastel.setCellValueFactory(new PropertyValueFactory<>("pastel"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tablaReportes.setItems(lista);
    }

    @FXML
    public void informacionUsuario(ActionEvent actionEvent) {
        // Implementación futura
    }

    // Cambiar vista sin forzar maximizado - reemplaza la root de la Scene actual
    private void cambiarVista(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root));
            }

            stage.show();

        } catch (IOException e) {
            System.err.println("Error cargando FXML: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista: " + fxml + "\n" + e.getMessage());
        }
    }


    @FXML
    void historial(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/historial-admin.fxml", e); }

    @FXML
    void inforPasteles(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/pasteles-admin.fxml", e); }

    @FXML
    void pedidos(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/pedidos-admin.fxml", e); }

    @FXML
    void resportes(ActionEvent e) { cambiarVista("/com/example/proyectoprogra/Admin/reportes-admin-view.fxml", e); }

    /**
     * Exporta la tabla a PDF usando reflexión para evitar dependencia directa a iText en tiempo de compilación.
     * Si la biblioteca no está disponible, muestra un mensaje al usuario.
     */
    @FXML
    public void convertirpdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(tablaReportes.getScene().getWindow());

        if (file == null) return;

        try {
            // Cargar clases de iText por reflexión
            Class<?> documentClass = Class.forName("com.itextpdf.text.Document");
            Class<?> pdfPTableClass = Class.forName("com.itextpdf.text.pdf.PdfPTable");
            Class<?> pdfWriterClass = Class.forName("com.itextpdf.text.pdf.PdfWriter");

            Constructor<?> documentCtor = documentClass.getConstructor();
            Object document = documentCtor.newInstance();

            // PdfWriter.getInstance(document, outputStream)
            Method getInstance = pdfWriterClass.getMethod("getInstance", documentClass, OutputStream.class);
            try (OutputStream out = new FileOutputStream(file)) {
                getInstance.invoke(null, document, out);

                Method open = documentClass.getMethod("open");
                open.invoke(document);

                Constructor<?> tableCtor = pdfPTableClass.getConstructor(int.class);
                Object pdfTable = tableCtor.newInstance(tablaReportes.getColumns().size());

                Method addCell = pdfPTableClass.getMethod("addCell", String.class);

                // Cabeceras
                for (TableColumn<Historial, ?> column : tablaReportes.getColumns()) {
                    addCell.invoke(pdfTable, column.getText());
                }

                // Filas
                for (Historial item : tablaReportes.getItems()) {
                    addCell.invoke(pdfTable, String.valueOf(item.getIdPedido()));
                    addCell.invoke(pdfTable, safeString(item.getCliente()));
                    addCell.invoke(pdfTable, safeString(item.getFechaPedido()));
                    addCell.invoke(pdfTable, safeString(item.getFechaEntrega()));
                    addCell.invoke(pdfTable, safeString(item.getEstado()));
                    addCell.invoke(pdfTable, safeString(item.getPastel()));
                    addCell.invoke(pdfTable, String.valueOf(item.getCantidad()));
                    addCell.invoke(pdfTable, String.valueOf(item.getPrecio()));
                    addCell.invoke(pdfTable, String.valueOf(item.getTotal()));
                }

                // document.add(pdfTable) - buscar método add que acepte un parámetro
                Method addMethod = null;
                for (Method m : documentClass.getMethods()) {
                    if (m.getName().equals("add") && m.getParameterCount() == 1) {
                        addMethod = m;
                        break;
                    }
                }
                if (addMethod != null) addMethod.invoke(document, pdfTable);

                Method close = documentClass.getMethod("close");
                close.invoke(document);
            }

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "PDF generado exitosamente.");

        } catch (ClassNotFoundException cnfe) {
            showAlert(Alert.AlertType.ERROR, "Dependencia faltante", "La biblioteca iText no está disponible en el classpath. No se puede exportar a PDF.");
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Error al generar PDF: " + e.getMessage());
        }
    }

    /**
     * Exporta la tabla a Excel (.xlsx) usando reflexión para evitar dependencia directa a Apache POI en compilación.
     */
    @FXML
    public void convertirexcel(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(tablaReportes.getScene().getWindow());

        if (file == null) return;

        try {
            Class<?> xssfClass = Class.forName("org.apache.poi.xssf.usermodel.XSSFWorkbook");

            // Workbook workbook = new XSSFWorkbook();
            Object workbook = xssfClass.getConstructor().newInstance();

            Method createSheet = workbook.getClass().getMethod("createSheet", String.class);
            Object sheet = createSheet.invoke(workbook, "Reportes");

            Method createRow = sheet.getClass().getMethod("createRow", int.class);
            Method createCell = null;

            // Header
            Object headerRow = createRow.invoke(sheet, 0);
            for (int i = 0; i < tablaReportes.getColumns().size(); i++) {
                Object cell = createRow.getDeclaringClass() == null ? null : createRow.invoke(headerRow, i);
                // Fallback: invocar método createCell en la fila por reflexión
                if (cell == null) {
                    Method fallbackCreateCell = headerRow.getClass().getMethod("createCell", int.class);
                    cell = fallbackCreateCell.invoke(headerRow, i);
                }
                Method setCellValue = cell.getClass().getMethod("setCellValue", String.class);
                setCellValue.invoke(cell, tablaReportes.getColumns().get(i).getText());
            }

            // Data rows
            int rowNum = 1;
            for (Historial item : tablaReportes.getItems()) {
                Object row = createRow.invoke(sheet, rowNum++);

                Method createCellOnRow = row.getClass().getMethod("createCell", int.class);
                Object c0 = createCellOnRow.invoke(row, 0);
                Method setValDouble = c0.getClass().getMethod("setCellValue", double.class);
                setValDouble.invoke(c0, (double) item.getIdPedido());

                Object c1 = createCellOnRow.invoke(row, 1);
                Method setValString = c1.getClass().getMethod("setCellValue", String.class);
                setValString.invoke(c1, safeString(item.getCliente()));

                Object c2 = createCellOnRow.invoke(row, 2);
                setValString = c2.getClass().getMethod("setCellValue", String.class);
                setValString.invoke(c2, safeString(item.getFechaPedido()));

                Object c3 = createCellOnRow.invoke(row, 3);
                setValString = c3.getClass().getMethod("setCellValue", String.class);
                setValString.invoke(c3, safeString(item.getFechaEntrega()));

                Object c4 = createCellOnRow.invoke(row, 4);
                setValString = c4.getClass().getMethod("setCellValue", String.class);
                setValString.invoke(c4, safeString(item.getEstado()));

                Object c5 = createCellOnRow.invoke(row, 5);
                setValString = c5.getClass().getMethod("setCellValue", String.class);
                setValString.invoke(c5, safeString(item.getPastel()));

                Object c6 = createCellOnRow.invoke(row, 6);
                Method setValDouble2 = c6.getClass().getMethod("setCellValue", double.class);
                setValDouble2.invoke(c6, (double) item.getCantidad());

                Object c7 = createCellOnRow.invoke(row, 7);
                setValDouble2 = c7.getClass().getMethod("setCellValue", double.class);
                setValDouble2.invoke(c7, item.getPrecio());

                Object c8 = createCellOnRow.invoke(row, 8);
                setValDouble2 = c8.getClass().getMethod("setCellValue", double.class);
                setValDouble2.invoke(c8, item.getTotal());
            }

            // Auto size columns
            for (int i = 0; i < tablaReportes.getColumns().size(); i++) {
                Method autosize = sheet.getClass().getMethod("autoSizeColumn", int.class);
                autosize.invoke(sheet, i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                Method writeMethod = workbook.getClass().getMethod("write", java.io.OutputStream.class);
                writeMethod.invoke(workbook, fileOut);
            }

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Excel generado exitosamente.");

        } catch (ClassNotFoundException cnfe) {
            showAlert(Alert.AlertType.ERROR, "Dependencia faltante", "La biblioteca Apache POI no está disponible en el classpath. No se puede exportar a Excel.");
        } catch (Exception e) {
            System.err.println("Error generando Excel: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Error al generar Excel: " + e.getMessage());
        }
    }

    private String safeString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
