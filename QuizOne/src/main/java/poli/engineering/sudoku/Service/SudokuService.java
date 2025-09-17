package poli.engineering.sudoku.Service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación del servicio de Sudoku que provee la lógica principal para:
 * <ul>
 *   <li>Calcular las dimensiones de los segmentos de un tablero (r × c).</li>
 *   <li>Generar un tablero vacío de tamaño N×N.</li>
 *   <li>Generar un tablero resuelto mediante backtracking.</li>
 *   <li>Validar si un número puede insertarse en una celda.</li>
 * </ul>
 *
 * <p>Esta implementación utiliza un algoritmo clásico de <b>backtracking</b>
 * para resolver el tablero, asegurando que todas las reglas del Sudoku se cumplan:
 * no se repiten números en filas, columnas ni segmentos.</p>
 */
public class SudokuService extends UnicastRemoteObject implements ISudokuService {

    public SudokuService() throws RemoteException {
        super();
    }

    /**
     * Calcula las dimensiones de los segmentos (r, c) de un tablero de Sudoku de tamaño N×N.
     *
     * <p>Si N es un cuadrado perfecto, se retorna {√N, √N}.
     * En caso contrario, se busca el divisor más grande posible de N para formar un rectángulo válido.</p>
     *
     * @param n Tamaño del tablero (N×N).
     * @return Un arreglo {r, c} con las dimensiones del segmento.
     */
    @Override
    public int[] calculateSegmentRC(int n) throws RemoteException {
        int root = (int) Math.floor(Math.sqrt(n));
        if (root * root == n) {
            return new int[]{root, root};
        }
        for (int d = root; d >= 2; d--) {
            if (n % d == 0) {
                return new int[]{d, n / d};
            }
        }
        return new int[]{1, n}; // fallback
    }

    /**
     * Genera un tablero vacío de Sudoku representado como una matriz de Strings.
     * Cada celda se inicializa con el valor "-".
     *
     * @param n Tamaño del tablero (N×N).
     * @return Matriz {@code String[][]} con todas las celdas vacías.
     */
    @Override
    public String[][] generateBoard(int n) throws RemoteException {
        String[][] board = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = "-";
            }
        }
        return board;
    }

    /**
     * Genera un tablero completamente resuelto de Sudoku mediante backtracking.
     *
     * <p>El tablero resultante respeta las reglas del Sudoku:
     * no hay repetición de números en filas, columnas ni segmentos.</p>
     *
     * @param n Tamaño del tablero (N×N).
     * @param r Número de filas en cada segmento (debe cumplirse {@code r × c = n}).
     * @param c Número de columnas en cada segmento (debe cumplirse {@code r × c = n}).
     * @return Una matriz {@code String[][]} representando un tablero resuelto.
     * @throws IllegalArgumentException Si {@code r × c ≠ n}.
     * @throws RuntimeException         Si no se pudo generar una solución válida.
     */
    @Override
    public String[][] generateSolvedBoard(int n, int r, int c) throws RemoteException, IllegalArgumentException {
        if (r * c != n) {
            throw new IllegalArgumentException(
                    "Dimensiones inválidas: r * c debe ser igual a n (" + r + " * " + c + " != " + n + ")"
            );
        }

        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = 0;
            }
        }

        if (solveBoard(board, n, r, c)) {
            String[][] result = new String[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    result[i][j] = String.valueOf(board[i][j]);
                }
            }
            return result;
        } else {
            throw new RuntimeException("No se pudo generar un tablero válido.");
        }
    }

    /**
     * Algoritmo de backtracking para llenar un tablero de Sudoku.
     * Intenta colocar números válidos en las celdas vacías de forma recursiva.
     *
     * @param board Tablero actual en construcción.
     * @param n     Tamaño del tablero.
     * @param r     Número de filas en cada segmento.
     * @param c     Número de columnas en cada segmento.
     * @return true si el tablero pudo resolverse completamente, false en caso contrario.
     */
    private boolean solveBoard(int[][] board, int n, int r, int c) {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] == 0) {
                    List<Integer> nums = getShuffledNumbers(n);
                    for (int num : nums) {
                        if (canInsert(board, row, col, num, r, c)) {
                            board[row][col] = num;

                            if (solveBoard(board, n, r, c)) {
                                return true;
                            }

                            board[row][col] = 0; // backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Genera una lista de números del 1 al N en orden aleatorio.
     * Esto garantiza variación en las soluciones generadas por backtracking.
     *
     * @param n Tamaño máximo de los números.
     * @return Lista mezclada de enteros entre 1 y n.
     */
    private List<Integer> getShuffledNumbers(int n) {
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);
        return nums;
    }

    /**
     * Verifica si un número puede insertarse en una celda dada sin violar
     * las reglas del Sudoku (filas, columnas y segmentos).
     *
     * @param board Tablero actual de Sudoku.
     * @param row   Fila de la celda (0-indexado).
     * @param col   Columna de la celda (0-indexado).
     * @param num   Número a insertar (1..N).
     * @param r     Número de filas en cada segmento.
     * @param c     Número de columnas en cada segmento.
     * @return true si el número puede insertarse, false en caso contrario.
     */
    private boolean canInsert(int[][] board, int row, int col, int num, int r, int c) {
        for (int i = 0; i < board.length; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        for (int i = 0; i < board.length; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        int boxRowStart = row - row % r;
        int boxColStart = col - col % c;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
}