import static org.junit.Assert.*;
import java.lang.*;
import com.google.gson.Gson;
import org.junit.Test;

public class MainTest {
    private static Gson gson = new Gson();

@org.junit.Test

    public void main() {
        }

    @Test
    public void testInvalidRooms() {
        assertFalse(Main.displayPaths(new Rooms()));
    }

    @Test
    public void testValidMap() {
        Maps test = gson.fromJson("{startingRoom: Room1, endingRoom: Room5}", Maps.class);
        assertTrue(test.checkMap());
    }

    @Test
    public void testInvalidMap() {
        Maps test = gson.fromJson("{startingRoom: Room1}", Maps.class);
        assertFalse(test.checkMap());
    }

    @Test
    public void testValidUrl() {
        assertTrue(Main.setUrl("https://courses.engr.illinois.edu/cs126/adventure/siebel.json"));
    }
}