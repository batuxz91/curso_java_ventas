package com.cesarmaydana.cursojava;

import com.cesarmaydana.cursojava.dto.ProductRequestDto;
import com.cesarmaydana.cursojava.dto.ProductResponseDto;
import com.cesarmaydana.cursojava.model.PriceList;
import com.cesarmaydana.cursojava.model.Product;
import com.cesarmaydana.cursojava.repository.PriceListRepository;
import com.cesarmaydana.cursojava.repository.ProductoRepository;
import com.cesarmaydana.cursojava.service.PriceListService;
import com.cesarmaydana.cursojava.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PriceListRepository listaPrecioRepository;

    @Mock
    private PriceListService listaPrecioService;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private PriceList priceList;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setNombre("Producto Test");
        product.setStock(10);

        priceList = new PriceList();
        priceList.setId(1L);
        priceList.setPrecio(100.0);
        priceList.setProducto(product);
    }

    @Test
    void testObtenerTodosLosProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(product));
        when(listaPrecioService.obtenerListaPrecioVigente(product.getId())).thenReturn(priceList);

        List<ProductResponseDto> productos = productService.obtenerTodosLosProductos();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals(product.getNombre(), productos.get(0).getNombre());
        assertEquals(priceList.getPrecio(), productos.get(0).getPrecio());
    }

    @Test
    void testObtenerProductoPorId() {
        when(productoRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(listaPrecioService.obtenerListaPrecioVigente(product.getId())).thenReturn(priceList);

        Optional<ProductResponseDto> productoOpt = productService.obtenerProductoPorId(product.getId());

        assertTrue(productoOpt.isPresent());
        assertEquals(product.getNombre(), productoOpt.get().getNombre());
        assertEquals(priceList.getPrecio(), productoOpt.get().getPrecio());
    }

    @Test
    void testGuardarProducto() {
        ProductRequestDto productoRequestDTO = new ProductRequestDto();
        productoRequestDTO.setNombre("Producto Test");
        productoRequestDTO.setStock(20);
        productoRequestDTO.setPrecio(150.0);

        when(productoRepository.save(any(Product.class))).thenReturn(product);
        when(listaPrecioRepository.findByProductoId(product.getId())).thenReturn(Arrays.asList());
        when(listaPrecioRepository.save(any(PriceList.class))).thenReturn(priceList);

        Product productoGuardado = productService.guardarProducto(productoRequestDTO);

        assertNotNull(productoGuardado);
        assertEquals(productoRequestDTO.getNombre(), productoGuardado.getNombre());
        verify(productoRepository, times(1)).save(any(Product.class));
        verify(listaPrecioRepository, times(1)).save(any(PriceList.class));
    }

    @Test
    void testModificarProducto() {
        ProductRequestDto productoRequestDTO = new ProductRequestDto();
        productoRequestDTO.setNombre("Producto Modificado");
        productoRequestDTO.setStock(5);
        productoRequestDTO.setPrecio(200.0);

        when(productoRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(listaPrecioRepository.findByProductoId(product.getId())).thenReturn(Arrays.asList(priceList));
        when(listaPrecioService.obtenerListaPrecioVigente(product.getId())).thenReturn(priceList);

        Optional<ProductResponseDto> productoModificado = productService.modificarProducto(product.getId(), productoRequestDTO);

        assertTrue(productoModificado.isPresent());
        assertEquals(productoRequestDTO.getNombre(), productoModificado.get().getNombre());
        assertEquals(productoRequestDTO.getPrecio(), productoModificado.get().getPrecio());
        verify(productoRepository, times(1)).save(any(Product.class));
        verify(listaPrecioRepository, times(1)).save(any(PriceList.class));
    }
}
