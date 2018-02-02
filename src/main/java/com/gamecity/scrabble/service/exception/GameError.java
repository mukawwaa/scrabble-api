package com.gamecity.scrabble.service.exception;

public enum GameError
{
    INVALID_PARAMETERS(1001, "Invalid parameters!"), //
    INVALID_BOARD_ID(1002, "Invalid board id!"), //
    ALREADY_ON_BOARD(1003, "You are already playing on board {0}!"), //
    INVALID_USER_ID(1004, "Invalid user id!"), //
    GAME_IS_NOT_STARTED(1005, "The game has not started yet!"), //
    NOT_YOUR_TURN(1006, "It is not your turn!"), //
    CELL_IS_NOT_EMPTY(1007, "Cell [{0},{1}] is not empty!"), //
    OWNER_CANNOT_LEAVE_BOARD(1008, "Board owner cannot leave the board"), //
    UNKNOWN_ACTION_TYPE(1009, "{0} is an unknown action type!"), //
    NOT_ON_BOARD(1010, "You are not on board {0}!"), //
    GAME_IS_STARTED(1011, "The game has already started!"), //
    WORD_IS_NOT_DEFINED(1012, "Words {0} are not defined in {1} language!"), //
    INVALID_RACK(1013, "Rack is not valid!"), //
    STARTING_CELL_CANNOT_BE_EMPTY(1014, "Starting cell cannot be empty!"), //
    WORD_IS_NOT_LINKED(1015, "Words {0} are not linked with any existing words!");

    private int code;
    private String message;

    private GameError(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
