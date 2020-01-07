package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller
public class HelloController {

    @Get("/ok")
    @Produces(MediaType.TEXT_PLAIN)
    public String ok() {
        return "ok";
    }

    @Get("/authenticate")
    @Produces(MediaType.TEXT_PLAIN)
    public String blocking() {
        return "authenticate";
    }
}
