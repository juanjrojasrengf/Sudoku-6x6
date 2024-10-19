package com.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Clase principal que lanza la aplicación de Sudoku 6x6.
 * Extiende de {@link Application} para utilizar el ciclo de vida de una aplicación JavaFX.
 */

public class Main extends Application {

    /**
     * Inicia la aplicación y configura la escena principal del juego.
     *
     * @param stage Es el escenario principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Sudoku-View.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("SUDOKU 6x6");
        stage.show();
    }
    /**
     * Punto de entrada principal de la aplicación.
     *
     * @param args Argumentos de línea de comando.
     */

    public static void main(String[] args) {
        launch(args);
    }
}
