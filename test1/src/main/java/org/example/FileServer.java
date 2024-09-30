package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class FileServer {

    public static void main(String[] args) {
        AtomicInteger connectedClients = new AtomicInteger(0); // 현재 연결된 클라이언트 수
        AtomicInteger completedClients = new AtomicInteger(0); // 파일 전송 완료된 클라이언트 수

        // 감시 스레드 시작
        Thread monitorThread = new Thread(new MonitorThread(connectedClients, completedClients));
        monitorThread.setDaemon(true); // 메인 스레드 종료 시 함께 종료되도록 데몬 스레드로 설정
        monitorThread.start();

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                // 클라이언트의 연결을 기다림
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // 새로운 클라이언트 연결을 처리할 스레드 생성
                new Thread(new FileHandler(clientSocket,connectedClients, completedClients)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}