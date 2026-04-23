package z_project3.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class NotificationSweeper {

    private final StringRedisTemplate redis;

    public NotificationSweeper(StringRedisTemplate redis) {
        this.redis = redis;
    }
    // Ye method har 5 minute mein automatically chalega
    @Scheduled(fixedRate =  1 * 60 * 1000)
    public void sweep() {
        System.out.println("CRON chala — pending notifications check ho rahi hain...");
        // Dhundo kaunse users ki notifications pending hain
        Set<String> keys = redis.keys("user:*:pending_notifs");
        if (keys == null || keys.isEmpty()) {
            System.out.println("Koi pending notification nahi mili.");
            return;
        }

        for (String key : keys) {
            // Key se userId nikalo — "user:42:pending_notifs" → "42"
            String userId = key.split(":")[1];
            // Us user ki saari pending notifications lo
            List<String> messages = redis.opsForList().range(key, 0, -1);
            if (messages == null || messages.isEmpty()) continue;
            // Summary banao
            String summary;
            if (messages.size() == 1) {
                summary = messages.get(0);
            } else {
                summary = messages.get(0) + " aur [" + (messages.size() - 1) + "] others ne bhi interact kiya.";
            }

            System.out.println("Summarized Push Notification → User " + userId + ": " + summary);
            // List clean kri — warna kal phir same notifications aayengi
            redis.delete(key);
        }
    }}
