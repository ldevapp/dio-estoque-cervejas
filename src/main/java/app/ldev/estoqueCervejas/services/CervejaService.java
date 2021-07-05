package app.ldev.estoqueCervejas.services;

import app.ldev.estoqueCervejas.EstoqueCervejasApplication;
import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.entity.Cerveja;
import app.ldev.estoqueCervejas.exception.CervejaEstoqueExcedidoException;
import app.ldev.estoqueCervejas.exception.CervejaJaRegistradaException;
import app.ldev.estoqueCervejas.exception.CervejaNaoEncontradaException;
import app.ldev.estoqueCervejas.mapper.CervejaMapper;
import app.ldev.estoqueCervejas.repository.CervejaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaService {

    private final CervejaRepository cervejaRepository;
    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    public CervejaDTO criar(CervejaDTO cervejaDTO) throws CervejaJaRegistradaException {
        verificarSeJaEstaRegistrado(cervejaDTO.getNome());
        Cerveja cerveja = cervejaMapper.toModel(cervejaDTO);

        Cerveja salvarCerveja = cervejaRepository.save(cerveja);
        return cervejaMapper.toDTO(salvarCerveja);
    }

    public List<CervejaDTO> listarTodos() {
        return cervejaRepository.findAll()
                .stream()
                .map(cervejaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deletarPorId(Long id) throws CervejaNaoEncontradaException {
        verificarSeExistePorId(id);
        cervejaRepository.deleteById(id);
    }

    public CervejaDTO buscarPorNome(String nome) throws CervejaNaoEncontradaException {
        Cerveja cervejaEncontrada = cervejaRepository.findByNome(nome)
                .orElseThrow(() -> new CervejaNaoEncontradaException(nome));
        return cervejaMapper.toDTO(cervejaEncontrada);
    }

    private void verificarSeJaEstaRegistrado(String nome) throws CervejaJaRegistradaException {
        Optional<Cerveja> optSalvar = cervejaRepository.findByNome(nome);
        if (optSalvar.isPresent()) {
            throw new CervejaJaRegistradaException(nome);
        }
    }

    private Cerveja verificarSeExistePorId(Long id) throws CervejaNaoEncontradaException {
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new CervejaNaoEncontradaException(id));
    }

    public CervejaDTO incremento(Long id, int quantidade) throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
        Cerveja cervejaParaIncrementarEstoque = verificarSeExistePorId(id);
        int estoqueCervejaAposIncremento = quantidade + cervejaParaIncrementarEstoque.getQuantidade();
        if (estoqueCervejaAposIncremento <= cervejaParaIncrementarEstoque.getMax()) {
            cervejaParaIncrementarEstoque.setQuantidade(estoqueCervejaAposIncremento);
            Cerveja cervejaIncrementada = cervejaRepository.save(cervejaParaIncrementarEstoque);
            return cervejaMapper.toDTO(cervejaIncrementada);
        }
        throw new CervejaEstoqueExcedidoException(id, quantidade);
    }

    public CervejaDTO decremento(Long id, int quantidade) throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
        Cerveja cervejaParaDecrementar = verificarSeExistePorId(id);
        int cervejaDecrementadaEstoque = cervejaParaDecrementar.getQuantidade() - quantidade;
        if (cervejaDecrementadaEstoque >= 0) {
            cervejaParaDecrementar.setQuantidade(cervejaDecrementadaEstoque);
            Cerveja decrementedBeerStock = cervejaRepository.save(cervejaParaDecrementar);
            return cervejaMapper.toDTO(decrementedBeerStock);
        }
        throw new CervejaEstoqueExcedidoException(id, quantidade);
    }
}
