package com.eventcatalog.controller.web;

import com.eventcatalog.dto.EventResponseDTO;
import com.eventcatalog.service.IEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controlador para la página de inicio.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final IEventService eventService;

    /**
     * Página de inicio - Muestra eventos destacados.
     * 
     * @param model Modelo para la vista
     * @return Nombre de la plantilla
     */
    @GetMapping("/")
    public String home(Model model) {
        log.info("GET / - Página de inicio");
        
        try {
            // Obtener eventos próximos para mostrar en el home
            List<EventResponseDTO> upcomingEvents = eventService.findUpcomingEvents()
                    .stream()
                    .limit(6) // Mostrar solo 6 eventos destacados
                    .toList();
            
            model.addAttribute("upcomingEvents", upcomingEvents);
        } catch (Exception e) {
            log.error("Error al obtener eventos destacados: {}", e.getMessage());
        }
        
        return "home";
    }

    /**
     * Redirección desde /web a /web/events.
     */
    @GetMapping("/web")
    public String webRedirect() {
        return "redirect:/web/events";
    }
}