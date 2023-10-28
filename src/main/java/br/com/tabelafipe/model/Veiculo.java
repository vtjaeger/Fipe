package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(
        @JsonAlias("Valor") String valor,
        @JsonAlias("Marca") String marca,
        @JsonAlias("Modelo") String modelo,
        @JsonAlias("AnoModelo") Integer ano,
        @JsonAlias("Combustivel") String tipoCombustivel
) {
    @Override
    public String toString() {
        return String.format("Marca: %s%n" +
                "Modelo: %s%n" +
                "Ano: %d%n" +
                "Valor: %s%n" +
                "Combustível: %s%n", marca, modelo, ano, valor, tipoCombustivel);
    }
}

