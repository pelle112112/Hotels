import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class DAO implements IDAO{
    private List<Object> list;
    @Override
    public Iterable<Object> getall() {
       return list.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Object getById(int id) {
        return list.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.equals(id))
                .findFirst();
    }

    @Override
    public void create(Object o) {
        list.add(o);

    }

    @Override
    public void update(Object o, int id) {
        Object objectToChange = getById(id);
        list.set(id, objectToChange);

    }

    @Override
    public void delete(int id) {
        List<Object> testList = new ArrayList<>();
        testList.addAll(this.list);
        testList.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.equals(id))
                .findFirst()
                .ifPresent(list::remove);
        this.list = testList;

    }


}
