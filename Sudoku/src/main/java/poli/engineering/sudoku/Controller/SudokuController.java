package poli.engineering.sudoku.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import poli.engineering.sudoku.Service.ISudokuService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Controlador MVC de Sudoku encargado de manejar las peticiones HTTP relacionadas
 * con la generación y resolución de tableros de Sudoku.
 *
 * <p>Expone endpoints accesibles desde la ruta base <b>/sudoku</b> que permiten:
 * <ul>
 *   <li>Generar un tablero vacío de tamaño N×N.</li>
 *   <li>Generar un tablero resuelto automáticamente mediante backtracking.</li>
 * </ul>
 *
 * <p>Los resultados se envían a la vista <b>board.html</b> mediante el objeto {@link Model}.</p>
 */
@Controller
@RequestMapping("/sudoku")
public class SudokuController {

    private final ISudokuService sudokuService;

    public SudokuController(ISudokuService sudokuService) throws MalformedURLException, NotBoundException, RemoteException {
        this.sudokuService = (ISudokuService) Naming.lookup("rmi://localhost:1099/SudokuService");
        System.out.println("🔗 Conectado a SudokuService vía RMI");
    }

    /**
     * Endpoint para generar un tablero vacío de Sudoku de tamaño N×N.
     *
     * <p>Este método calcula automáticamente las dimensiones de los segmentos (r, c),
     * inicializa un tablero vacío y lo envía al modelo para renderizarlo en la vista.</p>
     *
     * @param size  Tamaño del tablero (N×N), obtenido desde la URL.
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return Nombre de la plantilla de la vista a renderizar (board.html).
     */
    @GetMapping("/{size}")
    public String generateSudoku(@PathVariable("size") int size, Model model) throws RemoteException, IllegalArgumentException {
        int[] rc = sudokuService.calculateSegmentRC(size);
        String[][] board = sudokuService.generateBoard(size);

        model.addAttribute("size", size);
        model.addAttribute("rows", size);
        model.addAttribute("cols", size);
        model.addAttribute("r", rc[0]);
        model.addAttribute("c", rc[1]);
        model.addAttribute("board", board);

        return "board";
    }

    /**
     * Endpoint para generar un tablero resuelto de Sudoku de tamaño N×N.
     *
     * <p>Este método calcula las dimensiones de los segmentos (r, c),
     * genera un tablero resuelto con backtracking y lo envía al modelo
     * para mostrarlo en la vista.</p>
     *
     * @param size  Tamaño del tablero (N×N), obtenido desde la URL.
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return Nombre de la plantilla de la vista a renderizar (board.html).
     */
    @GetMapping("/{size}/resolver")
    public String solveSudoku(@PathVariable("size") int size, Model model) throws RemoteException, IllegalArgumentException {
        int[] rc = sudokuService.calculateSegmentRC(size);
        String[][] board = sudokuService.generateSolvedBoard(size, rc[0], rc[1]);

        model.addAttribute("size", size);
        model.addAttribute("rows", size);
        model.addAttribute("cols", size);
        model.addAttribute("r", rc[0]);
        model.addAttribute("c", rc[1]);
        model.addAttribute("board", board);

        return "board";
    }
}