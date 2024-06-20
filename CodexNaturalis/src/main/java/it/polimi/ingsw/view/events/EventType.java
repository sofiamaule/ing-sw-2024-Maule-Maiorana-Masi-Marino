package it.polimi.ingsw.view.events;

/**
 * Enum representing various types of events that can occur in the game.
 */
public enum EventType {
    BACK_TO_MENU,
    PLAYER_JOINED,
    PLAYER_READY,
    GAME_FULL,
    NICKNAME_ALREADY_IN,
    NICKNAME_TO_RECONNECT,
    GENERIC_ERROR,
    ERROR_RECONNECTING,
    PLAYER_LEFT,
    GAME_STARTED,
    GAME_ENDED,
    NEXT_TURN,
    PLAYER_DISCONNECTED,
    PLAYER_RECONNECTED,
    CARD_PLACED,
    CARD_PLACED_NOT_CORRECT,
    CARD_DRAWN,
    ENTERING_GAME,
    CARDS_READY,
    SENT_MESSAGE;
}
