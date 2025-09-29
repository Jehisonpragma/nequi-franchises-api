package co.com.bancolombia.api.routers;

import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.api.handlers.ReportMaxStocksHandler;
import co.com.bancolombia.model.reportmaxstocksmodel.ReportMaxStocksModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ReportMaxStocksRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchise/branches/products/max-stock",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = ReportMaxStocksHandler.class,
                    beanMethod = "listenGETFranchiseMaxStockProductUseCase",
                    operation = @Operation(
                            operationId = "GetMaxStockProductInFranchise",
                            summary = "Get the products with maximum stock per branch for a franchise",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "franchise_id",
                                            required = true,
                                            description = "Franchise Id"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = ReportMaxStocksModel.class))),
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
    public RouterFunction<ServerResponse> reportMaxStocksRouterFunction(ReportMaxStocksHandler reportMaxStocksHandler) {
        return route(GET("/api/franchise/branches/products/max-stock"), reportMaxStocksHandler::listenGETFranchiseMaxStockProductUseCase);
    }
}
