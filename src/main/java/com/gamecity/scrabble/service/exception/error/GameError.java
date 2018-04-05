package com.gamecity.scrabble.service.exception.error;

public enum GameError
{
    NOT_YOUR_TURN(3001, "It is not your turn!"), //
    INVALID_RACK(3002, "Rack is not valid!"), //
    STARTING_CELL_CANNOT_BE_EMPTY(3003, "Starting cell cannot be empty!"), //
    CELL_IS_NOT_EMPTY(3004, "Cell [{0},{1}] is not empty!"), //
    WORD_IS_NOT_LINKED(3005, "Words {0} are not linked with any existing words!"), //
    WORD_IS_NOT_DEFINED(3006, "Words {0} are not defined in {1} language!");

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
