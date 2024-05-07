package it.polimi.ingsw.view.Utilities;

import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameImmutable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

//Definisce l'interfaccia per la visualizzazione del gioco comune che servirà sia per la TUI che per la GUI

/**
 * User Interface
 * This class is used also for TUI and GUI
 */
public abstract class UI {
    protected List<String> eventsToShow; //eventi che devono essere mostrati sullo schermo

    /**
     * Initialize GUI or TUI
     */
    public abstract void init();
    /**
     * Show's Cranio Creation's logo
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract void show_publisher() throws IOException, InterruptedException;
    public abstract void show_askPlaceCardsMainMsg(GameImmutable model);
    public abstract void show_alwaysShow(GameImmutable model, String nickname);
    public abstract void show_alwaysShowForAll(GameImmutable model);
    public abstract void show_askWhichCellMsg(GameImmutable model);
    public abstract void show_cardPlacedMsg(GameImmutable model);
    public abstract void show_pointsAddedMsg(GameImmutable model);
    public abstract void show_joiningToGameMsg(String nick);
    /**
     * Asks the player for his nickname
     */
    public abstract void show_insertNicknameMessage();
    /**
     * Shows the player's chosen nickname
     *
     * @param nickname nickname just chosen by the player
     */
    public abstract void show_chosenNickname(String nickname);
    /**
     * Shows game started message
     * @param model model where the game has started
     */
    public abstract void show_gameStarted(GameImmutable model);
    /**
     * Shows the playerDeck
     * @param model
     */
    public abstract void show_gameEnded(GameImmutable model);
    public abstract void show_notValidMessage();
    public abstract void show_playerJoined(GameImmutable gameModel, String nick);
    public abstract void show_allPlayers(GameImmutable model);
    public abstract void show_youAreReady(GameImmutable model);
    protected abstract void show_playerDeck(GameImmutable model);
    /**
     * Shows that the playing player is ready to start
     *
     * @param gameModel     model where events happen
     * @param nickname player's nickname
     */
    public abstract void show_readyToStart(GameImmutable gameModel, String nickname);
    public abstract void show_returnToMenuMsg();
    public abstract void show_whichInitialCards();
    public abstract void show_whichObjectiveCards();
    /**
     * Shows message on important event added
     * @param input the string of the important event to add
     */
    public abstract void addImportantEvent(String input);
    /**
     * Resets the important events
     */
    public abstract void resetImportantEvents();











    //TODO
    public abstract void show_wrongSelectionInitialMsg();
    public abstract void show_wrongSelectionObjectiveMsg();
    protected abstract void show_askNum(String msg, GameImmutable gameModel, String nickname);
    public abstract void show_temporaryInitialCards(GameImmutable model) throws FileNotFoundException, FileReadException;
    protected abstract void show_ObjectiveCards(GameImmutable model);







    /**
     * Shows an error when there's no connection
     */
    protected abstract void show_noConnectionError();
    /**
     * Show the message for next turn or reconnected player
     *
     * @param model    model where events happen
     * @param nickname nick of reconnected player (or of the player that is now in turn)
     */
    protected abstract void show_nextTurnOrPlayerReconnected(GameImmutable model, String nickname);


}
