package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final FranchiseUseCase franchiseUseCase;
    private final BranchUseCase branchUseCase;

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

}
