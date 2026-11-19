import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// ==========================================
// 1. LOGIN CLASS (Part 1)
// ==========================================
class Login {
    String username;
    String password;
    String cellPhoneNumber;
    String firstName;
    String lastName;

    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) hasCapital = true;
            if (Character.isDigit(ch)) hasNumber = true;
            if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null && cellPhoneNumber.startsWith("+27") && cellPhoneNumber.length() == 12;
    }

    public String registerUser() {
        boolean isUserValid = checkUserName();
        boolean isPassValid = checkPasswordComplexity();
        boolean isCellValid = checkCellPhoneNumber();

        String userMsg = isUserValid
                ? "Username successfully captured."
                : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";

        String passMsg = isPassValid
                ? "Password successfully captured."
                : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";

        String cellMsg = isCellValid
                ? "Cell phone number successfully added."
                : "Cell phone number incorrectly formatted or does not contain international code.";

        if (isUserValid && isPassValid && isCellValid) {
            return userMsg + "\n" + passMsg + "\n" + cellMsg;
        } else {
            return !isUserValid ? userMsg : (!isPassValid ? passMsg : cellMsg);
        }
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username != null && this.password != null
                && this.username.equals(enteredUsername)
                && this.password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

// ==========================================
// 2. MESSAGE CLASS (Part 2 & Part 3)
// ==========================================
final class Message {
    private final String messageID;
    private final int numMessagesSent;
    private final String sender;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String flag; // "Sent", "Stored", "Disregard"

    public Message(int messageNumber, String recipient, String messageText) {
        this(messageNumber, "SystemUser", recipient, messageText, "Sent");
    }

    public Message(int messageNumber, String sender, String recipient, String messageText, String flag) {
        this.numMessagesSent = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.flag = flag;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = (messageText != null ? messageText.length() : 0) - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }

        String firstTwoID = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String combinedWords = (firstWord + lastWord).toUpperCase();
        return firstTwoID + ":" + numMessagesSent + ":" + combinedWords;
    }

    public String SentMessage(int choice) {
        return switch (choice) {
            case 1 -> {
                this.flag = "Sent";
                yield "Message successfully sent.";
            }
            case 2 -> {
                this.flag = "Disregard";
                yield "Press 0 to delete the message";
            }
            case 3 -> {
                this.flag = "Stored";
                yield "Message successfully stored.";
            }
            default -> "Invalid option selected.";
        };
    }

    public String printMessages() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Sender: " + sender + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n"
                + "Flag: " + flag;
    }

    // Appends this message as a JSON object line to stored_messages.json
    public void storeMessageToJson(String filePath) {
        String jsonLine = String.format(
                "{\"messageID\":\"%s\",\"num\":%d,\"sender\":\"%s\",\"recipient\":\"%s\",\"message\":\"%s\",\"hash\":\"%s\",\"flag\":\"%s\"}",
                escapeJson(messageID),
                numMessagesSent,
                escapeJson(sender),
                escapeJson(recipient),
                escapeJson(messageText),
                escapeJson(messageHash),
                escapeJson(flag)
        );
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(jsonLine + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
}

// ==========================================
// 3. PART 3 DATA & REPORT MANAGER
// ==========================================
class MessageManager {
    private static final String JSON_FILE = "stored_messages.json";

    // Required collections / arrays (no hard-coding)
    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    public void recordMessage(Message msg, int actionChoice) {
        if (msg == null) return;
        messageIDs.add(msg.getMessageID());
        messageHashes.add(msg.getMessageHash());

        switch (actionChoice) {
            case 1 -> {
                msg.setFlag("Sent");
                sentMessages.add(msg);
            }
            case 2 -> {
                msg.setFlag("Disregard");
                disregardedMessages.add(msg);
            }
            case 3 -> {
                msg.setFlag("Stored");
                storedMessages.add(msg);
                msg.storeMessageToJson(JSON_FILE);
            }
            default -> {
            }
        }
    }

    public void addDirectMessage(Message msg) {
        if (msg == null) return;
        messageIDs.add(msg.getMessageID());
        messageHashes.add(msg.getMessageHash());

        switch (msg.getFlag()) {
            case "Sent" -> sentMessages.add(msg);
            case "Disregard" -> disregardedMessages.add(msg);
            case "Stored" -> {
                storedMessages.add(msg);
                msg.storeMessageToJson(JSON_FILE);
            }
            default -> sentMessages.add(msg);
        }
    }

    // Reads JSON file and syncs stored messages into the storedMessages list
    public List<String> loadStoredMessagesFromJson() {
        List<String> loadedSummaries = new ArrayList<>();
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return loadedSummaries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    loadedSummaries.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
        return loadedSummaries;
    }

    // 2a. Display sender and recipient of all stored messages
    public String displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }
        StringBuilder sb = new StringBuilder("--- Stored Messages: Senders & Recipients ---\n");
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(m.getSender())
              .append(" | Recipient: ").append(m.getRecipient()).append("\n");
        }
        return sb.toString().trim();
    }

    // 2b. Display the longest stored message
    public String displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    // 2c. Search for a message ID and display corresponding recipient and message
    public String searchByMessageID(String targetID) {
        for (Message m : getAllMessages()) {
            if (m.getMessageID().equalsIgnoreCase(targetID.trim())) {
                return "Recipient: " + m.getRecipient() + "\nMessage: \"" + m.getMessageText() + "\"";
            }
        }
        return "Message ID not found.";
    }

    // 2d. Search for all messages stored/sent for a particular recipient
    public String searchMessagesByRecipient(String targetRecipient) {
        List<String> matches = new ArrayList<>();
        for (Message m : getAllMessages()) {
            if (m.getRecipient().equalsIgnoreCase(targetRecipient.trim())) {
                matches.add("\"" + m.getMessageText() + "\"");
            }
        }
        if (matches.isEmpty()) {
            return "No messages found for recipient: " + targetRecipient;
        }
        return String.join(", ", matches);
    }

    // 2e. Delete a message using the message hash
    public String deleteMessageByHash(String targetHash) {
        String cleanHash = targetHash.trim();
        for (Message m : sentMessages) {
            if (m.getMessageHash().equalsIgnoreCase(cleanHash)) {
                sentMessages.remove(m);
                messageHashes.remove(m.getMessageHash());
                messageIDs.remove(m.getMessageID());
                return "Message: \"" + m.getMessageText() + "\" successfully deleted.";
            }
        }
        for (Message m : storedMessages) {
            if (m.getMessageHash().equalsIgnoreCase(cleanHash)) {
                storedMessages.remove(m);
                messageHashes.remove(m.getMessageHash());
                messageIDs.remove(m.getMessageID());
                return "Message: \"" + m.getMessageText() + "\" successfully deleted.";
            }
        }
        for (Message m : disregardedMessages) {
            if (m.getMessageHash().equalsIgnoreCase(cleanHash)) {
                disregardedMessages.remove(m);
                messageHashes.remove(m.getMessageHash());
                messageIDs.remove(m.getMessageID());
                return "Message: \"" + m.getMessageText() + "\" successfully deleted.";
            }
        }
        return "No message found with hash: " + targetHash;
    }

    // 2f. Display a report listing full details of stored messages (plus sent summary)
    public String displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages in report.";
        }
        StringBuilder sb = new StringBuilder("=== STORED MESSAGES FULL REPORT ===\n");
        for (Message m : storedMessages) {
            sb.append("----------------------------------\n")
              .append("Message ID   : ").append(m.getMessageID()).append("\n")
              .append("Message Hash : ").append(m.getMessageHash()).append("\n")
              .append("Sender       : ").append(m.getSender()).append("\n")
              .append("Recipient    : ").append(m.getRecipient()).append("\n")
              .append("Message      : ").append(m.getMessageText()).append("\n")
              .append("Flag         : ").append(m.getFlag()).append("\n");
        }
        return sb.toString().trim();
    }

    public String displaySentMessagesReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available.";
        }
        StringBuilder sb = new StringBuilder("=== SENT MESSAGES REPORT ===\n");
        for (Message m : sentMessages) {
            sb.append("Hash: ").append(m.getMessageHash())
              .append(" | Recipient: ").append(m.getRecipient())
              .append(" | Message: \"").append(m.getMessageText()).append("\"\n");
        }
        return sb.toString().trim();
    }

    private List<Message> getAllMessages() {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        return all;
    }

    public List<Message> getSentMessages() { return sentMessages; }
    public List<Message> getDisregardedMessages() { return disregardedMessages; }
    public List<Message> getStoredMessages() { return storedMessages; }
    public List<String> getMessageHashes() { return messageHashes; }
    public List<String> getMessageIDs() { return messageIDs; }
}

