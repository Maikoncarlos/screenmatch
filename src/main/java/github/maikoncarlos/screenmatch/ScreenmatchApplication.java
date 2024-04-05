package github.maikoncarlos.screenmatch;

import github.maikoncarlos.screenmatch.model.DadosEpisodio;
import github.maikoncarlos.screenmatch.model.DadosSerie;
import github.maikoncarlos.screenmatch.model.DadosTemporadas;
import github.maikoncarlos.screenmatch.model.Episodio;
import github.maikoncarlos.screenmatch.service.ConsumoApi;
import github.maikoncarlos.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    Logger logger = Logger.getLogger(getClass().getName());

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados("https://www.omdbapi.com/?t=euphoria&apikey=6585022c");
        System.out.println(json + "\n");


        var converteDados = new ConverteDados();
        var dadosSerie = converteDados.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie + "\n");

        json = consumoApi.obterDados("https://www.omdbapi.com/?t=euphoria&season=1&episode=2&apikey=6585022c");
        var dadosEpisodio = converteDados.obterDados(json, DadosEpisodio.class);
        System.out.println(dadosEpisodio + "\n");


        List<DadosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoApi.obterDados("https://www.omdbapi.com/?t=euphoria&season=" + i + "&apikey=6585022c");
            var dadosTemporada = converteDados.obterDados(json, DadosTemporadas.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.stream().limit(1).forEach(System.out::println);
        System.out.println("\n");


        //Aqui nós pegamos a lista dentro da lista( flatMap )
        List<DadosEpisodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios()
                .stream())
                .toList();
        System.out.println("\n");

        episodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .limit(10)
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .forEach(System.out::println);
        System.out.println("\n");


        episodios.stream().filter(e -> Objects.equals(e.dataLancamento(), "2006-09-26")).forEach(System.out::println);
        System.out.println("\n");


        episodios.stream().limit(20) //limita somente aos dez primeiros
                .filter(e -> e.titulo().startsWith("S")) //filtra somente os que iniciam com a letra S
                .map(e -> e.titulo().toUpperCase()) //mapeia / transforma todas letras para maiusculas
                .forEach(System.out::println); //percorre a lista de episodios e imprime cada um
        System.out.println("\n");

        List<Episodio> episodioList = temporadas.stream()
                .flatMap(t -> t.episodios()
                        .stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .toList();

        episodioList.stream()
                .filter(e -> Objects.equals(e.getNumero(), 1))
                .forEach(System.out::println);
        System.out.println("\n");


        episodioList.stream()
                .filter(e -> e.getDataLancamento() != null &&
                        e.getDataLancamento().isAfter(LocalDate.of(2019, 1, 1)) &&
                        e.getTitulo().length() > 5 )
                .limit(10)
                .forEach(e -> System.out.println(
                        "Temporada:" + e.getTemporada() +
                        "  |  Episódio:" + e.getTitulo() +
                        "  |  Data Lançamento:" + e.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ));
    }
}
