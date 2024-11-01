package com.cesarmaydana.cursojava;

import com.cesarmaydana.cursojava.dto.SaleRequestDto;
import com.cesarmaydana.cursojava.dto.ProductDto;
import com.cesarmaydana.cursojava.model.Client;
import com.cesarmaydana.cursojava.model.PriceList;
import com.cesarmaydana.cursojava.model.Product;
import com.cesarmaydana.cursojava.model.Sale;
import com.cesarmaydana.cursojava.repository.ClientRepository;
import com.cesarmaydana.cursojava.repository.ProductoRepository;
import com.cesarmaydana.cursojava.repository.SaleRepository;

import com.cesarmaydana.cursojava.service.PriceListService;
import com.cesarmaydana.cursojava.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SaleTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SaleRepository saleRepositoryRepository;

    @Mock
    private PriceListService listaPrecioService;

    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearVenta_Success() {
        // Preparación de los datos de prueba
        SaleRequestDto saleRequestDto = new SaleRequestDto();
        saleRequestDto.setClientId(1L);
        saleRequestDto.setProducts(List.of(new ProductDto(1L, 5)));

        Client client = new Client();
        client.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setStock(10);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(product));
        PriceList priceList = new PriceList();
        priceList.setId(1L);
        priceList.setPrecio(100.0);
        when(listaPrecioService.obtenerListaPrecioVigente(1L)).thenReturn(priceList);

        // Llamada al método bajo prueba
        String result = saleService.crearVenta(saleRequestDto);

        // Verificación
        assertEquals("Venta creada exitosamente", result);
        verify(productoRepository).save(any(Product.class));
        verify(saleRepositoryRepository).save(any(Sale.class));
    }

    @Test
    public void testCrearVenta_StockInsuficiente() {
        // Preparación de los datos de prueba
        SaleRequestDto saleRequestDto = new SaleRequestDto();
        saleRequestDto.setClientId(1L);
        saleRequestDto.setProducts(List.of(new ProductDto(1L, 20)));

        Client client = new Client();
        client.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setStock(10);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(product));
        PriceList priceList = new PriceList();
        priceList.setId(1L);
        priceList.setPrecio(100.0);
        when(listaPrecioService.obtenerListaPrecioVigente(1L)).thenReturn(priceList);

        // Llamada al método bajo prueba
        String result = saleService.crearVenta(saleRequestDto);

        // Verificación
        assertEquals("Stock insuficiente para el producto: " + product.getNombre(), result);
        verify(productoRepository, never()).save(any(Product.class));
        verify(saleRepositoryRepository, never()).save(any(Sale.class));
    }
}
