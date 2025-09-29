package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.usecase.reportmaxstocks.ReportMaxStocksUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReportMaxStocksHandler {

    private final ReportMaxStocksUseCase reportMaxStocksUseCase;

    public Mono<ServerResponse> listenGETFranchiseMaxStockProductUseCase(ServerRequest serverRequest) {

        Optional<Integer> optFranchiseId = serverRequest.queryParam("franchise_id").map(Integer::parseInt);

        return optFranchiseId.map(integer ->
                        reportMaxStocksUseCase.findMaxStockProductsPerEachBranchByFranchiseId(integer)
                        .flatMap(response -> ServerResponse.ok().bodyValue(response))
                        .onErrorResume(HandlerUtils::mapException))
                .orElseGet(() -> ServerResponse.badRequest().bodyValue(ResponseMessageDto.builder()
                                    .code("400")
                                    .message("Bad Request Error")
                                    .build()));
    }

}
