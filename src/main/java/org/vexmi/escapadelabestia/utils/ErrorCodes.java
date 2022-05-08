package org.vexmi.escapadelabestia.utils;

public enum ErrorCodes {
    GOOD("GOOD", 0, Messages.ErrorCodes_GOOD),
    WORLD_NOT_FOUND("WORLD_NOT_FOUND", 1, Messages.ErrorCodes_WORLD_NOT_FOUND),
    LOBBY_NOT_FOUND("LOBBY_NOT_FOUND", 2, Messages.ErrorCodes_LOBBY_NOT_FOUND),
    MATERIAL_NOT_FOUND("MATERIAL_NOT_FOUND", 3, "MATERIAL_NOT_FOUND"),
    UNKNOWN_ERROR("UNKNOWN_ERROR", 4, "UNKNOWN_ERROR"),
    PLAYER_ALREADY_IN_GAME("PLAYER_ALREADY_IN_GAME", 5, "PLAYER_ALREADY_IN_GAME"),
    NO_BESTIA_SPAWN("NO_BESTIA_SPAWN", 6, "NO_BESTIA_SPAWN"),
    NO_PLAYERS_SPAWN("NO_PLAYERS_SPAWN", 7, "NO_PLAYERS_SPAWN");

    private final String name;
    private final int code;
    private final String message;

    ErrorCodes(String name, int code, String message) {
        this.name = name;
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static ErrorCodes getByCode(int code) {
        for (ErrorCodes foundcode : values())
            if (foundcode.getCode() == code)
                return foundcode;

        return null;
    }
}
