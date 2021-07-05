package app.ldev.estoqueCervejas.controllers;

import app.ldev.estoqueCervejas.builder.CervejaDTOBuilder;
import app.ldev.estoqueCervejas.dto.CervejaDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {
    private static final String CERVEJA_API_URL_PATH = "/api/v1/cervejas";
    private static final long ID_CERVEJA_INVALIDO = 2l;

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

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(CERVEJA_API_URL_PATH + "/" + ID_CERVEJA_INVALIDO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
