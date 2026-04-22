# Spring Boot Microservice Assignment

## What I Built
A Spring Boot microservice that acts as an API gateway with guardrails
for managing posts, comments, and bot interactions.

## Tech Stack
- Java 17
- Spring Boot 3.x
- PostgreSQL(locally)
- Redis(locally)

## Phase 2 — How I Guaranteed Thread Safety

## My Approach

I kept the architecture simple — PostgreSQL stores the actual data,
and Redis acts as the gatekeeper before anything touches the database.

For the Atomic Locks in Phase 2, the core idea was:
never let two requests make a decision based on the same state.

### How I Guaranteed Thread Safety

**Horizontal Cap (100 bot replies):**
Use of Redis INCR is atomic. No matter how many requests hit
at the same time, Redis processes each INCR one at a time internally.
So when I do:

    Long count = redisTemplate.opsForValue().increment(key, 1);

Every request gets a unique count — no two requests ever see the same number.
If count goes above 100, I decrement it back and return 429.
This is what stops the counter at exactly 100.

**Cooldown Cap (10 min per bot-human pair):**
I set a Redis key with a 10 minute TTL the moment a bot interacts with a human.
If the key already exists — request is blocked. Simple and race-condition free.

**Vertical Cap (depth > 20):**
This is a simple check on the incoming request — no Redis needed here.

The app holds zero state in Java memory.
No HashMaps, no static variables — everything lives in Redis.
This keeps the service completely stateless and horizontally scalable.

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/post | Create a post |
| GET | /api/posts | Get all posts |
| POST | /api/comment/{id} | Add a comment |
| POST | /api/like/{id} | Like a post |
| GET | /api/post/{id}/virality | Get virality score |

## Phase 3 — Notification Engine
- First bot interaction → notification sent immediately
- Subsequent interactions within 15 min → queued in Redis
- CRON job runs every 5 min → sends summarized notification