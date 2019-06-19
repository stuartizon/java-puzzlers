import org.junit.Test;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

public class HeaderForwardingFilterTest {
    final HeaderForwardingFilter filter = new HeaderForwardingFilter();
    final ExchangeFunction exchangeFunction = request ->
            Mono.just(ClientResponse.create(OK).headers(h -> h.addAll(request.headers())).build());

    @Test
    public void forwardTheHeadersFromContext() {
        ClientRequest request = ClientRequest.create(GET, URI.create("/whatever")).build();
        ClientResponse response = filter
                .filter(request, exchangeFunction)
                .subscriberContext(context -> context.put(HeaderForwardingFilter.CORRELATION_ID, "12345"))
                .block();
        assert response.headers().header(HeaderForwardingFilter.CORRELATION_ID).get(0).equals("12345");
    }
}
