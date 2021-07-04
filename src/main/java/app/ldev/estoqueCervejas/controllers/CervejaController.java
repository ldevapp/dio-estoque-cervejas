package app.ldev.estoqueCervejas.controllers;

import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.exception.CervejaJaRegistradaException;
import app.ldev.estoqueCervejas.services.CervejaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/cervejas")
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController {

    private final CervejaService cervejaService;

    @Autowired
    public CervejaController(CervejaService cervejaService) {
        this.cervejaService = cervejaService;
    }

    @GetMapping
    public String listar(){
        return "Lista de Cervejas";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO criar(@RequestBody @Valid CervejaDTO cervejaDTO) throws CervejaJaRegistradaException {
        return cervejaService.criar(cervejaDTO);
    }
}
