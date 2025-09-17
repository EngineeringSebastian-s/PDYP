package poli.engineering.sudoku.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poli.engineering.sudoku.Service.ISudokuService;
import poli.engineering.sudoku.Service.SudokuService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

@Configuration
public class RmiServerConfig {

    @Bean
    public ISudokuService sudokuServiceRmi() throws RemoteException, MalformedURLException {
        try {
            // Iniciar el registro en el puerto 1099
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("📡 Registro RMI creado en 1099");
            } catch (ExportException e) {
                System.out.println("📡 Registro RMI ya existe en 1099");
            }

            // Crear la implementación remota
            ISudokuService service = new SudokuService();

            // Publicarla con el nombre "SudokuService"
            Naming.rebind("SudokuService", service);

            System.out.println("✅ SudokuService RMI registrado en el puerto 1099");

            return service;
        } catch (Exception e) {
            System.err.println("❌ Error al registrar SudokuService RMI: " + e.getMessage());
            throw e;
        }
    }
}
