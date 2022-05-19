package org.vexmi.escapadelabestia.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ErrorCodes {
    GOOD("GOOD", 0, Messages.Error.GOOD),
    WORLD_NOT_FOUND("WORLD_NOT_FOUND", 1, Messages.Error.WORLD_NOT_FOUND),
    LOBBY_NOT_FOUND("LOBBY_NOT_FOUND", 2, Messages.Error.LOBBY_NOT_FOUND),
    MATERIAL_NOT_FOUND("MATERIAL_NOT_FOUND", 3, Messages.Error.MATERIAL_NOT_FOUND),
    UNKNOWN_ERROR("UNKNOWN_ERROR", 4, Messages.Error.UNKNOWN_ERROR),
    PLAYER_ALREADY_IN_GAME("PLAYER_ALREADY_IN_GAME", 5, Messages.Error.PLAYER_ALREADY_IN_GAME),
    NO_BESTIA_SPAWN("NO_BESTIA_SPAWN", 6, Messages.Error.NO_BESTIA_SPAWN),
    NO_PLAYERS_SPAWN("NO_PLAYERS_SPAWN", 7, Messages.Error.NO_PLAYERS_SPAWN),
    GAME_NOT_FOUND("GAME_NOT_FOUND", 8, Messages.Error.GAME_NOT_FOUND),
    GAME_NOT_ENABLED("GAME_NOT_ENABLED", 9, Messages.Error.GAME_NOT_ENABLED),
    GAME_IS_PLAYING("GAME_IS_PLAYING", 10, Messages.Error.GAME_IS_PLAYING),
    GAME_IS_FULL("GAME_IS_FULL", 11, Messages.Error.GAME_IS_FULL);

    @NotNull private final String name;
    private final int code;
    private final String message;

    ErrorCodes(@NotNull String name, int code, String message) {
        this.name = name;
        this.code = code;
        this.message = message;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    @Nullable
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
