package app.ldev.estoqueCervejas.controllers;

import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.dto.QuantidadeDTO;
import app.ldev.estoqueCervejas.exception.CervejaEstoqueExcedidoException;
import app.ldev.estoqueCervejas.exception.CervejaJaRegistradaException;
import app.ldev.estoqueCervejas.exception.CervejaNaoEncontradaException;
import app.ldev.estoqueCervejas.services.CervejaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public List<CervejaDTO> listarTodos() {
        return cervejaService.listarTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO criar(@RequestBody @Valid CervejaDTO cervejaDTO) throws CervejaJaRegistradaException {
        return cervejaService.criar(cervejaDTO);
    }

    @GetMapping("/{nome}")
    public CervejaDTO buscarPorNome(@PathVariable String nome) throws CervejaNaoEncontradaException {
        return cervejaService.buscarPorNome(nome);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPorId(@PathVariable Long id) throws CervejaNaoEncontradaException {
        cervejaService.deletarPorId(id);
    }

    @PatchMapping("/{id}/incremento")
    public CervejaDTO incremento(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
        return cervejaService.incremento(id, quantidadeDTO.getQuantidade());
    }

    @PatchMapping("/{id}/decremento")
    public CervejaDTO decremento(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
        return cervejaService.decremento(id, quantidadeDTO.getQuantidade());
    }
}
