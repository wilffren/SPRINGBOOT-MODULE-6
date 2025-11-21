package com.example.HU4.infrastructure.controllers;

import com.example.HU4.domain.model.Task;
import com.example.HU4.domain.ports.in.CreateTaskPort;
import com.example.HU4.domain.ports.in.FindTasksPort;
import com.example.HU4.domain.ports.in.UpdateTaskPort;
import com.example.HU4.infrastructure.dto.TaskFilterRequest;
import com.example.HU4.infrastructure.dto.TaskRequest;
import com.example.HU4.infrastructure.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para tareas
 * Expone endpoints siguiendo arquitectura hexagonal
 */
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    
    private final CreateTaskPort createTaskPort;
    private final FindTasksPort findTasksPort;
    private final UpdateTaskPort updateTaskPort;
    
    /**
     * Crear nueva tarea
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        log.info("POST /api/tasks - Creando tarea: {}", request.getTitle());
        
        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .status(request.getStatus() != null ? request.getStatus() : "PENDING")
            .priority(request.getPriority() != null ? request.getPriority() : "MEDIUM")
            .dueDate(request.getDueDate())
            .userId(request.getUserId())
            .build();
        
        Task created = createTaskPort.createTask(task);
        TaskResponse response = mapToResponse(created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtener todas las tareas
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        log.info("GET /api/tasks - Obteniendo todas las tareas");
        
        List<TaskResponse> tasks = findTasksPort.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Obtener tarea por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        log.info("GET /api/tasks/{} - Obteniendo tarea", id);
        
        return findTasksPort.findById(id)
            .map(this::mapToResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * TASK 2: Buscar tareas con filtros dinámicos
     * Ejemplo: /api/tasks/search?userId=1&status=PENDING&priority=HIGH
     */
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTasks(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String priority,
        @RequestParam(required = false) LocalDate dueDate
    ) {
        log.info("GET /api/tasks/search - Filtros: userId={}, status={}, priority={}, dueDate={}", 
            userId, status, priority, dueDate);
        
        List<TaskResponse> tasks = findTasksPort.findByFilters(userId, status, priority, dueDate)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Obtener tareas por usuario
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUserId(@PathVariable Long userId) {
        log.info("GET /api/tasks/user/{} - Obteniendo tareas del usuario", userId);
        
        List<TaskResponse> tasks = findTasksPort.findByUserId(userId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * TASK 2: Obtener tareas por usuario con carga optimizada (sin N+1)
     */
    @GetMapping("/user/{userId}/optimized")
    public ResponseEntity<List<TaskResponse>> getTasksByUserIdOptimized(@PathVariable Long userId) {
        log.info("GET /api/tasks/user/{}/optimized - Consulta optimizada", userId);
        
        List<TaskResponse> tasks = findTasksPort.findByUserIdOptimized(userId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Actualizar tarea
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
        @PathVariable Long id,
        @Valid @RequestBody TaskRequest request
    ) {
        log.info("PUT /api/tasks/{} - Actualizando tarea", id);
        
        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .status(request.getStatus())
            .priority(request.getPriority())
            .dueDate(request.getDueDate())
            .userId(request.getUserId())
            .build();
        
        Task updated = updateTaskPort.updateTask(id, task);
        TaskResponse response = mapToResponse(updated);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Completar tarea
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
        log.info("PATCH /api/tasks/{}/complete - Completando tarea", id);
        
        Task completed = updateTaskPort.completeTask(id);
        TaskResponse response = mapToResponse(completed);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar tarea
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /api/tasks/{} - Eliminando tarea", id);
        
        updateTaskPort.deleteTask(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Mapper de Task a TaskResponse
     */
    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .status(task.getStatus())
            .priority(task.getPriority())
            .dueDate(task.getDueDate())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getUpdatedAt())
            .completedAt(task.getCompletedAt())
            .userId(task.getUserId())
            .overdue(task.isOverdue())
            .build();
    }
}