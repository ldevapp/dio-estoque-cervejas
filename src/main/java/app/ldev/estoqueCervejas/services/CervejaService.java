package app.ldev.estoqueCervejas.services;

import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.entity.Cerveja;
import app.ldev.estoqueCervejas.exception.CervejaJaRegistradaException;
import app.ldev.estoqueCervejas.mapper.CervejaMapper;
import app.ldev.estoqueCervejas.repository.CervejaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    private void verificarSeJaEstaRegistrado(String nome) throws CervejaJaRegistradaException {
        Optional<Cerveja> optSalvar = cervejaRepository.findByNome(nome);
        if (optSalvar.isPresent()) {
            throw new CervejaJaRegistradaException(nome);
        }
    }
}
