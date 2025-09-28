package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/franchise"), handler::listenPOSTFranchiseUseCase)
                .and(route(PATCH("/api/franchise/name"), handler::listenPATCHFranchiseNameUseCase))
                .and(route(POST("/api/branch"), handler::listenPOSTBranchUseCase))
                .and(route(PATCH("/api/branch/name"), handler::listenPATCHBranchNameUseCase))
                .and(route(POST("/api/product"), handler::listenPOSTProductUseCase))
                .and(route(PATCH("/api/product/name"), handler::listenPATCHProductNameUseCase))
                .and(route(DELETE("/api/product"), handler::listenDELETEProductUseCase))
                .and(route(PATCH("/api/product/stock"), handler::listenPATCHProductStockUseCase))
                .and(route(GET("/api/franchise/branches/products/max-stock"), handler::listenGETFranchiseMaxStockProductUseCase));
    }
}
