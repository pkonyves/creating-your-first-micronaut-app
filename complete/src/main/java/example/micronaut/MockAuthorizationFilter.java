package example.micronaut;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import org.reactivestreams.Publisher;

import java.util.concurrent.atomic.AtomicReference;

//@Filter("/call-remote")
public class MockAuthorizationFilter implements HttpFilter {

    public static AtomicReference<String> selfUrl = new AtomicReference<>("");

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {
        callAuthService();
        return chain.proceed(request);
    }

    private void callAuthService() {
        try {
            final int waitTime = 1000*60*15;
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        try {
            java.net.http.HttpRequest identityRequest = null;
            identityRequest = java.net.http.HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(selfUrl.get() + "/ok"))
                    .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        java.net.http.HttpResponse<String> response = httpClient.send(identityRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }
}
