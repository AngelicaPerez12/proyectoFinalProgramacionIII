package com.example.proyectoprogra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.example.proyectoprogra.controllers.LoginController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        
        String[] resourceCandidates = new String[] {
                "/com/example/proyectoprogra/Styles/fonts/PlayfairDisplay-VariableFont_wght.ttf",
                "/com/example/proyectoprogra/Styles/fonts/Roboto-VariableFont_wdth,wght.ttf"
        };

        String[] fileCandidates = new String[] {
                "src/main/resources/com/example/proyectoprogra/Styles/fonts/PlayfairDisplay-VariableFont_wght.ttf",
                "src/main/resources/com/example/proyectoprogra/Styles/fonts/Roboto-VariableFont_wdth,wght.ttf",
                "PlayfairDisplay-VariableFont_wght.ttf",
                "Roboto-VariableFont_wdth,wght.ttf"
        };

        
        for (String resPath : resourceCandidates) {
            try (InputStream is = HelloApplication.class.getResourceAsStream(resPath)) {
                if (is != null) {
                    Font f = Font.loadFont(is, 12);
                    System.out.println("Fuente cargada desde recurso: " + resPath + " -> " + (f != null ? f.getName() : "<unknown>") );
                }
            } catch (Exception e) {
                System.err.println("No se pudo cargar fuente desde recurso " + resPath + ": " + e.getMessage());
            }
        }

        for (String filePath : fileCandidates) {
            File fpath = new File(filePath);
            if (fpath.exists()) {
                try (InputStream fis = new FileInputStream(fpath)) {
                    Font f = Font.loadFont(fis, 12);
                    System.out.println("Fuente cargada desde archivo: " + fpath.getPath() + " -> " + (f != null ? f.getName() : "<unknown>") );
                } catch (Exception e) {
                    System.err.println("No se pudo cargar fuente desde archivo " + fpath.getPath() + ": " + e.getMessage());
                }
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        URL cssUrl = HelloApplication.class.getResource("/com/example/proyectoprogra/Styles/login.css");
        if (cssUrl != null) {
            String external = cssUrl.toExternalForm();
            scene.getStylesheets().add(external);
            System.out.println("Se agregó stylesheet explícito: " + external);
        } else {
            System.err.println("No se encontró /com/example/proyectoprogra/Styles/login.css como recurso.");
        }

        stage.setTitle("Login - Pastelería");
        stage.setScene(scene);

        System.out.println("Hojas de estilo en la escena:");
        scene.getStylesheets().forEach(s -> System.out.println("  - " + s));

        LoginController controller = fxmlLoader.getController();
        controller.configurarStage(stage);

        stage.show();
    }
}

