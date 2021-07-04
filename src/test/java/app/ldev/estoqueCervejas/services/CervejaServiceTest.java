package app.ldev.estoqueCervejas.services;

import app.ldev.estoqueCervejas.builder.CervejaDTOBuilder;
import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.entity.Cerveja;
import app.ldev.estoqueCervejas.exception.CervejaJaRegistradaException;
import app.ldev.estoqueCervejas.mapper.CervejaMapper;
import app.ldev.estoqueCervejas.repository.CervejaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {

    private static final long ID_CERVEJA_INVALIDO = 1L;

    @Mock
    private CervejaRepository cervejaRepository;

    private CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    @InjectMocks
    private CervejaService cervejaService;

    @Test
    void quandoACervejaEInformadaElaDeveSerCriada() throws CervejaJaRegistradaException {
        // Dado
        CervejaDTO esperadoCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja esperadoSalvarCerveja = cervejaMapper.toModel(esperadoCervejaDTO);

        // Quando
        when(cervejaRepository.findByNome(esperadoCervejaDTO.getNome())).thenReturn(Optional.empty());
        when(cervejaRepository.save(esperadoSalvarCerveja)).thenReturn(esperadoSalvarCerveja);

        // Então
        CervejaDTO createdCervejaDTO = cervejaService.criar(esperadoCervejaDTO);

        assertThat(createdCervejaDTO.getId(), is(equalTo(esperadoCervejaDTO.getId())));
        assertThat(createdCervejaDTO.getNome(), is(equalTo(esperadoCervejaDTO.getNome())));
        assertThat(createdCervejaDTO.getQuantidade(), is(equalTo(esperadoCervejaDTO.getQuantidade())));
    }

    @Test
    void quandoJaRegistradoCervejaInformadaEntaoUmaExcecaoDeveSerLancada() {
        // Dado
        CervejaDTO esperadoCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDuplicada = cervejaMapper.toModel(esperadoCervejaDTO);

        // Quando
        when(cervejaRepository.findByNome(esperadoCervejaDTO.getNome())).thenReturn(Optional.of(cervejaDuplicada));

        // Então
        assertThrows(CervejaJaRegistradaException.class, () -> cervejaService.criar(esperadoCervejaDTO));
    }

//    @Test
//    void whenValidCervejaNameIsGivenThenReturnACerveja() throws CervejaNotFoundException {
//        // given
//        CervejaDTO expectedFoundCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
//        Cerveja expectedFoundCerveja = cervejaMapper.toModel(expectedFoundCervejaDTO);
//
//        // when
//        when(cervejaRepository.findByName(expectedFoundCerveja.getName())).thenReturn(Optional.of(expectedFoundCerveja));
//
//        // then
//        CervejaDTO foundCervejaDTO = cervejaService.findByName(expectedFoundCervejaDTO.getName());
//
//        assertThat(foundCervejaDTO, is(equalTo(expectedFoundCervejaDTO)));
//    }
//
//    @Test
//    void whenNotRegisteredCervejaNameIsGivenThenThrowAnException() {
//        // given
//        CervejaDTO expectedFoundCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
//
//        // when
//        when(cervejaRepository.findByName(expectedFoundCervejaDTO.getName())).thenReturn(Optional.empty());
//
//        // then
//        assertThrows(CervejaNotFoundException.class, () -> cervejaService.findByName(expectedFoundCervejaDTO.getName()));
//    }

}
