package it.polimi.ingsw.model;
import java.util.*;

public class ResourceCard extends PlayableCard {
   private ResourceType mainResource;
   private int victoryPoints;
   private int numResources;
   private List<ResourceType> resourceList;
   private boolean hasSymbol;
   private SymbolType symbol;

   public ResourceType getMainResource() {
      return mainResource;
   }

   public int getVictoryPoints() {
      return victoryPoints;
   }

   public int getNumResources() {
      return numResources;
   }

   public List<ResourceType> getResourceList() {
      return resourceList;
   }

   public boolean isHasSymbol() {
      return hasSymbol;
   }

   public SymbolType getSymbol() {
      return symbol;
   }



   public List<String> getResourceCornerContent() {
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

   // Metodo per ottenere la stringa rappresentante il contenuto di un angolo
   private String getCornerContentString(CornerLabel cornerLabel, int i) {
      switch (cornerLabel) {
         case Empty:
            return "Empty";
         case WithResource:
            // Se l'angolo contiene una risorsa, restituisci la risorsa effettiva
           resourceList.get(i).toString();
         case WithSymbol:
            // Se l'angolo contiene un simbolo, restituisci il simbolo effettivo
            return symbol.toString();
         case NoCorner:
            return "NoCorner";
         default:
            throw new IllegalArgumentException();
      }
   }

}
