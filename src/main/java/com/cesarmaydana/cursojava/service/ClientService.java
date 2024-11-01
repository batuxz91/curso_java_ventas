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

    public List<Client> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Client> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Client crearCliente(Client cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Client> actualizarCliente(Long id, Client clienteActualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setEmail(clienteActualizado.getEmail());
            cliente.setTelefono(clienteActualizado.getTelefono());
            return clienteRepository.save(cliente);
        });
    }

}
