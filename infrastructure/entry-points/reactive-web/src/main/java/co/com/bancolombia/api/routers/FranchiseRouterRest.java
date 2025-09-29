package co.com.bancolombia.api.routers;

import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.api.handlers.FranchiseHandler;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.reportmaxstocksmodel.ReportMaxStocksModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchise",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "listenPOSTFranchiseUseCase",
                    operation = @Operation(
                            operationId = "createFranchise",
                            summary = "Create new franchise",
                            requestBody = @RequestBody(
                                    description = "Franchise object that needs to be created",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = RequestCreateFranchiseDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = FranchiseModel.class))),
                                    @ApiResponse(responseCode = "400", description = "Bad Request Error",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class))),
                                    @ApiResponse(responseCode = "422", description = "BusinessError",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class))),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchise/name",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.PATCH,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "listenPATCHFranchiseNameUseCase",
                    operation = @Operation(
                            operationId = "updateFranchiseName",
                            summary = "Update name of franchise",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "id",
                                            required = true,
                                            description = "Franchise Id"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "name",
                                            required = true,
                                            description = "Franchise name to be set"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = FranchiseModel.class))),
                                    @ApiResponse(responseCode = "400", description = "Bad Request Error",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class))),
                                    @ApiResponse(responseCode = "422", description = "BusinessError",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class))),
                                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class)))
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> franchiseRouterFunction(FranchiseHandler franchiseHandler) {
        return route(POST("/api/franchise"), franchiseHandler::listenPOSTFranchiseUseCase)
                .and(route(PATCH("/api/franchise/name"), franchiseHandler::listenPATCHFranchiseNameUseCase));
    }
}
