package com.gamecity.scrabble.dao.impl.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.decr;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.incr;
import static com.datastax.driver.core.querybuilder.QueryBuilder.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gamecity.scrabble.dao.BoardUserCounterRepository;
import com.gamecity.scrabble.entity.cassandra.BoardUserCounter;

@Component
public class BoardUserCounterRepositoryImpl implements BoardUserCounterRepository
{
    @Autowired
    private Session session;

    private Mapper<BoardUserCounter> mapper;

    @Autowired
    private void init(MappingManager mappingManager)
    {
        mapper = mappingManager.mapper(BoardUserCounter.class);
    }

    @Override
    public BoardUserCounter addPlayer(BoardUserCounter counter)
    {
        RegularStatement statement =
            update(BoardUserCounter.TABLE_NAME)
                .with(incr(BoardUserCounter.PLAYER_COUNT)).and(incr(BoardUserCounter.ACTION_COUNT)).where(eq(BoardUserCounter.BOARD_ID, counter.getBoardId()));
        session.execute(statement);
        return mapper.get(counter.getBoardId());
    }

    @Override
    public BoardUserCounter removePlayer(BoardUserCounter counter)
    {
        RegularStatement statement =
            update(BoardUserCounter.TABLE_NAME)
                .with(decr(BoardUserCounter.PLAYER_COUNT)).and(incr(BoardUserCounter.ACTION_COUNT)).where(eq(BoardUserCounter.BOARD_ID, counter.getBoardId()));
        session.execute(statement);
        return mapper.get(counter.getBoardId());
    }

    @Override
    public BoardUserCounter getBoardUserCounter(Long boardId)
    {
        return mapper.get(boardId);
    }

}
