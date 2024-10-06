
package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundManager {

    private static RoundManager instance = null;
    private AtomicInteger round;  // 현재 라운드
    private AtomicInteger connectedClients;  // 현재 연결된 클라이언트 수
    private CountDownLatch latch;  // 라운드 완료 대기

    // 싱글톤 패턴으로 RoundManager 생성
    private RoundManager() {
        this.round = new AtomicInteger(0);  // 초기 라운드 0로 시작
        this.connectedClients = new AtomicInteger(0);  // 처음엔 0명
    }

    // 싱글톤 인스턴스 반환
    public static synchronized RoundManager getInstance() {
        if (instance == null) {
            instance = new RoundManager();
        }
        return instance;
    }

    // 클라이언트가 연결될 때마다 호출하여 연결된 클라이언트 수 증가
    public synchronized void clientConnected() {
        int currentConnected = connectedClients.incrementAndGet();
        latch = new CountDownLatch(currentConnected); // 새로운 라운드에 맞춰 latch 설정
        System.out.println("Connected clients: " + currentConnected);
    }

    // 현재 라운드 가져오기
    public int getRound() {
        return round.get();
    }

    // 다음 라운드 정보 전송
    public synchronized void nextRound() {
        round.incrementAndGet();  // 라운드 증가
        latch = new CountDownLatch(connectedClients.get());  // 라운드 완료 대기자 수 갱신
        System.out.println("[RoundManager] : Proceeding to round " + round.get());
    }

    // 클라이언트가 라운드 작업을 완료할 때 호출
    public synchronized void clientCompleted() {
        latch.countDown();
        System.out.println("Client completed. Remaining: " + latch.getCount());

        // 모든 클라이언트가 완료된 경우
        if (latch.getCount() == 0) {
            System.out.println("[RoundManager] : All clients completed the round.");
        }
    }

    // 모든 클라이언트가 완료될 때까지 대기
    public void awaitCompletion() throws InterruptedException {
        latch.await();  // 모든 클라이언트가 완료될 때까지 대기
    }
}
