package app.ldev.estoqueCervejas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CervejaNaoEncontradaException extends Exception {

    public CervejaNaoEncontradaException(String nomeCerveja) {
        super(String.format("Cerveja com nome %s não encontrada no sistema.", nomeCerveja));
    }

    public CervejaNaoEncontradaException(Long id) {
        super(String.format("Cerveja com id %s não encontrada no sistema.", id));
    }
}
