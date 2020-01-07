package example.micronaut;


import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.ReadTimeoutException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import java.util.Set;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HelloControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Property(name="micronaut.server.netty.worker.threads")
    String nettyWorkerThreads;

    @Test
    @Order(1)
    public void successful_test_non_blocking_requests_are_properly_handled() throws InterruptedException {
        out("\n-------\nSuccessful test:");
        out("Call service 10 times");
        for (int i = 0; i < 10; i++) {
            out("call /ok " + i);
            HttpRequest<String> reqOk1 = HttpRequest.GET("/ok");
            client.toBlocking().retrieve(reqOk1);
        }

        out("everything ok");
    }

    /**
     * Given the number of threads (N) defined for netty in application.yml this method will fail
     * at the Nth call
     */
    @Test
    @Order(2)
    public void failing_test_blocking_http_filter_blocks_subsequent_calls() {
        out("\n-------\nFailing test:");
        try  {
            out("call /authenticate will 'suspend' a thread on the server");
            HttpRequest<String> request = HttpRequest.GET("/authenticate");
            client.toBlocking().retrieve(request);
        }
        catch (ReadTimeoutException e) {
            out("/authenticate call timeout: ok");
        }

        out("Netty worker threads: " + nettyWorkerThreads);
        out("Call service 10 times");
        for (int i = 0; i < 10; i++) {
            out("\ncalling /ok " + i);
            HttpRequest<String> reqOk1 = HttpRequest.GET("/ok");
            client.toBlocking().retrieve(reqOk1);
            out("return /ok " + i);
        }

        out("everything ok");
    }

    private void out(String str) {
        System.out.println(str);
    }

}
