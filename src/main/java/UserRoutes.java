
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.time.LocalDate;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoutes {
    public static void main(String[] args) {
        ISecurity securityController = new SecurityController();


        Javalin app = Javalin
                .create()
                .start(7007)
                .routes(()->{
                    path("auth", ()->{
                        try {
                            post("login", securityController.login());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                })
                .routes(()->{
                    path("protected", ()->{
                        before(securityController.authenticate());
                        get("demo", ctx-> ctx.result("Hello from protected"),Role.USER);
                    });
                });
    }
    private enum Role implements RouteRole {
        USER, ADMIN
    }
}