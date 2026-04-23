package z_project3.service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // Virality Score update
    public void updateViralityScore(Long postId, String interactionType) {
        String key = "post:" + postId + ":virality_score";
        switch (interactionType) {
            case "BOT_REPLY"     -> redisTemplate.opsForValue().increment(key, 1);
            case "HUMAN_LIKE"    -> redisTemplate.opsForValue().increment(key, 20);
            case "HUMAN_COMMENT" -> redisTemplate.opsForValue().increment(key, 50);
        }
    }
    // Horizontal Cap - 100 bot replies max
    public boolean isBotCapReached(Long postId) {
        String key = "post:" + postId + ":bot_count";
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count > 100) {
            redisTemplate.opsForValue().decrement(key, 1);
            return true;
        }
        return false;

    }

    // Vertical Cap - depth > 20 reject
    public boolean isDepthCapReached(int depthLevel) {
        return depthLevel > 20;
    }

    // Cooldown Cap - 10 min TTL
    public boolean isCooldownActive(Long botId, Long humanId) {
        String key = "cooldown:bot" + botId + ":human" + humanId;
        Boolean exists = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(exists)) {
            return true;
        }
        redisTemplate.opsForValue().set(key, "1", 10, TimeUnit.MINUTES);
        return false;
    }

    // Virality Score get karo
    public String getViralityScore(Long postId) {
        String key = "post:" + postId + ":virality_score";
        String score = redisTemplate.opsForValue().get(key);
        return score != null ? score : "0";
    }
}
