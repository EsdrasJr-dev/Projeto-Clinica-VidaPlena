package clinicavidaplena.model;

// Especialidade médica (ex: Cardiologia, Pediatria).

public class Especialidade {

    private final String nome;

    public Especialidade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
