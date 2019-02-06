import java.util.ArrayList;

public class Maps {
    // The starting room
    private String startingRoom = "";
    // An array of all rooms as Rooms objects
    private Rooms[] rooms;
    // The ending room
    private String endingRoom = "";

    /**
     * Constructor for testing mostly.
     */
    public Maps() {
    }

    public boolean checkMap() {
        if (!this.startingRoom.equals("") && !this.endingRoom.equals("")) {
            return true;
        }
        return false;
    }
    /**
     * Gets the room name as a String parsed to have spaces and all that.
     * @return The room name parsed to have spaces.
     */
    public String getStartingRoom() {
        return roomNameParse(this.startingRoom);
    }

    /**
     * The final destination of the player.
     * @return the ending room parsed from the json map.
     */
    public String getEndingRoom() {
        return this.endingRoom;
    }
    /**
     * Parses a String to make replace camelcase humps with spaces
     * @param input A string to be parsed
     * @return A new string, with spaces.
     */
    public String roomNameParse(String input) {
        // J Sauer https://stackoverflow.com/questions/4886091 to add spaces at caps
        return input.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
    }

    public String roomNameParse(Rooms room) {
        return roomNameParse(room.getName());
    }

    /**
     * Gets the Rooms object associated with the starting room.
     * @return Rooms object that player starts in
     */
    public Rooms getStartingRoomObj() {
        for (Rooms curr : rooms) {
            if (curr.getName().equals(this.startingRoom)) {
                return curr;
            }
        }
        return rooms[1];
    }

    /**
     * The whole array of rooms that are available.
     * @return All rooms as saved in a Rooms object array.
     */
    public Rooms[] getAllRooms() {
        return rooms;
    }

    /**
     * Goes through the paths, and gets an array of the Rooms objects attached to them.
     * @param room room that is checked for connections
     * @return An array if all connected rooms as Rooms objects.
     */
    public ArrayList<Rooms> accesibleRooms(Rooms room) {
        Directions[] allPaths = room.getAllDirections();
        ArrayList<Rooms> allRooms = new ArrayList<>();

        for (Rooms currRoom : rooms) {
            for (Directions direction : allPaths) {
                if (direction.getRoom().equals(currRoom.getName())) {
                    allRooms.add(currRoom);
                }
            }
        }
        return allRooms;
    }

    public ArrayList<String> printAccesibleRooms(Rooms room) {
        ArrayList<Rooms> allRooms = accesibleRooms(room);
        ArrayList<String> roomNames = new ArrayList<>();
        for (Rooms currRoom : allRooms) {
            roomNames.add(currRoom.getName());
        }
        return roomNames;
    }
}
