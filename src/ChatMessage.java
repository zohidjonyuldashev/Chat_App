import java.time.LocalDateTime;

public class ChatMessage {
    private User sender;
    private String message;
    private LocalDateTime timestamp;

    public ChatMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
