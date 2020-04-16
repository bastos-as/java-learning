package br.com.flowtalents.bastos.banco;

public class OperacaoInvalidaException extends RuntimeException {
    public OperacaoInvalidaException() {
        super();
    }
    public OperacaoInvalidaException(String s) {
        super(s);
    }
    public OperacaoInvalidaException(String s, Throwable throwable) {
        super(s, throwable);
    }
    public OperacaoInvalidaException(Throwable throwable) {
        super(throwable);
    }
}
