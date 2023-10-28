package br.com.tabelafipe.principal;

import br.com.tabelafipe.model.Dados;
import br.com.tabelafipe.model.Modelos;
import br.com.tabelafipe.model.Veiculo;
import br.com.tabelafipe.service.ConsumoApi;
import br.com.tabelafipe.service.ConverteDados;
import org.springframework.boot.Banner;

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
        var menu = """
                *** OPCOES ***
                Carros
                Motos
                Caminhoes
                
                Digite a opcao escolhida:""";
        System.out.println(menu);

        var opcao = scanner.nextLine();
        String endereco;

        if(opcao.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if(opcao.toLowerCase().contains("mot")){
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }
        var json = consumoApi.obterDados(endereco);
        //System.out.println(json);

        var marcas = converteDados.obterLista(json, Dados.class);
        //System.out.println(marcas);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o codigo da marca para consulta:");
        var codigoMarca = scanner.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumoApi.obterDados(endereco);

        var modeloLista = converteDados.obterDados(json, Modelos.class);
        System.out.println("Modelos da marca: \n");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite o nome do carro:");
        var nomeAuto = scanner.nextLine();

        List<Dados> modelosAuto = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeAuto.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosAuto.forEach(System.out::println);

        System.out.println("\nDigite o codigo do carro");
        var codigoAuto = scanner.nextInt();

        endereco = endereco + "/" + codigoAuto + "/anos";
        json = consumoApi.obterDados(endereco);

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
