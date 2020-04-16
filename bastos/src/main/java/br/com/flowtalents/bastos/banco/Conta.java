package br.com.flowtalents.bastos.banco;

import br.com.flowtalents.bastos.banco.repositorios.RepositorioContas;
import br.com.flowtalents.bastos.banco.repositorios.RepositorioTransacoes;

import javax.management.InvalidAttributeValueException;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"agencia", "numero"})
})
public class Conta {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String agencia;
    private String numero;
    private TipoConta tipoConta;
    @OneToOne
    private Cliente cliente;
    private BigDecimal valor;

    public Conta() {
        this.valor = BigDecimal.valueOf(0);
    }

    public Conta(String agencia, String numero, TipoConta tipoConta, BigDecimal valor) throws InvalidAttributeValueException {
        setAgencia(agencia);
        setNumero(numero);
        setTipoConta(tipoConta);
        this.valor = valor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) throws InvalidAttributeValueException {
        agencia = agencia.replaceAll("[^0-9]", "");
        if(agencia.length() != 5) {
            throw new InvalidAttributeValueException("A agência deve conter 5 números.");
        }
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) throws InvalidAttributeValueException {
        numero = numero.replaceAll("[^0-9]", "");
        if(numero.isEmpty()) {
            throw new InvalidAttributeValueException("O número da conta deve conter no mínimo um número.");
        }
        this.numero = numero;
    }

    public TipoConta getTipoConta() {
        return this.tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    void deposita(BigDecimal valor) throws InvalidAttributeValueException {
        if(valor.compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidAttributeValueException("O valor do depósito deve ser positivo.");
        }
        this.valor = this.valor.add(valor);
    }

    void saca(BigDecimal valor) throws InvalidAttributeValueException {
        if(valor.compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidAttributeValueException("O valor a sacar deve ser positivo.");
        }
        BigDecimal novoValor = this.valor.subtract(valor);
        if(novoValor.compareTo(BigDecimal.valueOf(-2000)) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        this.valor = novoValor;
    }

    void transfere(BigDecimal valor, Conta contaDestino) throws InvalidAttributeValueException {
        if(valor.compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidAttributeValueException("O valor a ser transferido deve ser positivo.");
        }
        BigDecimal novoValor = this.valor.subtract(valor);
        if(novoValor.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        if(contaDestino.getTipoConta() == TipoConta.SALARIO) {
            throw new OperacaoInvalidaException("A conta de destino não pode ser Conta Salário.");
        }
        contaDestino.deposita(valor);
        this.valor = novoValor;
    }

    public List<Transacao> verificarTransacoes(RepositorioTransacoes repositorioTransacoes) {
        return repositorioTransacoes.findByContaOrigemOrContaDestino(this, this);
    }

    @Override
    public String toString() {
        return "Agência: " + getAgencia() + " | Conta " + getTipoConta().toString().toLowerCase() + ": " + getNumero();
    }

    public static void deposita(String agencia, String numero,
                                BigDecimal valor, RepositorioContas repositorioContas) throws InvalidAttributeValueException {
        if (valor.compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidAttributeValueException("O valor do depósito deve ser positivo.");
        }
        Conta conta = repositorioContas.findByAgenciaAndNumero(agencia, numero);
        conta.deposita(valor);
        repositorioContas.save(conta);
    }

}
