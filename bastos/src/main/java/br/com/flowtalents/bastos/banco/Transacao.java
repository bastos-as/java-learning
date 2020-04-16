package br.com.flowtalents.bastos.banco;

import br.com.flowtalents.bastos.banco.repositorios.RepositorioContas;
import br.com.flowtalents.bastos.banco.repositorios.RepositorioTransacoes;

import javax.management.InvalidAttributeValueException;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private BigDecimal valor;
    private BigDecimal saldoContaOrigemAposTransferencia;
    private BigDecimal saldoContaDestinoAposTransferencia;
    @OneToOne
    private Conta contaOrigem;
    @OneToOne
    private Conta contaDestino;

    public Transacao() {}

    public Transacao(BigDecimal valor, Conta contaOrigem, Conta contaDestino) {
        setValor(valor);
        this.saldoContaOrigemAposTransferencia = contaOrigem.getValor();
        this.saldoContaDestinoAposTransferencia = contaDestino.getValor();
        setContaOrigem(contaOrigem);
        setContaDestino(contaDestino);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldoContaOrigemAposTransferencia() {
        return saldoContaOrigemAposTransferencia;
    }

    public BigDecimal getSaldoContaDestinoAposTransferencia() {
        return saldoContaDestinoAposTransferencia;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    @Override
    public String toString() {
        return "Conta origem - " + getContaOrigem().toString() +
                " | Saldo após a transferência: " + getSaldoContaOrigemAposTransferencia() +
                " | Conta destino - " + getContaDestino().toString() +
                " | Saldo após a transferência: " + getSaldoContaDestinoAposTransferencia() +
                " | Valor transferido - " + getValor();
    }

    public static void transfere(String agenciaOrigem, String numeroOrigem,
                                 String agenciaDestino, String numeroDestino,
                                 BigDecimal valor,
                                 RepositorioContas repositorioContas,
                                 RepositorioTransacoes repositorioTransacoes) throws InvalidAttributeValueException {
        if(valor.compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidAttributeValueException("O valor a ser transferido deve ser positivo.");
        }

        Conta contaOrigem = repositorioContas.findByAgenciaAndNumero(agenciaOrigem, numeroOrigem);
        Conta contaDestino = repositorioContas.findByAgenciaAndNumero(agenciaDestino, numeroDestino);

        contaOrigem.transfere(valor, contaDestino);
        repositorioContas.save(contaOrigem);
        repositorioContas.save(contaDestino);

        repositorioTransacoes.save(new Transacao(valor, contaOrigem, contaDestino));

    }

}
