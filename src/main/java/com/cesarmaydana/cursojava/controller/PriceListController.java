package com.cesarmaydana.cursojava.controller;

import com.cesarmaydana.cursojava.dto.PriceListResponseDto;
import com.cesarmaydana.cursojava.service.PriceListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Lista de Precios", description = "Operaciones relacionadas con las listas de precios")
@RestController
@RequestMapping("/listaprecios")
public class PriceListController {

    @Autowired
    private PriceListService listaPrecioService;

    @Operation(summary = "Obtener todas las listas de precios", description = "Devuelve una lista con el historial de precio de los productos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de precios obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lista de precios no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ResponseEntity<List<PriceListResponseDto>> getListaPrecios() {
        try {
            List<PriceListResponseDto> lista = listaPrecioService.getListaPrecio();
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener lista de precios por ID", description = "Devuelve el historial de precios según el ID del producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de precios encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista de precios no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PriceListResponseDto> getListPrecioById(@PathVariable Long id) {
        try {
            PriceListResponseDto lista = listaPrecioService.getListaPrecioById(id);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
