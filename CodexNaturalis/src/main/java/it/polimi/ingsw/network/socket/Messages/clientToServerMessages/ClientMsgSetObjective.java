package it.polimi.ingsw.network.socket.Messages.clientToServerMessages;

import it.polimi.ingsw.exceptions.NotPlayerTurnException;
import it.polimi.ingsw.network.rmi.GameControllerInterface;

import java.rmi.RemoteException;

public class ClientMsgSetObjective extends ClientGenericMessage {
    int index;

    public ClientMsgSetObjective(String nickname, int index) {
        this.nickname = nickname;
        this.index = index;
    }


    /**
     * Method to execute the corresponding action for the message.
     * @param gameController the game controller interface
     * @throws RemoteException if there is an error in remote communication
     */
    @Override
    public void execute(GameControllerInterface gameController) throws RemoteException, NotPlayerTurnException {
        gameController.setGoalCard(this.nickname, this.index);
    }
}
