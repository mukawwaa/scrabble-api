package com.gamecity.scrabble;

public interface Constants
{
    String BASE_APP_SECURITY_REALM = "BASE_APP_SECURITY_REALM/client";
    String BASE_APP_REST_RESOURCE = "BASE_APP_REST_RESOURCE";
    String BASE_APP_TRUSTED_CLIENT = "BASE_APP_TRUSTED_CLIENT";
    String BASE_APP_SECRET = "$2a$10$kDbFMTh8J1fkDdFdPTrDFOERUd2RPFSa7kdUiR4YmGCjoq6CeZkWS";
    String BASE_APP_SIGNING_KEY = "BASE_APP_SIGNING_KEY";
    String BASE_APP_AUTHORIZATION_TYPE = "Basic";
    Integer TOKEN_VALIDITY_SECONDS = 60 * 100;

    interface Headers
    {
        String ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        String ALLOW_METHODS = "Access-Control-Allow-Methods";
        String MAX_AGE = "Access-Control-Max-Age";
        String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    }

    interface Cache
    {
        String CELL_RULES = "CELL_RULES";
        String TILE_RULES = "TILE_RULES";
        String CELL_RULE_BY_CELL_NUMBER = "CELL_RULE_BY_CELL_NUMBER";
        String TILE_RULE_BY_LETTER = "TILE_RULE_BY_LETTER";
        String BOARD_CELL = "BOARD_CELL";
        String BOARD_TILE = "BOARD_TILE";
        String RACK = "RACK";
    }

    interface RedisListener
    {
        String BOARD_CHAT = "BOARD_CHAT";
        String BOARD_CONTENT = "BOARD_CONTENT";
        String BOARD_USER = "BOARD_USER";
        String BOARD_PLAYERS = "BOARD_PLAYERS";

        interface Method
        {
            String RECEIVE_USER_UPDATE = "receiveUserUpdate";
        }
    }

    interface BoardSettings
    {
        Integer FIRST_ROUND = 1;
    }

    interface DatabaseSettings
    {

        String CREATE_DROP = "create-drop";
    }

    interface KafkaTopic
    {
        String BOARD_ACTION = "BOARD_ACTION";
    }
}
