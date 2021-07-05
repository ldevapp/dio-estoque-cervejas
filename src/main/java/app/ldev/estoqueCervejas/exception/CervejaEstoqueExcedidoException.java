package app.ldev.estoqueCervejas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervejaEstoqueExcedidoException extends Exception {

    public CervejaEstoqueExcedidoException(Long id, int quantidadeParaIncrementar) {
        super(String.format("Cervejas com %s ID para incremento informado excede a capacidade máxima de estoque: %s", id, quantidadeParaIncrementar));
    }
}
