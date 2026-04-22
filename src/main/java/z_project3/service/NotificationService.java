package z_project3.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private final StringRedisTemplate redis;

    public NotificationService(StringRedisTemplate redis) {
        this.redis = redis;
    }
    public void handleBotNotification(Long userId, String message) {
        String cooldownKey = "notif_cooldown:user_" + userId;
        String pendingKey = "user:" + userId + ":pending_notifs";
        boolean userRecentlyNotified = Boolean.TRUE.equals(redis.hasKey(cooldownKey));

        if (userRecentlyNotified) {
            // Queue mein daalo
            redis.opsForList().rightPush(pendingKey, message);
            // Debug — confirm karo ki save hua
            Long size = redis.opsForList().size(pendingKey);
            System.out.println("Saved for later → User " + userId + ": " + message);

        } else {
            System.out.println("Push Notification Sent to User " + userId + ": " + message);
            redis.opsForValue().set(cooldownKey, "yes", 15, TimeUnit.MINUTES);
        }
    }
}