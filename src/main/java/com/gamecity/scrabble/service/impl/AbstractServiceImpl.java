package com.gamecity.scrabble.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gamecity.scrabble.dao.BaseDao;
import com.gamecity.scrabble.service.BaseService;

public abstract class AbstractServiceImpl<T, D extends BaseDao<T>> implements BaseService<T>
{
    @Autowired
    protected D baseDao;

    @Override
    public T get(Long id)
    {
        return baseDao.get(id);
    }

    @Override
    public List<T> list()
    {
        return baseDao.list();
    }

    @Override
    public T save(T entity)
    {
        return baseDao.save(entity);
    }

    @Override
    public void remove(Long id)
    {
        baseDao.remove(id);
    }
}
