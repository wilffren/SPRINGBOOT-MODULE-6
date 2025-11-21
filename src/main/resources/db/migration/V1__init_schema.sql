-- V1__init_schema.sql
-- Migración inicial: Creación de tablas Venue, Event y Category

CREATE TABLE IF NOT EXISTS venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    location VARCHAR(300) NOT NULL,
    capacity INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_venue_name (name),
    INDEX idx_venue_location (location),
    INDEX idx_venue_capacity (capacity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_event_name (name),
    INDEX idx_event_start_date (start_date),
    INDEX idx_event_status (status),
    INDEX idx_event_dates (start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos de prueba iniciales
INSERT INTO venues (name, location, capacity, description) VALUES
('Teatro Nacional', 'San José, Costa Rica', 1500, 'Principal teatro del país'),
('Auditorio Municipal', 'Heredia, Costa Rica', 800, 'Auditorio para eventos culturales'),
('Centro de Convenciones', 'Alajuela, Costa Rica', 2000, 'Centro para grandes eventos'),
('Sala de Conciertos', 'Cartago, Costa Rica', 500, 'Espacio íntimo para conciertos');

INSERT INTO categories (name, description) VALUES
('Música', 'Eventos musicales y conciertos'),
('Teatro', 'Obras de teatro y presentaciones'),
('Conferencia', 'Conferencias y charlas'),
('Deportes', 'Eventos deportivos');

INSERT INTO events (name, description, start_date, end_date, status) VALUES
('Concierto de Rock', 'Gran concierto de bandas locales', '2025-12-01 20:00:00', '2025-12-01 23:00:00', 'ACTIVE'),
('Obra de Shakespeare', 'Romeo y Julieta en versión moderna', '2025-12-05 19:00:00', '2025-12-05 21:30:00', 'ACTIVE'),
('Conferencia Tech', 'Tendencias en desarrollo de software', '2025-12-10 09:00:00', '2025-12-10 17:00:00', 'ACTIVE'),
('Partido de Fútbol', 'Final del campeonato local', '2025-12-15 15:00:00', '2025-12-15 17:00:00', 'CANCELLED');