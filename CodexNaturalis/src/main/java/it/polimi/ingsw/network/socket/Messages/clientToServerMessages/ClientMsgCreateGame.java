package it.polimi.ingsw.network.socket.Messages.clientToServerMessages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.network.rmi.GameControllerInterface;

import java.rmi.RemoteException;

public class ClientMsgCreateGame extends ClientGenericMessage {
    int numPlayers;
    int GameID;

    /**
     * Constructor of the class.
     * @param nickname the player's nickname
     */
    public ClientMsgCreateGame(int numPlayers, int GameID, String nickname) {
        this.numPlayers= numPlayers;
        this.GameID=GameID;
        this.nickname = nickname;
        this.isJoinGame= true;
    }

    /**
     * Executes the corresponding action for the message.
     * @param lis the game listener
     * @param gameController the main controller interface
     * @throws RemoteException if there is a remote exception
     */
    @Override
    public void execute(GameListenerInterface lis, GameController gameController) throws RemoteException {
        gameController.settingGame(lis,numPlayers, GameID, this.nickname);
    }

    /**
     * Executes the corresponding action for the message.
     * @param gameController the game controller interface
     * @throws RemoteException if there is a remote exception
     * @throws GameEndedException if the game has ended
     */
    @Override
    public void execute(GameControllerInterface gameController) throws RemoteException, GameEndedException {

    }


}


