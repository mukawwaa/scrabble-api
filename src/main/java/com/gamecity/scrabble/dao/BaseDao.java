package com.gamecity.scrabble.dao;

import java.util.List;

public interface BaseDao<T>
{
    T get(Long id);

    List<T> list();

    T save(T entity);

    void remove(Long id);
}
