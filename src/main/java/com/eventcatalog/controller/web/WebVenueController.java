package com.eventcatalog.controller.web;

import com.eventcatalog.dto.VenueRequestDTO;
import com.eventcatalog.dto.VenueResponseDTO;
import com.eventcatalog.service.IVenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador MVC para gestión de lugares (Venues).
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Controller
@RequestMapping("/web/venues")
@RequiredArgsConstructor
@Slf4j
public class WebVenueController {

    private final IVenueService venueService;

    /**
     * Lista todos los lugares.
     */
    @GetMapping
    public String listVenues(Model model) {
        log.info("GET /web/venues - Mostrando lista de lugares");
        
        List<VenueResponseDTO> venues = venueService.findAll();
        model.addAttribute("venues", venues);
        
        return "venues/list";
    }

    /**
     * Muestra el detalle de un lugar.
     */
    @GetMapping("/{id}")
    public String viewVenue(@PathVariable Long id, Model model) {
        log.info("GET /web/venues/{} - Mostrando detalle de lugar", id);
        
        try {
            VenueResponseDTO venue = venueService.findById(id);
            model.addAttribute("venue", venue);
            return "venues/detail";
        } catch (Exception e) {
            log.error("Error al obtener venue {}: {}", id, e.getMessage());
            return "redirect:/web/venues?error=notfound";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo lugar.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("GET /web/venues/new - Mostrando formulario de creación");
        
        model.addAttribute("venueRequest", new VenueRequestDTO());
        model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
        
        return "venues/form";
    }

    /**
     * Procesa el formulario de creación de lugar.
     */
    @PostMapping
    public String createVenue(
            @Valid @ModelAttribute("venueRequest") VenueRequestDTO venueRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        log.info("POST /web/venues - Creando lugar: {}", venueRequest.getName());
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            return "venues/form";
        }
        
        try {
            VenueResponseDTO createdVenue = venueService.create(venueRequest);
            redirectAttributes.addFlashAttribute("success", 
                "Lugar '" + createdVenue.getName() + "' creado exitosamente");
            return "redirect:/web/venues";
        } catch (Exception e) {
            log.error("Error al crear venue: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            return "venues/form";
        }
    }

    /**
     * Muestra el formulario para editar un lugar.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("GET /web/venues/{}/edit - Mostrando formulario de edición", id);
        
        try {
            VenueResponseDTO venue = venueService.findById(id);
            
            VenueRequestDTO venueRequest = VenueRequestDTO.builder()
                    .name(venue.getName())
                    .address(venue.getAddress())
                    .city(venue.getCity())
                    .capacity(venue.getCapacity())
                    .description(venue.getDescription())
                    .build();
            
            model.addAttribute("venueRequest", venueRequest);
            model.addAttribute("venueId", id);
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            
            return "venues/form";
        } catch (Exception e) {
            log.error("Error al obtener venue para editar: {}", e.getMessage());
            return "redirect:/web/venues?error=notfound";
        }
    }

    /**
     * Procesa el formulario de actualización de lugar.
     */
    @PostMapping("/{id}")
    public String updateVenue(
            @PathVariable Long id,
            @Valid @ModelAttribute("venueRequest") VenueRequestDTO venueRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        log.info("POST /web/venues/{} - Actualizando lugar", id);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("venueId", id);
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            return "venues/form";
        }
        
        try {
            VenueResponseDTO updatedVenue = venueService.update(id, venueRequest);
            redirectAttributes.addFlashAttribute("success", 
                "Lugar '" + updatedVenue.getName() + "' actualizado exitosamente");
            return "redirect:/web/venues";
        } catch (Exception e) {
            log.error("Error al actualizar venue: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("venueId", id);
            model.addAttribute("cities", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
            return "venues/form";
        }
    }

    /**
     * Elimina un lugar.
     */
    @PostMapping("/{id}/delete")
    public String deleteVenue(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        log.info("POST /web/venues/{}/delete - Eliminando lugar", id);
        
        try {
            VenueResponseDTO venue = venueService.findById(id);
            venueService.delete(id);
            redirectAttributes.addFlashAttribute("success", 
                "Lugar '" + venue.getName() + "' eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar venue: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", 
                "No se pudo eliminar el lugar: " + e.getMessage());
        }
        
        return "redirect:/web/venues";
    }
}