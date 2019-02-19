import com.google.gson.Gson;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Main function that handles gameplay
 */
public class Main {

    /** The json file as a string. */
    private static String jsonString = "";

    /** User input scanner. */
    private static Scanner userIn = new Scanner(System.in);

    /** The current map. */
    private static Map map;

    /** The Room object of the room the player is currently in. */
    private static Room myRoom;

    /** The url of the current json to be used, default set here. */
    private static String urlString = "https://courses.engr.illinois.edu/cs126/adventure/siebel.json";

    /** all items being carried. */
    private static ArrayList<Item> itemBag = new ArrayList<>();

    /** A list of all players in the current game*/
    private static ArrayList<Player> players = new ArrayList<>();

    /** A map of the room each player is in */
    private static HashMap<Player, Room> playerRoom = new HashMap<>();

    /** Counts how many moves each player has taken */
    private static HashMap<Player, Integer> playCount = new HashMap<>();


    /**
     * Main function that starts the game, or allows new url to be input.
     * @param args unused
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            urlString = args[0];
        }
        setup();
    }

    /**
     * Initial loading of game, waiting for user to start
     */
    private static void setup() {
        System.out.println("Start Game? Choose N to change map Y/N ");
        if (userIn.nextLine().toLowerCase().equals("y")) {

            System.out.println("Number of bots: ");
            int botCount = userIn.nextInt();

            for (int i = 0; i < botCount + 1; i++) {
                players.add(new Player(false, "Bot " + (i + 1)));
            }

            System.out.println("Number of players : ");
            botCount = userIn.nextInt();
            for (int i = 0; i < botCount + 1; i++) {
                System.out.println("Name for this player: ");
                players.add(new Player(true, userIn.nextLine()));
            }

            getInfo();
            Gson gson = new Gson();
            map = gson.fromJson(jsonString, Map.class);
            myRoom = map.getStartingRoomObj();

            // Put each player in the playerRoom map with the starting room
            for (Player play : players) {
                playerRoom.put(play, myRoom);
                playCount.put(play, 0);
            }

            displayPaths(myRoom, players.get(0));
        } else {
            System.out.println("Enter a different map url or 'exit' to quit:");
            String input = userIn.nextLine();

            if (input.equals("exit")) {
                System.exit(0);
            } else {
                urlString = input;
                getInfo();
                Gson gson = new Gson();
                map = gson.fromJson(jsonString, Map.class);
                myRoom = map.getStartingRoomObj();
                displayPaths(myRoom, players.get(0));
            }
        }
    }
    /**
     * During gameplay, this function handles results of getting to new room.
     * @param room current room
     * @return true if success, false if room was invalid
     */
    public static boolean displayPaths(Room room, Player player) {
        System.out.println("\n\n" + player.getName() + ", you are in " + map.roomNameParse(playerRoom.get(player).getName()));

        // Get items and print them and all that
        if (room.getItems() != null) {
            System.out.println("Items in this room: ");
            for (Item item : room.getItems()) {
                System.out.println(item.getName());
            }
        } else {
            System.out.println("No items in this room.");
        }

        System.out.println(playerRoom.get(player).getDescription());
        ArrayList<String> tempPathStrings = map.printAccesibleRooms(room);
        for (String pathOption : tempPathStrings) {
            System.out.println("You can see " + map.roomNameParse(pathOption));
        }
        System.out.print("You can go ");
        for (Direction pathOption : playerRoom.get(player).getAllDirections()) {
            System.out.print("<" + pathOption.getDirectionName() + "> ");
        }
        System.out.println("\nWhat Now?");
        scanForMovement(player);
        return true;
    }
    /**
     * Called when it is time to ask the user what they want to do next, takes user input
     * and decides what to do next.
     */
    private static void scanForMovement(Player player) {
        String input = player.getMove(0, playerRoom.get(player));
        if (input.toLowerCase().equals("quit") || input.toLowerCase().equals("exit")) {
            System.exit(0);
        }

        // For each possible outcome, increment play count and pass the turn off
       if (checkMove(input, player) != null) {
           playCount.put(player, playCount.get(player) + 1);
           enterRoom(player, input, false);
       } else if (checkPickup(input) != null) {
           itemBag.add(checkPickup(input));
           player.setItems(checkPickup(input));
           playCount.put(player, playCount.get(player) + 1);
           displayPaths(playerRoom.get(nextPlayer(player)), nextPlayer(player));
       } else if (checkMoveWithKey(input, player) != null) {
           playCount.put(player, playCount.get(player) + 1);
           enterRoom(player, input, true);
       }else {
           // Unknown input
           char[] shakedown = input.toLowerCase().toCharArray();
           if (shakedown[0] == 'g' && shakedown[1] == 'o') {
               System.out.println("You can't " + input + ".\n");
           } else {
               System.out.println("I don't understand '" + input + "'\n");
           }
           displayPaths(playerRoom.get(player), player);
       }
    }

