package app.ldev.estoqueCervejas.controllers;

import app.ldev.estoqueCervejas.builder.CervejaDTOBuilder;
import app.ldev.estoqueCervejas.dto.CervejaDTO;
import app.ldev.estoqueCervejas.dto.QuantidadeDTO;
import app.ldev.estoqueCervejas.exception.CervejaEstoqueExcedidoException;
import app.ldev.estoqueCervejas.exception.CervejaNaoEncontradaException;
import app.ldev.estoqueCervejas.services.CervejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static app.ldev.estoqueCervejas.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {
    private static final String CERVEJA_API_URL_PATH = "/api/v1/cervejas";
    private static final long ID_CERVEJA_INVALIDO = 2l;
    private static final String CERVEJA_API_SUBPATH_INCREMENTO_URL = "/incremento";
    private static final String CERVEJA_API_SUBPATH_DECREMENTO_URL = "/decremento";

    private MockMvc mockMvc;

    @Mock
    private CervejaService cervejaService;

    @InjectMocks
    private CervejaController cervejaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void quandoChamadoPOSTUmaCervejaECriada() throws Exception {
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        when(cervejaService.criar(cervejaDTO)).thenReturn(cervejaDTO);

        mockMvc.perform(post(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoOPOSTEChamadoSemCampoObrigatorioUmErroERetornado() throws Exception {
        
        // Dado
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setMarca(null);

        // Então
        mockMvc.perform(post(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void quandoGETEChamadoComUmNomeValidoOStatusDeOKERetornado() throws Exception {

        // Dado
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // Quando
        when(cervejaService.buscarPorNome(cervejaDTO.getNome())).thenReturn(cervejaDTO);

        // Então
        mockMvc.perform(MockMvcRequestBuilders.get(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoGETEChamadoSemNomeRegistradoOStatusNaoEncontradaERetornado() throws Exception {

        // Dado
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // Quando
        when(cervejaService.buscarPorNome(cervejaDTO.getNome())).thenThrow(CervejaNaoEncontradaException.class);

        // Então
        mockMvc.perform(MockMvcRequestBuilders.get(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void quandoGETListarTodosEChamadaOStatusDeOKERetornado() throws Exception {

        // Dado
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // Quando
        when(cervejaService.listarTodos()).thenReturn(Collections.singletonList(cervejaDTO));

        // Então
        mockMvc.perform(MockMvcRequestBuilders.get(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$[0].marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$[0].tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoGETListarTodosSemCervejasEChamadaOStatusDeOKERtornado() throws Exception {

        // Dado
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // Quando
        when(cervejaService.listarTodos()).thenReturn(Collections.singletonList(cervejaDTO));

        // Então
        mockMvc.perform(MockMvcRequestBuilders.get(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void quandoDELETEEChamadoComUmIDValidoNenhumStatusDeConteudoEretornado() throws Exception {

        // Dado
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // Quando
        doNothing().when(cervejaService).deletarPorId(cervejaDTO.getId());

        // Etnão
        mockMvc.perform(MockMvcRequestBuilders.delete(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void quandoDELETEEChamadoComIDInvalidoOStatusNaoEncontradoERetornado() throws Exception {
        //when
        doThrow(CervejaNaoEncontradaException.class).when(cervejaService).deletarPorId(ID_CERVEJA_INVALIDO);

        // Então
        mockMvc.perform(MockMvcRequestBuilders.delete(CERVEJA_API_URL_PATH + "/" + ID_CERVEJA_INVALIDO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void quandoPATCHEChamadoParaIncrementoDoEstoqueOStatusOkERetornado() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(10)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.incremento(cervejaDTO.getId(), quantidadeDTO.getQuantidade())).thenReturn(cervejaDTO);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getId() + CERVEJA_API_SUBPATH_INCREMENTO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())))
                .andExpect(jsonPath("$.quantidade", is(cervejaDTO.getQuantidade())));
    }

    @Test
    void quandoPATCHEChamadoParaIncrementoMaisQueOMaximoEntaoOStatusDeSolicitacaoInvalidaERetornado() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(30)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.incremento(cervejaDTO.getId(), quantidadeDTO.getQuantidade())).thenThrow(CervejaEstoqueExcedidoException.class);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getId() + CERVEJA_API_SUBPATH_INCREMENTO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void quandoPATCHChamadoComIdInvalidaParaIncrementoOStatusNaoEncontradoERetornado() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(30)
                .build();

        when(cervejaService.incremento(ID_CERVEJA_INVALIDO, quantidadeDTO.getQuantidade())).thenThrow(CervejaNaoEncontradaException.class);
        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + ID_CERVEJA_INVALIDO + CERVEJA_API_SUBPATH_INCREMENTO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void quandoPATCHChamadoParaDecrementoOStatusOkERetornado() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(5)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.decremento(cervejaDTO.getId(), quantidadeDTO.getQuantidade())).thenReturn(cervejaDTO);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getId() + CERVEJA_API_SUBPATH_DECREMENTO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())))
                .andExpect(jsonPath("$.quantidade", is(cervejaDTO.getQuantidade())));
    }

    @Test
    void quandoPATCHChamadoParaDecrementoInferiorAZeroOStatusDeSolicitacaoIncorretaERetornado() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(60)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.decremento(cervejaDTO.getId(), quantidadeDTO.getQuantidade())).thenThrow(CervejaEstoqueExcedidoException.class);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getId() + CERVEJA_API_SUBPATH_DECREMENTO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void quandoPATCHChamadoComIdInvalidoParaDecrementoOStatusNaoEncontradoERetornado() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(5)
                .build();

        when(cervejaService.decremento(ID_CERVEJA_INVALIDO, quantidadeDTO.getQuantidade())).thenThrow(CervejaEstoqueExcedidoException.class);
        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + ID_CERVEJA_INVALIDO + CERVEJA_API_SUBPATH_DECREMENTO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO)))
                .andExpect(status().isBadRequest());
    }
}
