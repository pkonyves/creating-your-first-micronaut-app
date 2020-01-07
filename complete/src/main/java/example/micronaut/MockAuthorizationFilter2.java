package example.micronaut;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.concurrent.atomic.AtomicReference;

@Filter("/ok")
public class MockAuthorizationFilter2 implements HttpFilter {

    public static final AtomicReference<String> selfUrl = new AtomicReference<>("");

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {
        callMockAuthService();
        return chain.proceed(request);
    }

    /**
     * A Mock authentication service implemented by this exact running service
     */
    private void callMockAuthService() {
        try {
            java.net.http.HttpRequest identityRequest = null;
            identityRequest = java.net.http.HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(selfUrl.get() + "/authenticate"))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            java.net.http.HttpResponse<String> response = httpClient.send(identityRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
