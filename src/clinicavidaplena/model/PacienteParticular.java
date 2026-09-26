package clinicavidaplena.model;

public class PacienteParticular extends Paciente {

    private final double valorConsultaParticular;

    public PacienteParticular(String nome, String documento, String telefone,
                               double valorConsultaParticular) {
        super(nome, documento, telefone);
        if (Double.isNaN(valorConsultaParticular)
                || Double.isInfinite(valorConsultaParticular)
                || valorConsultaParticular < 0) {
            throw new IllegalArgumentException("O valor da consulta nao pode ser negativo.");
        }
        this.valorConsultaParticular = valorConsultaParticular;
    }

    @Override
    public double valorConsulta() {
        return valorConsultaParticular;
    }
}
