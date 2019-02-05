import com.google.gson.Gson;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


public class Main {

    // json file in String format
    private static String jsonString = "";
    // User input scanner
    private static Scanner user = new Scanner(System.in);
    // The map as found from Json
    private static Maps map;
    // The current room
    private static Rooms myRoom;

    public static void main(String[] args) {
        getInfo();
        Gson gson = new Gson();
        map = gson.fromJson(jsonString, Maps.class);
        System.out.println("Start Game? Y/N ");
        if (user.nextLine().toLowerCase().equals("y")) {
            System.out.println("ok buddy");
            myRoom = map.getStartingRoomObj();
            gameplay();
        } else {
            System.out.println("ok");
        }
    }

    /**
     * gameplay handling user input and system output
     */
    private static void gameplay() {
        System.out.println("You are in " + map.roomNameParse(myRoom.getName()));
        displayPaths(myRoom);
    }

    private static void displayPaths(Rooms room) {
        ArrayList<String> tempPathStrings = map.printAccesibleRooms(room);
        for (String pathOption : tempPathStrings) {
            System.out.println("You can see " + map.roomNameParse(pathOption));
        }
        System.out.println("What Now? :");
        scanForMovement();
    }

    private static boolean scanForMovement() {
        ArrayList<String> tempPathStrings = map.printAccesibleRooms(myRoom);
        String input = user.nextLine();
        for (String checkMatch : tempPathStrings) {
            if (input.toLowerCase().equals("go " + map.roomNameParse(checkMatch).toLowerCase())) {
                System.out.println("Going over to " + map.roomNameParse(checkMatch).toLowerCase() + ".");
                myRoom = map.accesibleRooms(myRoom).get(tempPathStrings.indexOf(checkMatch));
                gameplay();
                return true;
            }
        }
        System.out.println("You can't go to " + input + ".\n");
        displayPaths(myRoom);
        return false;
    }

    private static void getInfo() {
        try {
            URL url = new URL("https://courses.engr.illinois.edu/cs126/adventure/siebel.json");
            InputStream inStream = url.openStream();
            jsonString = convertStreamToString(inStream);
            System.out.println(jsonString);
        } catch (Exception MalformedURLException) {
            System.out.println("Malformed URL Error");
        }
    }

    // Peter Mortennson
    // https://stackoverflow.com/questions/309424
    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
