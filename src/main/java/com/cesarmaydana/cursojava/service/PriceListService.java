package com.cesarmaydana.cursojava.service;

import com.cesarmaydana.cursojava.dto.PriceListDto;
import com.cesarmaydana.cursojava.dto.PriceListResponseDto;
import com.cesarmaydana.cursojava.model.PriceList;
import com.cesarmaydana.cursojava.model.Product;
import com.cesarmaydana.cursojava.repository.PriceListRepository;
import com.cesarmaydana.cursojava.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceListService {

    @Autowired
    private PriceListRepository listaPrecioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Double obtenerPrecioVigente(Long productoId) {
        Optional<Product> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isPresent()) {
            Product producto = productoOpt.get();
            return producto.getListaPrecios().stream()
                    .filter(precio -> precio.getFechaFinVigencia() == null)
                    .map(PriceList::getPrecio)
                    .findFirst()
                    .orElse(0.0);
        }
        return 0.0; // Valor por defecto si no se encuentra el producto
    }

    public PriceListResponseDto getListaPrecioById(Long productoId) {
        Optional<Product> productoOpt = productoRepository.findById(productoId);
        if (!productoOpt.isPresent()) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }

        Product producto = productoOpt.get();

        // Obtener todos los precios históricos
        List<PriceList> preciosHistoricos = listaPrecioRepository.findByProductoId(productoId);

        // Mapear precios a DTO
        List<PriceListDto> listaPreciosDto = preciosHistoricos.stream()
                .map(precio -> new PriceListDto(precio.getPrecio(), precio.getFechaFinVigencia()))
                .collect(Collectors.toList());

        return new PriceListResponseDto(producto.getId(), producto.getNombre(), producto.getStock(), listaPreciosDto);
    }

    public List<PriceListResponseDto> getListaPrecio() {
        // Obtener todos los productos
        List<Product> productos = productoRepository.findAll();

        // Mapear cada producto con su historial de precios
        List<PriceListResponseDto> productosConPreciosDto = productos.stream()
                .map(producto -> {
                    // Obtener todos los precios históricos para el producto actual
                    List<PriceList> preciosHistoricos = listaPrecioRepository.findByProductoId(producto.getId());

                    // Mapear cada precio a DTO
                    List<PriceListDto> listaPreciosDto = preciosHistoricos.stream()
                            .map(precio -> new PriceListDto(precio.getPrecio(), precio.getFechaFinVigencia()))
                            .collect(Collectors.toList());

                    // Crear y retornar el DTO del producto con su lista de precios
                    return new PriceListResponseDto(producto.getId(), producto.getNombre(), producto.getStock(), listaPreciosDto);
                })
                .collect(Collectors.toList());

        return productosConPreciosDto;
    }
}