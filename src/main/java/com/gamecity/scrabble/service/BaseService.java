package com.gamecity.scrabble.service;

import java.util.List;

public interface BaseService<T>
{
    T get(Long id);

    List<T> list();

    T save(T entity);

    void remove(Long id);
}
