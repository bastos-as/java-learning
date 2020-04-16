package br.com.flowtalents.bastos.banco;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super();
    }
    public SaldoInsuficienteException(String s) {
        super(s);
    }
    public SaldoInsuficienteException(String s, Throwable throwable) {
        super(s, throwable);
    }
    public SaldoInsuficienteException(Throwable throwable) {
        super(throwable);
    }
}
