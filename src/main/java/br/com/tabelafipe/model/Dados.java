package br.com.tabelafipe.model;

public record Dados(String codigo, String nome) {
    @Override
    public String toString() {
        return String.format("Código: %s%n" +
                "Nome: %s%n", codigo, nome);
    }
}
