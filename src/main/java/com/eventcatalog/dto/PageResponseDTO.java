package com.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO<T> {

    /**
     * Lista de elementos de la página actual
     */
    private List<T> content;

    /**
     * Número de página actual (empezando desde 0)
     */
    private int pageNumber;

    /**
     * Tamaño de la página (cantidad de elementos por página)
     */
    private int pageSize;

    /**
     * Total de elementos en todas las páginas
     */
    private long totalElements;

    /**
     * Total de páginas disponibles
     */
    private int totalPages;

    /**
     * Indica si es la primera página
     */
    private boolean first;

    /**
     * Indica si es la última página
     */
    private boolean last;

    /**
     * Indica si hay una página anterior
     */
    private boolean hasPrevious;

    /**
     * Indica si hay una página siguiente
     */
    private boolean hasNext;

    /**
     * Indica si la página está vacía
     */
    private boolean empty;

    /**
     * Criterio de ordenamiento aplicado
     */
    private String sort;

    /**
     * Constructor estático para crear desde Spring Data Page.
     * 
     * @param page Page de Spring Data
     * @param <T> Tipo de dato
     * @return PageResponseDTO construido
     */
    public static <T> PageResponseDTO<T> fromPage(org.springframework.data.domain.Page<T> page) {
        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasPrevious(page.hasPrevious())
                .hasNext(page.hasNext())
                .empty(page.isEmpty())
                .sort(page.getSort().toString())
                .build();
    }
}

/**
 * EJEMPLO DE USO:
 * 
 * En el controlador:
 * 
 * @GetMapping
 * public ResponseEntity<PageResponseDTO<EventResponseDTO>> findAll(Pageable pageable) {
 *     Page<EventEntity> page = eventRepository.findAll(pageable);
 *     Page<EventResponseDTO> dtoPage = page.map(this::toDTO);
 *     PageResponseDTO<EventResponseDTO> response = PageResponseDTO.fromPage(dtoPage);
 *     return ResponseEntity.ok(response);
 * }
 * 
 * Respuesta JSON:
 * {
 *   "content": [...],
 *   "pageNumber": 0,
 *   "pageSize": 10,
 *   "totalElements": 50,
 *   "totalPages": 5,
 *   "first": true,
 *   "last": false,
 *   "hasPrevious": false,
 *   "hasNext": true,
 *   "empty": false,
 *   "sort": "name: ASC"
 * }
 */