package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Random;

/**
 * Controlador para el juego de Sudoku 6x6.
 * Gestiona la inicialización del tablero, validación de movimientos,
 * generación aleatoria de juegos, y la interacción del usuario.
 */
public class SudokuController {

    @FXML
    private GridPane sudokuGrid;

    private final int GRID_SIZE = 6; // Tamaño del tablero 6x6
    private final int BLOCK_ROW_SIZE = 2; // Número de filas por bloque
    private final int BLOCK_COL_SIZE = 3; // Número de columnas por bloque

    private final TextField[][] cells = new TextField[GRID_SIZE][GRID_SIZE]; // Celdas del tablero
    private int[][] activeBoardSolution; // Solución del tablero activo

    private final Random random = new Random(); // Generador de números aleatorios
    private boolean gameStarted = false; // Indica si el juego ha comenzado

    /**
     * Tableros predefinidos para generar juegos aleatorios.
     */
    private final List<int[][]> preGeneratedBoards = List.of(
            new int[][]{
                    {6, 1, 3, 5, 2, 4},
                    {4, 5, 2, 1, 3, 6},
                    {5, 3, 6, 2, 4, 1},
                    {2, 4, 1, 3, 6, 5},
                    {1, 2, 4, 6, 5, 3},
                    {3, 6, 5, 4, 1, 2}
            },
            new int[][]{
                    {1, 3, 5, 4, 2, 6},
                    {6, 4, 2, 5, 1, 3},
                    {3, 5, 4, 2, 6, 1},
                    {2, 6, 1, 3, 5, 4},
                    {4, 2, 6, 1, 3, 5},
                    {5, 1, 3, 6, 4, 2}
            },
            new int[][]{
                    {5, 1, 4, 6, 2, 3},
                    {6, 2, 3, 1, 4, 5},
                    {4, 3, 2, 5, 1, 6},
                    {1, 6, 5, 4, 3, 2},
                    {2, 5, 1, 3, 6, 4},
                    {3, 4, 6, 2, 5, 1}
            },
            new int[][]{
                    {3, 4, 2, 6, 5, 1},
                    {1, 6, 5, 2, 4, 3},
                    {5, 2, 3, 4, 1, 6},
                    {6, 1, 4, 3, 2, 5},
                    {2, 3, 1, 5, 6, 4},
                    {4, 5, 6, 1, 3, 2}
            },
            new int[][]{
                    {3, 6, 1, 2, 4, 5},
                    {2, 5, 4, 6, 1, 3},
                    {6, 1, 3, 4, 5, 2},
                    {4, 2, 5, 3, 1, 6},
                    {1, 3, 2, 5, 6, 4},
                    {5, 4, 6, 1, 2, 3}
            }
    );

    /**
     * Inicializa la interfaz y los eventos del juego.
     */
    @FXML
    public void initialize() {
        initializeGrid();
        addListenersToCells();
    }

    /**
     * Inicializa el tablero de Sudoku agregando celdas y asignando colores de bloque.
     */
    private void initializeGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                assignBlockColor(cell, row, col);
                cells[row][col] = cell;
                sudokuGrid.add(cell, col, row);
            }
        }
    }

    /**
     * Asigna colores de bloque a las celdas según su posición.
     *
     * @param cell La celda a la que se le asignará el color.
     * @param row  La fila de la celda.
     * @param col  La columna de la celda.
     */
    private void assignBlockColor(TextField cell, int row, int col) {
        int block = (row / BLOCK_ROW_SIZE) * (GRID_SIZE / BLOCK_COL_SIZE) + (col / BLOCK_COL_SIZE);
        switch (block) {
            case 0 -> cell.getStyleClass().add("cell-blue");
            case 1 -> cell.getStyleClass().add("cell-yellow");
            case 2 -> cell.getStyleClass().add("cell-orange");
            case 3 -> cell.getStyleClass().add("cell-purple");
            case 4 -> cell.getStyleClass().add("cell-brown");
            case 5 -> cell.getStyleClass().add("cell-gray");
        }
        cell.getStyleClass().add("text-field");
    }

    /**
     * Agrega listeners para detectar cambios en las celdas.
     */
    private void addListenersToCells() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int finalRow = row;
                int finalCol = col;
                cells[row][col].textProperty().addListener((observable, oldValue, newValue) -> {
                    handleCellInput(finalRow, finalCol, newValue);
                });
            }
        }
    }

    /**
     * Inicia un nuevo juego limpiando el tablero y generando uno nuevo.
     */
    @FXML
    public void handleStartGame() {
        clearGrid();
        loadRandomBoard();
        gameStarted = true;
    }

    /**
     * Limpia el tablero eliminando texto y resaltados.
     */
    private void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setText("");
                cells[row][col].getStyleClass().removeAll("cell-error", "cell-help");
            }
        }
    }

    /**
     * Carga un tablero aleatorio y muestra algunas celdas iniciales.
     */
    private void loadRandomBoard() {
        activeBoardSolution = preGeneratedBoards.get(random.nextInt(preGeneratedBoards.size()));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (random.nextBoolean()) {
                    cells[row][col].setText(String.valueOf(activeBoardSolution[row][col]));
                }
            }
        }
    }

    /**
     * Revela una celda aleatoria como ayuda.
     */
    @FXML
    public void handleHelp() {
        if (!gameStarted) {
            showAlert("Error", "Inicia un juego primero.");
            return;
        }
        while (true) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            if (cells[row][col].getText().isEmpty()) {
                cells[row][col].setText(String.valueOf(activeBoardSolution[row][col]));
                break;
            }
        }
    }

    /**
     * Maneja las entradas del usuario en las celdas.
     */
    @FXML
    public void handleCellInput(int row, int col, String newValue) {
        if (newValue.isEmpty()) {
            cells[row][col].getStyleClass().remove("cell-error");
        } else {
            try {
                int number = Integer.parseInt(newValue);
                if (isValidMove(row, col, number)) {
                    if (isSudokuCompleted()) {
                        showAlert("¡Felicidades!", "Has completado el Sudoku.");
                    }
                } else {
                    cells[row][col].getStyleClass().add("cell-error");
                    showAlert("Error", "Número incorrecto.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Entrada inválida.");
            }
        }
    }

    /**
     * Verifica si el movimiento es válido.
     */
    private boolean isValidMove(int row, int col, int number) {
        return activeBoardSolution[row][col] == number;
    }

    /**
     * Verifica si el Sudoku ha sido completado.
     *
     * @return true si el tablero está completo y es correcto, de lo contrario false.
     */
    private boolean isSudokuCompleted() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getText().isEmpty() ||
                        Integer.parseInt(cells[row][col].getText()) != activeBoardSolution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Muestra un cuadro de diálogo con un mensaje de alerta.
     *
     * @param title   El título del cuadro de diálogo.
     * @param message El mensaje que se mostrará en el cuadro de diálogo.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra una guía del juego en un cuadro de diálogo.
     */
    @FXML
    public void handleGuide() {
        String guideMessage = """
                Saludos jugador! El objetivo del juego es completar 
                una cuadrícula de 6x6 con números del 1 al 6. 
                La cuadrícula está dividida en bloques de 2x3, y debes asegurarte 
                de que cada fila, columna y bloque contenga todos los números sin repetir. 
                ¡Buena suerte!
                """;
        showAlert("Guía del Juego", guideMessage);
    }
}
