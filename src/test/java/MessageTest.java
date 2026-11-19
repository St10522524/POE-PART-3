import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLength_Success() {
        Message msg = new Message(1, "+27834557896", "Did you get the cake?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testCheckMessageLength_Failure() {
        String longMessage = "A".repeat(255);
        Message msg = new Message(1, "+27834557896", longMessage);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", msg.checkMessageLength());
    }

    @Test
    public void testCheckRecipientCell_Success() {
        Message msg = new Message(1, "+27834557896", "Did you get the cake?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_Failure() {
        Message msg = new Message(4, "0838884567", "It is dinner time !");
        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell()
        );
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message(1, "Developer", "+27834557896", "Did you get the cake?", "Sent");
        String hash = msg.createMessageHash();
        assertNotNull(hash);
        assertTrue(hash.endsWith(":1:DIDCAKE?"));
    }

    @Test
    public void testCheckMessageID() {
        Message msg = new Message(1, "+27834557896", "Did you get the cake?");
        assertTrue(msg.checkMessageID());
        assertNotNull(msg.getMessageID());
        assertTrue(msg.getMessageID().length() <= 10);
    }

    @Test
    public void testSentMessageOptions() {
        Message msg = new Message(1, "+27834557896", "Did you get the cake?");
        assertEquals("Message successfully sent.", msg.SentMessage(1));
        assertEquals("Press 0 to delete the message", msg.SentMessage(2));
        assertEquals("Message successfully stored.", msg.SentMessage(3));
        assertEquals("Invalid option selected.", msg.SentMessage(99));
    }

    @Test
    public void testPrintMessagesContainsDetails() {
        Message msg = new Message(1, "Developer", "+27834557896", "Did you get the cake?", "Sent");
        String output = msg.printMessages();
        assertTrue(output.contains("Developer"));
        assertTrue(output.contains("+27834557896"));
        assertTrue(output.contains("Did you get the cake?"));
    }
}