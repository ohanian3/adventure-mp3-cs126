import com.google.gson.Gson;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Main function that handles gameplay
 */
public class Main {

    /**
     * The json file as a string.
     */
    private static String jsonString = "";
    /**
     * User input scanner.
     */
    private static Scanner user = new Scanner(System.in);
    /**
     * The current map.
     */
    private static Map map;
    /**
     * The Rooms object of the room the player is currently in.
     */
    private static Room myRoom;
    /**
     * The url of the current json to be used, default set here.
     */
    private static String urlString = "https://courses.engr.illinois.edu/cs126/adventure/siebel.json";

    /**
     * Main function that starts the game, or allows new url to be input.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Start Game? Choose N to change map Y/N ");
        if (user.nextLine().toLowerCase().equals("y")) {
            System.out.println("ok buddy");
            getInfo();
            Gson gson = new Gson();
            map = gson.fromJson(jsonString, Map.class);
            myRoom = map.getStartingRoomObj();
            displayPaths(myRoom);
        } else {
            System.out.println("Enter a different map url or 'exit' to quit:");
            String input = user.nextLine();

            if (input.equals("exit")) {
                System.exit(69);
            } else {
                urlString = input;
                getInfo();
                Gson gson = new Gson();
                map = gson.fromJson(jsonString, Map.class);
                myRoom = map.getStartingRoomObj();
                displayPaths(myRoom);
            }
        }

    }
    /**
     * During gameplay, this function handles results of getting to new room.
     * @param room current room
     * @return true if success, false if room was invalid
     */
    public static boolean displayPaths(Room room) {
        // Test that it is a usable room
        try {
            map.printAccesibleRooms(room).get(0);
        } catch (Exception E) {
            System.out.println("Room does not exist or is not connected to any rooms");
            return false;
        }
        System.out.println("You are in " + map.roomNameParse(myRoom.getName()));

        // Get items and print them and all that
        if (room.getItems() != null) {
            System.out.println("Items in this room: ");
            for (Item item : room.getItems()) {
                System.out.println(item.getName());
            }
        } else {
            System.out.println("No items in this room.");
        }


        System.out.println("\n" + myRoom.getDescription());
        ArrayList<String> tempPathStrings = map.printAccesibleRooms(room);
        for (String pathOption : tempPathStrings) {
            System.out.println("You can see " + map.roomNameParse(pathOption));
        }
        for (Direction pathOption : myRoom.getAllDirections()) {
            System.out.println("You can go " + pathOption.getDirectionName());
        }
        System.out.println("What Now?");
        scanForMovement();
        return true;
    }
    /**
     * Called when it is time to ask the user what they want to do next, takes user input
     * and decides what to do next.
     */
    private static void scanForMovement() {
        ArrayList<String> tempPathStrings = new ArrayList<>();
        for (Direction dir : myRoom.getAllDirections()) {
            tempPathStrings.add(dir.getDirectionName());
        }
        String input = user.nextLine();
        if (input.toLowerCase().equals("quit") || input.toLowerCase().equals("exit")) {
            System.exit(69);
        }
        // Checks for a matching direction, then decides what to do from there
        for (String checkMatch : tempPathStrings) {
            if (input.toLowerCase().equals("go " + map.roomNameParse(checkMatch).toLowerCase())) {
                System.out.println("Going " + map.roomNameParse(checkMatch).toLowerCase() + ".");
                myRoom = map.accesibleRooms(myRoom).get(tempPathStrings.indexOf(checkMatch));
                if (myRoom.getName().equals(map.getEndingRoom())) {
                    System.out.println("You have reached your final destination");
                    System.exit(69);
                }
                displayPaths(myRoom);
            }
        }
        // Goes through invalid outputs and decides what to say
        char[] shakedown = input.toLowerCase().toCharArray();
        if (shakedown[0] == 'g' && shakedown[1] == 'o') {
            System.out.println("You can't " + input + ".\n");
        } else {
            System.out.println("I don't understand '" + input + "'\n");
        }
        displayPaths(myRoom);
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
    // Peter Mortennson
    // https://stackoverflow.com/questions/309424
    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
