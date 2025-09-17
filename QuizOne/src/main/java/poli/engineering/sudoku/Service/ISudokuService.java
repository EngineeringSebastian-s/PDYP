package poli.engineering.sudoku.Service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz que define los métodos esenciales para la gestión de la lógica de un Sudoku,
 * incluyendo la generación de tableros vacíos y resueltos, así como la validación
 * y el cálculo de las dimensiones de los segmentos.
 *
 * <p>Un tablero de Sudoku siempre es de tamaño {@code N×N}, donde {@code N}
 * puede variar (ej: 4, 6, 9, 16, 25...). Los segmentos o "subcuadros" se
 * definen como divisiones rectangulares de tamaño {@code r×c}, cumpliendo
 * siempre que {@code r × c = N}.</p>
 *
 * <p>Ejemplos comunes:</p>
 * <ul>
 *   <li>N=9 → segmentos de 3×3.</li>
 *   <li>N=16 → segmentos de 4×4.</li>
 *   <li>N=6 → segmentos de 2×3.</li>
 *   <li>N=32 → segmentos de 4×8 o 8×4 (dependiendo de la división elegida).</li>
 * </ul>
 */
public interface ISudokuService extends Remote {

    /**
     * Calcula las dimensiones de los segmentos de un tablero de Sudoku de tamaño {@code N×N}.
     *
     * <p>El objetivo es determinar las dimensiones válidas {@code r} (filas) y {@code c} (columnas)
     * de cada segmento, de manera que se cumpla siempre {@code r × c = N}.</p>
     *
     * <p>Ejemplo:</p>
     * <pre>
     * N = 9  → devuelve [3, 3]
     * N = 16 → devuelve [4, 4]
     * N = 6  → devuelve [2, 3]
     * </pre>
     *
     * @param n Tamaño del tablero de Sudoku (debe ser un entero positivo).
     * @return Un array de dos enteros: {@code [r, c]} indicando las dimensiones del segmento.
     */
    int[] calculateSegmentRC(int n) throws RemoteException;

    /**
     * Genera un tablero vacío de Sudoku de tamaño {@code N×N}.
     *
     * <p>Cada celda del tablero será inicializada con una cadena vacía ({@code ""}),
     * lista para ser llenada posteriormente.</p>
     *
     * @param n Tamaño del tablero (N×N).
     * @return Una matriz {@code String[][]} representando un tablero vacío.
     */
    String[][] generateBoard(int n) throws RemoteException;

    /**
     * Genera un tablero de Sudoku completamente resuelto de tamaño {@code N×N},
     * utilizando un algoritmo de <strong>backtracking</strong>.
     *
     * <p>Este método asegura que se respeten todas las reglas del Sudoku:</p>
     * <ul>
     *   <li>No hay números repetidos en una misma fila.</li>
     *   <li>No hay números repetidos en una misma columna.</li>
     *   <li>No hay números repetidos en un mismo segmento {@code r×c}.</li>
     * </ul>
     *
     * <p>Ejemplo:</p>
     * <pre>
     * generateSolvedBoard(9, 3, 3) → genera un Sudoku clásico 9×9.
     * generateSolvedBoard(6, 2, 3) → genera un Sudoku rectangular 6×6 con bloques 2×3.
     * </pre>
     *
     * @param size Tamaño del tablero (N×N).
     * @param r    Número de filas de cada segmento (debe cumplir {@code r × c = size}).
     * @param c    Número de columnas de cada segmento (debe cumplir {@code r × c = size}).
     * @return Una matriz {@code String[][]} representando un tablero de Sudoku resuelto.
     * @throws IllegalArgumentException si {@code r × c ≠ size}.
     */
    String[][] generateSolvedBoard(int size, int r, int c) throws RemoteException, IllegalArgumentException;
}
