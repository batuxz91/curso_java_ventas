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

    public List<ProductResponseDto> obtenerTodosLosProductos() {
        List<Product> productos = productoRepository.findAll();

        List<ProductResponseDto> productosDto = productos.stream()
                .map(producto -> new ProductResponseDto(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        listaPrecioService.obtenerListaPrecioVigente(producto.getId()).getPrecio()
                ))
                .collect(Collectors.toList());

        return productosDto;
    }

    public Optional<ProductResponseDto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .map(producto -> new ProductResponseDto(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        listaPrecioService.obtenerListaPrecioVigente(producto.getId()).getPrecio()
                ));
    }

    public Product guardarProducto(ProductRequestDto productoRequestDTO) {

        if (productoRequestDTO.getNombre() == null || productoRequestDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener un nombre.");
        }

        if (productoRequestDTO.getPrecio() == null) {
            throw new IllegalArgumentException("El producto debe tener al menos un precio.");
        }

        Product nuevoProducto = new Product();
        nuevoProducto.setNombre(productoRequestDTO.getNombre());
        nuevoProducto.setStock(productoRequestDTO.getStock());

        Product productoGuardado = productoRepository.save(nuevoProducto);

        List<PriceList> preciosAnteriores = listaPrecioRepository.findByProductoId(productoGuardado.getId());
        for (PriceList precio : preciosAnteriores) {
            if (precio.getFechaFinVigencia() == null) {
                precio.setFechaFinVigencia(LocalDateTime.now());
                listaPrecioRepository.save(precio);
            }
        }

        PriceList nuevoPrecio = new PriceList();
        nuevoPrecio.setPrecio(productoRequestDTO.getPrecio());
        nuevoPrecio.setFechaFinVigencia(null);
        nuevoPrecio.setProducto(productoGuardado);

        listaPrecioRepository.save(nuevoPrecio);

        return productoGuardado;
    }

    public Optional<ProductResponseDto> modificarProducto(Long id, ProductRequestDto productoRequestDTO) {

        Optional<Product> productoOpt = productoRepository.findById(id);
        if (!productoOpt.isPresent()) {
            return Optional.empty();
        }

        Product producto = productoOpt.get();

        if (productoRequestDTO.getNombre() == null && productoRequestDTO.getNombre().isEmpty()){
            throw new IllegalArgumentException("El producto debe tener un nombre.");
        }

        if (productoRequestDTO.getPrecio() == null){
            throw new IllegalArgumentException("El producto debe tener al menos un precio.");
        }

        producto.setNombre(productoRequestDTO.getNombre());
        producto.setStock(productoRequestDTO.getStock());

        List<PriceList> preciosAnteriores = listaPrecioRepository.findByProductoId(producto.getId());
        for (PriceList precio : preciosAnteriores) {
            if (precio.getFechaFinVigencia() == null) {
                precio.setFechaFinVigencia(LocalDateTime.now());
                listaPrecioRepository.save(precio);
            }
        }

        PriceList nuevoPrecio = new PriceList();
        nuevoPrecio.setPrecio(productoRequestDTO.getPrecio());
        nuevoPrecio.setFechaFinVigencia(null);
        nuevoPrecio.setProducto(producto);

        listaPrecioRepository.save(nuevoPrecio);
        productoRepository.save(producto);

        return Optional.of(new ProductResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                listaPrecioService.obtenerListaPrecioVigente(producto.getId()).getPrecio()
        ));
    }

}
