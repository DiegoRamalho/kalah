package nl.com.kalah.api.rest;

import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.com.kalah.api.rest.dto.GameDTO;
import nl.com.kalah.api.util.Constants;

/**
 * Game Rest Service.
 *
 * @author Diego
 * @since 13/01/2020
 */
public interface GameRestService {

    @PostMapping("/")
    @ApiOperation(value = "Creates a new game.", notes = "* The created game already has n-stones at each pit.\n* The current player is the northern player.")
    @ApiResponses(@ApiResponse(code = 201, message = "Game created with success."))
    ResponseEntity<GameDTO> create() throws URISyntaxException;

    @PutMapping("/{gameId}/pits/{pitId}")
    @ApiOperation(value = "Makes a move in the game.", notes =
            "If the movement fails, the application will return one of this messages:"
            + Constants.ALL_ERRORS_FMT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful move"),
            @ApiResponse(code = 500, message = "Validation errors")
    })
    ResponseEntity<GameDTO> move(@ApiParam(value = "Game ID", required = true) @PathVariable("gameId") String gameId,
                                 @ApiParam(value = "Pit ID", required = true) @PathVariable("pitId") String pitId);

    @DeleteMapping("/{gameId}")
    @ApiOperation("Deletes the game.")
    @ApiResponses(@ApiResponse(code = 200, message = "Successful delete"))
    ResponseEntity<Void> delete(@ApiParam(value = "Game ID", required = true) @PathVariable("gameId") String gameId);

    @DeleteMapping("/")
    @ApiOperation("Deletes all games.")
    @ApiResponses(@ApiResponse(code = 200, message = "All games successfully deleted"))
    ResponseEntity<Void> deleteAll();
}
