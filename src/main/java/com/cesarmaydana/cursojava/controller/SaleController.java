package com.cesarmaydana.cursojava.controller;

import com.cesarmaydana.cursojava.dto.SaleDto;
import com.cesarmaydana.cursojava.model.Sale;
import com.cesarmaydana.cursojava.service.ClientService;
import com.cesarmaydana.cursojava.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Ventas", description = "Operaciones relacionadas con las ventas")
@RestController
@RequestMapping("/ventas")
public class SaleController {

    @Autowired
    private SaleService ventaService;

    @Autowired
    private ClientService clienteService;

    @Operation(summary = "Obtener todas las ventas", description = "Devuelve una lista de todas las ventas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente")
    })
    @GetMapping
    public List<SaleDto> obtenerTodasLasVentas() {
        // Mapea la lista de ventas a DTO
        return ventaService.obtenerTodasLasVentas().stream()
                .map(venta -> ventaService.convertirAVentaDTO(venta))
                .toList();
    }

    @Operation(summary = "Obtener venta por ID", description = "Busca una venta en el sistema según el ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("{id}")
    public ResponseEntity<SaleDto> obtenerVentasPorId(@PathVariable Long id) {
        Optional<Sale> venta = ventaService.obtenerVentaPorId(id);
        return venta.map(v -> ResponseEntity.ok(ventaService.convertirAVentaDTO(v)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva venta", description = "Registra una nueva venta en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente para la venta"),
            @ApiResponse(responseCode = "404", description = "Cliente o producto no encontrado")
    })
    @PostMapping
    public ResponseEntity<String> crearVenta(@RequestBody Sale nuevaVenta) {
        String resultado = ventaService.crearVenta(nuevaVenta);

        if (resultado.equals("Venta creada exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } else if (resultado.contains("Stock insuficiente")) {
            return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Cancelar una venta", description = "Cancela una venta existente y restaura el stock de productos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta cancelada y stock restaurado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarVenta(@PathVariable Long id) {
        if (ventaService.cancelarVenta(id)) {
            return new ResponseEntity<>("Venta cancelada y stock restaurado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Venta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

}
