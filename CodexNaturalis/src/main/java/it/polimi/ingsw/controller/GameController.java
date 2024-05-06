package it.polimi.ingsw.controller;

//da capire addPlayer e disconnectPlayer
//implementa i metodi dell'interfaccia GameControllerInterface (cartella RMI). chiama i rispettivi metodi del model
//saranno da implementare altri metodi in base agli input della view

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.model.Heartbeat;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameStatus;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.rmi.GameControllerInterface;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static it.polimi.ingsw.view.PrintAsync.printAsync;

/**
 * GameController Class
 * Controls a specific Game {@link Game} by allowing a player to perform all actions that can be executed in a game
 * The class can add, remove, reconnect and disconnects players to the game and let players grab and position tiles
 * from the playground to the shelf. <br>
 * <br>
 * It manages all the game from the beginning (GameStatus.WAIT to the ending {GameStatus.Ended}
 */
public class GameController implements GameControllerInterface, Serializable, Runnable {

    /**
     * The {@link Game} to control
     */
    private final Game model;

    /**
     * A random object for implementing pseudo-random choice     */
    private final Random random = new Random();

    /**
     * Map of heartbeats for detecting disconnections
     * For implementing AF: "Clients disconnections"
     */
    private final transient Map<GameListenerInterface, Heartbeat> heartbeats;

    private Thread reconnectionTh;


    /**GameController Constructor
     * Init a GameModel
     */
    public GameController(Map<GameListenerInterface, <Heartbeat> heartbeats) throws FileNotFoundException, FileReadException, DeckEmptyException {
        this.heartbeats = heartbeats;
        model = new Game();
    }



    /**
     * @return the number of online players that are in the game
     */
    @Override
    public int getNumOnlinePlayers() throws RemoteException{
        return model.getNumPlayersOnline();
    }

    /**
     * Set the @param p player ready to start
     * When all the players are ready to start, the game starts (game status changes to running)
     * @param p Player to set has ready
     * @return true if the game has started, false else            */
    @Override
    public synchronized boolean playerIsReadyToStart(String p) {
        model.playerIsReadyToStart(model.getPlayerByNickname(p));

        //La partita parte automaticamente quando tutti i giocatori sono pronti
        if (model.arePlayersReadyToStartAndEnough()) {
            model.chooseOrderPlayers(); //assegna l'ordine ai giocatori nbell'orderArray
            ArrayList<Player> players= model.getPlayers();
            int[] orderArray= model.getOrderArray();
            model.setCurrentPlayer(players.get(orderArray[0]);

            model.initializeBoard();
            model.initializeCards();
            model.setInitialStatus();
            return true;
        }
        return false;//Game non started yet
    }

    /**
     * Check if it's your turn
     *
     * @param nick the nickname of the player
     * @return true if it's your turn, false else
     * @throws RemoteException if there is a connection error (RMI)
     */
    @Override
    public synchronized boolean isThisMyTurn(String nick) throws RemoteException {
        return model.getCurrentPlayer().getNickname().equals(nick);
    }


    @Override
    public void disconnectPlayer(String nick, GameListenerInterface lisOfClient) throws RemoteException {
        //TODO
    }

    /**
     * Adds a new player to the match.
     *
     * @param nick the nickname of the player to add.
     * @throws MatchFull if the match is already full and no more players can be added.
     * @throws NicknameAlreadyTaken if the specified nickname is already in use by another player.
     */
    public void addPlayer(String nick) throws MatchFull, NicknameAlreadyTaken {
        model.addPlayer(nick);
    }
    /**
     * remove a player from the game.
     *
     * @param nick the nickname of the player to add.
     * @throws MatchFull if the match is already full and no more players can be added.
     * @throws NicknameAlreadyTaken if the specified nickname is already in use by another player.
     */
    public void removePlayer(String nick) {
        model.removePlayer(nick);
    }



    @Override
    public synchronized void setInitialCard(String playerName, int index) throws NotPlayerTurnException {
        Player currentPlayer = model.getPlayerByNickname(playerName);
        if(currentPlayer.equals(model.getCurrentPlayer())){
            model.setInitialCard(currentPlayer, index);
        }else{
            throw new NotPlayerTurnException("ERROR: not the Player's turn");
        }
    }

    @Override
    public synchronized void placeCardInBook(String playerName, int chosenCard, int rowCell, int columnCell){
        Player currentPlayer = model.getPlayerByNickname(playerName);
        if(currentPlayer.equals(model.getCurrentPlayer())){

        }
    }


    /**
     * gets th Game ID of the current Game
     * @return
     * @throws RemoteException
     */
    @Override
    public int getGameId() throws RemoteException {
        return model.getGameId();
    }

    @Override
    public void leave(GameListenerInterface lis, String nick) throws RemoteException {
        //IMPLEMENTA
    }


    @Override
    public synchronized void setGoalCard(String playerName, int index) throws NotPlayerTurnException {
        Player currentPlayer = model.getPlayerByNickname(playerName);
        if(currentPlayer.equals(model.getCurrentPlayer())){
            model.setPlayerGoal(currentPlayer, index);
        }else{
            throw new NotPlayerTurnException("ERROR: not the Player's turn");
        }
    }

    /**
     * Create a new game and join to it
     *
     * @param lis GameListener of the player who is creating the game
     * @param nick Nickname of the player who is creating the game
     * @return GameControllerInterface associated to the created game
     * @throws RemoteException
     */
    @Override
    public synchronized GameControllerInterface createGame(GameListenerInterface lis, String nick) throws RemoteException, FileNotFoundException, FileReadException, DeckEmptyException {
        Player p = new Player(nick);


        GameController c = new GameController();
        c.addListener(lis, p);

        printAsync("\t>Game " + c.getGameId() + " added to runningGames, created by player:\"" + nick + "\"");

        try {
            c.addPlayer(p);
        } catch (MaxPlayersInException | PlayerAlreadyInException e) {
            lis.genericErrorWhenEnteringGame(e.getMessage());
        }

        return c;
    }
    /**
     * Add listener @param l to model listeners and player listeners
     *
     * @param l GameListener to add
     * @param p entity of the player
     */
    public void addListener(GameListenerInterface l, Player p) {
        model.addListener(l);
        for (GameListenerInterface othersListener : model.getListeners()) {
            p.addListener(othersListener);
        }
        for (Player otherPlayer : model.getPlayers()) {
            if (!otherPlayer.equals(p)) {
                otherPlayer.addListener(l);
            }
        }
    }



    @Override
    public void heartbeat(String nick, GameListenerInterface me) throws RemoteException {

    }
    @Override
    public void run() {
        // IMPLEMENTA
    }

}
