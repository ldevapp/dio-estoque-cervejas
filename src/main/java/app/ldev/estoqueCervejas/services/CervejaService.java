package app.ldev.estoqueCervejas.services;

import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.entity.Cerveja;
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
}
