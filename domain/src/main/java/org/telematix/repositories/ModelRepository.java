package org.telematix.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ModelRepository<T> {
    default List<T> getAll() {
        return new ArrayList<>();
    }

    default Optional<T> getById(int itemId) {
        return Optional.empty();
    }

    default Optional<T> saveItem(T item) {
        return Optional.empty();
    }

    default Optional<T> updateItem(int itemId, T item) {
        return Optional.empty();
    }

    default void deleteItem(int itemId) {}
}
