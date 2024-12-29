package at.technikum.persistence.dao;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of the Data-Access-Object Pattern
 *
 * @param <T>
 */
// DAO overview see: https://www.baeldung.com/java-dao-pattern
public interface Dao<T> {

    // READ
    Optional<T> get(int id);

    Collection<T> getAll();

    // CREATE
    void save(T t);
    void saveWithId(T t);

    // UPDATE
    void update(T t, String[] params);

    // DELETE
    void delete(T t);

    Optional<T> getByUsername(String username);
}
