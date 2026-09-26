package clinicavidaplena.app;

import clinicavidaplena.model.*;
import clinicavidaplena.service.AgendaConsultas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Sistema de agendamento da Clinica VidaPlena rodando apenas no console, sem armazenamento em DB.

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final List<Medico> medicos = new ArrayList<>();
    private static final List<Convenio> convenios = new ArrayList<>();
    private static final List<Paciente> pacientes = new ArrayList<>();
    private static final AgendaConsultas agenda = new AgendaConsultas();

    public static void main(String[] args) {
        System.out.println("=== Clinica VidaPlena - Sistema de Agendamento ===");
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opcao = SCANNER.nextLine().trim();
            switch (opcao) {
                case "1": cadastrarMedico(); break;
                case "2": cadastrarConvenio(); break;
                case "3": cadastrarPacienteParticular(); break;
                case "4": cadastrarPacienteConvenio(); break;
                case "5": autorizarConvenio(); break;
                case "6": agendarConsulta(); break;
                case "7": realizarConsulta(); break;
                case "8": cancelarConsulta(); break;
                case "9": prescrever(); break;
                case "10": listarConsultas(); break;
                case "0":
                    rodando = false;
                    System.out.println("Encerrando o sistema. Ate mais!");
                    break;
                default:
                    System.out.println("Opcao invalida, tente novamente.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("1  - Cadastrar medico");
        System.out.println("2  - Cadastrar convenio");
        System.out.println("3  - Cadastrar paciente particular");
        System.out.println("4  - Cadastrar paciente de convenio");
        System.out.println("5  - Autorizar paciente de convenio");
        System.out.println("6  - Agendar consulta");
        System.out.println("7  - Realizar consulta");
        System.out.println("8  - Cancelar consulta");
        System.out.println("9  - Prescrever medicamento");
        System.out.println("10 - Listar consultas");
        System.out.println("0  - Sair");
        System.out.print("Escolha uma opcao: ");
    }

    //Cadastros

    private static void cadastrarMedico() {
        System.out.print("Nome do medico: ");
        String nome = SCANNER.nextLine();
        System.out.print("Especialidade: ");
        String nomeEspecialidade = SCANNER.nextLine();
        medicos.add(new Medico(nome, new Especialidade(nomeEspecialidade)));
        System.out.println("Medico cadastrado com sucesso.");
    }

    private static void cadastrarConvenio() {
        System.out.print("Nome do convenio: ");
        String nome = SCANNER.nextLine();
        convenios.add(new Convenio(nome));
        System.out.println("Convenio cadastrado com sucesso.");
    }

    private static void cadastrarPacienteParticular() {
        System.out.print("Nome do paciente: ");
        String nome = SCANNER.nextLine();
        System.out.print("Documento: ");
        String documento = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        double valor = lerValor("Valor da consulta particular: ");
        pacientes.add(new PacienteParticular(nome, documento, telefone, valor));
        System.out.println("Paciente particular cadastrado com sucesso.");
    }

    private static void cadastrarPacienteConvenio() {
        if (convenios.isEmpty()) {
            System.out.println("Cadastre um convenio primeiro (opcao 2).");
            return;
        }
        System.out.print("Nome do paciente: ");
        String nome = SCANNER.nextLine();
        System.out.print("Documento: ");
        String documento = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        Convenio convenio = escolherConvenio();
        if (convenio == null) {
            return;
        }
        double valor = lerValor("Valor da consulta pelo convenio: ");
        pacientes.add(new PacienteConvenio(nome, documento, telefone, convenio, valor));
        System.out.println("Paciente de convenio cadastrado (ainda sem autorizacao -- use a opcao 5).");
    }

    private static void autorizarConvenio() {
        PacienteConvenio paciente = escolherPacienteConvenio();
        if (paciente == null) {
            return;
        }
        paciente.autorizar();
        System.out.println("Autorizacao concedida.");
    }

    //Agenda

    private static void agendarConsulta() {
        if (medicos.isEmpty() || pacientes.isEmpty()) {
            System.out.println("Cadastre ao menos um medico e um paciente antes de agendar.");
            return;
        }
        System.out.println("-- Pacientes --");
        Paciente paciente = escolherPaciente();
        if (paciente == null) {
            return;
        }
        System.out.println("-- Medicos --");
        Medico medico = escolherMedico();
        if (medico == null) {
            return;
        }
        LocalDateTime dataHora = lerDataHora("Data e hora (dd/MM/yyyy HH:mm): ");
        if (dataHora == null) {
            return;
        }
        try {
            Consulta consulta = agenda.agendar(paciente, medico, dataHora);
            System.out.println("Consulta agendada. Valor: R$ " + consulta.getValor());
        } catch (IllegalStateException e) {
            System.out.println("Nao foi possivel agendar: " + e.getMessage());
        }
    }

    private static void realizarConsulta() {
        Consulta consulta = escolherConsulta();
        if (consulta == null) {
            return;
        }
        try {
            consulta.realizar();
            System.out.println("Consulta marcada como realizada.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void cancelarConsulta() {
        Consulta consulta = escolherConsulta();
        if (consulta == null) {
            return;
        }
        try {
            consulta.cancelar();
            System.out.println("Consulta cancelada.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void prescrever() {
        Consulta consulta = escolherConsulta();
        if (consulta == null) {
            return;
        }
        System.out.print("Medicamento: ");
        String medicamento = SCANNER.nextLine();
        System.out.print("Posologia: ");
        String posologia = SCANNER.nextLine();
        try {
            consulta.prescrever(medicamento, posologia);
            System.out.println("Prescricao registrada.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarConsultas() {
        List<Consulta> consultas = agenda.getConsultas();
        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta agendada ainda.");
            return;
        }
        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            System.out.printf("%d) %s com %s em %s - estado: %s - valor: R$ %.2f%n",
                    i, c.getPaciente().getNome(), c.getMedico().getNome(),
                    c.getDataHora().format(FORMATO_DATA), c.getEstado(), c.getValor());
        }
    }

    //Selecao a partir das listas

    private static Medico escolherMedico() {
        for (int i = 0; i < medicos.size(); i++) {
            System.out.println(i + " - " + medicos.get(i).getNome());
        }
        int indice = lerInteiro("Escolha o medico pelo numero: ");
        if (indice < 0 || indice >= medicos.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return medicos.get(indice);
    }

    private static Convenio escolherConvenio() {
        for (int i = 0; i < convenios.size(); i++) {
            System.out.println(i + " - " + convenios.get(i).getNome());
        }
        int indice = lerInteiro("Escolha o convenio pelo numero: ");
        if (indice < 0 || indice >= convenios.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return convenios.get(indice);
    }

    private static Paciente escolherPaciente() {
        for (int i = 0; i < pacientes.size(); i++) {
            System.out.println(i + " - " + pacientes.get(i).getNome());
        }
        int indice = lerInteiro("Escolha o paciente pelo numero: ");
        if (indice < 0 || indice >= pacientes.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return pacientes.get(indice);
    }

    private static PacienteConvenio escolherPacienteConvenio() {
        List<PacienteConvenio> lista = new ArrayList<>();
        for (Paciente p : pacientes) {
            if (p instanceof PacienteConvenio) {
                lista.add((PacienteConvenio) p);
            }
        }
        if (lista.isEmpty()) {
            System.out.println("Nenhum paciente de convenio cadastrado.");
            return null;
        }
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(i + " - " + lista.get(i).getNome());
        }
        int indice = lerInteiro("Escolha o paciente pelo numero: ");
        if (indice < 0 || indice >= lista.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return lista.get(indice);
    }

    private static Consulta escolherConsulta() {
        List<Consulta> consultas = agenda.getConsultas();
        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta cadastrada ainda.");
            return null;
        }
        listarConsultas();
        int indice = lerInteiro("Escolha a consulta pelo numero: ");
        if (indice < 0 || indice >= consultas.size()) {
            System.out.println("Indice invalido.");
            return null;
        }
        return consultas.get(indice);
    }

    //Leitura com validacao

    private static double lerValor(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(
                        SCANNER.nextLine().trim().replace(",", "."));
                if (Double.isNaN(valor) || Double.isInfinite(valor) || valor < 0) {
                    System.out.println("O valor precisa ser um numero igual ou maior que zero.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite um numero, por exemplo 150,00.");
            }
        }
    }

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(SCANNER.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static LocalDateTime lerDataHora(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                LocalDateTime dataHora = LocalDateTime.parse(
                        SCANNER.nextLine().trim(), FORMATO_DATA);

                if (dataHora.isBefore(LocalDateTime.now())) {
                    System.out.println("A consulta nao pode ser agendada no passado.");
                } else {
                    return dataHora;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Data/hora invalida. Use o formato dd/MM/yyyy HH:mm.");
            }
        }
    }
}
