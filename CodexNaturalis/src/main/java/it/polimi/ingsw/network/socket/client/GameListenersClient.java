package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.Chat.Message;
import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.game.GameImmutable;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.flow.Flow;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * This class implements the {@link GameListenerInterface} and acts as a client-side listener for game events from the server.
 * It forwards these events to the provided {@link Flow} instance for handling.
 *
 * <p>Instances of this class receive various game events, such as player actions, game state changes, and messages.
 * These events are then delegated to corresponding methods in the {@link Flow} object, which usually manages the user interface or application flow.
 *
 * <p>Methods in this class handle remote method calls from the server, each corresponding to a specific game event that triggers a client response.
 *
 * <p>Events handled include player join/leave, game start/end, card actions, player readiness, reconnection requests, and error notifications.
 * For each event, the relevant method in the {@link Flow} object is called to update the client interface or state.
 *
 * <p>Instances of this class are created and managed by the client-side application to handle real-time updates and interactions with the game server.
 */

public class GameListenersClient implements GameListenerInterface, Serializable {


    private Flow flow;

    public GameListenersClient(Flow gui) {
        this.flow = gui;
    }


    @Override
    public void playerJoined(GameImmutable model, String nickname, Color playerColor) throws RemoteException {
        flow.playerJoined(model, nickname, playerColor);
    }

    @Override
    public void requireNumPlayersGameID(GameImmutable model)throws RemoteException{
            flow.requireNumPlayersGameID(model);
    }

    @Override
    public void wrongChooseCard(GameImmutable model, String msg) throws RemoteException{
        flow.wrongChooseCard(model, msg);
    }

    @Override
    public void playerLeft(GameImmutable model, String nickname) throws RemoteException {
        flow.playerLeft(model,nickname);
    }

    @Override
    public void joinUnableGameFull(Player triedToJoin, GameImmutable model) throws RemoteException {
        flow.joinUnableGameFull(triedToJoin, model);
    }


    @Override
    public void joinUnableNicknameAlreadyIn(Player triedToJoin, GameImmutable model) throws RemoteException {
        flow.joinUnableNicknameAlreadyIn(triedToJoin, model);
    }

    @Override
    public void AskForReconnection (Player triedToJoin, GameImmutable gameModel) throws RemoteException {
        flow. AskForReconnection(triedToJoin, gameModel);
    }


    @Override
    public void gameStarted(GameImmutable model) throws RemoteException {
        flow.gameStarted(model);
    }

    @Override
    public void gameEnded(GameImmutable model) throws RemoteException {
        flow.gameEnded(model);
    }

    @Override
    public void requireInitialReady(GameImmutable model, int index) throws IOException, FileReadException {
        flow.requireInitialReady(model, index);
    }

    @Override
    public void requireGoalsReady(GameImmutable model, int index) throws RemoteException {
        flow.requireGoalsReady(model, index);
    }
    @Override
    public void cardsReady(GameImmutable model) throws RemoteException {
        flow.cardsReady(model);
    }
    @Override
    public void cardPlaced(GameImmutable model) throws RemoteException {
        flow.cardPlaced(model );
    }
    @Override
    public void pointsAdded(GameImmutable model) throws RemoteException {
        flow.pointsAdded(model );
    }


    @Override
    public void cardDrawn(GameImmutable model) throws RemoteException {
        flow.cardDrawn(model);
    }

    @Override
    public void nextTurn(GameImmutable model) throws RemoteException {
        flow.nextTurn(model);
    }

    @Override
    public void playerDisconnected(GameImmutable model, String nickname) throws RemoteException {
        flow.playerDisconnected(model, nickname);
    }

    @Override
    public void lastCircle(GameImmutable model) throws RemoteException {
        flow.lastCircle(model);
    }

  @Override
    public void playerReady(GameImmutable model, String nickname) throws RemoteException {
        flow.playerReady(model, nickname);
    }

    @Override
    public void playerReconnected(GameImmutable model,  String nickPlayerReconnected) throws RemoteException{
        flow.playerReconnected(model, nickPlayerReconnected);
    }

    @Override
    public void onlyOnePlayerConnected(GameImmutable gameModel, int secondsToWaitUntilGameEnded) throws RemoteException {
        flow.onlyOnePlayerConnected(gameModel,secondsToWaitUntilGameEnded);
    }

    @Override
    public void errorReconnecting(String why) throws RemoteException {
        flow.errorReconnecting(why);
    }

    @Override
    public void sentMessage(GameImmutable model, Message msg) throws RemoteException  {
        flow.sentMessage(model, msg);
    }

}
