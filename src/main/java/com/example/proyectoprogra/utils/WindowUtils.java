package com.example.proyectoprogra.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utilidad para configurar ventanas de manera consistente en toda la aplicación
 */
public class WindowUtils {

    // Tamaños por defecto
    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 700;
    private static final double MIN_WIDTH = 800;
    private static final double MIN_HEIGHT = 600;

    /**
     * Configura una ventana con los ajustes estándar de la aplicación
     * La ventana se maximizará automáticamente
     * 
     * @param stage La ventana a configurar
     * @param root  El contenido de la ventana
     * @param title El título de la ventana
     */
    public static void setupStage(Stage stage, Parent root, String title) {
        // Crear escena con tamaño por defecto
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Configurar tamaño mínimo
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // Configurar ventana
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(true);

        // IMPORTANTE: Maximizar automáticamente
        stage.setMaximized(true);
    }

    /**
     * Configura una ventana con los ajustes estándar y la muestra
     * 
     * @param stage La ventana a configurar
     * @param root  El contenido de la ventana
     * @param title El título de la ventana
     */
    public static void setupAndShowStage(Stage stage, Parent root, String title) {
        setupStage(stage, root, title);
        stage.show();
    }

    /**
     * Configura una ventana existente para que se maximice
     * 
     * @param stage La ventana a maximizar
     */
    public static void maximizeStage(Stage stage) {
        stage.setMaximized(true);
    }
}
