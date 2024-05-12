package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.SymbolType;
import org.fusesource.jansi.Ansi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class InitialCard extends PlayableCard implements Serializable {
    List<ResourceType> centralResources;
    int numCentralResource;
    int numResources;
    List<ResourceType> resourceList;

    public List<ResourceType> getCentralResources() {
        return centralResources;
    }

    public int getNumCentralResources() {
        return numCentralResource;
    }

    @Override
    public int getNumResources() {
        return numResources;
    }
    public SymbolType getSymbol() {
        return null;
    }
    @Override
    public int getVictoryPoints() {
        return 0;
    }

    @Override
    public List<ResourceType> getResourceList() {
        return resourceList;
    }
    @Override
    public boolean hasSymbol() {
        return false;
    }
    @Override
    public ResourceType getMainResource() {
        return null;
    }


    /**
     * Retrieves the content of the initial corners of the card as a list of strings.
     * Each string represents the content of a corner in the following order:
     * top-left, top-right, bottom-right, bottom-left.
     * @author Margherita Marino
     * @return A list containing the corner content strings.
     */
    @Override
    public List<String> getCornerContent() {
        List<String> cornerContent = new ArrayList<>();
        int i = 0;

        // Angolo in alto a SX
        cornerContent.add(getCornerContentString(getTLCorner(), i));

        if (getTLCorner() == CornerLabel.WithResource) {
            i++;
        }

        // Angolo in alto a DX
        cornerContent.add(getCornerContentString(getTRCorner(), i));

        if (getTRCorner() == CornerLabel.WithResource) {
            i++;
        };

        // Angolo in basso a DX
        cornerContent.add(getCornerContentString(getBRCorner(), i));

        if (getBRCorner() == CornerLabel.WithResource) {
            i++;
        };
        // Angolo in basso a SX
        cornerContent.add(getCornerContentString(getBLCorner(), i));

        return cornerContent;
    }



    /**
     * Retrieves the string representation of the content of a corner.
     * @param cornerLabel The label indicating the type of content in the corner.
     * @param i           An index used to retrieve the actual resource in case of 'WithResource' label.
     * @return The string representing the corner content.
     * @throws IllegalArgumentException If an invalid corner label is provided.
     */
    // Metodo per ottenere la stringa rappresentante il contenuto di un angolo
    public String getCornerContentString(CornerLabel cornerLabel, int i) {
        switch (cornerLabel) {
            case Empty:
                return "Empty";
            case WithResource:
                // Se l'angolo contiene una risorsa, restituisci la risorsa effettiva
                return resourceList.get(i).toString();
            case NoCorner:
                return "NoCorner";
            default:
                throw new IllegalArgumentException();
        }
    }


    public List<ResourceType> getPlacementCondition() {
        return null;
    }
    public boolean isPointsCondition() {
        return false;
    }
    public boolean isCornerCondition() {
        return false;
    }
    public SymbolType getSymbolCondition() {
        return null;
    }

    public InitialCard() {
        super();
        this.resourceList = new ArrayList<ResourceType>();
        this.numCentralResource = 0;
        this.numResources = 0;
        this.centralResources = new ArrayList<>();
    }

    public InitialCard(int cardID, int numCorners, boolean isFront, CardType cardType, CornerLabel TLCorner, CornerLabel TRCorner, CornerLabel BRCorner, CornerLabel BLCorner, List<ResourceType> centralResources, int numCentralResource, int numResources, List<ResourceType> resourceList) {
        super(cardID, numCorners, isFront, cardType, TLCorner, TRCorner, BRCorner, BLCorner);
        this.centralResources = centralResources;
        this.numCentralResource = numCentralResource;
        this.numResources = numResources;
        this.resourceList = resourceList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Ansi.Color bgColor = Ansi.Color.YELLOW;
        Ansi.Color textColor = Ansi.Color.WHITE;
        String cardTypeName = "Initial";

        List<String> corners = getCornerContent();
        List<ResourceType> centralR = getCentralResources();

        // Costruzione del risultato con colori e nome della carta
        result.append(ansi().fg(textColor).bg(bgColor).a(" "));
        result.append(cardTypeName);
        result.append(" ");
        result.append(ansi().fg(Ansi.Color.DEFAULT).bg(Ansi.Color.DEFAULT));
        String formattedCorners = String.join(" ", corners);
        result.append(formattedCorners);
        result.append(" ");
        // Aggiungi gli elementi di conditionList formattati con spazi tra di loro
        for (int i = 0; i < centralR.size(); i++) {
            result.append(centralR.get(i));
            // Aggiungi uno spazio solo se non è l'ultimo elemento della lista
            if (i < centralR.size() - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}
