package app.daos;

import java.util.List;

public interface IDAO<T> {

    T getById(Long id);

    List<T> getAll();

    void create(T t);

    void update(T t);

    void delete(Long id);
}
