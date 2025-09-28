package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.dto.RequestCreateProductDto;
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
        );
    }

    public Mono<ServerResponse> listenPOSTBranchUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateBranchDto> bodyMono = serverRequest.bodyToMono(RequestCreateBranchDto.class);

        return bodyMono.flatMap(body ->
                branchUseCase.createBranch(body.getName(), body.getFranchiseId()).flatMap(response ->
                        ServerResponse.ok().bodyValue(response)
                )
        );
    }

    public Mono<ServerResponse> listenPOSTProductUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateProductDto> bodyMono = serverRequest.bodyToMono(RequestCreateProductDto.class);

        return bodyMono.flatMap(body ->
                productUseCase.createProduct(body.getName(), body.getBranchId(), body.getStock()).flatMap(response ->
                        ServerResponse.ok().bodyValue(response)
                )
        );
    }

    public Mono<ServerResponse> listenDELETEProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optProductId = serverRequest.queryParam("id").map(Integer::parseInt);

        return Mono.just(optProductId).flatMap(optProductIdProcessed ->
                optProductId.map(productId ->
                        productUseCase.deleteProduct(productId).thenReturn("Product deleted successfully"))
                        .orElseGet(() -> Mono.just("Product id is empty"))
        ).flatMap(response -> ServerResponse.ok().bodyValue(response));

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
                                .flatMap(productModel -> ServerResponse.ok().bodyValue(productModel));
                    });
        } else {
            return ServerResponse.ok().bodyValue("Insufficient query parameters");
        }
    }

    public Mono<ServerResponse> listenGETFranchiseMaxStockProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optFranchiseId = serverRequest.queryParam("franchise_id").map(Integer::parseInt);

        return Mono.just(optFranchiseId).flatMap(optFranchiseIdProcessed ->
                optFranchiseIdProcessed.map(productId ->
                        franchiseUseCase.findMaxStockProductsPerEachBranchByFranchiseId(productId)
                                .flatMap(response -> ServerResponse.ok().bodyValue(response)))
                        .orElseGet(() -> ServerResponse.ok().bodyValue("")));

    }

}
