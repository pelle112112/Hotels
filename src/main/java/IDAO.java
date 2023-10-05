import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;
import java.util.Optional;

public interface IDAO<T> {
    Iterable<T> getall();
    T getById(int id);
    void create(T t);
    void update(T t, int id);
    void delete(int id);
}
