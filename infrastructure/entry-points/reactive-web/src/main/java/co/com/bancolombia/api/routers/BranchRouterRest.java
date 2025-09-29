package co.com.bancolombia.api.routers;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.api.handlers.BranchHandler;
import co.com.bancolombia.model.branchmodel.BranchModel;
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
public class BranchRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/branch",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = BranchHandler.class,
                    beanMethod = "listenPOSTBranchUseCase",
                    operation = @Operation(
                            operationId = "createBranch",
                            summary = "Create new branch",
                            requestBody = @RequestBody(
                                    description = "Branch object that needs to be created",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = RequestCreateBranchDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = BranchModel.class))),
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
                    path = "/api/branch/name",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.PATCH,
                    beanClass = BranchHandler.class,
                    beanMethod = "listenPATCHBranchNameUseCase",
                    operation = @Operation(
                            operationId = "updateBranchName",
                            summary = "Update name of branch",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "id",
                                            required = true,
                                            description = "Branch Id"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "name",
                                            required = true,
                                            description = "Branch name to be set"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = BranchModel.class))),
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
    public RouterFunction<ServerResponse> branchRouterFunction(BranchHandler branchHandler) {
        return route(POST("/api/branch"), branchHandler::listenPOSTBranchUseCase)
                .and(route(PATCH("/api/branch/name"), branchHandler::listenPATCHBranchNameUseCase));
    }
}
