package clinicavidaplena.model;

public abstract class Paciente {

    private final String nome;
    private final String documento;
    private final String telefone;

    protected Paciente(String nome, String documento, String telefone) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public abstract double valorConsulta();
}
