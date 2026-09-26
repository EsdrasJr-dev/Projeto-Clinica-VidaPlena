package clinicavidaplena.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Consulta {

    private final Paciente paciente;
    private final Medico medico;
    private final LocalDateTime dataHora;
    private final double valor;
    private EstadoConsulta estado;
    private final List<Prescricao> prescricoes = new ArrayList<>();

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        // Polimorfismo: cada tipo de paciente calcula o próprio valor.
        this.valor = paciente.valorConsulta();
        this.estado = EstadoConsulta.MARCADA;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public double getValor() {
        return valor;
    }

    public EstadoConsulta getEstado() {
        return estado;
    }

    public void realizar() {
        if (estado != EstadoConsulta.MARCADA) {
            throw new IllegalStateException("So e possivel realizar uma consulta que esteja MARCADA.");
        }
        this.estado = EstadoConsulta.REALIZADA;
    }

    public void cancelar() {
        if (estado != EstadoConsulta.MARCADA) {
            throw new IllegalStateException("So e possivel cancelar uma consulta que esteja MARCADA.");
        }
        this.estado = EstadoConsulta.CANCELADA;
    }

    public void prescrever(String medicamento, String posologia) {
        if (estado != EstadoConsulta.REALIZADA) {
            throw new IllegalStateException("So e possivel prescrever depois que a consulta for REALIZADA.");
        }
        prescricoes.add(new Prescricao(medicamento, posologia));
    }

    public List<Prescricao> getPrescricoes() {
        return new ArrayList<>(prescricoes);
    }
}
