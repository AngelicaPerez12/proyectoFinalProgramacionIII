package com.example.proyectoprogra.controllers.Cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardClienteController {
    @FXML
    void hacerpedido(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/pedido-cliente.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);

            newStage.setMaximized(true);

            newStage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void historial(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/historial-cliente.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);

            newStage.setMaximized(true);

            newStage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void perfil(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectoprogra/Cliente/perfil-cliente.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);

            newStage.setMaximized(true);

            newStage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void vercatalogo(ActionEvent actionEvent) {
        System.out.println("DEBUG: DashboardClienteController.vercatalogo invoked");
        try {
            URL res = getClass().getResource("/com/example/proyectoprogra/Cliente/catalogo-cliente.fxml");
            System.out.println("DEBUG: Resource URL = " + res);
            if (res == null) {
                System.out.println("DEBUG: catalogo-cliente.fxml not found on classpath");
                showCatalogFallback(actionEvent);
                return;
            }

            String content;
            try (InputStream is = res.openStream()) {
                byte[] data = is.readAllBytes();
                content = new String(data, StandardCharsets.UTF_8);
            }

             content = content.replaceAll("@\\.\\./Imagenes/", "@/com/example/proyectoprogra/Imagenes/");

             Pattern p = Pattern.compile("<Label([^>]*?)\\s+text=\"(\\$[^\"]+)\"([^>]*)/\\s*>", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(content);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String before = m.group(1);
                String textVal = m.group(2);
                String after = m.group(3);
                String replacement = "<Label" + before + after + ">" +
                        "<text><String fx:value=\"" + textVal + "\"/></text>" +
                        "</Label>";
                m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            }
            m.appendTail(sb);
            String fixed = sb.toString();

            System.out.println("DEBUG: Fixed FXML preview (first 400 chars):\n" + fixed.substring(0, Math.min(fixed.length(), 400)));

            try (ByteArrayInputStream bais = new ByteArrayInputStream(fixed.getBytes(StandardCharsets.UTF_8))) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(res);
                Parent root = loader.load(bais);

                Stage newStage = new Stage();
                Scene scene = new Scene(root);
                newStage.setScene(scene);

                newStage.setMaximized(true);

                newStage.show();

                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Exception loading catalog FXML: " + e);
            e.printStackTrace();
             showCatalogFallback(actionEvent);
        }

    }

      private void showCatalogFallback(ActionEvent actionEvent) {
        try {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: linear-gradient(to right, #FFF8E7, #FFE4E1);");

            HBox nav = new HBox(12);
            nav.setPadding(new Insets(10, 20, 10, 20));
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/com/example/proyectoprogra/Imagenes/sweetharmony.png")));
            logo.setFitHeight(36);
            logo.setFitWidth(36);
            Label title = new Label("SWEET HARMONY");
            title.setStyle("-fx-font-size:20px; -fx-padding: 0 10 0 8;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button btnCatalog = new Button("Catálogo");
            btnCatalog.setOnAction(e -> {/* ya estamos aquí */});
            Button btnPedido = new Button("Hacer Pedido");
            btnPedido.setOnAction(e -> {
                try { hacerpedido(actionEvent); } catch (Exception ex) { ex.printStackTrace(); }
            });
            Button btnHist = new Button("Historial");
            btnHist.setOnAction(e -> { try { historial(actionEvent); } catch (Exception ex) { ex.printStackTrace(); } });
            Button btnPerfil = new Button("Perfil");
            btnPerfil.setOnAction(e -> { try { perfil(actionEvent); } catch (Exception ex) { ex.printStackTrace(); } });

            nav.getChildren().addAll(logo, title, spacer, btnCatalog, btnPedido, btnHist, btnPerfil);
            root.setTop(nav);

            ScrollPane scroll = new ScrollPane();
            scroll.setFitToWidth(true);
            VBox container = new VBox(20);
            container.setPadding(new Insets(20));

            FlowPane products = new FlowPane();
            products.setHgap(20);
            products.setVgap(20);


            String[] imgs = {"Imagen01.jpg","Pastel02.jpg","Pastel3.jpg","Pastel4.jpg","Pastel6.jpg"};
            String[] names = {"Chocolatoso","Aroma Café","Limonsito","Fresa","Canela y Manzana"};
            String[] prices = {"$25","$40","$25","$25","$35"};

            for (int i = 0; i < imgs.length; i++) {
                VBox card = new VBox(8);
                card.setStyle("-fx-background-color: white; -fx-padding: 12; -fx-border-radius:8; -fx-background-radius:8;");
                card.setPrefWidth(240);
                ImageView iv = null;
                try (InputStream is = getClass().getResourceAsStream("/com/example/proyectoprogra/Imagenes/" + imgs[i])) {
                    if (is != null) {
                        Image img = new Image(is, 210, 180, true, true);
                        iv = new ImageView(img);
                    } else {
                        iv = new ImageView();
                        iv.setFitWidth(210);
                        iv.setFitHeight(180);
                    }
                } catch (Exception ex) {
                    iv = new ImageView();
                    iv.setFitWidth(210);
                    iv.setFitHeight(180);
                }
                Label name = new Label(names[i]);
                name.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");
                Label price = new Label(prices[i]);
                price.setStyle("-fx-font-size:18px; -fx-text-fill: #D4AF37;");
                Button order = new Button("ORDENAR");
                order.setPrefWidth(180);
                card.getChildren().addAll(iv, name, new Label("Delicioso pastel"), price, order);
                products.getChildren().add(card);
            }

            container.getChildren().add(products);
            scroll.setContent(container);
            root.setCenter(scroll);

            Stage newStage = new Stage();
            Scene scene = new Scene(root, 1000, 700);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
