package com.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Clase principal que lanza la aplicación de Sudoku 6x6.
 * Extiende de {@link Application} para utilizar el ciclo de vida de una aplicación JavaFX.
 */
public class Main extends Application {

    /**
     * Inicia la aplicación y configura la escena principal del juego.
     *
     * @param stage Es el escenario principal de la aplicación.
     */
    @Override
    public void start(Stage stage) {
        try {
            // Cargamos el archivo FXML
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Sudoku-View.fxml"));
            Scene scene = new Scene(loader.load());

            // Verificamos y cargamos el archivo CSS
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("/com/example/sudoku/styles/style.css")
                    ).toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("SUDOKU 6x6");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo FXML.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error: No se pudo cargar el archivo CSS.");
            e.printStackTrace();
        }
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
