import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageManagerTest {

    private MessageManager manager;
    private Message msg1;
    private Message msg2;
    private Message msg3;
    private Message msg4;
    private Message msg5;

    @BeforeEach
    public void setUp() {
        manager = new MessageManager();

        msg1 = new Message(1, "Developer", "+27834557896", "Did you get the cake?", "Sent");
        msg2 = new Message(2, "Developer", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        msg3 = new Message(3, "Developer", "+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        msg4 = new Message(4, "0838884567", "+27838884567", "It is dinner time !", "Sent");
        msg5 = new Message(5, "Developer", "+27838884567", "Ok, I am leaving without you.", "Stored");

        manager.addDirectMessage(msg1);
        manager.addDirectMessage(msg2);
        manager.addDirectMessage(msg3);
        manager.addDirectMessage(msg4);
        manager.addDirectMessage(msg5);
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(2, manager.getSentMessages().size());
        assertEquals("Did you get the cake?", manager.getSentMessages().get(0).getMessageText());
        assertEquals("It is dinner time !", manager.getSentMessages().get(1).getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                manager.displayLongestStoredMessage()
        );
    }

    @Test
    public void testSearchByMessageID() {
        String result = manager.searchByMessageID(msg4.getMessageID());
        assertTrue(result.contains("It is dinner time !"));
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String result = manager.searchMessagesByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        String deleteResult = manager.deleteMessageByHash(msg2.getMessageHash());
        assertEquals(
                "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                deleteResult
        );
    }

    @Test
    public void testDisplayReport() {
        String report = manager.displayStoredMessagesReport();
        assertNotNull(report);
        assertTrue(report.contains(msg2.getMessageHash()));
        assertTrue(report.contains(msg2.getRecipient()));
        assertTrue(report.contains(msg2.getMessageText()));
    }
}