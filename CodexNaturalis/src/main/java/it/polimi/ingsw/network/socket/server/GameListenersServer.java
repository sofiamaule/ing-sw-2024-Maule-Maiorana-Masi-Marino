package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.Chat.Message;
import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.game.GameImmutable;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.socket.Messages.serverToClientMessages.*;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;


/**
 * Class used to pass the GameListener to the client via socket
 * It has a private ObjectOutputStream where it writes the data
 **/
public class GameListenersServer implements GameListenerInterface, Serializable {

    /**
     * To send objects from the Server to the Client
     */
    private final ObjectOutputStream out;

    /**
     * Constructor to create a GameListenersHandlerSocket
     * @param out the ObjectOutputStream
     */
    public GameListenersServer(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     * This method is used to write on the ObjectOutputStream the message that a player has joined the game
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void playerJoined(GameImmutable model, String nickname, Color playerColor) throws RemoteException {
        try {
            out.writeObject(new msgPlayerJoined(model, nickname, playerColor));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send playerJoined message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream the message that a player chose the gameID and the players number
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void requireNumPlayersGameID(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new MsgNumPlayersGameID(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send playerJoined message", e);
        }
    }

    /**
     * This method is used to notify the client that a player has to choose again the CARD
     *
     * @param model is the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void wrongChooseCard(GameImmutable model, String msg) throws RemoteException{
        try {
            out.writeObject(new msgWrongChooseCard(model, msg));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send WrongChooseCard message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream the message that a player is unable to join the game because it is full
     * @param triedToJoin is the player that has tried to join the game {@link Player}
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void joinUnableGameFull(Player triedToJoin, GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgJoinUnableGameFull(triedToJoin,model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send gameStarted message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream the message that a player is unable to join the game because the nickname is already in use
     * @param triedToJoin is the player that has tried to join the game {@link Player}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void joinUnableNicknameAlreadyIn(Player triedToJoin, GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgJoinUnableNicknameAlreadyIn(triedToJoin, model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send gameStarted message", e);
        }
    }

    /**
     * This method is used to notify the client to ask for reconnection if the player is trying to reconnect.
     *
     * @param triedToJoin the player who is trying to reconnect
     * @param model the current state of the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void AskForReconnection (Player triedToJoin, GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgAskForReconnection(triedToJoin, model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send gameStarted message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream the game started
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void gameStarted(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgGameStarted(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send gameStarted message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream that the front and back of the initialCard are ready to be shown to the player
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void requireInitialReady(GameImmutable model, int index) throws RemoteException, FileReadException {
        try {
            out.writeObject(new msgRequireInitialReady(model, index));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send requireInitialReady message", e);
        }
    }

    /**
     * Notifies the listeners that objective cards are ready to be chosen.
     *
     * @param model the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void requireGoalsReady(GameImmutable model, int index) throws RemoteException {
        try {
            out.writeObject(new msgRequireGoalsReady(model, index));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send requireGoalsReady message", e);
        }
    }

    /**
     * Notifies the listeners that objective cards are chosen.
     *
     * @param model the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void cardsReady(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgCardsReady(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send requireCardsReady message", e);
        }
    }

    /**
     * Notifies the client that a card has been placed on the board.
     *
     * @param model the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void cardPlaced(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgCardPlaced(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send CardPlaced message", e);
        }
    }

    /**
     * Notifies the client that points have been added.
     *
     * @param model the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void pointsAdded(GameImmutable model)throws RemoteException{
        try {
            out.writeObject(new msgPointsAdded(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send CardPlaced message", e);
        }
    }

    /**
     * Notifies the client that a card has been drawn.
     *
     * @param model the game model
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void cardDrawn(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgCardDrawn(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send CardDwawn message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream that the next turn is started
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void nextTurn(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgNextTurn(model));
            finishSending();
        }  catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send Next_Turn message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream that the game ended
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void gameEnded(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgGameEnded(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send Game_Ended message", e);
        }
    }


    /**
     * This method is used to write on the ObjectOutputStream that the last circle is started
     * @param model is the game model {@link GameImmutable}
     * @throws RemoteException if the connection fails
     */
    @Override
    public void lastCircle(GameImmutable model) throws RemoteException {
        try {
            out.writeObject(new msgLastCircle(model));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed Last_Circle message", e);
        }
    }

    /**
     * Notifies the client that a player is ready.
     *
     * @param model the game model
     * @param nickname      the nickname of the player who is ready
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void playerReady(GameImmutable model, String nickname) throws RemoteException {
        try {
            out.writeObject(new msgPlayerReady(model, nickname));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed platerReady message", e);

        }
    }

    /**
     * This method is used to write on the ObjectOutputStream that a player has disconnected
     * @param model is the game model {@link GameImmutable}
     * @param nickname is the nickname of the player that has disconnected
     * @throws RemoteException if the connection fails
     */
    @Override
    public void playerDisconnected(GameImmutable model, String nickname) throws RemoteException {
        try {
            out.writeObject(new msgPlayerDisconnected(model,nickname));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send playerLeft message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream the message that a player has left the game
     * @param model is the game model {@link GameImmutable}
     * @param nickname is the nickname of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void playerLeft(GameImmutable model, String nickname) throws RemoteException {
        try {
            out.writeObject(new msgPlayerLeft(model, nickname));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send playerLeft message", e);
        }
    }

    /**
     * Makes sure the message has been sent
     * @throws IOException
     */
    private void finishSending() throws IOException {
        out.flush();
        out.reset();
    }

    /**
     * This method is used to write on the ObjectOutputStream the message that a player has reconnected to the game
     * @param gamemodel is the game model {@link GameImmutable}
     * @param nickPlayerReconnected is the nickname of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void playerReconnected(GameImmutable gamemodel, String nickPlayerReconnected) throws RemoteException {
        try {
            out.writeObject(new msgPlayerReconnected(gamemodel, nickPlayerReconnected));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send playerReconnected message", e);
        }
    }

    /**
     * This is a generic error that can happen when a player is entering the game
     * @param why is the reason why the error happened
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void errorReconnecting(String why) throws RemoteException{
        try {
            out.writeObject(new msgGenericErrorWhenEnteringGame(why));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send errorReconnecting message", e);
        }
    }

    /**
     * This method is used to write on the ObjectOutputStream that only one player is connected
     * @param gameModel is the game model {@link GameImmutable}
     * @param secondsToWaitUntilGameEnded is the number of seconds to wait until the game ends
     * @throws RemoteException if the connection fails
     */
    @Override
    public void onlyOnePlayerConnected(GameImmutable gameModel, int secondsToWaitUntilGameEnded) throws RemoteException {
        try {
            out.writeObject(new msgOnlyOnePlayerConnected(gameModel,secondsToWaitUntilGameEnded));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send onlyOnePlayerConnected message", e);
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param model the game model
     * @param msg   the message to send
     * @throws RemoteException if the reference could not be accessed
     */
    @Override
    public void sentMessage(GameImmutable model, Message msg) throws RemoteException {
        try {
            out.writeObject(new msgSentMessage(model, msg));
            finishSending();
        } catch (IOException e) {
            System.err.println("Error occurred while writing to ObjectOutputStream: " + e.getMessage());
            throw new RemoteException("Failed to send SentMessage message", e);

        }
    }
}
