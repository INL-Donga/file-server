package org.example;

import java.util.concurrent.atomic.AtomicInteger;

class MonitorThread implements Runnable {
    private static AtomicInteger connectedClients;
    private static AtomicInteger completedClients;

    public MonitorThread(AtomicInteger connectedClients, AtomicInteger completedClients) {
        MonitorThread.connectedClients = connectedClients;
        MonitorThread.completedClients = completedClients;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 10초마다 클라이언트 수 체크
                Thread.sleep(1000);

                // 모든 클라이언트가 파일을 전송 완료했는지 확인
                if (completedClients.get() == connectedClients.get() && connectedClients.get() > 0) {
                    System.out.println("모든 클라이언트에서 받았으니 후처리 해주기");
                    sendFilesToMasterServer();
                    completedClients.set(0); // 다음 라운드를 위해 클라이언트 완료 수 초기화
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 마스터 서버로 파일을 전송하는 메소드 (추후 구현 필요)
    private void sendFilesToMasterServer() {
        // 마스터 서버로 파일 전송 로직 구현
        System.out.println("Files sent to master server.");
    }
}
