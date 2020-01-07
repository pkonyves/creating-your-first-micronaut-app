package example.micronaut;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.ReadTimeoutException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

@MicronautTest
public class HelloControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    EmbeddedServer embeddedServer;

    @Test
    public void testHello() {
        int port = embeddedServer.getPort();
        MockAuthorizationFilter.selfUrl.set("http://localhost:"+port);

        try  {
            System.out.println("call /call-remote blocking, this will 'suspend' a thread on the server");
            HttpRequest<String> request = HttpRequest.GET("/call-remote");
            client.toBlocking().retrieve(request);
        }
        catch (ReadTimeoutException e) {
            System.out.println("/call-remote call timeout: ok");
        }

        for (int i = 0; i < 30; i++) {
            System.out.println("call /ok " + i);
            HttpRequest<String> reqOk1 = HttpRequest.GET("/ok");
            client.toBlocking().retrieve(reqOk1);
        }

        System.out.println("everything ok");
    }

    @Test
    public void testHell2() {
        int port = embeddedServer.getPort();
        MockAuthorizationFilter2.selfUrl.set("http://localhost:"+port);

        /*
        try  {
            System.out.println("call /call-remote blocking");
            HttpRequest<String> request = HttpRequest.GET("/call-remote");
            client.toBlocking().retrieve(request);
        }
        catch (ReadTimeoutException e) {
            System.out.println("/call-remote call timeout: ok");
        }
        */

        int nioThreadCount = getNumOfNioThreads();
        for (int i = 0; i < 10; i++) {
            System.out.println("call /ok " + i);
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
