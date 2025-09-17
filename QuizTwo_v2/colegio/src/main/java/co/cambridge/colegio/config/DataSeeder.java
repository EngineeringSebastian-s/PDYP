package co.cambridge.colegio.config;

import co.cambridge.colegio.domain.*;
import co.cambridge.colegio.domain.enums.TipoProfesor;
import co.cambridge.colegio.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            AreaRepository areaRepo,
            OficinaRepository oficinaRepo,
            SalonRepository salonRepo,
            ProfesorRepository profesorRepo,
            AdministrativoRepository adminRepo) {
        return args -> {
            // Si ya hay datos, no insertar de nuevo
            if (areaRepo.count() > 0)
                return;

            // Crear Áreas
            Area academica = areaRepo.save(Area.builder().nombre("Académica").build());
            Area administrativa = areaRepo.save(Area.builder().nombre("Administrativa").build());

            // Crear Oficinas
            Oficina o101 = oficinaRepo.save(Oficina.builder().codigo("O-101").area(administrativa).build());
            Oficina o201 = oficinaRepo.save(Oficina.builder().codigo("O-201").area(academica).build());

            // Crear Salones
            salonRepo.save(Salon.builder().codigo("S-301").build());
            salonRepo.save(Salon.builder().codigo("S-302").build());

            // Crear Profesores
            Profesor prof1 = new Profesor();
            prof1.setNombre("Ana Pérez");
            prof1.setDocumento("CC123");
            prof1.setArea(academica);
            prof1.setOficina(o201);
            profesorRepo.save(prof1);

            Profesor prof2 = new Profesor();
            prof2.setNombre("Luis Rodríguez");
            prof2.setDocumento("CC789");
            prof2.setArea(academica);
            prof2.setOficina(o201);
            profesorRepo.save(prof2);

            // Crear Administrativos
            Administrativo adm1 = new Administrativo();
            adm1.setNombre("Carlos Gómez");
            adm1.setDocumento("CC456");
            adm1.setArea(administrativa);
            adm1.setOficina(o101);
            adminRepo.save(adm1);

            Administrativo adm2 = new Administrativo();
            adm2.setNombre("María López");
            adm2.setDocumento("CC987");
            adm2.setArea(administrativa);
            adm2.setOficina(o101);
            adminRepo.save(adm2);

            System.out.println("✅ Datos iniciales insertados en la BD H2");
        };
    }
}
