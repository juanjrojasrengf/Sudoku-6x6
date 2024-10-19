package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Random;

/**
 * Controlador del juego Sudoku 6x6. Maneja la lógica del juego, la interacción con el usuario,
 * y la gestión de eventos del tablero.
 */

public class SudokuController {

    @FXML
    private GridPane sudokuGrid;

    private final int GRID_SIZE = 6;
    private final int BLOCK_ROW_SIZE = 2;
    private final int BLOCK_COL_SIZE = 3;

    private final TextField[][] cells = new TextField[GRID_SIZE][GRID_SIZE];
    private final Random random = new Random();

    /**
     * Inicializa la interfaz del juego, configura las celdas del tablero
     * y añade listeners para la entrada del usuario.
     */

    @FXML
    public void initialize() {
        initializeGrid();
        addListenersToCells();
    }
    /**
     * Inicializa el tablero con referencias a las celdas del {@link GridPane}.
     */

    private void initializeGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                TextField cell = (TextField) sudokuGrid.getChildren()
                        .get(row * GRID_SIZE + col);
                cells[row][col] = cell;

                // Prueba: Verificar la asignación
                System.out.println("Asignando TextField en (" + row + ", " + col + ")");
            }
        }
    }
    /**
     * Añade listeners a cada celda del tablero para manejar la entrada del usuario.
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
     * Maneja la acción del botón "Inicio". Limpia el tablero y genera dos números aleatorios.
     */
    @FXML
    public void handleStartGame() {
        clearGrid();
        generateTwoRandomNumbers();
    }

    @FXML
    public void handleHelp() {
        int[] suggestedPosition = findValidPosition();
        if (suggestedPosition != null) {
            int suggestedNumber = findValidNumber(suggestedPosition[0], suggestedPosition[1]);
            if (suggestedNumber != -1) {
                cells[suggestedPosition[0]][suggestedPosition[1]]
                        .setStyle("-fx-background-color: lightgreen;");
                showAlert("Ayuda", "Coloca el número " + suggestedNumber + " en la posición resaltada.");
            }
        }
    }
    /**
     * Limpia el tablero de Sudoku, eliminando cualquier número y restaurando el estilo por defecto.
     */

    private void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setText("");
                cells[row][col].setStyle("");
            }
        }
    }

    /**
     * Genera dos números aleatorios en posiciones válidas del tablero.
     */

    private void generateTwoRandomNumbers() {
        for (int i = 0; i < 2; i++) {
            int number = random.nextInt(6) + 1;
            placeRandomNumber(number);
        }
    }
    /**
     * Coloca un número aleatorio en una posición válida del tablero.
     *
     * @param number El número a colocar.
     */

    private void placeRandomNumber(int number) {
        int row, col;
        do {
            row = random.nextInt(GRID_SIZE);
            col = random.nextInt(GRID_SIZE);
        } while (!cells[row][col].getText().isEmpty() || !isValidMove(row, col, number));

        cells[row][col].setText(String.valueOf(number));
        System.out.println("Generated number: " + number + " at position: (" + row + ", " + col + ")");
    }

    /**
     * Maneja la entrada del usuario en una celda específica.
     *
     * @param row      La fila de la celda.
     * @param col      La columna de la celda.
     * @param newValue El valor ingresado por el usuario.
     */

    private void handleCellInput(int row, int col, String newValue) {
        if (newValue.isEmpty()) {
            cells[row][col].setStyle("");
        } else {
            try {
                int number = Integer.parseInt(newValue);
                if (isValidMove(row, col, number)) {
                    cells[row][col].setStyle("");
                } else {
                    System.out.println("Invalid move: " + number + " at (" + row + ", " + col + ")");
                    cells[row][col].setStyle("-fx-background-color: lightcoral;");
                    showAlert("Error", "El número " + number + " no puede estar en la posición (" + row + ", " + col + ").");
                }
            } catch (NumberFormatException e) {
                cells[row][col].setStyle("-fx-background-color: lightcoral;");
                showAlert("Error", "Entrada inválida. Debe ser un número entre 1 y 6.");
            }
        }
    }

    /**
     * Verifica si un número puede colocarse en una posición específica del tablero.
     *
     * @param row    La fila de la celda.
     * @param col    La columna de la celda.
     * @param number El número a verificar.
     * @return {@code true} si el movimiento es válido, de lo contrario {@code false}.
     */

    private boolean isValidMove(int row, int col, int number) {
        // Validar la fila
        for (int i = 0; i < GRID_SIZE; i++) {
            if (i != col && cells[row][i].getText().equals(String.valueOf(number))) {
                System.out.println("Número " + number + " ya existe en la fila " + row);
                return false;
            }
        }

        // Validar la columna
        for (int i = 0; i < GRID_SIZE; i++) {
            if (i != row && cells[i][col].getText().equals(String.valueOf(number))) {
                System.out.println("Número " + number + " ya existe en la columna " + col);
                return false;
            }
        }

        // Validar el bloque 2x3
        int blockRow = row - row % BLOCK_ROW_SIZE;
        int blockCol = col - col % BLOCK_COL_SIZE;
        for (int i = 0; i < BLOCK_ROW_SIZE; i++) {
            for (int j = 0; j < BLOCK_COL_SIZE; j++) {
                if ((blockRow + i != row || blockCol + j != col) &&
                        cells[blockRow + i][blockCol + j].getText().equals(String.valueOf(number))) {
                    System.out.println("Número " + number + " ya existe en el bloque.");
                    return false;
                }
            }
        }

        return true;
    }


    private int[] findValidPosition() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    private int findValidNumber(int row, int col) {
        for (int number = 1; number <= GRID_SIZE; number++) {
            if (isValidMove(row, col, number)) {
                return number;
            }
        }
        return -1;
    }
    /**
     * Muestra una alerta con un mensaje
     *
     * @param title   Título de la alerta.
     * @param message Mensaje a mostrar.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
