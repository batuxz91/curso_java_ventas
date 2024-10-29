package com.cesarmaydana.cursojava.service;

import com.cesarmaydana.cursojava.model.Client;
import com.cesarmaydana.cursojava.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clienteRepository;

    // Obtener todos los clientes
    public List<Client> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    // Obtener cliente por ID
    public Optional<Client> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Crear un nuevo cliente
    public Client crearCliente(Client cliente) {
        // Aquí puedes agregar cualquier lógica adicional como validaciones
        return clienteRepository.save(cliente);
    }

    // Actualizar un cliente existente
    public Optional<Client> actualizarCliente(Long id, Client clienteActualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setEmail(clienteActualizado.getEmail());
            cliente.setTelefono(clienteActualizado.getTelefono());
            return clienteRepository.save(cliente);
        });
    }

    // Eliminar un cliente por ID
    public boolean eliminarCliente(Long id) {
        return clienteRepository.findById(id).map(cliente -> {
            clienteRepository.delete(cliente);
            return true;
        }).orElse(false);
    }
}
