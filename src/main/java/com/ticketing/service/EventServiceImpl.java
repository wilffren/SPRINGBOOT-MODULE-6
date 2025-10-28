package com.ticketing.service;

import com.ticketing.dto.EventDTO;
import com.ticketing.exception.ResourceNotFoundException;
import com.ticketing.repository.IEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de eventos.
 * 
 * Contiene la lógica de negocio para la gestión de eventos.
 * 
 * Aplica el principio de Responsabilidad Única (S de SOLID):
 * - Solo se encarga de la lógica de negocio de eventos.
 * 
 * Aplica el principio de Inversión de Dependencias (D de SOLID):
 * - Depende de la abstracción IEventRepository, no de la implementación concreta.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements IEventService {
    
    // Inyección de dependencias por constructor (inmutable y testeable)
    private final IEventRepository eventRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EventDTO createEvent(EventDTO event) {
        log.info("Creando nuevo evento: {}", event.getName());
        
        // Establecer valores por defecto si no están definidos
        if (event.getActive() == null) {
            event.setActive(true);
        }
        
        EventDTO savedEvent = eventRepository.save(event);
        
        log.info("Evento creado exitosamente con ID: {}", savedEvent.getId());
        return savedEvent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EventDTO getEventById(Long id) {
        log.info("Buscando evento con ID: {}", id);
        
        return eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Evento no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Evento no encontrado con ID: " + id);
                });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventDTO> getAllEvents() {
        log.info("Obteniendo todos los eventos");
        
        List<EventDTO> events = eventRepository.findAll();
        
        log.info("Se encontraron {} eventos", events.size());
        return events;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EventDTO updateEvent(Long id, EventDTO event) {
        log.info("Actualizando evento con ID: {}", id);
        
        // Verificar que el evento existe
        if (!eventRepository.existsById(id)) {
            log.error("Evento no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + id);
        }
        
        EventDTO updatedEvent = eventRepository.update(id, event)
                .orElseThrow(() -> new ResourceNotFoundException("Error al actualizar evento con ID: " + id));
        
        log.info("Evento actualizado exitosamente con ID: {}", id);
        return updatedEvent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEvent(Long id) {
        log.info("Eliminando evento con ID: {}", id);
        
        // Verificar que el evento existe
        if (!eventRepository.existsById(id)) {
            log.error("Evento no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + id);
        }
        
        eventRepository.deleteById(id);
        
        log.info("Evento eliminado exitosamente con ID: {}", id);
    }
}