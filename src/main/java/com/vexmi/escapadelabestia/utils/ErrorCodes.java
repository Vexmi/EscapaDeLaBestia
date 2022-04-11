package com.vexmi.escapadelabestia.utils;

import com.vexmi.escapadelabestia.classes.GameState;

public enum ErrorCodes
{
    GOOD("GOOD", 0),
    WORLD_NOT_FOUND("WORLD_NOT_FOUND", 1),
    LOBBY_NOT_FOUND("LOBBY_NOT_FOUND", 2),
    MATERIAL_NOT_FOUND("MATERIAL_NOT_FOUND", 3),
    UNKNOWN_ERROR("UNKNOWN_ERROR", 4),
    PLAYER_ALREADY_IN_GAME("PLAYER_ALREADY_IN_GAME", 5),
    NO_BESTIA_SPAWN("NO_BESTIA_SPAWN", 6),
    NO_PLAYERS_SPAWN("NO_PLAYERS_SPAWN", 7);

    private static final ErrorCodes[] META_LOOKUP = new ErrorCodes[values().length];
    private final String name;
    private final int code;

    private ErrorCodes(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    public String getName()
    {
        return this.name;
    }

    public int getCode()
    {
        return this.code;
    }

    public ErrorCodes getByCode(int code)
    {
        for(ErrorCodes foundcode : values())
        {
            if(foundcode.getCode() == code)
            {
                return foundcode;
            }
        }
        return null;
    }

    static
    {
        for(ErrorCodes errorCodes : values())
        {
            META_LOOKUP[errorCodes.getCode()] = errorCodes;
        }
    }

}
