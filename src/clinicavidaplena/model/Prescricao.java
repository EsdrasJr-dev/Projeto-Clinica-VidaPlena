package clinicavidaplena.model;

public class Prescricao {

    private final String medicamento;
    private final String posologia;

    Prescricao(String medicamento, String posologia) {
        this.medicamento = medicamento;
        this.posologia = posologia;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public String getPosologia() {
        return posologia;
    }
}
