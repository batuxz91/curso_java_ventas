package com.cesarmaydana.cursojava.controller;

import com.cesarmaydana.cursojava.dto.ProductRequestDto;
import com.cesarmaydana.cursojava.dto.ProductResponseDto;
import com.cesarmaydana.cursojava.service.ProductService;
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

@Tag(name = "Productos", description = "Operaciones relacionadas con los productos")
@RestController
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productoService;

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> obtenerTodosLosProductos() {
        List<ProductResponseDto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener producto por ID", description = "Busca un producto en el sistema según el ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> obtenerProductoPorId(@PathVariable Long id) {
        Optional<ProductResponseDto> productoDtoOpt = productoService.obtenerProductoPorId(id);
        return productoDtoOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema y lo guarda en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para el producto"),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el producto")
    })
    @PostMapping
    public ResponseEntity<String> crearProducto(@RequestBody ProductRequestDto productoRequest) {
        try {
            productoService.guardarProducto(productoRequest);
            return new ResponseEntity<>("Producto creado exitosamente", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el producto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Modificar un producto existente", description = "Actualiza los datos de un producto ya registrado en el sistema según el ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para el producto"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al modificar el producto")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> modificarProducto(@PathVariable Long id, @RequestBody ProductRequestDto productoRequest) {
        try {
            Optional<ProductResponseDto> productoActualizado = productoService.modificarProducto(id, productoRequest);
            return productoActualizado.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Cambia a null aquí
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Cambia a null aquí
        }
    }

}
