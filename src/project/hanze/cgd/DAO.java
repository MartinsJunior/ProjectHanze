package project.hanze.cgd;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    public void create();
    public void insert(T obj);
    public void delete(T obj);
    public List<T> read(Class<T> classe);  
}
