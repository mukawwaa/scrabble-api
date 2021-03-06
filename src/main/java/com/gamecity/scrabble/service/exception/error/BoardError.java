package com.gamecity.scrabble.service.exception.error;

public enum BoardError
{
    INVALID_BOARD_ID(2001, "Invalid board id!"), //
    ALREADY_ON_BOARD(2002, "You are already playing on board {0}!"), //
    OWNER_CANNOT_LEAVE_BOARD(2003, "Board owner cannot leave the board"), //
    NOT_ON_BOARD(2004, "You are not on board {0}!"), //
    BOARD_IS_NOT_STARTED(2005, "The game is not started yet!"), //
    BOARD_IS_STARTED(2006, "The game is already started!"),
    BOARD_IS_FINISHED(2007, "The board is finished!"),
    BOARD_IS_TERMINATED(2008, "The board is terminated!");

    private int code;
    private String message;

    private BoardError(int code, String message)
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
