package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.model.DefaultValue;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.SymbolType;
import org.fusesource.jansi.Ansi;

import java.io.Serializable;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class ResourceCard extends PlayableCard implements Serializable {
   private ResourceType mainResource;
   private int victoryPoints;
   private int numResources;
   private List<ResourceType> resourceList;
   private boolean hasSymbol;
   private SymbolType symbol;

   @Override
   public ResourceType getMainResource() {
      return mainResource;
   }
   @Override
   public int getVictoryPoints() {
      return victoryPoints;
   }

   @Override
   public int getNumResources() {
      return numResources;
   }
   public List<ResourceType> getResourceList() {
      return resourceList;
   }

   public boolean hasSymbol() {
      return hasSymbol;
   }

   public SymbolType getSymbol() {
      return symbol;
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
      } else i = 0;

      // Angolo in alto a DX
      cornerContent.add(getCornerContentString(getTRCorner(), i));

      if (getTRCorner() == CornerLabel.WithResource) {
         i++;
      } else i = 0;

      // Angolo in basso a DX
      cornerContent.add(getCornerContentString(getBRCorner(), i));

      if (getBRCorner() == CornerLabel.WithResource) {
         i++;
      } else i = 0;

      // Angolo in basso a SX
      cornerContent.add(getCornerContentString(getBLCorner(), i));

      return cornerContent;
   }

   /**
    * Retrieves the string representation of the content of a corner.
    *
    * @param cornerLabel The label indicating the type of content in the corner.
    * @param i           An index used to retrieve the actual resource in case of 'WithResource' label.
    * @return The string representing the corner content.
    * @throws IllegalArgumentException If an invalid corner label is provided.
    */
   // Metodo per ottenere la stringa rappresentante il contenuto di un angolo
   public String getCornerContentString(CornerLabel cornerLabel, int i) {
       return switch (cornerLabel) {
           case Empty -> "Empty";
           case WithResource ->
               // Se l'angolo contiene una risorsa, restituisci la risorsa effettiva
                   resourceList.get(i).toString();
           case WithSymbol ->
               // Se l'angolo contiene un simbolo, restituisci il simbolo effettivo
                   symbol.toString();
           case NoCorner -> "NoCorner";
           default -> throw new IllegalArgumentException();
       };
   }

   public List<ResourceType> getCentralResources() {
      return null;
   }
   public int getNumCentralResources() {
      return 0;
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

  /* public ResourceCard() {
      super();
      this.victoryPoints = 0; //
      this.mainResource = null;
      this.numResources = 0;
      this.resourceList = new ArrayList<>();
      this.hasSymbol = false;
      this.symbol = null;
   }*/

   public ResourceCard(String cssLabel, int cardID, int numCorners, boolean isFront, CardType cardType, CornerLabel TLCorner, CornerLabel TRCorner, CornerLabel BRCorner, CornerLabel BLCorner, ResourceType mainResource, int victoryPoints, int numResources, List<ResourceType> resourceList, boolean hasSymbol, SymbolType symbol) {
      super(cssLabel, cardID, numCorners, isFront, cardType, TLCorner, TRCorner, BRCorner, BLCorner);
      this.mainResource = mainResource;
      this.victoryPoints = victoryPoints;
      this.numResources = numResources;
      this.resourceList = resourceList;
      this.hasSymbol = hasSymbol;
      this.symbol = symbol;
   }


   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      Ansi.Color bgColor;
      Ansi.Color textColor = Ansi.Color.WHITE;
      String FoB;
      if (isFront()) {
         FoB = "Front";
      } else {
         FoB = "Back";
      }

      // Cambia il colore della carta in base alla mainResource
      switch (mainResource) {
         case Fungi:
            bgColor = Ansi.Color.RED;
            break;
         case Insect:
            bgColor = Ansi.Color.MAGENTA;
            break;
         case Plant:
            bgColor = Ansi.Color.GREEN;
            break;
         case Animal:
            bgColor = Ansi.Color.BLUE;
            break;
         default:
            bgColor = Ansi.Color.DEFAULT;
      }

      String cardTypeName = "Resource";
      int points = victoryPoints;
      List<String> corners = getCornerContent();
      List<String> emojiCorners = new ArrayList<>();
      for (String corner : corners) {
         emojiCorners.add(convertToEmoji(corner));
      }

      // Costruzione delle righe del contenuto
      List<String> contentLines = new ArrayList<>();
      contentLines.add("CardType: " + cardTypeName);
      contentLines.add("Face: " + FoB);
      contentLines.add("Points: " + points);
      contentLines.add("Corners: " + String.join(" ", emojiCorners));

      // Trova la lunghezza massima delle linee di contenuto
      // Trova la lunghezza massima delle linee di contenuto
      int maxWidth = DefaultValue.printLenght;
        /*
        for (String line : contentLines) {
            maxWidth = Math.max(maxWidth, line.length());
        }

         */

      // Costruzione del bordo superiore
      String borderLine = "+" + "-".repeat(maxWidth + 2) + "+";
      result.append(borderLine).append("\n");

      // Costruzione delle linee di contenuto con bordi laterali


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


      return result.toString();
   }

}
