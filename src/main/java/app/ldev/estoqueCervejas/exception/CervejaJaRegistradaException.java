package app.ldev.estoqueCervejas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervejaJaRegistradaException extends Exception{

    public CervejaJaRegistradaException(String nomeCerveja) {
        super(String.format("Cerveja com nome %s já cadastrada no sistema.", nomeCerveja));
    }
}
