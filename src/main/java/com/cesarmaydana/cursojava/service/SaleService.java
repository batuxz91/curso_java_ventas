package com.cesarmaydana.cursojava.service;

import com.cesarmaydana.cursojava.dto.ProductDto;
import com.cesarmaydana.cursojava.dto.SaleResponseDto;
import com.cesarmaydana.cursojava.dto.SaleProductDto;
import com.cesarmaydana.cursojava.dto.SaleRequestDto;
import com.cesarmaydana.cursojava.model.*;
import com.cesarmaydana.cursojava.repository.ClientRepository;
import com.cesarmaydana.cursojava.repository.ProductoRepository;
import com.cesarmaydana.cursojava.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private PriceListService listaPrecioService;

    public List<Sale> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public Optional<Sale> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public String crearVenta(SaleRequestDto saleRequestDto) {

        Sale sale = new Sale();
        Optional<Client> clientOpt = clientRepository.findById(saleRequestDto.getClientId());
        if (clientOpt.isPresent()) {
            sale.setCliente(clientOpt.get());
        } else {
            return "Cliente no encontrado";
        }

        if (saleRequestDto.getFecha() == null) {
            sale.setFecha(LocalDate.now());
        } else {
            sale.setFecha(saleRequestDto.getFecha());
        }

        double totalMonto = 0.0;

        for (ProductDto productDto : saleRequestDto.getProducts()) {
            Optional<Product> productoOpt = productoRepository.findById(productDto.getProductId());

            if (productoOpt.isPresent()) {
                Product producto = productoOpt.get();

                PriceList listaPrecioVigente = listaPrecioService.obtenerListaPrecioVigente(producto.getId());
                if (listaPrecioVigente == null) {
                    return "Lista de precios no encontrada para el producto: " + producto.getNombre();
                }
                Double precio = listaPrecioVigente.getPrecio();
                if (producto.getStock() >= productDto.getCantidad()) {
                    SaleProduct saleProduct = new SaleProduct();
                    saleProduct.setProducto(producto);
                    saleProduct.setVenta(sale);
                    saleProduct.setCantidad(productDto.getCantidad());
                    saleProduct.setListaPrecioVigente(listaPrecioVigente);
                    sale.getVentaProductos().add(saleProduct);

                    totalMonto += precio * productDto.getCantidad();
                    producto.setStock(producto.getStock() - productDto.getCantidad());

                    productoRepository.save(producto);
                } else {
                    return "Stock insuficiente para el producto: " + producto.getNombre();
                }
            } else {
                return "Producto no encontrado: " + productDto.getProductId();
            }
        }

        sale.setMonto(totalMonto);
        ventaRepository.save(sale);

        return "Venta creada exitosamente";
    }


    public boolean cancelarVenta(Long id) {
        Optional<Sale> ventaOpt = obtenerVentaPorId(id);

        if (ventaOpt.isPresent()) {
            Sale venta = ventaOpt.get();
            for (SaleProduct ventaProducto : venta.getVentaProductos()) {
                Product producto = ventaProducto.getProducto();
                producto.setStock(producto.getStock() + ventaProducto.getCantidad()); // Restaurar stock
                productoRepository.save(producto); // Guardar los cambios en el producto
            }
            ventaRepository.deleteById(id); // Eliminar la venta
            return true;
        }

        return false;
    }

    public SaleResponseDto convertirAVentaDTO(Sale venta) {
        SaleResponseDto ventaDTO = new SaleResponseDto();
        ventaDTO.setId(venta.getId());
        ventaDTO.setFecha(venta.getFecha());
        ventaDTO.setClienteId(venta.getCliente());
        ventaDTO.setMonto(venta.getMonto());
        ventaDTO.setProductos(
                venta.getVentaProductos().stream().map(vp -> {
                    SaleProductDto productoDTO = new SaleProductDto();
                    productoDTO.setNombre(vp.getProducto().getNombre());
                    productoDTO.setCantidad(vp.getCantidad());
                    productoDTO.setPrecio(vp.getListaPrecioVigente().getPrecio());
                    return productoDTO;
                }).toList()
        );
        return ventaDTO;
    }
}