    /**
     * A function to make the changes to a player as well as playerRoom map after a new room is entered.
     * @param player Player changing rooms
     * @param input The input of the current turn
     * @param key boolean : true if a key (Item) was used
     */
    private static void enterRoom(Player player, String input, boolean key) {
        if (key) {
            playerRoom.put(player, checkMoveWithKey(input, player));
        } else {
            playerRoom.put(player, checkMove(input, player));
        }
        System.out.println(player.getName() + " : " + input);

        // Check if this player has reached the final room
        if (playerRoom.get(player).getName().equals(map.getEndingRoom())) {
            System.out.print(player.getName() + " has reached the final destination");
            System.out.print(". It took him " + playCount.get(player) + " moves.");
            System.exit(0);
        }

        // Get next player and print their turn info
        displayPaths(playerRoom.get(nextPlayer(player)), nextPlayer(player));
    }

    /**
     * Check if the input is an attempt to move.
     * @param input user input
     * @return Room that is being moved into, if any.
     */
    private static Room checkMove(String input, Player player) {
        ArrayList<String> tempPathStrings = new ArrayList<>();
        ArrayList<Boolean> unlockedDirections = new ArrayList<>();
        for (Direction dir : playerRoom.get(player).getAllDirections()) {
            tempPathStrings.add(dir.getDirectionName());
            unlockedDirections.add(dir.getEnabled());
        }
        // Checks for a matching direction, then decides what to do from there
        for (String checkMatch : tempPathStrings) {
            if (input.toLowerCase().equals("go " + map.roomNameParse(checkMatch).toLowerCase())) {
                if (unlockedDirections.get(tempPathStrings.indexOf(checkMatch))){
                    return map.accesibleRooms(playerRoom.get(player)).get(tempPathStrings.indexOf(checkMatch));
                } else {
                    return null;
                }
            }
            if (input.toLowerCase().contains("use ")) {
                if (checkMatch.equals(input.toLowerCase().substring(13, input.length()))) {
                    //TODO fix this mess, make a valid key open locked doors
                }
            }
        }
        return null;
    }

    /**
     * Check if a move is possible with a key.
     * @param input the player input
     * @param player The player trying to move
     * @return The room if possible to get into, or null.
     */
    private static Room checkMoveWithKey(String input, Player player) {
        ArrayList<String> tempPathStrings = new ArrayList<>();
        for (Direction dir : playerRoom.get(player).getAllDirections()) {
            tempPathStrings.add(dir.getDirectionName());
        }

        Room room = playerRoom.get(player);
        for (Direction dir : room.getAllDirections()) {
            if (dir.getEnabled()) {
                return null;
            } else {
                for (String validKey : dir.getValidKeyNames()) {
                    if (input.toLowerCase().equals("use " + validKey.toLowerCase() +" with "+ dir.getDirectionName())) {
                        return map.accesibleRooms(playerRoom.get(player)).get(tempPathStrings.indexOf(dir.getDirectionName()));
                    }
                }
            }
        }
        return null;
    }
    /**
     * Check if the input is an attempt to pick up an item.
     * @param input user input
     * @return Item that is being picked up, if any.
     */
    private static Item checkPickup(String input) {
        if (myRoom.getItems() != null) {
            for (Item item : myRoom.getItems()) {
                if (input.toLowerCase().equals("pickup " + item.getName())) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Helper ot determine the next player.
     * @param player current player
     * @return next player
     */
    private static Player nextPlayer(Player player) {
       if (players.indexOf(player) < players.size() - 2) {
           return players.get(players.indexOf(player) + 1);
       }
       return players.get(0);
    }

    /**
     * Setter for test cases.
     * @param url url to set.
     */
    public static boolean setUrl(String url) {
        urlString = url;
        return getInfo();
    }

    /**
     * Gets the json file from given source urlString.
     */
    private static boolean getInfo() {
        try {
            URL url = new URL(urlString);
            InputStream inStream = url.openStream();
            jsonString = convertStreamToString(inStream);
        } catch (Exception MalformedURLException) {
            System.out.println("Malformed URL Error, please try a different URL");
            String[] args = {" ", " "};
            main(args);
        }
        return true;
    }

    /**
     * by Peter Mortennson.
     * https://stackoverflow.com/questions/309424
     * @param is a java input stream object
     * @return A string of the json file parsed
     */
    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
