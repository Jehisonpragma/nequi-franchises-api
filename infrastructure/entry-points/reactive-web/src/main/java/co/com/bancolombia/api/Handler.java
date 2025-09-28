package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.dto.RequestCreateProductDto;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
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
public class Handler {

    private final FranchiseUseCase franchiseUseCase;
    private final BranchUseCase branchUseCase;
    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> listenPOSTFranchiseUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateFranchiseDto> bodyMono = serverRequest.bodyToMono(RequestCreateFranchiseDto.class);

        return bodyMono.flatMap(body ->
            franchiseUseCase.createFranchise(body.getName()).flatMap(response ->
                    ServerResponse.ok().bodyValue(response)
            )
        ).onErrorResume(Handler::mapException);
    }

    public Mono<ServerResponse> listenPOSTBranchUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateBranchDto> bodyMono = serverRequest.bodyToMono(RequestCreateBranchDto.class);

        return bodyMono.flatMap(body ->
                branchUseCase.createBranch(body.getName(), body.getFranchiseId()).flatMap(response ->
                        ServerResponse.ok().bodyValue(response)
                )

        ).onErrorResume(Handler::mapException);
    }

    public Mono<ServerResponse> listenPOSTProductUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateProductDto> bodyMono = serverRequest.bodyToMono(RequestCreateProductDto.class);

        return bodyMono.flatMap(body ->
                productUseCase.createProduct(body.getName(), body.getBranchId(), body.getStock()).flatMap(response ->
                        ServerResponse.ok().bodyValue(response)
                )
        ).onErrorResume(Handler::mapException);
    }

    public Mono<ServerResponse> listenDELETEProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optProductId = serverRequest.queryParam("id").map(Integer::parseInt);

        return optProductId.map(integer ->
                        productUseCase.deleteProduct(integer)
                            .flatMap(isDeleted -> ServerResponse.ok().bodyValue(ResponseMessageDto.builder()
                                                    .code("200")
                                                    .message("Product deleted successfully")
                                                    .build())))
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
                                .onErrorResume(Handler::mapException);
                    });
        } else {
            return ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                    .code("400")
                    .message("Bad Request Error")
                    .build());
        }
    }

    public Mono<ServerResponse> listenGETFranchiseMaxStockProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optFranchiseId = serverRequest.queryParam("franchise_id").map(Integer::parseInt);

        return optFranchiseId.map(integer ->
                    franchiseUseCase.findMaxStockProductsPerEachBranchByFranchiseId(integer)
                        .flatMap(response -> ServerResponse.ok().bodyValue(response))
                        .onErrorResume(Handler::mapException))
                .orElseGet(() -> ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                                    .code("400")
                                    .message("Bad Request Error")
                                    .build()));
    }

    private static Mono<ServerResponse> mapException(Throwable e) {
        if (e instanceof IllegalArgumentException) {
            return ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                    .code("400")
                    .message("Bad Request Error")
                    .build());
        } else if (e instanceof BusinessException businessException) {
            return ServerResponse.status(422).bodyValue(ResponseMessageDto.builder()
                            .code(businessException.getError().getCode())
                            .message(businessException.getError().getLog())
                            .build());
        } else {
            return ServerResponse.status(500).bodyValue(ResponseMessageDto.builder()
                    .code("500")
                    .message("Internal Server Error")
                    .build());
        }
    }

}
