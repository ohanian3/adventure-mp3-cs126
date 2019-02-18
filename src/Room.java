public class Room extends Map{
    /**
     * The name of this room.
     */
    private String name;
    /**
     * A description of this room.
     */
    private String description;
    /**
     * All directions that can be gone from this room.
     */
    private Direction[] directions;

    /** Items array */
    private Item[] items;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Direction[] getAllDirections() {
        return this.directions;
    }

    public Item[] getItems() {
        return this.items;
    }


}
