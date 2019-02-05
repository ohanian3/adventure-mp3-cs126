public class Rooms extends Maps{
    private String name;
    private String description;
    private Directions[] directions;
    Rooms () {
    }

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
