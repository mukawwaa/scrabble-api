CREATE KEYSPACE IF NOT EXISTS scrabble WITH REPLICATION = { 'class' : 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '3' } AND DURABLE_WRITES = true;

CREATE TABLE IF NOT EXISTS scrabble.board_user_counter (
    board_id bigint,
    action_count counter,
    player_count counter,
    PRIMARY KEY (board_id)
);