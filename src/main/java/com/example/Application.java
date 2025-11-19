

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          🏗️  ARQUITECTURA HEXAGONAL - INICIADA  🏗️          ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  🚀 Servidor corriendo en: http://localhost:8080            ║\n" +
                "║                                                              ║\n" +
                "║  📋 ENDPOINTS CRUD:                                          ║\n" +
                "║  POST   /api/usuarios      → Crear usuario                  ║\n" +
                "║  GET    /api/usuarios      → Obtener todos                  ║\n" +
                "║  GET    /api/usuarios/{id} → Obtener por ID                 ║\n" +
                "║  DELETE /api/usuarios/{id} → Eliminar usuario               ║\n" +
                "║                                                              ║\n" +
                "║  🧪 EJEMPLO CURL:                                            ║\n" +
                "║  curl -X POST http://localhost:8080/api/usuarios \\          ║\n" +
                "║    -H \"Content-Type: application/json\" \\                   ║\n" +
                "║    -d '{\"username\":\"test\",\"password\":\"123\",\"role\":\"USER\"}' ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");
    }
}