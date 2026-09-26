package clinicavidaplena.model;

public class Medico {

    private final String nome;
    private final Especialidade especialidade;

    public Medico(String nome, Especialidade especialidade) {
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public String getNome() {
        return nome;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }
}
