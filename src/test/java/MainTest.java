import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testMainClassLoaded() {
        assertNotNull(Main.class);
    }

    @Test
    public void testMainInstantiation() {
        Main mainApp = new Main();
        assertNotNull(mainApp);
    }

    @Test
    public void testCoreComponentsAccessible() {
        Login login = new Login();
        Message message = new Message(1, "Developer", "+27834557896", "Did you get the cake?", "Sent");
        MessageManager manager = new MessageManager();

        assertNotNull(login);
        assertNotNull(message);
        assertNotNull(manager);
    }
}