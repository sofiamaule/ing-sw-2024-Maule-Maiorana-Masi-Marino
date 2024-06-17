package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.*;
import org.fusesource.jansi.Ansi;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Objective card class
 */
public class ObjectiveCard implements Serializable {
    private int cardID;
    private boolean isFront;
    private GoalType goalType;
    private int victoryPoints;
    private ResourceType mainResource;
    private CornerType direction;
    private int numResources;
    private int numSymbols;
    private List<SymbolType> symbols;
    private ResourceType secondResource;

    /**
     * this method is used for the GUI
     * @return the path for the specific istance of Objective Card (contained in the package resources-->img)
     */
    public String getImagePath(){
        String path;
        int idTemp= this.getCardID();

        if(this.isFront()){
            path="/img/Cards/ObjectiveCards/"+ idTemp+ "_ObjectiveFront.png";

        } else {
            path="/img/Cards/ObjectiveCards/ObjectiveBack.png";
        }
        return Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
    }

    public int getCardID() {
        return cardID;
    }

    public boolean isFront() {
        return isFront;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getNumSymbols() {
        return numSymbols;
    }

    public List<SymbolType> getSymbols() {
        return symbols;
    }

    public int getNumResources() {
        return numResources;
    }

    public ResourceType getMainResource() {
        return mainResource;
    }

    public CornerType getDirection() {
        return direction;
    }

    public ResourceType getSecondResource() {
        return secondResource;
    }


    public ObjectiveCard(int cardID, boolean isFront, GoalType goalType, int victoryPoints, ResourceType mainResource, CornerType direction, int numResources, int numSymbols, List<SymbolType> symbols, ResourceType secondResource) {
        this.cardID = cardID;
        this.isFront = isFront;
        this.goalType = goalType;
        this.victoryPoints = victoryPoints;
        this.numSymbols = numSymbols;
        this.symbols = symbols;
        this.numResources = numResources;
        this.mainResource = mainResource;
        this.direction = direction;
        this.secondResource = secondResource;
    }

    /**
     * @return a copy of the current objective Card
     */
    public ObjectiveCard copy(){
        // Creo una nuova istanza di ObjectiveCard con gli stessi valori dei campi
        ObjectiveCard copiedCard = new ObjectiveCard(
                this.cardID,
                this.isFront,
                this.goalType,
                this.victoryPoints,
                this.mainResource,
                this.direction,
                this.numResources,
                this.numSymbols,
                new ArrayList<>(this.symbols), // Creo una copia della lista di simboli
                this.secondResource
        );
        return copiedCard;
    }

    /**
     * Provides a string representation of the {@code ObjectiveCard} for display purposes.
     * This includes details such as points and conditions.
     *
     * @return A formatted string representing the card.
     */
   /* @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Ansi.Color bgColor = Ansi.Color.DEFAULT;
        Ansi.Color textColor = Ansi.Color.WHITE;
        String condition = " ";
        List<String> emojiSymbol = new ArrayList<>();

        switch(goalType) {
            case ResourceCondition:
                changeColor();
                condition = convertToEmoji(mainResource.toString());
                break;
            case SymbolCondition:
                bgColor = Ansi.Color.YELLOW;
                List<SymbolType> symbols = getSymbols();
                for (SymbolType s : symbols) {
                    emojiSymbol.add(convertToEmoji(s.toString()));
                }
                break;
            case DiagonalPlacement, LPlacement:
                changeColor();
                condition = direction.toString();
                break;
            default:
                bgColor = Ansi.Color.DEFAULT;
        }

        String cardTypeName = "Objective";
        int points = victoryPoints;

        // Costruzione delle righe del contenuto
        List<String> contentLines = new ArrayList<>();
        contentLines.add("CardType: " + cardTypeName);
        contentLines.add("Points: " + points);
        contentLines.add("CondType: " + goalType.toString());

        StringBuilder conditionLine = new StringBuilder("Cond: " + condition);
        for (int i = 0; i < emojiSymbol.size(); i++) {
            conditionLine.append(emojiSymbol.get(i));
            if (i < emojiSymbol.size() - 1) {
                conditionLine.append(" ");
            }
        }
        contentLines.add(conditionLine.toString());


        int maxWidth = DefaultValue.printLenght;

        // Costruzione del bordo superiore
        String borderLine = "+" + "-".repeat(maxWidth + 2) + "+";
        result.append(borderLine).append("\n");

        for (String line : contentLines) {
            result.append("| ").append(line);
            // Aggiungi spazi per allineare al massimo
            result.append(" ".repeat(maxWidth - line.length()));
            result.append(" |\n");
        }
        for(int i=contentLines.size(); i< DefaultValue.printHeight; i++){
            result.append("| ");
            // Aggiungi spazi per allineare al massimo
            result.append(" ".repeat(maxWidth));
            result.append(" |\n");
        }

        // Costruzione del bordo inferiore
        result.append(borderLine);

        // Applicazione del colore
        String finalResult = ansi().fg(textColor).bg(bgColor).a(result.toString()).reset().toString();

        return finalResult;
    }
*/
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Ansi.Color bgColor = Ansi.Color.DEFAULT;
        Ansi.Color textColor = Ansi.Color.WHITE;
        String condition = " ";
        List<String> emojiSymbol = new ArrayList<>();

        switch(goalType) {
            case ResourceCondition:
                changeColor();
                condition = convertToEmoji(mainResource.toString());
                break;
            case SymbolCondition:
                bgColor = Ansi.Color.YELLOW;
                List<SymbolType> symbols = getSymbols();
                for (SymbolType s : symbols) {
                    emojiSymbol.add(convertToEmoji(s.toString()));
                }
                break;
            case DiagonalPlacement, LPlacement:
                changeColor();
                condition = direction.toString();
                break;
            default:
                bgColor = Ansi.Color.DEFAULT;
        }


        String cardTypeName = "Objective";
        String pointsLine = "Points: [" + victoryPoints + "]";
        String goalLine = "Goal: " + goalType.toString();

        // Costruzione delle righe del contenuto
        List<String> contentLines = new ArrayList<>();
        contentLines.add(cardTypeName);
        contentLines.add(pointsLine);
        contentLines.add(goalLine);

        StringBuilder conditionLine = new StringBuilder(condition);
        for (String symbol : emojiSymbol) {
            conditionLine.append(" ").append(symbol);
        }
        contentLines.add(conditionLine.toString());

        // Calcola la larghezza massima
        int maxWidth = DefaultValue.printLenght;
        for (String line : contentLines) {
            maxWidth = Math.max(maxWidth, line.length());
        }
        maxWidth += 4; // Aggiungiamo spazio per i bordi "|  |"

        // Calcola l'altezza massima
        int cardHeight = DefaultValue.printHeight;

        // Costruzione del bordo superiore
        String borderLine = "+" + "-".repeat(maxWidth - 2) + "+";
        result.append(borderLine).append("\n");

        // Aggiungi righe di contenuto
        for (String line : contentLines) {
            int padding = Math.max(0, (maxWidth - line.length() - 4) / 2); // -4 per "|  |"
            result.append("| ")
                    .append(" ".repeat(padding))
                    .append(line)
                    .append(" ".repeat(maxWidth - padding - line.length() - 4))
                    .append(" |\n");
        }

        // Aggiungi righe vuote fino a raggiungere l'altezza desiderata della carta
        for (int i = contentLines.size(); i < cardHeight - 1; i++) {
            result.append("|").append(" ".repeat(maxWidth)).append("|\n");
        }

        // Costruzione del bordo inferiore
        result.append(borderLine).append("\n");

        // Applicazione del colore
        String finalResult = ansi().fg(textColor).bg(bgColor).a(result.toString()).reset().toString();

        return finalResult;
    }



    public void changeColor(){
        Ansi.Color bgColor = switch (mainResource) {
            case Fungi -> Ansi.Color.RED;
            case Insect -> Ansi.Color.MAGENTA;
            case Plant -> Ansi.Color.GREEN;
            case Animal -> Ansi.Color.BLUE;
            default -> Ansi.Color.DEFAULT;
        };
        //cambia il colore della carta in base alla mainResource
    }

    public String convertToEmoji(String input){
        String output;
        if(input.equals("Fungi")){
            output =  "\uD83C\uDF44";
        }else if(input.equals("Animal")){
            output = "\uD83D\uDC3A";
        }else if(input.equals("Insect")){
            output = "\uD83E\uDD8B";
        }else if(input.equals("Plant")){
            output = "\uD83C\uDF40";
        }else if(input.equals("NoCorner")){
            output = "\u274C";
        }else if(input.equals("Ink")){
            output = "\u26AB";
        }else if(input.equals("Manuscript")){
            output= "\uD83D\uDCDC";
        }else if(input.equals("Quill")){
            output = "\uD83E\uDEB6";
        }else output = input;
        return output;
    }

}
