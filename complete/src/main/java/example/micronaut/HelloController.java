package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/")
public class HelloController {

    @Get("/ok")
    @Produces(MediaType.TEXT_PLAIN)
    public String ok() {
        return "ok";
    }

    @Get("/blocking")
    @Produces(MediaType.TEXT_PLAIN)
    public String blocking() {
        try {
            Thread.sleep(10*60*1000);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "blocking";
    }
}
