package nl.com.kalah.api.rest.impl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import nl.com.kalah.api.KalahApplication;
import nl.com.kalah.api.service.GameService;
import nl.com.kalah.api.util.Constants;

/**
 * Test class for the Game Rest Service.
 *
 * @author Diego
 * @see GameRestServiceImpl
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahApplication.class)
public class GameRestServiceImplTest {

    private static final String BASE_URI = "/games/";
    private static final String DEFAULT_URL = "/games/1";
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    private GameService gameService;
    private MockMvc restMockMvc;

    @Before
    public void setup() {
        final GameRestServiceImpl service = new GameRestServiceImpl(gameService);
        restMockMvc = MockMvcBuilders.standaloneSetup(service)
                .setMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8), jacksonMessageConverter).build();

        gameService.invalidateAll();
    }

    @Test
    public void createTest() throws Exception {
        restMockMvc.perform(post(BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.url").value(Matchers.containsString(DEFAULT_URL)))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void moveValidTest() throws Exception {
        gameService.create();
        //Move the pit with id 1
        restMockMvc.perform(put(DEFAULT_URL + "/pits/1")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.url").value(Matchers.containsString(DEFAULT_URL)))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void deleteValidTest() throws Exception {
        gameService.create();
        restMockMvc.perform(delete(DEFAULT_URL)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteInvalidValidTest() throws Exception {
        restMockMvc.perform(delete(DEFAULT_URL)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAllTest() throws Exception {
        restMockMvc.perform(delete(BASE_URI)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void moveInvalidGameTest() throws Exception {
        expectedException.expect(NestedServletException.class);
        expectedException.expectMessage(Constants.GAME_ID_NOT_NUMBER_ERROR);
        gameService.create();
        //Move the pit with id 1
        restMockMvc.perform(put(BASE_URI + "a/pits/1")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
        Assert.fail("Shouldn't accept game ID as not number.");
    }

    @Test
    public void moveInvalidPitTest() throws Exception {
        expectedException.expect(NestedServletException.class);
        expectedException.expectMessage(Constants.PIT_ID_NOT_NUMBER_ERROR);
        gameService.create();
        //Move the pit with id 1
        restMockMvc.perform(put(BASE_URI + "1/pits/a")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        Assert.fail("Shouldn't accept pits ID as not number.");
    }
}