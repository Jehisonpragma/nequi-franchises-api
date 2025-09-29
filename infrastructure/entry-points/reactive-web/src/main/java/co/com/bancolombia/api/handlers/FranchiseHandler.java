package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yaml.snakeyaml.util.Tuple;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> listenPOSTFranchiseUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateFranchiseDto> bodyMono = serverRequest.bodyToMono(RequestCreateFranchiseDto.class);

        return bodyMono.flatMap(body ->
            franchiseUseCase.createFranchise(body.getName()).flatMap(response ->
                    ServerResponse.ok().bodyValue(response)
            )
        ).onErrorResume(HandlerUtils::mapException);
    }

    public Mono<ServerResponse> listenPATCHFranchiseNameUseCase(ServerRequest serverRequest) {

        Optional<Integer> optFranchiseId = serverRequest.queryParam("id").map(Integer::parseInt);
        Optional<String> optName = serverRequest.queryParam("name");

        if (optFranchiseId.isPresent() && optName.isPresent()) {
            return Mono.just(new Tuple<>(optFranchiseId.get(), optName.get()))
                    .flatMap(integerIntegerTuple -> {
                        Integer franchiseId = integerIntegerTuple._1();
                        String name = integerIntegerTuple._2();

                        return franchiseUseCase.updateFranchiseName(franchiseId, name)
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

    public Mono<ServerResponse> listenGETFranchiseMaxStockProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optFranchiseId = serverRequest.queryParam("franchise_id").map(Integer::parseInt);

        return optFranchiseId.map(integer ->
                    franchiseUseCase.findMaxStockProductsPerEachBranchByFranchiseId(integer)
                        .flatMap(response -> ServerResponse.ok().bodyValue(response))
                        .onErrorResume(HandlerUtils::mapException))
                .orElseGet(() -> ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                                    .code("400")
                                    .message("Bad Request Error")
                                    .build()));
    }

}
