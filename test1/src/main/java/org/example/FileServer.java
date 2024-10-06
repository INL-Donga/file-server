package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileServer {
    public static int client_count = 0;
    public static boolean isRunning = false;
    private static List<Socket> clientList = new ArrayList<>();  // 클라이언트 리스트

    public static void main(String[] args) {
        RoundManager roundManager = RoundManager.getInstance();  // RoundManager 인스턴스 생성

        try (ServerSocket serverSocket = new ServerSocket(9090)) {
            System.out.println("[FileServer] : Server started. Waiting for connections...");
            System.out.println("[FileServer] : Please enter 'start' to execute.");

            while (true) {
                // 클라이언트의 연결을 기다림
                Socket clientSocket = serverSocket.accept();
                System.out.println("[FileServer] : Accepted Socket: " + clientSocket.getInetAddress());

                synchronized (clientList) {
                    clientList.add(clientSocket);
                    client_count++;
                }

                // 마스터 핸들러는 localhost에서 접속한 경우에만 실행
                if (clientSocket.getInetAddress().getHostAddress().equals("127.0.0.1") && !isRunning) {
                    new Thread(new MasterHandler(clientSocket, roundManager, clientList)).start();
                    isRunning = true;  // MasterHandler 실행 중임을 표시
                } else {
                    // 새로운 클라이언트 연결을 처리할 스레드 생성
                    new Thread(new FileHandler(clientSocket, roundManager)).start();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
