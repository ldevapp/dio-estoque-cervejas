package app.ldev.estoqueCervejas.services;

import app.ldev.estoqueCervejas.builder.CervejaDTOBuilder;
import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.entity.Cerveja;
import app.ldev.estoqueCervejas.exception.CervejaJaRegistradaException;
import app.ldev.estoqueCervejas.exception.CervejaNaoEncontradaException;
import app.ldev.estoqueCervejas.mapper.CervejaMapper;
import app.ldev.estoqueCervejas.repository.CervejaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    @Test
    void quandoUmNomeValidoDeCervejaEFornecidoEntaoDevolvaUmaCerveja() throws CervejaNaoEncontradaException {

        // Dado
        CervejaDTO esperadoEncontrarCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja esperadoEncontrarCerveja = cervejaMapper.toModel(esperadoEncontrarCervejaDTO);

        // Quando
        when(cervejaRepository.findByNome(esperadoEncontrarCerveja.getNome())).thenReturn(Optional.of(esperadoEncontrarCerveja));

        // Então
        CervejaDTO encountroCervejaDTO = cervejaService.buscarPorNome(esperadoEncontrarCervejaDTO.getNome());

        assertThat(encountroCervejaDTO, is(equalTo(esperadoEncontrarCervejaDTO)));
    }

    @Test
    void quandoNaoRegistradoONomeDeCervejaEFornecidoLancaUmaExcecao() {

        // Dado
        CervejaDTO esperadoEncontrarCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // Quando
        when(cervejaRepository.findByNome(esperadoEncontrarCervejaDTO.getNome())).thenReturn(Optional.empty());

        // Então
        assertThrows(CervejaNaoEncontradaException.class, () -> cervejaService.buscarPorNome(esperadoEncontrarCervejaDTO.getNome()));
    }

    @Test
    void quandoListarTodosForChamadaRetorneUmaListaDeCervejas() {

        // Dado
        CervejaDTO esperadoEncontrarCervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja esperadoEncontrarCerveja = cervejaMapper.toModel(esperadoEncontrarCervejaDTO);

        //Quando
        when(cervejaRepository.findAll()).thenReturn(Collections.singletonList(esperadoEncontrarCerveja));

        //Entao
        List<CervejaDTO> listaEncontradaCervejaDTO = cervejaService.listarTodos();

        assertThat(listaEncontradaCervejaDTO, is(not(empty())));
        assertThat(listaEncontradaCervejaDTO.get(0), is(equalTo(esperadoEncontrarCervejaDTO)));
    }

    @Test
    void quandoNenhumRegistroCadastradoEListaToddosForChamadaEntaoRetornaUmListaVaziaDeCervejas() {

        // Quando
        when(cervejaRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        // Entao
        List<CervejaDTO> listaLimpaCervejaDTO = cervejaService.listarTodos();

        assertThat(listaLimpaCervejaDTO, is(empty()));
    }

}
