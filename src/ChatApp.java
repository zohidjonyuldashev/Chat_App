import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ChatApp {
    private static final String USER_FILE = "users.txt";
    private static final String CHAT_HISTORY_FILE = "chat_history.txt";
    private static final Map<String, User> registeredUsers = new HashMap<>();
    private static final Logger logger = Logger.getLogger(ChatApp.class.getName());
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    Chat App
                    1. Register
                    2. Login
                    3. Exit""");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    logger.info("Goodbye!");
                    System.exit(0);
                default:
                    logger.warning("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("Enter your email: ");
        String email = scanner.next();

        if (isValidEmailAddress(email)) {
            User newUser = new User(email);
            registeredUsers.put(email, newUser);

            System.out.println("User registered: " + email);

            saveUserToFile(newUser);

            System.out.println("Registration successful!");
        } else {
            logger.warning("Invalid email format. Registration failed.");
        }
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("Enter your email: ");
        String email = scanner.next();

        if (registeredUsers.containsKey(email)) {
            // Log successful login
            log("User logged in: " + email);

            // Simulate chat
            User currentUser = registeredUsers.get(email);
            chat(currentUser, scanner);
        } else {
            logger.warning("User not found. Login failed.");
        }
    }

    private static void chat(User currentUser, Scanner scanner) {
        while (true) {
            System.out.println("Enter your message (type 'exit' to end the chat): ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("exit")) {
                break;
            }

            ChatMessage chatMessage = new ChatMessage(currentUser, message);

            // Save to chat history file
            saveMessageToFile(chatMessage);
        }
    }

    private static void log(String message) {
        logger.info("LOG: " + message);
    }

    private static void saveUserToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(user.getEmail() + "\n");
        } catch (IOException e) {
            log("Error saving user to file: " + e.getMessage());
        }
    }

    private static void saveMessageToFile(ChatMessage message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_HISTORY_FILE, true))) {
            String line = String.format(
                    "[%s] %s: %s",
                    message.getTimestamp(), message.getSender().getEmail(), message.getMessage()
            );
            writer.write(line + "\n");
        } catch (IOException e) {
            log("Error saving message to file: " + e.getMessage());
        }
    }


    public static boolean isValidEmailAddress(String input) {
        if (input == null)
            throw new RuntimeException("Input can not be null");
        return VALID_EMAIL_ADDRESS_REGEX.matcher(input).matches();
    }
}
