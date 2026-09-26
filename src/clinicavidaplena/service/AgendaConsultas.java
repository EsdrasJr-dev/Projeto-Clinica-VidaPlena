package clinicavidaplena.service;

import clinicavidaplena.model.Consulta;
import clinicavidaplena.model.EstadoConsulta;
import clinicavidaplena.model.Medico;
import clinicavidaplena.model.Paciente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaConsultas {

    private final List<Consulta> consultas = new ArrayList<>();

    public Consulta agendar(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Nao e possivel agendar uma consulta no passado.");
        }

        for (Consulta consulta : consultas) {
            boolean mesmoMedico = consulta.getMedico().equals(medico);
            boolean mesmoHorario = consulta.getDataHora().equals(dataHora);
            boolean naoFoiCancelada = consulta.getEstado() != EstadoConsulta.CANCELADA;

            if (mesmoMedico && mesmoHorario && naoFoiCancelada) {
                throw new IllegalStateException(
                        "Ja existe uma consulta com este medico neste horario.");
            }
        }

        Consulta consulta = new Consulta(paciente, medico, dataHora);
        consultas.add(consulta);
        return consulta;
    }

    public List<Consulta> getConsultas() {
        return new ArrayList<>(consultas);
    }
}
