package it.polimi.ingsw.view.Utilities;

import it.polimi.ingsw.model.DefaultValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * FileDisconnection class
 * FileDisconnection is the class that manages the disconnection of the player
 * It reads and writes the disconnection state from and to a file using json (JSONParser)
 */
public class FileDisconnection {
    private final String path;

    /**
     * Init class
     */
    public FileDisconnection() {
        path = System.getProperty("user.home") + "/AppData/Roaming/.CodexNaturalis";
    }

    /**
     * Returns the game id from the file
     * @param nickname The player's nickname.
     * @return game ID stored in the file, or -1 if no valid game ID is found or if the file couldn't be read.
     */
    public int getGameId(String nickname) {
        //game data related to the player is stored in a json file named after the nickname the player had in that game
        String gameId = null;
        String time = null;
        JSONParser parser = new JSONParser();
        File file = new File(path + "/" + nickname + ".json");

        try (InputStream is = new FileInputStream(file);
             Reader reader = new InputStreamReader(Objects.requireNonNull(is, "Couldn't find json file"), StandardCharsets.UTF_8)) {
            JSONObject obj = (JSONObject) parser.parse(reader);
            gameId = (String) obj.get(DefaultValue.gameIdData);
            time = (String) obj.get(DefaultValue.gameIdTime);
        } catch (ParseException | IOException ex) {
            return -1;
        }
        assert gameId != null;
        if (LocalDateTime.parse(time).isBefore(LocalDateTime.now().plusSeconds(DefaultValue.twelveHS)))
            return Integer.parseInt(gameId);
        else
            return -1;
    }

    /**
     * Creates or updates the file with the latest game ID associated with the player's nickname.
     *
     * @param nickname The player's nickname.
     * @param gameId   The latest game ID to be stored in the file.
     */
    @SuppressWarnings("unchecked")
    public void setLastGameId(String nickname, int gameId) {
        JSONObject data = new JSONObject();
        data.put(DefaultValue.gameIdData, Integer.toString(gameId));
        data.put(DefaultValue.gameIdTime, LocalDateTime.now().toString());

        // Create the directory if it doesn't exist
        new File(path).mkdirs();

        File file = new File(path + "/" + nickname + ".json");
        try {
            // Create the file if it doesn't exist
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (FileWriter fileWriter = new FileWriter(path + "/" + nickname + ".json")) {
            fileWriter.write(data.toJSONString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

