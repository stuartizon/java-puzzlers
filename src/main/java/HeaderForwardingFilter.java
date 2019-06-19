import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class HeaderForwardingFilter implements ExchangeFilterFunction {
    public static final String CORRELATION_ID = "correlationId";

    private Mono<ClientRequest> enrichRequest(ClientRequest clientRequest) {
        return Mono.subscriberContext()
                .map(context -> ClientRequest.from(clientRequest)
                        .header(CORRELATION_ID, context.get(CORRELATION_ID)).build()
                );
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction exchangeFunction) {
        return enrichRequest(request).flatMap(exchangeFunction::exchange);
    }
}
