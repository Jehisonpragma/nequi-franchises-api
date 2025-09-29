package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.dto.RequestCreateProductDto;
import co.com.bancolombia.api.dto.ResponseMessageDto;
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
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> listenPOSTBranchUseCase(ServerRequest serverRequest) {

        Mono<RequestCreateBranchDto> bodyMono = serverRequest.bodyToMono(RequestCreateBranchDto.class);

        return bodyMono.flatMap(body ->
                branchUseCase.createBranch(body.getName(), body.getFranchiseId()).flatMap(response ->
                        ServerResponse.ok().bodyValue(response)
                )

        ).onErrorResume(HandlerUtils::mapException);
    }

    public Mono<ServerResponse> listenPATCHBranchNameUseCase(ServerRequest serverRequest) {

        Optional<Integer> optBranchId = serverRequest.queryParam("id").map(Integer::parseInt);
        Optional<String> optName = serverRequest.queryParam("name");

        if (optBranchId.isPresent() && optName.isPresent()) {
            return Mono.just(new Tuple<>(optBranchId.get(), optName.get()))
                    .flatMap(integerIntegerTuple -> {
                        Integer branchId = integerIntegerTuple._1();
                        String name = integerIntegerTuple._2();

                        return branchUseCase.updateBranchName(branchId, name)
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
