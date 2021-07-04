package app.ldev.estoqueCervejas.builder;

import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.enums.TipoCerveja;
import lombok.Builder;

@Builder
public class CervejaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "Brahma";

    @Builder.Default
    private String marca = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantidade = 10;

    @Builder.Default
    private TipoCerveja tipo = TipoCerveja.LAGER;

    public CervejaDTO toCervejaDTO() {
        return new CervejaDTO(id,
                nome,
                marca,
                max,
                quantidade,
                tipo);
    }

}
