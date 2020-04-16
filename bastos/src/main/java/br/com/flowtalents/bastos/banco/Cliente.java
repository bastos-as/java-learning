package br.com.flowtalents.bastos.banco;

import javax.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String nome;
    private String telefone;
    private String endereco;
    private String email;
    private char sexo;
    @Column(unique=true)
    private String cpf;
    @OneToOne
    private Conta conta;

    public Cliente() {}

    public Cliente(String nome, String telefone, String endereco, String email, char sexo, String cpf) {
        setNome(nome);
        setTelefone(telefone);
        setEndereco(endereco);
        setEmail(email);
        setSexo(sexo);
        setCpf(cpf);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws IllegalArgumentException {
        String[] listaNome = nome.split(" ");
        for(int i = 0; i < listaNome.length; i++) {
            listaNome[i] = listaNome[i].replaceAll("[^a-zA-Z]", "");
        }
        String nomeCompleto = String.join(" ", listaNome);
        if(nomeCompleto.isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente deve ser informado.");
        }
        this.nome = nomeCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getFormattedTelefone() {
        return "(" + this.telefone.substring(0, 2) + ") " +
                this.telefone.substring(2);
    }

    public void setTelefone(String telefone) throws IllegalArgumentException {
        if (!telefone.matches("[0-9]+")) {
            throw new IllegalArgumentException("O telefone só pode conter números.");
        }
        if(telefone.length() < 10 || telefone.length() > 11) {
            throw new IllegalArgumentException("O número deve incluir o DDD e o número do telefone." +
                    "\nEx.: 51999994444, 5133334444");
        }
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException {
        if(!email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }

    public char getSexo() {
        return sexo;
    }

    public String getFormattedSexo() {
        switch(this.sexo) {
            case 'M':
                return "Masculino";
            case 'F':
                return "Feminino";
            case 'O':
                return "Outro";
            default:
                return "Não informado";
        }
    }

    public void setSexo(char sexo) {
        sexo = Character.toUpperCase(sexo);
        if(sexo != 'M' && sexo != 'F' && sexo != 'O') {
            throw new IllegalArgumentException("Sexo informado inválido." +
                    "\nO sexo informado deve ser M (masculino), F (feminino) ou O (outro).");
        }
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public String getFormattedCpf() {
        return this.cpf.substring(0, 3) +
                '.' +
                this.cpf.substring(3, 6) +
                '.' +
                this.cpf.substring(6, 9) +
                '-' +
                this.cpf.substring(9, 11);
    }

    public void setCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        if(cpf.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter 11 números.");
        }
        this.cpf = cpf;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        if(this.conta != null) {
            throw new OperacaoInvalidaException("Cliente já possui uma conta vinculada");
        }
        this.conta = conta;
    }

    @Override
    public String toString() {
        return "Nome: " + nome +
                " | Telefone: " + getFormattedTelefone() +
                " | Endereço: " + getEndereco() +
                " | E-mail: " + getEmail() +
                " | Sexo: " + getFormattedSexo() +
                " | CPF: " + getFormattedCpf() +
                " | " + this.conta.toString();
    }
}
