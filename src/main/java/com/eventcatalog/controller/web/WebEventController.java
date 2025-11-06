package com.eventcatalog.controller.web;

import com.eventcatalog.dto.EventFilterDTO;
import com.eventcatalog.dto.EventRequestDTO;
import com.eventcatalog.dto.EventResponseDTO;
import com.eventcatalog.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador web para la gestión de eventos con Thymeleaf.
 * 
 * Usa el servicio IEventService (no la implementación directa),
 * siguiendo el principio de Inversión de Dependencias (DIP).
 */
@Slf4j
@Controller
@RequestMapping("/web/events")
public class WebEventController {

    @Autowired
    private IEventService eventService;

    /**
     * 📄 Listar todos los eventos
     */
    @GetMapping
    public String listEvents(Model model) {
        List<EventResponseDTO> events = eventService.findAll();
        model.addAttribute("events", events);
        return "events/list"; // templates/events/list.html
    }

    /**
     * ➕ Mostrar formulario para crear un nuevo evento
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new EventRequestDTO());
        return "events/form";
    }

    /**
     * ✏️ Mostrar formulario para editar un evento existente
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EventResponseDTO eventDTO = eventService.findById(id);
            model.addAttribute("event", eventDTO);
            return "events/form";
        } catch (Exception e) {
            log.error("No se encontró el evento con ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "No se encontró el evento con ID: " + id);
            return "redirect:/web/events";
        }
    }

    /**
     * 💾 Guardar evento (crear o actualizar)
     */
    @PostMapping("/save")
    public String saveEvent(
            @ModelAttribute("event") EventRequestDTO eventRequest,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (eventRequest.getId() == null) {
                eventService.create(eventRequest);
                redirectAttributes.addFlashAttribute("success", "Evento creado correctamente ✅");
            } else {
                eventService.update(eventRequest.getId(), eventRequest);
                redirectAttributes.addFlashAttribute("success", "Evento actualizado correctamente ✅");
            }
        } catch (Exception e) {
            log.error("Error al guardar el evento", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el evento ❌: " + e.getMessage());
        }
        return "redirect:/web/events";
    }

    /**
     * 🗑️ Eliminar evento
     */
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Evento eliminado correctamente 🗑️");
        } catch (Exception e) {
            log.error("Error al eliminar el evento", e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el evento ❌: " + e.getMessage());
        }
        return "redirect:/web/events";
    }

    /**
     * 🔍 Buscar eventos por filtros simples
     */
    @GetMapping("/search")
    public String searchEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            Model model
    ) {
        try {
            EventFilterDTO filters = new EventFilterDTO();
            filters.setCity(city);
            filters.setCategory(category);
            filters.setStartDate(startDate);

            var page = eventService.findWithFilters(filters, null);
            List<EventResponseDTO> events = page.getContent();

            model.addAttribute("events", events);
            model.addAttribute("city", city);
            model.addAttribute("category", category);
        } catch (Exception e) {
            log.error("Error al buscar eventos", e);
            model.addAttribute("error", "Error al buscar eventos: " + e.getMessage());
        }
        return "events/list";
    }
}
