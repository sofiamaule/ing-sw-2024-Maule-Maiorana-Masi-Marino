package it.polimi.ingsw.network.socket.client.gameControllerMessages;

import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.listener.GameListenerInterface;
import it.polimi.ingsw.network.rmi.GameControllerInterface;
import it.polimi.ingsw.network.rmi.MainControllerInterface;
import it.polimi.ingsw.network.socket.client.SocketClientGenericMessage;

import java.rmi.RemoteException;

public class SocketClientMessageHeartBeat extends SocketClientGenericMessage {
    public SocketClientMessageHeartBeat(String nick) {
        this.nickname = nickname;
        this.isMessageForMainController = false;
        this.isHeartbeat=true;
    }


    @Override
    public GameControllerInterface execute(GameListenerInterface lis, MainControllerInterface mainController) throws RemoteException {
        return null;
    }


    @Override
    public void execute(GameControllerInterface gameController) throws RemoteException, GameEndedException {
        return;
    }
}
