public class Rooms extends Maps{
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
    private Directions[] directions;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Directions[] getAllDirections() {
        return this.directions;
    }


}
