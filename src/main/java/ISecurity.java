import io.javalin.http.Handler;

public interface ISecurity {
    Handler login() throws Exception;
    Handler authenticate();
}