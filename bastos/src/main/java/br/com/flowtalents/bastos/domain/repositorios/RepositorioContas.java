package br.com.flowtalents.bastos.domain.repositorios;

import br.com.flowtalents.bastos.domain.Conta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioContas extends CrudRepository<Conta, Long> {

    Conta findByAgenciaAndNumero(String agencia, String numero);

    Conta findById(long id);
}
