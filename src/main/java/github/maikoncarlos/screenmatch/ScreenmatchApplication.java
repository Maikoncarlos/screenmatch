package github.maikoncarlos.screenmatch;

import github.maikoncarlos.screenmatch.model.DadosEpisodio;
import github.maikoncarlos.screenmatch.model.DadosSerie;
import github.maikoncarlos.screenmatch.service.ConsumoApi;
import github.maikoncarlos.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);

		var converteDados = new ConverteDados();
		var dadosSerie = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);

		json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c");
		var dadosEpisodio = converteDados.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);


	}
}
