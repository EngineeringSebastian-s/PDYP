# Sudoku

Este proyecto es una **aplicación web de Sudoku** desarrollada con **Spring Boot** y **Thymeleaf**.  

Incluye:  
- Controladores MVC  
- Servicio de Sudoku con backtracking  
- Exposición vía RMI  
- Vistas dinámicas con HTML + CSS  
- Configuración con `application.yml`  

---

## Arquitectura General

El siguiente diagrama muestra la arquitectura de la aplicación, incluyendo frontend, backend, recursos estáticos y cliente externo vía RMI:


<img width="3840" height="1502" alt="Untitled diagram _ Mermaid Chart-2025-08-20-231930" src="https://github.com/user-attachments/assets/b3ec8303-6c67-49c5-a96d-ef760d75f723" />



## Diagrama de clases UML
Este diagrama representa las clases Java principales, sus métodos y relaciones:

```mermaid
classDiagram
    class MenuController {
        +showMenu() String
    }

    class SudokuController {
        +getBoard(size : int) ModelAndView
        +solveBoard(size : int) ModelAndView
    }

    class ISudokuService {
        <<interface>>
        +generateEmptyBoard(size : int) int[][]
        +generateSolvedBoard(size : int) int[][]
        +getSegmentDimensions(size : int) int[]
        +isValidMove(board : int[][], row : int, col : int, num : int) boolean
    }

    class SudokuService {
        +generateEmptyBoard(size : int) int[][]
        +generateSolvedBoard(size : int) int[][]
        +getSegmentDimensions(size : int) int[]
        +isValidMove(board : int[][], row : int, col : int, num : int) boolean
        -backtrackingAlgorithm()
    }

    class RmiServerConfig {
        -port : int = 1099
        +rmiServiceExporter(service : ISudokuService) RmiServiceExporter
    }

    %% Relations
    MenuController --> ISudokuService : uses
    SudokuController --> ISudokuService : uses
    SudokuService ..|> ISudokuService : implements
    RmiServerConfig --> SudokuService : exposes
```
