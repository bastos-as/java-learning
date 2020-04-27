package br.com.flowtalents.bastos;

import br.com.flowtalents.bastos.domain.*;
import br.com.flowtalents.bastos.domain.repositorios.RepositorioClientes;
import br.com.flowtalents.bastos.domain.repositorios.RepositorioContas;
import br.com.flowtalents.bastos.domain.repositorios.RepositorioTransacoes;
import br.com.flowtalents.bastos.exceptions.OperacaoInvalidaException;
import br.com.flowtalents.bastos.exceptions.SaldoInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;

import javax.management.InvalidAttributeValueException;
import java.math.BigDecimal;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RepositorioClientes repositorioClientes,
                                               RepositorioContas repositorioContas,
                                               RepositorioTransacoes repositorioTransacoes) {

        return args -> {

            Cliente cliente1 = new Cliente(
                    "Adair",
                    "51995599559",
                    "Rua Abc 123",
                    "adair@email.com.br",
                    'M',
                    "333.555.777-22");

            Cliente cliente2 = new Cliente(
                    "Carlos da Silva",
                    "5133334444",
                    "Rua Abc 321",
                    "joao@ggggg",
                    'O',
                    "55544433322");

            Cliente cliente3 = new Cliente(
                    "Ivone",
                    "01888884646",
                    "Rua Abc 222",
                    "ivone@email.com.br",
                    'F',
                    "99999955511");


            Conta conta1 = new Conta("12345", "555", TipoConta.CORRENTE, BigDecimal.valueOf(0));
            try {
                repositorioContas.save(conta1);
            } catch(DataIntegrityViolationException e) {
                e.getMessage();
            }
            try {
                cliente1.setConta(conta1);
                repositorioClientes.save(cliente1);
            } catch(DataIntegrityViolationException e) {
                e.getMessage();
            }

            Conta conta2 = new Conta("12345", "556", TipoConta.POUPANCA, BigDecimal.valueOf(500));
            try {
                repositorioContas.save(conta2);
            } catch(DataIntegrityViolationException e) {
                e.getMessage();
            }
            try {
                cliente2.setConta(conta2);
                repositorioClientes.save(cliente2);
            } catch(DataIntegrityViolationException e) {
                e.getMessage();
            }

            Conta conta3 = new Conta("54321", "557", TipoConta.SALARIO, BigDecimal.valueOf(200));
            try {
                repositorioContas.save(conta3);
            } catch(DataIntegrityViolationException e) {
                e.getMessage();
            }
            try {
                cliente3.setConta(conta3);
                repositorioClientes.save(cliente3);
            } catch(DataIntegrityViolationException e) {
                e.getMessage();
            }

            log.info("-------------------------------");

            try {
                Transacao.transfere("54321", "557",
                        "12345", "555",
                        BigDecimal.valueOf(50),
                        repositorioContas, repositorioTransacoes);
                log.info("Transação 1 concluída com sucesso");
            } catch (SaldoInsuficienteException |InvalidAttributeValueException| OperacaoInvalidaException e) {
                e.getMessage();
            }

            try {  //Transferência de realizada por conta sem saldo
                Transacao.transfere("12345", "555",
                        "12345", "556",
                        BigDecimal.valueOf(500),
                        repositorioContas, repositorioTransacoes);
                log.info("Transação 2 concluída com sucesso");
            } catch (SaldoInsuficienteException|InvalidAttributeValueException|OperacaoInvalidaException e) {
                e.getMessage();
            }

            try {  //Transferência para conta salário
                Transacao.transfere("12345", "556",
                        "54321", "557",
                        BigDecimal.valueOf(500),
                        repositorioContas, repositorioTransacoes);
                log.info("Transação 3 concluída com sucesso");
            } catch (SaldoInsuficienteException|InvalidAttributeValueException|OperacaoInvalidaException e) {
                e.getMessage();
            }

            log.info("Transações realizadas pela conta 556, agência 12345:");
            Conta conta = repositorioContas.findByAgenciaAndNumero("12345", "555");
            for(Transacao transacao : repositorioTransacoes.findByContaOrigemOrContaDestino(conta, conta)) {
                log.info(transacao.toString());
            }

            log.info("Informações dos clientes cadastrados:");
            for(Cliente cliente : repositorioClientes.findAll()) {
                log.info(cliente.toString());
            }

        };
    }

}