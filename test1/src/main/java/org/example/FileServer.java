package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    public static void main(String[] args) {
        RoundManager roundManager = RoundManager.getInstance();  // RoundManager 인스턴스 생성

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                // 클라이언트의 연결을 기다림
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // 새로운 클라이언트 연결을 처리할 스레드 생성
                new Thread(new FileHandler(clientSocket, roundManager)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
