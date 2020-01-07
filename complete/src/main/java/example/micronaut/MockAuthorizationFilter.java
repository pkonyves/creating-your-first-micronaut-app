package example.micronaut;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import org.reactivestreams.Publisher;

@Filter("/authenticate")
public class MockAuthorizationFilter implements HttpFilter {

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {
        callAuthService();
        return chain.proceed(request);
    }

    /**
     * Mimic calling a blocking authentication service
     * that takes long time to respond
     */
    private void callAuthService() {
        try {
            final int wait5Sec = 1000*5;
            Thread.sleep(wait5Sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
