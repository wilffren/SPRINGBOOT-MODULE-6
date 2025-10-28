package com.ticketing.repository;

import com.ticketing.dto.EventDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación In-Memory del repositorio de eventos.
 * 
 * Almacena eventos en memoria usando un ConcurrentHashMap para simular
 * un repositorio de datos sin persistencia real (preparatorio para futura integración con BD).
 * 
 * Aplica el principio de Responsabilidad Única (S de SOLID):
 * - Solo se encarga de gestionar el almacenamiento de eventos.
 * 
 * Aplica el principio de Sustitución de Liskov (L de SOLID):
 * - Puede ser sustituida por cualquier otra implementación de IEventRepository.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Repository
public class EventRepositoryImpl implements IEventRepository {
    
    // ConcurrentHashMap para almacenamiento thread-safe en memoria
    private final ConcurrentHashMap<Long, EventDTO> eventStore = new ConcurrentHashMap<>();
    
    // Generador de IDs autoincremental
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EventDTO save(EventDTO event) {
        // Asignar ID si no existe
        if (event.getId() == null) {
            event.setId(idGenerator.getAndIncrement());
        }
        
        // Guardar en el store
        eventStore.put(event.getId(), event);
        
        return event;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<EventDTO> findById(Long id) {
        return Optional.ofNullable(eventStore.get(id));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventDTO> findAll() {
        return new ArrayList<>(eventStore.values());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<EventDTO> update(Long id, EventDTO event) {
        if (!eventStore.containsKey(id)) {
            return Optional.empty();
        }
        
        // Mantener el ID original
        event.setId(id);
        
        // Actualizar en el store
        eventStore.put(id, event);
        
        return Optional.of(event);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(Long id) {
        return eventStore.remove(id) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return eventStore.containsKey(id);
    }
}