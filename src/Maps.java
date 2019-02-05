public class Maps {
    // The starting room
    private String startingRoom;
    // An array of all rooms as Rooms objects
    private Rooms[] rooms;

    public String getStartingRoom() {
        // J Sauer https://stackoverflow.com/questions/4886091 to add spaces at caps
        String out = this.startingRoom.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
        return out;
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
        return null;
    }

    public Rooms[] getAllRooms() {
        return rooms;
    }



}
