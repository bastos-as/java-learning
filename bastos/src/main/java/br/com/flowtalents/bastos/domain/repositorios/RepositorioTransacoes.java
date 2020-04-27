package br.com.flowtalents.bastos.domain.repositorios;

import br.com.flowtalents.bastos.domain.Conta;
import br.com.flowtalents.bastos.domain.Transacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioTransacoes extends CrudRepository<Transacao, Long> {

    List<Transacao> findByContaOrigemOrContaDestino(Conta conta1, Conta conta2);

    Transacao findById(long id);


}
