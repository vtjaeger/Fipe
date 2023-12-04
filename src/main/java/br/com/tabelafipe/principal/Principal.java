package br.com.tabelafipe.principal;

import br.com.tabelafipe.model.Dados;
import br.com.tabelafipe.model.Modelos;
import br.com.tabelafipe.model.Veiculo;
import br.com.tabelafipe.service.ConsumoApi;
import br.com.tabelafipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    public void exibeMenu(){
        var menu = "Deseja pesquisar entre: " +
                "Carros, Motos ou Caminhoes?";
        System.out.println(menu);

        var opcaoDigitada = scanner.nextLine();
        String endereco;

        // se a opcao digitada (em minusculo) conter "xxx", altere o endereco para
        // https://parallelum.com.br/fipe/api/v1/OPCAODIGITADA/marcas

        if(opcaoDigitada.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if(opcaoDigitada.toLowerCase().contains("mot")){
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        // requisicao para api com o endereco
        var json = consumoApi.obterDados(endereco);

        // json sem toString: System.out.println(json);

        var marcas = converteDados.obterLista(json, Dados.class);

        marcas.stream()
                //ordena pela propriedade CODIGO que tem na record DADOS
                .sorted(Comparator.comparing(Dados::codigo))
                //para cada MARCA imprime de acordo com a record DADOS (CODIGO E NOME)
                .forEach(System.out::println);

        System.out.println("Informe o codigo da marca para consulta:");
        var codigoMarca = scanner.nextLine();

        // altera o endereco dnv para enderecoAntigo/codigoMarca/modelos
        endereco = endereco + "/" + codigoMarca + "/modelos";

        // requisicao com o novo endereco
        json = consumoApi.obterDados(endereco);

        // transforma o json em uma lista formato MODELOS
        var modeloLista = converteDados.obterDados(json, Modelos.class);

        System.out.println("Modelos da marca: \n");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite o nome do carro:");
        var nomeAuto = scanner.nextLine();

        List<Dados> modelosAuto = modeloLista.modelos().stream()
                // filtra por modelos (m) em minusculo que contem nomeAuto em minusculo
                .filter(m -> m.nome().toLowerCase().contains(nomeAuto.toLowerCase()))
                // transforma em uma lista imutavel (nao pode remover em adicionar
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        // imprime a lista acima
        modelosAuto.forEach(System.out::println);

        System.out.println("\nDigite o codigo do carro");
        var codigoAuto = scanner.nextInt();

        endereco = endereco + "/" + codigoAuto + "/anos";
        // requisicao com novo json
        json = consumoApi.obterDados(endereco);

        // cria lista de dados
        List<Dados> anos = converteDados.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = converteDados.obterDados(json, Veiculo.class);

            veiculos.add(veiculo);
        }
        System.out.println("\nTodos os veiculos:");
        veiculos.forEach(System.out::println);
    }

}
