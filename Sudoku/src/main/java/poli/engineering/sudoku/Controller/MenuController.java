package poli.engineering.sudoku.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador principal de la aplicación.
 * <p>
 * Este controlador gestiona las rutas iniciales del proyecto
 * y redirige al usuario a la vista del menú principal.
 * </p>
 * <p>
 * Anotaciones utilizadas:
 * <ul>
 *   <li>{@code @Controller}: Define la clase como un controlador Spring MVC.</li>
 *   <li>{@code @RequestMapping("/")}: Establece la ruta base del controlador.</li>
 *   <li>{@code @GetMapping}: Define las rutas GET que se atenderán.</li>
 * </ul>
 */
@Controller
@RequestMapping("/")
public class MenuController {

    /**
     * Maneja las solicitudes GET para la raíz ("/") y "/menu".
     * <p>
     * Este método no procesa lógica adicional, únicamente
     * retorna la vista asociada al menú principal de la aplicación.
     * </p>
     *
     * @param model objeto {@link org.springframework.ui.Model}
     *              que permite pasar atributos desde el backend a la vista.
     * @return el nombre de la vista "menu" que será renderizada.
     */
    @GetMapping({"/", "/menu"})
    public String generateSudoku(Model model) {
        return "menu";
    }
}
