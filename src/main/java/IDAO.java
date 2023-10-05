import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;
import java.util.Optional;

public interface IDAO<T> {
    List<T> getall(String entityName);
    T getById(int id, Class<T> tclass);
    void create(T t);
    void update(T t, int id);
    boolean delete(int id, Class<T> tclass);
}
