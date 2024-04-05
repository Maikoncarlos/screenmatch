package github.maikoncarlos.screenmatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Episodio{
       private Integer temporada;
       private String titulo;
       private Integer numero;
       private Double avaliacao;
       private LocalDate dataLancamento;

    public Episodio(Integer numero, DadosEpisodio episodio) {
        this.temporada = numero;
        this.titulo = episodio.titulo();
        this.numero = episodio.numero();

        try{
            this.avaliacao = Double.valueOf(episodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(episodio.dataLancamento());
        }catch (DateTimeParseException ex){
            this.dataLancamento = null;
        }
    }
}

