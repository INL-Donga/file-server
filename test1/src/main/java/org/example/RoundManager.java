package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundManager {


    public static boolean isBool = false;

    private static RoundManager instance = null;
    private AtomicInteger round;  // 현재 라운드
    private AtomicInteger connectedClients;  // 현재 연결된 클라이언트 수
    private AtomicInteger completedClients;  // 현재 라운드에서 파일 전송을 완료한 클라이언트 수

    // 싱글톤 패턴으로 RoundManager 생성
    private RoundManager() {
        this.round = new AtomicInteger(0);  // 초기 라운드 0로 시작
        this.connectedClients = new AtomicInteger(0);  // 처음엔 0명
        this.completedClients = new AtomicInteger(0);  // 처음엔 0명
    }

    // 싱글톤 인스턴스 반환
    public static synchronized RoundManager getInstance() {
        if (instance == null) {
            instance = new RoundManager();
        }
        return instance;
    }

    // 클라이언트가 연결될 때마다 호출하여 연결된 클라이언트 수 증가
    public void clientConnected() {
        int currentConnected = connectedClients.incrementAndGet();
        System.out.println("Connected clients: " + currentConnected);
    }

    // 현재 라운드 가져오기
    public int getRound() {
        return round.get();
    }

    public int getConnectedClients() {return connectedClients.get();};

    // 클라이언트가 파일 전송을 완료할 때마다 호출
    public synchronized void clientCompleted() {
        int completed = completedClients.incrementAndGet();
//        System.out.println("[RoundManager] : Completed clients in this round: " + completed + "/" + connectedClients.get());

        // 모든 클라이언트가 완료된 경우 다음 라운드로 넘어감
        if (completed == connectedClients.get()) {
//            System.out.println("[RoundManager] : All clients completed. Proceeding to next round.");
            MasterHandler.isRunning = true;
            System.out.println("rm : "+MasterHandler.isRunning);
            System.out.println("Complete ALL");
        }
    }

    // 다음 라운드로 전환
    private void nextRound() {
        round.incrementAndGet();  // 라운드 증가
        completedClients.set(0);  // 완료된 클라이언트 수 초기화
        System.out.println("[RoundManager] : Round updated to: " + round.get());
    }

    public int getCompletedClients() {return completedClients.get();};
}
