package com.eventcatalog.controller.web;

import com.eventcatalog.dto.EventFilterDTO;
import com.eventcatalog.dto.EventRequestDTO;
import com.eventcatalog.dto.EventResponseDTO;
import com.eventcatalog.dto.PageResponseDTO;
import com.eventcatalog.dto.VenueResponseDTO;
import com.eventcatalog.service.IEventService;
import com.eventcatalog.service.IVenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador MVC para las vistas web de eventos.
 * 
 * Este controlador maneja las peticiones HTTP que devuelven vistas HTML
 * renderizadas con Thymeleaf, a diferencia de EventController que devuelve JSON.
 * 
 * Principios SOLID aplicados:
 * - SRP: Solo maneja las vistas web de eventos
 * - DIP: Depende de las interfaces de servicio
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Controller
@RequestMapping("/web/events")
@RequiredArgsConstructor
@Slf4j
public class WebEventController {

    private final IEventService eventService;
    private final IVenueService venueService;

    /**
     * Página principal - Lista de eventos con paginación y filtros.
     * 
     * @param page Número de página
     * @param size Tamaño de página
     * @param sort Criterio de ordenamiento
     * @param city Filtro por ciudad
     * @param category Filtro por categoría
     * @param startDate Filtro por fecha de inicio
     * @param endDate Filtro por fecha fin
     * @param searchTerm Búsqueda de texto
     * @param model Modelo para la vista
     * @return Nombre de la plantilla Thymeleaf
     */
    @GetMapping
    public String listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "eventDate,asc") String sort,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String searchTerm,
            Model model) {
        
        log.info("GET /web/events - Mostrando lista de eventos");
        
        // Crear objeto Pageable
        Pageable pageable = createPageable(page, size, sort);
        
        // Crear filtros
        EventFilterDTO filters = EventFilterDTO.builder()
                .city(city)
                .category(category)
                .startDate(startDate)
                .endDate(endDate)
                .searchTerm(searchTerm)
                .build();
        
        // Obtener eventos
        PageResponseDTO<EventResponseDTO> eventsPage;
        if (filters.hasAnyFilter()) {
            eventsPage = eventService.findWithFilters(filters, pageable);
        } else {
            eventsPage = eventService.findAllPaginated(pageable);
        }
        
        // Agregar atributos al modelo
        model.addAttribute("eventsPage", eventsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("currentSort", sort);
        model.addAttribute("filters", filters);
        
        // Para el select de ciudades y categorías
        model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
        model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
        
        return "events/list";
    }

    /**
     * Muestra el detalle de un evento.
     * 
     * @param id ID del evento
     * @param model Modelo para la vista
     * @return Nombre de la plantilla
     */
    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        log.info("GET /web/events/{} - Mostrando detalle de evento", id);
        
        try {
            EventResponseDTO event = eventService.findById(id);
            model.addAttribute("event", event);
            return "events/detail";
        } catch (Exception e) {
            log.error("Error al obtener evento {}: {}", id, e.getMessage());
            return "redirect:/web/events?error=notfound";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo evento.
     * 
     * @param model Modelo para la vista
     * @return Nombre de la plantilla
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("GET /web/events/new - Mostrando formulario de creación");
        
        model.addAttribute("eventRequest", new EventRequestDTO());
        
        // Cargar venues, si no hay ninguno, la lista estará vacía
        try {
            model.addAttribute("venues", venueService.findAll());
        } catch (Exception e) {
            log.warn("No se pudieron cargar los venues: {}", e.getMessage());
            model.addAttribute("venues", List.of());
        }
        
        model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
        model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
        
        return "events/form";
    }

    /**
     * Procesa el formulario de creación de evento.
     * 
     * @param eventRequest DTO con datos del evento
     * @param bindingResult Resultado de validación
     * @param redirectAttributes Atributos para redirección
     * @param model Modelo para la vista
     * @return Redirección o vista con errores
     */
    @PostMapping
    public String createEvent(
            @Valid @ModelAttribute("eventRequest") EventRequestDTO eventRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        log.info("POST /web/events - Creando evento: {}", eventRequest.getName());
        
        // Si hay errores de validación, volver al formulario
        if (bindingResult.hasErrors()) {
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
            return "events/form";
        }
        
        try {
            EventResponseDTO createdEvent = eventService.create(eventRequest);
            redirectAttributes.addFlashAttribute("success", 
                "Evento '" + createdEvent.getName() + "' creado exitosamente");
            return "redirect:/web/events";
        } catch (Exception e) {
            log.error("Error al crear evento: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
            return "events/form";
        }
    }

    /**
     * Muestra el formulario para editar un evento.
     * 
     * @param id ID del evento
     * @param model Modelo para la vista
     * @return Nombre de la plantilla
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("GET /web/events/{}/edit - Mostrando formulario de edición", id);
        
        try {
            EventResponseDTO event = eventService.findById(id);
            
            // Convertir EventResponseDTO a EventRequestDTO
            EventRequestDTO eventRequest = EventRequestDTO.builder()
                    .name(event.getName())
                    .description(event.getDescription())
                    .category(event.getCategory())
                    .city(event.getCity())
                    .eventDate(event.getEventDate())
                    .price(event.getPrice())
                    .availableTickets(event.getAvailableTickets())
                    .imageUrl(event.getImageUrl())
                    .venueId(event.getVenue().getId())
                    .status(event.getStatus())
                    .build();
            
            model.addAttribute("eventRequest", eventRequest);
            model.addAttribute("eventId", id);
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
            
            return "events/form";
        } catch (Exception e) {
            log.error("Error al obtener evento para editar: {}", e.getMessage());
            return "redirect:/web/events?error=notfound";
        }
    }

    /**
     * Procesa el formulario de actualización de evento.
     * 
     * @param id ID del evento
     * @param eventRequest DTO con nuevos datos
     * @param bindingResult Resultado de validación
     * @param redirectAttributes Atributos para redirección
     * @param model Modelo para la vista
     * @return Redirección o vista con errores
     */
    @PostMapping("/{id}")
    public String updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute("eventRequest") EventRequestDTO eventRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        log.info("POST /web/events/{} - Actualizando evento", id);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventId", id);
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
            return "events/form";
        }
        
        try {
            EventResponseDTO updatedEvent = eventService.update(id, eventRequest);
            redirectAttributes.addFlashAttribute("success", 
                "Evento '" + updatedEvent.getName() + "' actualizado exitosamente");
            return "redirect:/web/events";
        } catch (Exception e) {
            log.error("Error al actualizar evento: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("eventId", id);
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            model.addAttribute("categories", List.of("Concierto", "Teatro", "Deporte", "Conferencia", "Exposición"));
            return "events/form";
        }
    }

    /**
     * Elimina un evento.
     * 
     * @param id ID del evento
     * @param redirectAttributes Atributos para redirección
     * @return Redirección
     */
    @PostMapping("/{id}/delete")
    public String deleteEvent(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        log.info("POST /web/events/{}/delete - Eliminando evento", id);
        
        try {
            EventResponseDTO event = eventService.findById(id);
            eventService.delete(id);
            redirectAttributes.addFlashAttribute("success", 
                "Evento '" + event.getName() + "' eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar evento: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", 
                "No se pudo eliminar el evento: " + e.getMessage());
        }
        
        return "redirect:/web/events";
    }

    /**
     * Método helper para crear objeto Pageable.
     */
    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && 
            sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        return PageRequest.of(page, size, Sort.by(direction, property));
    }
}