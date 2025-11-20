-- V2__add_relations.sql
-- Migración: Agregar relaciones OneToMany entre User y Task

-- Agregar columna user_id a tasks (FK hacia users)
ALTER TABLE tasks 
ADD COLUMN user_id BIGINT,
ADD CONSTRAINT fk_task_user 
    FOREIGN KEY (user_id) 
    REFERENCES users(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- Índice para optimizar búsquedas por usuario
CREATE INDEX idx_task_user_id ON tasks(user_id);

-- Índice compuesto para búsquedas frecuentes
CREATE INDEX idx_task_user_status ON tasks(user_id, status);
CREATE INDEX idx_task_user_priority ON tasks(user_id, priority);

-- Actualizar tareas existentes con usuarios aleatorios
UPDATE tasks SET user_id = 1 WHERE id = 1;
UPDATE tasks SET user_id = 2 WHERE id = 2;
UPDATE tasks SET user_id = 1 WHERE id = 3;
UPDATE tasks SET user_id = 3 WHERE id = 4;

-- Agregar más tareas de ejemplo con relaciones
INSERT INTO tasks (title, description, status, priority, due_date, user_id) VALUES
('Revisar código de producción', 'Code review del sprint actual', 'PENDING', 'HIGH', '2025-11-21', 1),
('Actualizar dependencias', 'Actualizar librerías del proyecto', 'IN_PROGRESS', 'LOW', '2025-12-01', 2),
('Preparar demo para cliente', 'Demo de nuevas funcionalidades', 'PENDING', 'HIGH', '2025-11-23', 3),
('Refactorizar servicios', 'Mejorar arquitectura de servicios', 'IN_PROGRESS', 'MEDIUM', '2025-11-28', 1),
('Implementar tests unitarios', 'Cobertura de tests al 80%', 'PENDING', 'MEDIUM', '2025-12-05', 2);