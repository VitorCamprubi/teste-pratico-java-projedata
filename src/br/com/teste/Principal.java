package br.com.teste;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>();

        // 3.1 Inserir todos os funcionários
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        // 3.2 Remover João
        funcionarios.removeIf(funcionario -> funcionario.getNome().equalsIgnoreCase("João"));

        // 3.3 Imprimir todos os funcionários
        System.out.println("=== FUNCIONÁRIOS ===");
        imprimirLista(funcionarios);

        // 3.4 Aplicar aumento de 10%
        for (Funcionario funcionario : funcionarios) {
            BigDecimal novoSalario = funcionario.getSalario().multiply(new BigDecimal("1.10"));
            funcionario.setSalario(novoSalario.setScale(2, RoundingMode.HALF_UP));
        }

        System.out.println("\n=== FUNCIONÁRIOS COM AUMENTO DE 10% ===");
        imprimirLista(funcionarios);

        // 3.5 Agrupar por função em um MAP
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        // 3.6 Imprimir agrupados por função
        System.out.println("\n=== FUNCIONÁRIOS AGRUPADOS POR FUNÇÃO ===");
        for (Map.Entry<String, List<Funcionario>> entry : funcionariosPorFuncao.entrySet()) {
            System.out.println("\nFunção: " + entry.getKey());
            imprimirLista(entry.getValue());
        }

        // 3.8 Aniversariantes dos meses 10 e 12
        System.out.println("\n=== ANIVERSARIANTES DOS MESES 10 E 12 ===");
        funcionarios.stream()
                .filter(funcionario -> {
                    int mes = funcionario.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(Principal::imprimirFuncionario);

        // 3.9 Funcionário com maior idade
        Funcionario maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElse(null);

        if (maisVelho != null) {
            int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
            System.out.println("\n=== FUNCIONÁRIO COM MAIOR IDADE ===");
            System.out.println("Nome: " + maisVelho.getNome());
            System.out.println("Idade: " + idade + " anos");
        }

        // 3.10 Ordem alfabética
        System.out.println("\n=== FUNCIONÁRIOS EM ORDEM ALFABÉTICA ===");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(Principal::imprimirFuncionario);

        // 3.11 Total dos salários
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n=== TOTAL DOS SALÁRIOS ===");
        System.out.println("R$ " + formatarDecimal(totalSalarios));

        // 3.12 Quantos salários mínimos ganha cada funcionário
        System.out.println("\n=== SALÁRIOS MÍNIMOS POR FUNCIONÁRIO ===");
        for (Funcionario funcionario : funcionarios) {
            BigDecimal quantidadeSalarios = funcionario.getSalario()
                    .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);

            System.out.println(funcionario.getNome() + ": " + formatarDecimal(quantidadeSalarios) + " salários mínimos");
        }
    }

    private static void imprimirLista(List<Funcionario> funcionarios) {
        for (Funcionario funcionario : funcionarios) {
            imprimirFuncionario(funcionario);
        }
    }

    private static void imprimirFuncionario(Funcionario funcionario) {
        System.out.println(
                "Nome: " + funcionario.getNome()
                        + " | Data Nascimento: " + funcionario.getDataNascimento().format(FORMATADOR_DATA)
                        + " | Salário: R$ " + formatarDecimal(funcionario.getSalario())
                        + " | Função: " + funcionario.getFuncao()
        );
    }

    private static String formatarDecimal(BigDecimal valor) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(valor);
    }
}