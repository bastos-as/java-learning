package br.com.flowtalents.bastos.controller;

import br.com.flowtalents.bastos.banco.Cliente;
import br.com.flowtalents.bastos.banco.Conta;
import br.com.flowtalents.bastos.banco.Transacao;
import br.com.flowtalents.bastos.banco.repositorios.RepositorioClientes;
import br.com.flowtalents.bastos.banco.repositorios.RepositorioContas;
import br.com.flowtalents.bastos.banco.repositorios.RepositorioTransacoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/")
public class MainController {
    @Autowired
    private RepositorioClientes repositorioClientes;

    @Autowired
    private RepositorioContas repositorioContas;

    @Autowired
    private RepositorioTransacoes repositorioTransacoes;

    @GetMapping(path="/allclientes")
    public @ResponseBody Iterable<Cliente> getAllClientes() {
        return repositorioClientes.findAll();
    }

    @GetMapping(path="/transacoesrealizadas/{id}")
    public @ResponseBody Iterable<Transacao> transacoesRealizadas(
            @PathVariable("id") int id) {
        Conta conta = repositorioContas.findById(id);

        return conta.verificarTransacoes(repositorioTransacoes);
    }
}
