package com.example.proyectoprogra.controllers.Admin;

import com.example.proyectoprogra.DAO.ReportesDao;
import com.example.proyectoprogra.models.Historial;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        // Traer solo los pedidos Listos y Entregados
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

    public void informacionUsuario(ActionEvent actionEvent) {
        // Implementación futura
    }

    // Cambiar vista sin forzar maximizado
    private void cambiarVista(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);


            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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

    public void convertirpdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(tablaReportes.getScene().getWindow());

        if (file != null) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                PdfPTable pdfTable = new PdfPTable(tablaReportes.getColumns().size());

                for (TableColumn<Historial, ?> column : tablaReportes.getColumns()) {
                    pdfTable.addCell(column.getText());
                }

                for (Historial item : tablaReportes.getItems()) {
                    pdfTable.addCell(String.valueOf(item.getIdPedido()));
                    pdfTable.addCell(item.getCliente());
                    pdfTable.addCell(String.valueOf(item.getFechaPedido()));
                    pdfTable.addCell(String.valueOf(item.getFechaEntrega()));
                    pdfTable.addCell(item.getEstado());
                    pdfTable.addCell(item.getPastel());
                    pdfTable.addCell(String.valueOf(item.getCantidad()));
                    pdfTable.addCell(String.valueOf(item.getPrecio()));
                    pdfTable.addCell(String.valueOf(item.getTotal()));
                }

                document.add(pdfTable);
                document.close();
                System.out.println("PDF generado exitosamente.");

            } catch (DocumentException | java.io.IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void convertirexcel(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(tablaReportes.getScene().getWindow());

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Reportes");

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tablaReportes.getColumns().size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tablaReportes.getColumns().get(i).getText());
                }

                int rowNum = 1;
                for (Historial item : tablaReportes.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getIdPedido());
                    row.createCell(1).setCellValue(item.getCliente());
                    row.createCell(2).setCellValue(item.getFechaPedido());
                    row.createCell(3).setCellValue(item.getFechaEntrega());
                    row.createCell(4).setCellValue(item.getEstado());
                    row.createCell(5).setCellValue(item.getPastel());
                    row.createCell(6).setCellValue(item.getCantidad());
                    row.createCell(7).setCellValue(item.getPrecio());
                    row.createCell(8).setCellValue(item.getTotal());
                }

                for (int i = 0; i < tablaReportes.getColumns().size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();
                System.out.println("Excel generado exitosamente.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
