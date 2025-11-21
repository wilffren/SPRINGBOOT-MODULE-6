package com.example.HU4.domain.ports.in;

import com.example.HU4.domain.model.Task;

/**
 * Puerto de entrada - Crear Tarea
 * Define el contrato para crear una nueva tarea
 */
public interface CreateTaskPort {
    
    /**
     * Crea una nueva tarea en el sistema
     * @param task La tarea a crear
     * @return La tarea creada con su ID asignado
     */
    Task createTask(Task task);
}