// ==========================================
// 4. MAIN APPLICATION CLASS
// ==========================================
public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Login user = new Login();
            MessageManager manager = new MessageManager();
            
            // --- PART 1: REGISTRATION ---
            System.out.println("==================================");
            System.out.println("       USER REGISTRATION");
            System.out.println("==================================");
            System.out.print("Enter first name: ");
            user.firstName = sc.nextLine();
            
            System.out.print("Enter last name: ");
            user.lastName = sc.nextLine();
            
            System.out.print("Enter username (must contain '_' and <= 5 chars): ");
            user.username = sc.nextLine();
            
            System.out.print("Enter password (>= 8 chars, 1 uppercase, 1 number, 1 special): ");
            user.password = sc.nextLine();
            
            System.out.print("Enter cell phone number (e.g., +27718693002): ");
            user.cellPhoneNumber = sc.nextLine();
            
            System.out.println("\n" + user.registerUser());
            
            // --- PART 1: LOGIN ---
            System.out.println("\n==================================");
            System.out.println("           USER LOGIN");
            System.out.println("==================================");
            System.out.print("Enter username: ");
            String loginUser = sc.nextLine();
            
            System.out.print("Enter password: ");
            String loginPass = sc.nextLine();
            
            boolean isLoggedIn = user.loginUser(loginUser, loginPass);
            System.out.println("\n" + user.returnLoginStatus(isLoggedIn));
            
            // --- PART 2 & PART 3: QUICKCHAT APPLICATION ---
            if (isLoggedIn) {
                System.out.println("\n==================================");
                System.out.println("     Welcome to QuickChat.");
                System.out.println("==================================");
                
                String senderName = user.firstName + " " + user.lastName;
                boolean exit = false;
                int totalSentCount = 0;
                
                while (!exit) {
                    System.out.println("\nPlease choose an option:");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.println("4) Stored Messages");
                    System.out.print("Option: ");
                    
                    int menuOption = sc.nextInt();
                    sc.nextLine();
                    
                    switch (menuOption) {
                        case 1 -> {
                            System.out.print("\nHow many messages would you like to enter? ");
                            int numMessages = sc.nextInt();
                            sc.nextLine();
                            
                            for (int i = 0; i < numMessages; i++) {
                                System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
                                
                                System.out.print("Enter Recipient Number: ");
                                String recipient = sc.nextLine();
                                
                                System.out.print("Enter Message: ");
                                String msgText = sc.nextLine();
                                
                                Message msg = new Message(i + 1, senderName, recipient, msgText, "Sent");
                                
                                String lengthStatus = msg.checkMessageLength();
                                if (!lengthStatus.equals("Message ready to send.")) {
                                    System.out.println(lengthStatus);
                                    i--;
                                    continue;
                                }
                                
                                System.out.println("\nChoose an action:");
                                System.out.println("1) Send Message");
                                System.out.println("2) Disregard Message");
                                System.out.println("3) Store Message to send later");
                                System.out.print("Selection: ");
                                int sendOption = sc.nextInt();
                                sc.nextLine();
                                
                                String resultMsg = msg.SentMessage(sendOption);
                                System.out.println("\n" + resultMsg);
                                
                                manager.recordMessage(msg, sendOption);
                                
                                if (sendOption == 1) {
                                    totalSentCount++;
                                    System.out.println("\n--- Message Details ---");
                                    System.out.println(msg.printMessages());
                                }
                            }
                            
                            System.out.println("\nTotal messages sent in this session: " + totalSentCount);
                        }
                        
                        case 2 -> System.out.println(manager.displaySentMessagesReport());
                        
                        case 3 -> {
                            System.out.println("Exiting QuickChat. Goodbye!");
                            exit = true;
                        }
                        
                        case 4 -> {
                            boolean subExit = false;
                            while (!subExit) {
                                System.out.println("\n--- STORED MESSAGES MENU ---");
                                System.out.println("a) Display sender and recipient of all stored messages");
                                System.out.println("b) Display the longest stored message");
                                System.out.println("c) Search for a message ID (display recipient & message)");
                                System.out.println("d) Search all messages for a particular recipient");
                                System.out.println("e) Delete a message using the message hash");
                                System.out.println("f) Display full report of all stored messages");
                                System.out.println("g) Load JSON file records");
                                System.out.println("x) Back to Main Menu");
                                System.out.print("Choose option: ");
                                String subChoice = sc.nextLine().trim().toLowerCase();
                                
                                switch (subChoice) {
                                    case "a" -> System.out.println("\n" + manager.displayStoredSendersAndRecipients());
                                    case "b" -> System.out.println("\nLongest Stored Message:\n" + manager.displayLongestStoredMessage());
                                    case "c" -> {
                                        System.out.print("Enter Message ID to search: ");
                                        String id = sc.nextLine();
                                        System.out.println("\n" + manager.searchByMessageID(id));
                                    }
                                    case "d" -> {
                                        System.out.print("Enter Recipient number to search: ");
                                        String rec = sc.nextLine();
                                        System.out.println("\n" + manager.searchMessagesByRecipient(rec));
                                    }
                                    case "e" -> {
                                        System.out.print("Enter Message Hash to delete: ");
                                        String hash = sc.nextLine();
                                        System.out.println("\n" + manager.deleteMessageByHash(hash));
                                    }
                                    case "f" -> System.out.println("\n" + manager.displayStoredMessagesReport());
                                    case "g" -> {
                                        List<String> jsonLines = manager.loadStoredMessagesFromJson();
                                        System.out.println("\nJSON Records (" + jsonLines.size() + "):");
                                        for (String recLine : jsonLines) {
                                            System.out.println(recLine);
                                        }
                                    }
                                    case "x" -> subExit = true;
                                    default -> System.out.println("Invalid sub-option.");
                                }
                            }
                        }
                        
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
            } else {
                System.out.println("Access denied. You must be logged in to access QuickChat.");
            }
        }
    }
}