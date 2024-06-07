package it.polimi.ingsw.view.GUI.controllers;


import it.polimi.ingsw.Chat.Message;
import it.polimi.ingsw.exceptions.CellNotAvailableException;
import it.polimi.ingsw.exceptions.PlacementConditionViolated;
import it.polimi.ingsw.model.Book;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.game.GameImmutable;
import it.polimi.ingsw.model.player.PlayerDeck;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.GUIApplication;
import it.polimi.ingsw.view.GUI.scenes.SceneType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class MainSceneController extends ControllerGUI{



    @FXML
    private Text GameIDTextField;
    @FXML
    private Text nicknameTextField;

    //CHAT
    @FXML
    private Text labelMessage;
    @FXML
    private TextField messageText;
    @FXML
    private ListView chatList;
    @FXML
    private ComboBox<String> boxMessage;
    @FXML
    private ImageView actionSendMessage;

    @FXML
    private ImageView initialCardImg;

    //PLAYER DECK
    @FXML
    private PlayerDeck playerDeck;
    private boolean[] showBack = new boolean[3];
    @FXML
    private ImageView deckImg0;
    @FXML
    private ImageView deckImg1;
    @FXML
    private ImageView deckImg2;
    @FXML
    private Button turnbtn0;
    @FXML
    private Button turnbtn1;
    @FXML
    private Button turnbtn2;
    @FXML
    private ImageView personalObjective;

    @FXML
    private Button showScoretrackBtn;
    @FXML
    private Button showBoardBtn;
    private GUI gui;
    private GameImmutable model;

    //EVENTS
    @FXML
    private ListView EventsList;
    @FXML
    private AnchorPane PlayerDeckPane;
    @FXML
    private Stage boardPopUpStage;


    public void setImportantEvents(List<String> importantEvents){
        for (String s : importantEvents) {
            EventsList.getItems().add(s);
        }
        EventsList.scrollTo(EventsList.getItems().size());
    }
    public void setNicknameAndID(GameImmutable model, String nickname) {
        GameIDTextField.setText("GameID: "+model.getGameId());
        nicknameTextField.setText("Nickname: "+nickname );
    }

    public void setPlayerComboBoxItems(List<String> playerNames) {
        // Rimuovi eventuali elementi precedenti dalla ComboBox
        boxMessage.getItems().clear();
        // Aggiungi l'opzione per inviare un messaggio a tutti
        boxMessage.getItems().add("All players");

        // Aggiungi i nomi dei giocatori diversi dal tuo alla ComboBox
        for (String playerName : playerNames) {
            if (!playerName.equals(nicknameTextField)) {
                boxMessage.getItems().add(playerName);
            }
        }
        // Seleziona l'opzione "All players" come predefinita
        boxMessage.getSelectionModel().selectFirst();
    }

    /**
     * This method manage the sending of the message
     *
     * @param e the mouse event
     */
    public void actionSendMessage(MouseEvent e) {
        if (e == null || e.getSource() == actionSendMessage) {
            if (!messageText.getText().isEmpty()) {
                String recipient = boxMessage.getValue();

                if (recipient.equals("All players")) {
                    getInputGUI().addTxt("/c " + messageText.getText()); // Invia a tutti
                } else {
                    getInputGUI().addTxt("/cs " + recipient + " " + messageText.getText()); // Invia al giocatore selezionato
                }

                messageText.setText("");
            }
        }
    }

    public void handleKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            actionSendMessage(null);
        }
    }

    /**
     * This method manage the click on the button to send the message
     *
     * @param ke the key event
     */
    public void chatOnClick(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            actionSendMessage(null);
        }
    }

    /**
     * This method set the color of a message
     * @param msg the message to show
     * @param success if the message is a success or not
     */
    public void setMsgToShow(String msg, Boolean success) {
        labelMessage.setText(msg);
        if (success == null) {
            labelMessage.setFill(Color.BLACK);
        } else if (success) {
            labelMessage.setFill(Color.GREEN);
        } else {
            labelMessage.setFill(Color.RED);
        }
    }

    /**
     * This method set the message in the correct format
     * @param msgs the list of messages
     * @param myNickname the nickname of the player
     */
    public void setMessage(List<Message> msgs, String myNickname) {
        chatList.getItems().clear();
        for (Message m : msgs) {
            String r = "[" + m.getTime().getHour() + ":" + m.getTime().getMinute() + ":" + m.getTime().getSecond() + "] " + m.getSender().getNickname() + ": " + m.getText();

            if (m.whoIsReceiver().equals("*")) {
                chatList.getItems().add(r);
            } else if (m.whoIsReceiver().toUpperCase().equals(myNickname.toUpperCase()) || m.getSender().getNickname().toUpperCase().equals(myNickname.toUpperCase())) {
                //Message private
                chatList.getItems().add("[Private] " + r);
            }
        }
    }




    //PLAYERDECK
    public void setPlayerDeck(GameImmutable model, String nickname) {
        this.playerDeck= model.getPlayerByNickname(nickname).getPlayerDeck();
        String imagePath;

        imagePath=playerDeck.getMiniDeck().getFirst().getImagePath();
        Image image = new Image(imagePath);
        deckImg0.setImage(image);
        imagePath=playerDeck.getMiniDeck().get(2).getImagePath();
        image = new Image(imagePath);
        deckImg1.setImage(image);
        imagePath=playerDeck.getMiniDeck().get(4).getImagePath();
         image = new Image(imagePath);
        deckImg2.setImage(image);
        // Initialize card state to false (showing initial images)
        showBack[0] = false;
        showBack[1] = false;
        showBack[2] = false;

    }
    public void turnCard(ActionEvent e) {
        if (e.getSource() == turnbtn0) {
            toggleCard(0, 0, 1, deckImg0);
        } else if (e.getSource() == turnbtn1) {
            toggleCard(1, 2, 3, deckImg1);
        } else if (e.getSource() == turnbtn2) {
            toggleCard(2, 4, 5, deckImg2);
        }
    }

    private void toggleCard(int cardIndex, int initialImageIndex, int alternateImageIndex, ImageView deckImg) {
        String imagePath;
        if (showBack[cardIndex]) {
            imagePath = playerDeck.getMiniDeck().get(initialImageIndex).getImagePath();
        } else {
            imagePath = playerDeck.getMiniDeck().get(alternateImageIndex).getImagePath();
        }
        showBack[cardIndex] = !showBack[cardIndex];
        deckImg.setImage(new Image(imagePath));
    }

    public void enlargeAndHighlightBoardPane() {
        try {
            // Carica il file FXML della board
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it.polimi.ingsw.view.GUI/fxml/BoardPopUp.fxml"));
            Parent root = loader.load();

            // Ottieni il controller della BoardPopUp e imposta il modello
            BoardPopUpController controller = loader.getController();
            controller.setBoard(model);

            // Crea una nuova finestra per il pop-up
            boardPopUpStage = new Stage();
            boardPopUpStage.setScene(new Scene(root));
            boardPopUpStage.initStyle(StageStyle.UNDECORATED); // Rimuovi i bordi della finestra
            boardPopUpStage.show();

            // Applica l'effetto di illuminazione
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(20.0);
            dropShadow.setOffsetX(0.0);
            dropShadow.setOffsetY(0.0);
            dropShadow.setColor(Color.BLUE);
            root.setEffect(dropShadow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeBoardPopUp() {
        if (boardPopUpStage != null) {
            boardPopUpStage.close();
            boardPopUpStage = null;
        }
    }

    private boolean placeCardTurn=false;
    public void enlargeAndHighlightPlayerDeckPane() {
        placeCardTurn=true;
        // Ingrandire il pane
        PlayerDeckPane.setScaleX(1.2);
        PlayerDeckPane.setScaleY(1.2);

        // Applicare l'effetto di illuminazione
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setColor(Color.RED);
        PlayerDeckPane.setEffect(dropShadow);
    }

    public void resetPlayerDeckPane() {
        // Resettare la scala
        PlayerDeckPane.setScaleX(1.0);
        PlayerDeckPane.setScaleY(1.0);
        // Rimuovere l'effetto di illuminazione
        PlayerDeckPane.setEffect(null);
    }

    public void chooseCardClick(MouseEvent mouseEvent) {
        if (placeCardTurn) {
            int selectedIndex = -1;
            ImageView clickedImageView = null;

            if (mouseEvent.getSource() == deckImg0) {
                selectedIndex = showBack[0] ? 1 : 0;
                clickedImageView = deckImg0;
            } else if (mouseEvent.getSource() == deckImg1) {
                selectedIndex = showBack[1] ? 3 : 2;
                clickedImageView = deckImg1;
            } else if (mouseEvent.getSource() == deckImg2) {
                selectedIndex = showBack[2] ? 5 : 4;
                clickedImageView = deckImg2;
            }

            if (selectedIndex != -1) {
                getInputGUI().addTxt(String.valueOf(selectedIndex)); // Passa l'indice come stringa
                if (clickedImageView != null) {
                    clickedImageView.setImage(null); // Svuota l'immagine
                }
            }
        }
        placeCardTurn = false;
    }


//PLAYER GOAL
    public void setPersonalObjective(GameImmutable model, String nickname) {
        String imagePath = model.getPlayerGoalByNickname(nickname).getImagePath();
        Image image = new Image(imagePath);
        personalObjective.setImage(image);

    }
    // Metodo per impostare l'istanza di GUIApplication
    public void setGUI(GUI gui, GameImmutable model) {
        this.gui = gui;
        this.model =model;
    }
    public void showBoardPopUp(ActionEvent e){
        if (gui != null) {
            gui.show_board(this.model);

        }
    }

    public void showScoretrackPopUp(ActionEvent e){
        if (gui != null) {
            gui.show_scoretrack(model);
        }
    }




    //BOOK

    @FXML
    public ScrollPane gameBoardScrollPane;

    @FXML
    public AnchorPane gameBoardPane;
    @FXML
    public AnchorPane rootPane;

    private int numCells = 70;
    private ImageView[][] imageViews;
    private int cellSize = 50; // Dimensione delle celle delle ImageView

    public void setBook(GameImmutable model, String nickname) {
        Cell[][] bookMatrix = model.getPlayerByNickname(nickname).getPlayerBook().getBookMatrix();

        // Inizializza la matrice di ImageView
        imageViews = new ImageView[numCells][numCells];
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(cellSize); // Imposta la larghezza desiderata per le ImageView
                imageView.setFitHeight(cellSize); // Imposta l'altezza desiderata per le ImageView
                imageViews[i][j] = imageView;
                gameBoardPane.getChildren().add(imageView); // Aggiungi ImageView al AnchorPane
                imageView.setLayoutX(j * cellSize); // Imposta la posizione X in base alla colonna
                imageView.setLayoutY(i * cellSize); // Imposta la posizione Y in base alla riga
            }
        }

        // Carica l'immagine per le carte non nulle
        // Supponiamo che tu abbia un'array di Image chiamato cardImages che contiene le immagini delle carte
        // e che bookMatrix sia la tua matrice di carte
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                if (bookMatrix[i][j].getCard() != null) {
                    PlayableCard card = bookMatrix[i][j].getCard();
                    // Assicurati di avere un'immagine associata a ogni tipo di carta
                    Image cardImage = new Image(card.getImagePath());
                    imageViews[i][j].setImage(cardImage);
                } else {
                    // Carica l'immagine per le carte nulle (ImageView vuoto)
                    imageViews[i][j].setImage(null);
                }
            }
        }
    }

}
