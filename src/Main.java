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
            displayPaths(myRoom);
        } else {
            System.out.println("ok");
        }
    }


    private static void displayPaths(Rooms room) {
        System.out.println("You are in " + map.roomNameParse(myRoom.getName()));
        System.out.println(myRoom.getDescription());

        ArrayList<String> tempPathStrings = map.printAccesibleRooms(room);
        for (String pathOption : tempPathStrings) {
            System.out.println("You can see " + map.roomNameParse(pathOption));
        }
        for (Directions pathOption : myRoom.getAllDirections()) {
            System.out.println("You can go " + pathOption.getDirectionName());
        }
        System.out.println("What Now?");
        scanForMovement();
    }

    private static void scanForMovement() {

        ArrayList<String> tempPathStrings = new ArrayList<>();
        for (Directions dir : myRoom.getAllDirections()) {
            tempPathStrings.add(dir.getDirectionName());
        }

        String input = user.nextLine();
        if (input.toLowerCase().equals("quit") || input.toLowerCase().equals("exit")) {
            System.exit(69);
        }
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

        char[] shakedown = input.toLowerCase().toCharArray();
        if (shakedown[0] == 'g' && shakedown[1] == 'o') {
            System.out.println("You can't " + input + ".\n");
        } else {
            System.out.println("I don't understand '" + input + "'\n");
        }
        displayPaths(myRoom);
    }

    private static void getInfo() {
        try {
            URL url = new URL("https://courses.engr.illinois.edu/cs126/adventure/siebel.json");
            InputStream inStream = url.openStream();
            jsonString = convertStreamToString(inStream);
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
