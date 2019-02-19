import java.util.ArrayList;

/**
 * A Maps object represents a Map of rooms from a json file.
 */
public class Map {
    /**
     * The starting room as a string.
     */
    private String startingRoom = "";
    /**
     * An array of all rooms in the map.
     */
    private Room[] rooms;
    /**
     * The ending room as a String.
     */
    private String endingRoom = "";

    /**
     * Checks if the map given is valid.
     * @return True if valid, false if not.
     */
    public boolean checkMap() {
        return (!this.startingRoom.equals("") && !this.endingRoom.equals(""));
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

    /**
     * Gets the Rooms object associated with the starting room.
     * @return Rooms object that player starts in
     */
    public Room getStartingRoomObj() {
        for (Room curr : rooms) {
            if (curr.getName().equals(this.startingRoom)) {
                return curr;
            }
        }
        return rooms[1];
    }

    /**
     * Goes through the paths, and gets an array of the Rooms objects attached to them.
     * @param room room that is checked for connections
     * @return An array if all connected rooms as Rooms objects.
     */
    public ArrayList<Room> accesibleRooms(Room room) {
        Direction[] allPaths = room.getAllDirections();
        ArrayList<Room> allRooms = new ArrayList<>();

        for (Room currRoom : rooms) {
            for (Direction direction : allPaths) {
                if (direction.getRoom().equals(currRoom.getName())) {
                    allRooms.add(currRoom);
                }
            }
        }
        return allRooms;
    }

    /**
     * A helper to get accessible rooms as strings.
     * @param room The room to check for access.
     * @return An ArrayList of rooms that can be accessed as Strings.
     */
    public ArrayList<String> printAccesibleRooms(Room room) {
        ArrayList<Room> allRooms = accesibleRooms(room);
        ArrayList<String> roomNames = new ArrayList<>();
        for (Room currRoom : allRooms) {
            roomNames.add(currRoom.getName());
        }
        return roomNames;
    }
}
