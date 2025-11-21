package com.example.HU4.domain.ports.in;

import com.example.HU4.domain.model.Task;

public interface UpdateTaskPort {

    /**
     * Actualiza una tarea existente
     */
    Task updateTask(Long id, Task task);

    /**
     * Marca una tarea como completada
     */
    Task completeTask(Long id);

    /**
     * Elimina una tarea
     */
    void deleteTask(Long id);
}