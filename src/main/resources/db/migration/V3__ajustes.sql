-- V3__ajustes.sql
-- Migración: Ajustes adicionales y optimizaciones

-- Índice compuesto para búsquedas comunes de eventos activos en rangos de fechas
CREATE INDEX idx_event_status_dates ON events(status, start_date, end_date);

-- Constraint para asegurar que end_date sea después de start_date
ALTER TABLE events 
ADD CONSTRAINT chk_event_dates 
CHECK (end_date > start_date);

-- Constraint para valores válidos de status
ALTER TABLE events 
ADD CONSTRAINT chk_event_status 
CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED'));

-- Agregar más datos de prueba para mejor testing
INSERT INTO venues (name, location, capacity, description) VALUES
('Estadio Nacional', 'San José, Costa Rica', 35000, 'Estadio principal para grandes eventos'),
('Pequeño Teatro', 'San José, Centro', 100, 'Teatro íntimo para presentaciones pequeñas');

-- Eventos adicionales con diferentes estados
INSERT INTO events (name, description, start_date, end_date, status, venue_id) VALUES
('Concierto Internacional', 'Banda internacional de gira', '2026-02-14 19:00:00', '2026-02-14 23:00:00', 'ACTIVE', 5),
('Maratón Nacional', 'Carrera de 42km', '2026-03-01 06:00:00', '2026-03-01 14:00:00', 'ACTIVE', 5),
('Recital de Poesía', 'Noche de poetas locales', '2025-11-30 19:00:00', '2025-11-30 21:00:00', 'COMPLETED', 6);

-- Categorías adicionales
INSERT INTO categories (name, description) VALUES
('Cultura', 'Eventos culturales diversos'),
('Entretenimiento', 'Eventos de entretenimiento general');

-- Relaciones para nuevos eventos
INSERT INTO event_categories (event_id, category_id) VALUES
(8, 1),  -- Concierto Internacional -> Música
(9, 4),  -- Maratón Nacional -> Deportes
(10, 5); -- Recital de Poesía -> Cultura
