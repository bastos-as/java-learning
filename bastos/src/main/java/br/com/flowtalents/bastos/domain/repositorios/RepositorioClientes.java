package br.com.flowtalents.bastos.domain.repositorios;

import br.com.flowtalents.bastos.domain.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioClientes extends CrudRepository<Cliente, Long> {

    Cliente findById(long id);

    Cliente findByCpf(String cpf);

    List<Cliente> findByNomeContaining(String nome);
}
