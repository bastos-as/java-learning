package br.com.flowtalents.bastos.controller;

import br.com.flowtalents.bastos.domain.Cliente;
import br.com.flowtalents.bastos.domain.repositorios.RepositorioClientes;
import br.com.flowtalents.bastos.domain.service.CrudClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private RepositorioClientes repositorioClientes;

    @Autowired
    private CrudClienteService crudClienteService;

    @GetMapping
    public Iterable<Cliente> listarClientes() {
        return repositorioClientes.findAll();
    }

    @GetMapping(path="/{clienteId}")
    public ResponseEntity<Cliente> bascarCliente(@PathVariable Long clienteId) {

        Optional<Cliente> cliente = repositorioClientes.findById(clienteId);

        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path="/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente cadastrarCliente(@Valid @RequestBody Cliente cliente) {
        return crudClienteService.salvar(cliente);
    }

    @PutMapping(path="/atualizar/{clienteId}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente cliente) {
        if(!repositorioClientes.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        cliente.setId(clienteId);
        cliente = crudClienteService.salvar(cliente);

        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/excluir/{clienteId")
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId) {
        if(!repositorioClientes.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        crudClienteService.excluir(clienteId);

        return ResponseEntity.noContent().build();
    }


}
