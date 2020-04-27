package br.com.flowtalents.bastos.domain.service;

import br.com.flowtalents.bastos.domain.Cliente;
import br.com.flowtalents.bastos.domain.repositorios.RepositorioClientes;
import br.com.flowtalents.bastos.exceptions.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrudClienteService {

    @Autowired
    private RepositorioClientes repositorioClientes;

    public Cliente salvar(Cliente cliente) {
        Cliente cpfExistente = repositorioClientes.findByCpf(cliente.getCpf());

        if(cpfExistente != null && !cpfExistente.equals(cliente)) throw new NegocioException("Já existe um cadastro com o CPF informado.");

        return repositorioClientes.save(cliente);
    }

    public void excluir(Long clienteId) {
        repositorioClientes.deleteById(clienteId);
    }

}
