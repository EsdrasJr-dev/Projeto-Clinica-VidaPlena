package clinicavidaplena.model;

import java.util.ArrayList;
import java.util.List;

public class Convenio {

    private final String nome;
    private final List<PacienteConvenio> pacientes = new ArrayList<>();

    public Convenio(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    void registrarPaciente(PacienteConvenio paciente) {
        pacientes.add(paciente);
    }

    public List<PacienteConvenio> getPacientes() {
        return new ArrayList<>(pacientes);
    }
}
