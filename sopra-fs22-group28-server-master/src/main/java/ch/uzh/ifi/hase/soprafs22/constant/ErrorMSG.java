package ch.uzh.ifi.hase.soprafs22.constant;

public class ErrorMSG {
    private ErrorMSG() {
        throw new IllegalStateException("Do not instantiate - Utility class");
    }

    public static final String LOBBY_DOES_NOT_EXIST = "No Lobby with given Lobby ID exists!";
    public static final String ANSWER_DOES_NOT_EXIST = "No Answer with given Answer ID exists!";
    public static final String LOBBY_CREATION_ERROR = "Lobby could not be created with given settings!";
    public static final String ROUND_STARTED_ALREADY = "The round of this Lobby has already started.";
    public static final String ROUND_NOT_STARTED_YET = "The round of this Lobby has not started yet.";
    public static final String LOBBY_ALPHABET_USED_UP = "This lobby has already used the entire alphabet :(";
    public static final String ONLY_HOST_LOBBY_CHANGE = "Only the host may change the Lobby Status or Settings.";
    public static final String CANNOT_CHANGE_SETTINGS_AFTER_GAME_START = "Settings cannot be changed after the game has started.";
    public static final String PLAYER_TOKEN_DOES_NOT_EXIST = "No Player with given Player Token exists!";
    public static final String PLAYER_ALREADY_VOTED = "The player with the associated player token already voted!";
}
