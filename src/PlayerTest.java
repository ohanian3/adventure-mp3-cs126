import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    private Item item1;
    private Item item2;
    private Player player;

    @Before
    public void setup() {
        item1 = new Item();
        item2 = new Item();
        player = new Player();
        player.setItems(item1);
        player.setItems(item2);
        player.setName("test");
    }

    @Test
    public void setItems() {
        assertFalse(player.setItems(new Item()));
    }

    @Test
    public void getItems() {
        Item[] actual = {item1, item2};
        assertArrayEquals(actual, player.getItems());
    }

    @Test
    public void getInvalidMove() {
        assertEquals("quit", player.getMove(0, new Room()));
    }

    @Test
    public void getName() {
        assertEquals("test", player.getName());
    }
}