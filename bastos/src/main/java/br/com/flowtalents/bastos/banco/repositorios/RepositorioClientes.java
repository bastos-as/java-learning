package br.com.flowtalents.bastos.banco.repositorios;

import br.com.flowtalents.bastos.banco.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioClientes extends CrudRepository<Cliente, Long> {

    Cliente findById(long id);
}
