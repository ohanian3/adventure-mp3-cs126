/**
 * A Directions object represents a direction, and the room that is in that direction.
 */
public class Direction extends Room{
    /**
     * The direction that this direction object is.
     */
    private String directionName;
    /**
     * The name of the room that is in this direction.
     */
    private String room;

    private String enabled;

    public String getDirectionName() {
        return this.directionName;
    }

    public String getRoom() {
        return this.room;
    }

    public boolean getEnabled() {
        if (enabled == null) {
            return true;
        }
        return enabled.equals("true");
    }

}
