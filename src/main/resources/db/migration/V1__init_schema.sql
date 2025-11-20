-- V1__init_schema.sql
-- Migración inicial: Creación de tablas base

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    full_name VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_due_date (due_date),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos iniciales de prueba
INSERT INTO users (username, email, full_name, active) VALUES
('juan.perez', 'juan.perez@example.com', 'Juan Pérez', TRUE),
('maria.garcia', 'maria.garcia@example.com', 'María García', TRUE),
('carlos.lopez', 'carlos.lopez@example.com', 'Carlos López', TRUE);

INSERT INTO tasks (title, description, status, priority, due_date) VALUES
('Implementar autenticación JWT', 'Agregar seguridad con tokens JWT al API', 'IN_PROGRESS', 'HIGH', '2025-11-25'),
('Crear documentación API', 'Documentar endpoints con Swagger', 'PENDING', 'MEDIUM', '2025-11-30'),
('Optimizar consultas SQL', 'Revisar y optimizar queries lentas', 'PENDING', 'HIGH', '2025-11-22'),
('Configurar CI/CD', 'Setup pipeline de integración continua', 'COMPLETED', 'MEDIUM', '2025-11-15');