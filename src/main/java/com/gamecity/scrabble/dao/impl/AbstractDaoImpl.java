package com.gamecity.scrabble.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gamecity.scrabble.dao.BaseDao;
import com.gamecity.scrabble.entity.AbstractEntity;
import com.gamecity.scrabble.util.DateUtils;

public abstract class AbstractDaoImpl<T extends AbstractEntity> implements BaseDao<T>
{
    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> clazz;

    public AbstractDaoImpl()
    {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T get(Long id)
    {
        return entityManager.find(clazz, id);
    }

    @Override
    public List<T> list()
    {
        String query = "select entity from " + ((Entity) clazz.getAnnotation(Entity.class)).name() + " entity orderNo by id asc";
        return (List<T>) entityManager.createQuery(query).getResultList();
    }

    @Override
    public T save(T entity)
    {
        entity.setCreateDate(entity.getId() == null ? DateUtils.nowAsDate() : entity.getCreateDate());
        entity.setLastUpdateDate(entity.getId() != null ? DateUtils.nowAsDate() : null);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void remove(Long id)
    {
        T entity = get(id);
        entityManager.remove(entity);
    }

    protected List<T> listByNamedQuery(String querySql, List<String> aliases, Object... params)
    {
        Query query = createQueryWithParams(querySql, aliases, params);
        return query.getResultList();
    }

    protected <G> List<G> listGenericByNamedQuery(String querySql, List<String> aliases, Object... params)
    {
        Query query = createQueryWithParams(querySql, aliases, params);
        return query.getResultList();
    }

    protected int executeByNamedQuery(String querySql, List<String> aliases, Object... params)
    {
        Query query = createQueryWithParams(querySql, aliases, params);
        return query.executeUpdate();
    }

    protected T findByNamedQuery(String querySql, List<String> aliases, Object... params)
    {
        try
        {
            Query query = createQueryWithParams(querySql, aliases, params);
            query.setMaxResults(1);
            return (T) query.getSingleResult();
        }
        catch (NoResultException e)
        {
            return null;
        }
    }

    private Query createQueryWithParams(String querySql, List<String> aliases, Object... params)
    {
        Query query = entityManager.createNamedQuery(querySql);
        if (params != null)
        {
            IntStream.range(1, params.length + 1).forEach(index -> query.setParameter(aliases.get(index - 1), params[index - 1]));
        }
        return query;
    }

    protected <G> G findGenericTypeByNamedQuery(String querySql, List<String> aliases, Object... params)
    {
        try
        {
            Query query = createQueryWithParams(querySql, aliases, params);
            query.setMaxResults(1);
            return (G) query.getSingleResult();
        }
        catch (NoResultException e)
        {
            return null;
        }
    }
}
