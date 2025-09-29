package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.dto.RequestCreateProductDto;
import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yaml.snakeyaml.util.Tuple;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> listenPOSTProductUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateProductDto> bodyMono = serverRequest.bodyToMono(RequestCreateProductDto.class);

        return bodyMono.flatMap(body ->
                productUseCase.createProduct(body.getName(), body.getBranchId(), body.getStock()).flatMap(response ->
                        ServerResponse.ok().bodyValue(response)
                )
        ).onErrorResume(HandlerUtils::mapException);
    }

    public Mono<ServerResponse> listenPATCHProductNameUseCase(ServerRequest serverRequest) {

        Optional<Integer> optProductId = serverRequest.queryParam("id").map(Integer::parseInt);
        Optional<String> optName = serverRequest.queryParam("name");

        if (optProductId.isPresent() && optName.isPresent()) {
            return Mono.just(new Tuple<>(optProductId.get(), optName.get()))
                    .flatMap(integerIntegerTuple -> {
                        Integer productId = integerIntegerTuple._1();
                        String name = integerIntegerTuple._2();

                        return productUseCase.updateProductName(productId, name)
                                .flatMap(productModel -> ServerResponse.ok().bodyValue(productModel))
                                .onErrorResume(HandlerUtils::mapException);
                    });
        } else {
            return ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                    .code("400")
                    .message("Bad Request Error")
                    .build());
        }
    }

    public Mono<ServerResponse> listenDELETEProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optProductId = serverRequest.queryParam("id").map(Integer::parseInt);

        return optProductId.map(integer ->
                        productUseCase.deleteProduct(integer)
                            .flatMap(isDeleted -> ServerResponse.ok().bodyValue(ResponseMessageDto.builder()
                                                    .code("200")
                                                    .message("Product deleted successfully")
                                                    .build()))
                            .onErrorResume(HandlerUtils::mapException))
                .orElseGet(() -> ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                                .code("400")
                                .message("Bad Request Error")
                                .build()));

    }

    public Mono<ServerResponse> listenPATCHProductStockUseCase(ServerRequest serverRequest) {

        Optional<Integer> optProductId = serverRequest.queryParam("id").map(Integer::parseInt);
        Optional<Integer> optStock = serverRequest.queryParam("stock").map(Integer::parseInt);

        if (optProductId.isPresent() && optStock.isPresent()) {
            return Mono.just(new Tuple<>(optProductId.get(), optStock.get()))
                    .flatMap(integerIntegerTuple -> {
                        Integer productId = integerIntegerTuple._1();
                        Integer stock = integerIntegerTuple._2();

                        return productUseCase.modifyStockInProduct(productId, stock)
                                .flatMap(productModel -> ServerResponse.ok().bodyValue(productModel))
                                .onErrorResume(HandlerUtils::mapException);
                    });
        } else {
            return ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                    .code("400")
                    .message("Bad Request Error")
                    .build());
        }
    }

}
