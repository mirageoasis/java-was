package repository;

import handler.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionRepository {

    private final Map<Integer, Session> sessions = new ConcurrentHashMap<>();
    private static final AtomicInteger count = new AtomicInteger(0);

    public Integer addSession(Session session) {
        // 1. 세션 숫자를 더해주기
        Integer sessionId = findEmptySession();

        // 2. sessions에 session을 저장
        sessions.put(sessionId, session);
        return sessionId;
    }

    private synchronized Integer findEmptySession() {
        // 1. sessions에서 빈 세션을 찾기
        Integer sessionId = count.incrementAndGet();

        while (sessions.containsKey(sessionId)) {
            sessionId = count.incrementAndGet();
        }

        return sessionId;
    }

    public Session getSession(Integer sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(Integer sessionId) {
        sessions.remove(sessionId);
    }
}