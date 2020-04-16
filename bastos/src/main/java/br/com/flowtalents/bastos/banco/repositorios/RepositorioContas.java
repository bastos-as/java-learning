package br.com.flowtalents.bastos.banco.repositorios;

import br.com.flowtalents.bastos.banco.Conta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioContas extends CrudRepository<Conta, Long> {

    Conta findByAgenciaAndNumero(String agencia, String numero);

    Conta findById(long id);
}
