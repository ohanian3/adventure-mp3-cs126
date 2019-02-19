import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Player extends Map {
    private Item[] items;

    private String name;

    /** True if this is a human entering moves, false if this player is a bot */
    private boolean person;

    /** Most common opposite directions, to help bots not be dumb */
    private String[][] directionOpp =       {{"south", "north"}, {"north", "south"},
                                            {"east", "west"}, {"west", "east"},
                                            {"northeast", "southwest"}, {"southwest", "northeast"},
                                            {"northwest", "southeast"}, {"southeast", "northwest"}};

    /** The last direction the bot went, to help it not be dumb*/
    private String lastMove = "";

    public Player() {

    }

    public Player(boolean type, String setName) {
        this.person = type;
        this.name = setName;
    }

    public Item[] getItems() {
        return this.items;
    }

    public void setItems(Item item) {
        Item[] tempItems = new Item[items.length + 1];
        for (int i = 0; i < items.length; i++) {
            tempItems[i] = items[i];
        }
        tempItems[items.length] = item;
        this.items = tempItems;
    }

    public String getMove(int code, Room room) {
        // If real player
        if (this.person) {
            Scanner input = new Scanner(System.in);
            return input.nextLine();
        }
        // else
        if (room.getItems() != null) {
            for (Item items : room.getItems()) {
                return "pickup " + items.getName();
            }
        }

        ArrayList<String> validMoves = new ArrayList<>();
        for (Direction dir : room.getAllDirections()) {
            for (String[] move : directionOpp) {
                if (dir.getDirectionName().toLowerCase().equals(move[0])) {
                    if (lastMove.toLowerCase().equals(move[1])) {
                        break;
                    } else {
                        validMoves.add("go " + dir.getDirectionName());
                    }
                }
            }
        }
        if (validMoves.size() > 0) {
            Collections.shuffle(validMoves);
            this.lastMove = validMoves.get(0);
            return validMoves.get(0);
        } else {
            return "go " + room.getAllDirections()[0];
        }

        //return "quit";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
