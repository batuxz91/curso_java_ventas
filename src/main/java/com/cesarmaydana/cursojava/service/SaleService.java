package com.cesarmaydana.cursojava.service;

import com.cesarmaydana.cursojava.dto.SaleDto;
import com.cesarmaydana.cursojava.dto.SaleProductDto;
import com.cesarmaydana.cursojava.model.Product;
import com.cesarmaydana.cursojava.model.Sale;
import com.cesarmaydana.cursojava.model.SaleProduct;
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
    private PriceListService listaPrecioService;

    // Obtener todas las ventas
    public List<Sale> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta por ID
    public Optional<Sale> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // Crear una nueva venta con lógica de negocio
    public String crearVenta(Sale nuevaVenta) {

        if (nuevaVenta.getFecha() == null) {
            nuevaVenta.setFecha(LocalDate.now());
        }

        double totalMonto = 0.0;

        for (SaleProduct ventaProducto : nuevaVenta.getVentaProductos()) {
            Optional<Product> productoOpt = productoRepository.findById(ventaProducto.getProducto().getId());

            if (productoOpt.isPresent()) {
                Product producto = productoOpt.get();

                Double precio  = listaPrecioService.obtenerPrecioVigente(producto.getId());
                // Verificar si hay suficiente stock
                if (producto.getStock() >= ventaProducto.getCantidad()) {
                    ventaProducto.setProducto(producto);
                    ventaProducto.setVenta(nuevaVenta); // Asignar la venta al producto
                    totalMonto +=  precio * ventaProducto.getCantidad(); // Calcular el total
                    producto.setStock(producto.getStock() - ventaProducto.getCantidad()); // Actualizar stock
                    productoRepository.save(producto); // Guardar los cambios del producto
                } else {
                    return "Stock insuficiente para el producto: " + producto.getNombre();
                }
            } else {
                return "Producto no encontrado: " + ventaProducto.getProducto().getId();
            }
        }

        nuevaVenta.setMonto(totalMonto); // Establecer el monto total de la venta
        ventaRepository.save(nuevaVenta); // Guardar la venta en la base de datos

        return "Venta creada exitosamente";
    }

    // Cancelar venta y restaurar el stock
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

    // Método para convertir una venta a DTO
    public SaleDto convertirAVentaDTO(Sale venta) {
        SaleDto ventaDTO = new SaleDto();
        ventaDTO.setId(venta.getId());
        ventaDTO.setFecha(venta.getFecha());
        ventaDTO.setClienteId(venta.getCliente());
        ventaDTO.setMonto(venta.getMonto());
        ventaDTO.setProductos(
                venta.getVentaProductos().stream().map(vp -> {
                    SaleProductDto productoDTO = new SaleProductDto();
                    productoDTO.setNombre(vp.getProducto().getNombre());
                    productoDTO.setCantidad(vp.getCantidad());
                    productoDTO.setPrecio(listaPrecioService.obtenerPrecioVigente(vp.getProducto().getId()));
                    return productoDTO;
                }).toList()
        );
        return ventaDTO;
    }
}
