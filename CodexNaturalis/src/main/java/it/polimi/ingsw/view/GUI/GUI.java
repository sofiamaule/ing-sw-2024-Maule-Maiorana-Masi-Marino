package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.exceptions.FileReadException;
import it.polimi.ingsw.model.DefaultValue;
import it.polimi.ingsw.model.game.GameImmutable;
import it.polimi.ingsw.view.GUI.scenes.SceneType;
import it.polimi.ingsw.view.Utilities.InputGUI;
import it.polimi.ingsw.view.Utilities.UI;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GUI class.
 * This class is the GUI implementation of the UI abstract class and it manages all the GUI-related operations.
 */
public class GUI extends UI {

    private GUIApplication guiApplication;
    private InputGUI inputGUI;

    private String nickname;
    boolean showedPublisher = false;

    /**
     * Constructor of the class.
     *
     * @param guiApplication the GUI application
     * @param inputGUI the input reader
     */
    public GUI(GUIApplication guiApplication, InputGUI inputGUI) {
        this.guiApplication = guiApplication;
        this.inputGUI = inputGUI;
        nickname = null;
        init();
    }


    @Override
    public void init() {
        eventsToShow = new ArrayList<>();
    }

    public void callPlatformRunLater(Runnable r) {
        //Need to use this method to call any methods inside the GuiApplication
        //Doing so, the method requested will be executed on the JavaFX Thread (else exception)
        Platform.runLater(r);
    }

    @Override
    public void show_publisher() throws IOException, InterruptedException {
        callPlatformRunLater(() -> this.guiApplication.setActiveScene(SceneType.PUBLISHER));

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(DefaultValue.time_publisher_showing_seconds));
        pauseTransition.setOnFinished(event -> {
            showedPublisher = true; //serve un booleano per evitare che venga mostrato nuovamente in futuro

            this.show_insertNicknameMessage();
        });
    }


    @Override
    public void show_insertNicknameMessage() {
        callPlatformRunLater(() -> this.guiApplication.setActiveScene(SceneType.NICKNAME));
        this.show_chosenNickname(nickname);
    }

    @Override
    public void show_chosenNickname(String nickname) {
        //TODO
    }

    @Override
    public void show_CurrentTurnMsg() {
        //TODO
    }


    //TODO

    @Override
    public void show_askPlaceCardsMainMsg(GameImmutable model) {

    }

    @Override
    public void show_PickCardMsg(GameImmutable model) {

    }

    @Override
    public void show_askCardType(GameImmutable model, String nickname) {

    }

    @Override
    public void show_alwaysShow(GameImmutable model, String nickname) {

    }

    @Override
    public void show_alwaysShowForAll(GameImmutable model) {

    }

    @Override
    public void show_askWhichCellMsg(GameImmutable model) {

    }

    @Override
    public void show_cardPlacedMsg(GameImmutable model) {

    }

    @Override
    public void show_cardDrawnMsg(GameImmutable model) {

    }

    @Override
    public void show_nextTurnMsg(GameImmutable model) {

    }

    @Override
    public void show_pointsAddedMsg(GameImmutable model) {

    }

    @Override
    public void show_joiningToGameMsg(String nick) {

    }


    @Override
    public void show_gameStarted(GameImmutable model) {

    }

    @Override
    public void show_gameEnded(GameImmutable model) {

    }

    @Override
    public void show_askNumPlayersMessage() {

    }

    @Override
    public void show_askGameIDMessage() {

    }

    @Override
    public void show_notValidMessage() {

    }

    @Override
    public void show_playerJoined(GameImmutable gameModel, String nick) {

    }

    @Override
    public void show_allPlayers(GameImmutable model) {

    }

    @Override
    public void show_youAreReady(GameImmutable model) {

    }


    @Override
    public void show_readyToStart(GameImmutable gameModel, String nickname) {

    }

    @Override
    public void show_returnToMenuMsg() {

    }

    @Override
    public void show_whichInitialCards() {

    }

    @Override
    public void show_wrongCardSelMsg() {

    }

    @Override
    public void show_wrongCellSelMsg() {

    }

    @Override
    public void show_whichObjectiveCards() {

    }

    @Override
    public void show_askNum(String msg, GameImmutable gameModel, String nickname) {

    }

    @Override
    public void addImportantEvent(String input) {

    }

    @Override
    public void resetImportantEvents() {

    }

    @Override
    public void show_askDrawFromDeck(GameImmutable model, String nickname) {

    }

    @Override
    public void show_playerHasToChooseAgain(GameImmutable model, String nickname) {

    }

    @Override
    public void show_wrongSelectionMsg() {

    }

    @Override
    public void show_temporaryInitialCards(GameImmutable model) throws FileNotFoundException, FileReadException {

    }

    @Override
    public void show_ObjectiveCards(GameImmutable model) {

    }

    @Override
    public void show_personalObjective() {

    }

    @Override
    public void show_noConnectionError() {

    }

    @Override
    public void show_playerDeck(GameImmutable model, String nickname) {

    }

    @Override
    public void show_WaitTurnMsg(GameImmutable model, String nickname) {

    }
}
