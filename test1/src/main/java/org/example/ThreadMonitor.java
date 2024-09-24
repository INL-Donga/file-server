package org.example;
import java.util.Set;

public class ThreadMonitor implements Runnable {

    @Override
    public void run() {
        while (true) {
            // 모든 실행 중인 쓰레드를 가져옴
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

            System.out.println("=== Active Threads ===");
            for (Thread thread : threadSet) {
                // 쓰레드 정보 출력
                System.out.println("Thread Name: " + thread.getName() +
                        ", State: " + thread.getState() +
                        ", Is Alive: " + thread.isAlive());
            }

            // 주기적으로 정보를 출력하기 위해 일정 시간 대기
            try {
                Thread.sleep(10000); // 10초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
