package co.com.bancolombia.api.routers;

import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.dto.RequestCreateProductDto;
import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.api.handlers.FranchiseHandler;
import co.com.bancolombia.api.handlers.ProductHandler;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.FranchiseWithMaxStockProductsModel;
import co.com.bancolombia.model.productmodel.ProductModel;
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
public class ProductRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/product",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenPOSTProductUseCase",
                    operation = @Operation(
                            operationId = "createProduct",
                            summary = "Create new product",
                            requestBody = @RequestBody(
                                    description = "Product object that needs to be created",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = RequestCreateProductDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = ProductModel.class))),
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
                    path = "/api/product/name",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.PATCH,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenPATCHProductNameUseCase",
                    operation = @Operation(
                            operationId = "updateProductName",
                            summary = "Update name of product",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "id",
                                            required = true,
                                            description = "Product Id"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "name",
                                            required = true,
                                            description = "Product name to be set"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = ProductModel.class))),
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
                    path = "/api/product",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenDELETEProductUseCase",
                    operation = @Operation(
                            operationId = "DeleteProduct",
                            summary = "Delete a product",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "id",
                                            required = true,
                                            description = "Product Id"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = ResponseMessageDto.class))),
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
                    path = "/api/product/stock",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.PATCH,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenPATCHProductStockUseCase",
                    operation = @Operation(
                            operationId = "updateProductStock",
                            summary = "Update stock of product",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "id",
                                            required = true,
                                            description = "Product Id"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "stock",
                                            required = true,
                                            description = "Product stock to be set"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful Operation",
                                            content = @Content(schema = @Schema(implementation = ProductModel.class))),
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
    public RouterFunction<ServerResponse> productRouterFunction(ProductHandler productHandler) {
        return route(POST("/api/product"), productHandler::listenPOSTProductUseCase)
                .and(route(PATCH("/api/product/name"), productHandler::listenPATCHProductNameUseCase))
                .and(route(DELETE("/api/product"), productHandler::listenDELETEProductUseCase))
                .and(route(PATCH("/api/product/stock"), productHandler::listenPATCHProductStockUseCase));
    }
}
