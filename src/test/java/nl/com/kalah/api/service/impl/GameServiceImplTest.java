package nl.com.kalah.api.service.impl;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.com.kalah.api.KalahApplication;
import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.rest.dto.GameDTO;
import nl.com.kalah.api.util.Constants;

/**
 * Test class for the GameServiceImpl.
 *
 * @author Diego
 * @see GameServiceImpl
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahApplication.class)
public class GameServiceImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(GameServiceImplTest.class);
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Autowired
    private GameServiceImpl service;
    @Autowired
    private GameServiceImpl serviceMock;

    @Before
    public void setup() {
        service.invalidateAll();
    }

    @Test
    public void moveGameNullTest() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Constants.GAME_ID_NULL_ERROR);
        service.move(null, 1);
        Assert.fail("Should'n accept game null");
    }

    @Test
    public void moveGameNotValidTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Constants.GAME_NOT_FOUND_ERROR);
        service.move(1L, 1);
        Assert.fail("Should'n accept game not created");
    }

    @Test
    public void moveInValidPitTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage(String.format(Constants.PIT_ID_INVALID_ERROR, 0, Constants.INITIAL_PIT_ID, Constants.FINAL_PIT_ID));
        final GameDTO result = service.create();

        service.move(result.getId(), 0);
        Assert.fail("Should'n accept invalid pit ID");
    }

    @Test
    public void moveNorthernPlayerWinsTest() {
        GameDTO result = service.create();
        final Game currentGame = service.gameMap.get(result.getId());
        currentGame.clearPits();
        currentGame.getPitAt(1).stones(1);

        result = service.move(result.getId(), 1);
        Assert.assertEquals("The northern player should win.",
                            String.format(Constants.END_OF_THE_GAME_WINS, currentGame.getCurrentPlayer()), result.getEndGameMessage());
    }

    @Test
    public void moveDrawTest() {
        GameDTO result = service.create();
        final Game currentGame = service.gameMap.get(result.getId());
        currentGame.clearPits();
        currentGame.getPitAt(1).stones(1);
        currentGame.getPitAt(Constants.SOUTHERN_KALAH_ID).stones(1);

        result = service.move(result.getId(), 1);
        Assert.assertEquals("Should be a draw.", Constants.END_OF_THE_GAME_DRAW, result.getEndGameMessage());
    }

    private void createLoadTest() throws Exception {
        final GameDTO gameDTO = service.create();
        LOG.debug("Game {} created", gameDTO);
        final int executionTimes = 10;
        for (int executionIndex = 0; executionIndex < executionTimes; executionIndex++) {
            final ExecutorService executorService = Executors
                    .newFixedThreadPool(8);
            for (int moveIndex = Constants.INITIAL_PIT_ID; moveIndex <= Constants.FINAL_PIT_ID; moveIndex++) {
                final Integer currentPitId = moveIndex;
                executorService.execute(() -> {
                    try {
                        final Game previousGame = SerializationUtils.clone(service.gameMap.get(gameDTO.getId()));
                        final GameDTO gameResult = service.move(gameDTO.getId(), currentPitId);
                        service.rules.forEach(ruleHandler -> ruleHandler.handle(previousGame, currentPitId));

                        for (int pitIndex = Constants.INITIAL_PIT_ID; pitIndex <= Constants.FINAL_PIT_ID; pitIndex++) {
                            Assert.assertEquals("Result at " + pitIndex + " should be the same.",
                                                previousGame.getStatus().get(pitIndex),
                                                gameResult.getStatus().get(pitIndex));
                        }

                    } catch (final AssertionError | Exception exception) {
                        //LOG.error("Error on move pit ID {}", currentPitId, exception);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        service.invalidate(gameDTO.getId());
    }

    @Test
    public void loadTest() throws Exception {
        final ExecutorService executorService = Executors
                .newFixedThreadPool(8);

        final int executionTimes = 10;
        for (int index = 0; index < executionTimes; index++) {
            executorService.execute(() -> {
                try {
                    createLoadTest();
                } catch (final Exception exception) {
                    //LOG.error("Error at create load test", exception);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(20, TimeUnit.SECONDS);
        Assert.assertSame("Map should be empty.", 0, service.gameMap.size());
    }

    @Test
    public void getHttpsURL() throws Exception {
        final GameServiceImpl mock = Mockito.spy(serviceMock);
        Mockito.when(mock.isHttp()).thenReturn(false);
        Mockito.when(mock.getPort()).thenReturn("8080");
        Mockito.when(mock.getInetHost()).thenReturn("192.168.0.1");
        Assert.assertEquals("URL should be https://192.168.0.1:8080/games/", "https://192.168.0.1:8080/games/", mock.getURL());
    }

    @Test
    public void getLocalHostURL() throws Exception {
        final GameServiceImpl mock = Mockito.spy(serviceMock);
        Mockito.when(mock.isHttp()).thenReturn(true);
        Mockito.when(mock.getPort()).thenReturn("8080");
        Mockito.when(mock.getInetHost()).thenThrow(new UnknownHostException("Error"));
        Assert.assertEquals("URL should be http://localhost:8080/games/", "http://localhost:8080/games/", mock.getURL());
    }
}