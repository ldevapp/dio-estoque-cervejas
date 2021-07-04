package app.ldev.estoqueCervejas.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor
public class CervejaController {
    @GetMapping
    public String listaCervejas(){
        return "Lista de Cervejas";
    }
}
