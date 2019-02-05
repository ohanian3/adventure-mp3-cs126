import com.google.gson.Gson;

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

    public static void main(String[] args) {
        getInfo();
        Gson gson = new Gson();
        map = gson.fromJson(jsonString, Maps.class);
        System.out.println("Start Game? Y/N ");
        if (user.nextLine().toLowerCase().equals("y")) {
            System.out.println("ok buddy");
            gameplay();
        } else {
            System.out.println("ok");
        }


    }

    /**
     * gameplay handling user input and system output
     */
    private static void gameplay() {
        System.out.println("You start in " + map.getStartingRoom());
    }

    private static void getInfo() {
        try {
            // This code is from the lecture notes
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

