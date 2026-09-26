package clinicavidaplena.model;

public class PacienteConvenio extends Paciente {

    private final Convenio convenio;
    private final double valorConvenio;
    private boolean autorizado;

    public PacienteConvenio(String nome, String documento, String telefone,
                             Convenio convenio, double valorConvenio) {
        super(nome, documento, telefone);
        if (Double.isNaN(valorConvenio)
                || Double.isInfinite(valorConvenio)
                || valorConvenio < 0) {
            throw new IllegalArgumentException("O valor da consulta nao pode ser negativo.");
        }
        this.convenio = convenio;
        this.valorConvenio = valorConvenio;
        this.autorizado = false;
        convenio.registrarPaciente(this);
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public boolean estaAutorizado() {
        return autorizado;
    }

    public void autorizar() {
        this.autorizado = true;
    }

    @Override
    public double valorConsulta() {
        if (!autorizado) {
            throw new IllegalStateException(
                "Consulta nao pode ser cobrada: convenio ainda nao autorizou o atendimento.");
        }
        return valorConvenio;
    }
}
