-- V2__relaciones.sql
-- Migración: Agregar relaciones entre Venue, Event y Category

-- Agregar columna venue_id a events (FK hacia venues)
-- Primero como nullable para poder actualizar registros existentes
ALTER TABLE events 
ADD COLUMN venue_id BIGINT NULL;

-- Actualizar eventos existentes con venues (NO hay eventos aún, tabla vacía)
-- Estos comandos no harán nada pero los dejo para documentación

-- Ahora cambiar columna a NOT NULL
ALTER TABLE events 
MODIFY COLUMN venue_id BIGINT NOT NULL;

-- Agregar la foreign key constraint
ALTER TABLE events
ADD CONSTRAINT fk_event_venue 
    FOREIGN KEY (venue_id) 
    REFERENCES venues(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- Índices para optimizar búsquedas
CREATE INDEX idx_event_venue_id ON events(venue_id);
CREATE INDEX idx_event_venue_status ON events(venue_id, status);
CREATE INDEX idx_event_venue_date ON events(venue_id, start_date);

-- Tabla intermedia para relación ManyToMany entre Event y Category
CREATE TABLE IF NOT EXISTS event_categories (
    event_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    
    PRIMARY KEY (event_id, category_id),
    
    CONSTRAINT fk_event_category_event 
        FOREIGN KEY (event_id) 
        REFERENCES events(id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_event_category_category 
        FOREIGN KEY (category_id) 
        REFERENCES categories(id) 
        ON DELETE CASCADE,
        
    INDEX idx_category_event (category_id, event_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Ahora sí insertar eventos con venue_id
INSERT INTO events (name, description, start_date, end_date, status, venue_id) VALUES
('Concierto de Rock', 'Gran concierto de bandas locales', '2025-12-01 20:00:00', '2025-12-01 23:00:00', 'ACTIVE', 1),
('Obra de Shakespeare', 'Romeo y Julieta en versión moderna', '2025-12-05 19:00:00', '2025-12-05 21:30:00', 'ACTIVE', 1),
('Conferencia Tech', 'Tendencias en desarrollo de software', '2025-12-10 09:00:00', '2025-12-10 17:00:00', 'ACTIVE', 3),
('Partido de Fútbol', 'Final del campeonato local', '2025-12-15 15:00:00', '2025-12-15 17:00:00', 'CANCELLED', 2);

-- Asignar categorías a eventos
INSERT INTO event_categories (event_id, category_id) VALUES
(1, 1),  -- Concierto de Rock -> Música
(2, 2),  -- Obra de Shakespeare -> Teatro
(3, 3),  -- Conferencia Tech -> Conferencia
(4, 4);  -- Partido de Fútbol -> Deportes

-- Agregar más eventos de ejemplo con relaciones
INSERT INTO events (name, description, start_date, end_date, status, venue_id) VALUES
('Festival de Jazz', 'Tres días de música jazz', '2025-12-20 18:00:00', '2025-12-22 23:00:00', 'ACTIVE', 1),
('Comedia Stand-up', 'Noche de comediantes locales', '2025-12-25 20:00:00', '2025-12-25 22:00:00', 'ACTIVE', 2),
('Exposición de Arte', 'Arte contemporáneo costarricense', '2026-01-05 10:00:00', '2026-01-15 18:00:00', 'ACTIVE', 3);

-- Asignar categorías a nuevos eventos
INSERT INTO event_categories (event_id, category_id) VALUES
(5, 1),  -- Festival de Jazz -> Música
(6, 2),  -- Comedia Stand-up -> Teatro
(7, 2);  -- Exposición de Arte -> Teatro
