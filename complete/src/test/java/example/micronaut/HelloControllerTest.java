package example.micronaut;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.ReadTimeoutException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

@MicronautTest
public class HelloControllerTest {

    static {
        System.setProperty("io.netty.eventLoopThreads", String.valueOf(3));
    }

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void testHello() {
        System.out.println("call blocking");
        HttpRequest<String> request = HttpRequest.GET("/blocking");

        try {
            String body = client.toBlocking().retrieve(request);
        }
        catch (ReadTimeoutException e) {
            System.out.println("/blocking call timeout: ok");
        }

        int nioThreadCount = getNumOfNioThreads();
        for (int i = 0; i < 30; i++) {
            System.out.println("call ok " + i);
            HttpRequest<String> reqOk1 = HttpRequest.GET("/ok");
            client.toBlocking().retrieve(reqOk1);
        }

        System.out.println("everything ok");

    }

    private int getNumOfNioThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        int nioThreadNum = 0;
        for (Thread t : threadSet){
            if (t.getName().startsWith("nioEventLoopGroup")) {
                nioThreadNum++;
            }
        }

        return nioThreadNum;
    }
}
