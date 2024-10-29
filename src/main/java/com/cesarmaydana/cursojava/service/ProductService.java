package com.cesarmaydana.cursojava.service;

import com.cesarmaydana.cursojava.dto.ProductRequestDto;
import com.cesarmaydana.cursojava.dto.ProductResponseDto;
import com.cesarmaydana.cursojava.model.PriceList;
import com.cesarmaydana.cursojava.model.Product;
import com.cesarmaydana.cursojava.repository.PriceListRepository;
import com.cesarmaydana.cursojava.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PriceListRepository listaPrecioRepository;

    @Autowired
    private PriceListService listaPrecioService;

    // Obtener todos los productos
    public List<ProductResponseDto> obtenerTodosLosProductos() {
        List<Product> productos = productoRepository.findAll();

        // Convertir cada Producto a ProductoResponseDTO manualmente
        List<ProductResponseDto> productosDto = productos.stream()
                .map(producto -> new ProductResponseDto(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        listaPrecioService.obtenerPrecioVigente(producto.getId())
                ))
                .collect(Collectors.toList());

        return productosDto;
    }

    // Obtener producto por ID
    public Optional<ProductResponseDto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .map(producto -> new ProductResponseDto(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        listaPrecioService.obtenerPrecioVigente(producto.getId()) // Este método debe devolver un Double, no un Optional
                ));
    }

    // Guardar un producto con precio (a partir de un ProductoRequestDTO)
    public Product guardarProducto(ProductRequestDto productoRequestDTO) {

        // Validación de negocio: el producto debe tener al menos un precio

        if (productoRequestDTO.getNombre() == null || productoRequestDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener un nombre.");
        }

        if (productoRequestDTO.getPrecio() == null) {
            throw new IllegalArgumentException("El producto debe tener al menos un precio.");
        }

        // Crear la entidad Producto
        Product nuevoProducto = new Product();
        nuevoProducto.setNombre(productoRequestDTO.getNombre());
        nuevoProducto.setStock(productoRequestDTO.getStock());

        // Guardar el producto para obtener un ID asignado
        Product productoGuardado = productoRepository.save(nuevoProducto);

        // Finalizar la vigencia de precios anteriores que no tienen fecha de vigencia
        List<PriceList> preciosAnteriores = listaPrecioRepository.findByProductoId(productoGuardado.getId());
        for (PriceList precio : preciosAnteriores) {
            if (precio.getFechaFinVigencia() == null) {
                precio.setFechaFinVigencia(LocalDateTime.now());
                listaPrecioRepository.save(precio);
            }
        }

        // Guardar el nuevo precio
        PriceList nuevoPrecio = new PriceList();
        nuevoPrecio.setPrecio(productoRequestDTO.getPrecio());
        nuevoPrecio.setFechaFinVigencia(null);
        nuevoPrecio.setProducto(productoGuardado);

        listaPrecioRepository.save(nuevoPrecio);

        return productoGuardado;
    }

    public Optional<ProductResponseDto> modificarProducto(Long id, ProductRequestDto productoRequestDTO) {

        // Verificar si el producto existe
        Optional<Product> productoOpt = productoRepository.findById(id);
        if (!productoOpt.isPresent()) {
            return Optional.empty(); // Producto no encontrado
        }

        Product producto = productoOpt.get();
        // Actualizar los campos del producto

        if (productoRequestDTO.getNombre() == null && productoRequestDTO.getNombre().isEmpty()){
            throw new IllegalArgumentException("El producto debe tener un nombre.");
        }

        if (productoRequestDTO.getPrecio() == null){
            throw new IllegalArgumentException("El producto debe tener al menos un precio.");
        }

        producto.setNombre(productoRequestDTO.getNombre());
        producto.setStock(productoRequestDTO.getStock());

        // Finalizar la vigencia de precios anteriores que no tienen fecha de vigencia
        List<PriceList> preciosAnteriores = listaPrecioRepository.findByProductoId(producto.getId());
        for (PriceList precio : preciosAnteriores) {
            if (precio.getFechaFinVigencia() == null) {
                precio.setFechaFinVigencia(LocalDateTime.now());
                listaPrecioRepository.save(precio);
            }
        }

        // Guardar el nuevo precio
        PriceList nuevoPrecio = new PriceList();
        nuevoPrecio.setPrecio(productoRequestDTO.getPrecio());
        nuevoPrecio.setFechaFinVigencia(null);
        nuevoPrecio.setProducto(producto); // Asignar el producto a la lista de precios

        listaPrecioRepository.save(nuevoPrecio); // Guardar el nuevo precio
        productoRepository.save(producto); // Guardar los cambios en el producto

        // Convertir el producto actualizado a ProductoResponseDTO
        return Optional.of(new ProductResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                listaPrecioService.obtenerPrecioVigente(producto.getId()) // Obtener el precio vigente
        ));
    }

    // Eliminar un producto por ID
    public boolean eliminarProducto(Long id) {
        return productoRepository.findById(id).map(producto -> {
            productoRepository.delete(producto);
            return true;
        }).orElse(false);
    }

}
