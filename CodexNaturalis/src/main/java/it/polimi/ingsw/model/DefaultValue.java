package it.polimi.ingsw.model;
import java.io.Serializable;

/**
 * This class contains all the default values we need in the project
 */
public class DefaultValue implements Serializable {

    public final static boolean DEBUG = false;

    public final static int MaxNumOfPlayer = 4;
    public final static int minNumOfPlayer = 2;

    public final static int NumOfGoldCards = 40;
    public final static int NumOfResourceCards = 40;
    public final static int NumOfInitialCards = 6;
    public final static int NumOfObjectiveCards = 16;

    public final static int NumOfColumnsBook = 70;
    public final static int NumOfRowsBook = 70;

    //public final static int Default_port_RMI = 4321;
    //public final static int Default_port_Socket = 4320;
    //public static String serverIp = "127.0.0.1";
    //public final static String Remote_ip = "127.0.0.1";

    public final static String Default_servername_RMI = "CodexNaturalis";

    //aggiungiamo tutti i valori ROW/COL che specificano in quale parte
    //dell'INTERFACCIA UTENTE appariranno le INFO di ciascuna classe e i messaggi


}
