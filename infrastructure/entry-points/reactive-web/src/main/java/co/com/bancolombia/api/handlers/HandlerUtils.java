package co.com.bancolombia.api.handlers;

import co.com.bancolombia.api.dto.ResponseMessageDto;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class HandlerUtils {

    public static Mono<ServerResponse> mapException(Throwable e) {
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
