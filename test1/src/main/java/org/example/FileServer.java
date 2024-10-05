package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileServer {

    private static volatile boolean isRunning = true;
    private static List<Socket> clientList = new ArrayList<>();  // 클라이언트 리스트 전역 변수
    // 전역변수 배열로 client List => 마스터 핸들러에 주기
    public static void main(String[] args) {
        RoundManager roundManager = RoundManager.getInstance();  // RoundManager 인스턴스 생성

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started. Waiting for connections...");
            System.out.println("Please enter 'start' to execute.");

//            // 사용자 입력을 처리하는 스레드
//            Thread inputThread = new Thread(() -> {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//                try {
//                    while (true) {
//                        String userInput = reader.readLine();
//                        if ("start".equalsIgnoreCase(userInput)) {
//                            System.out.println("Total Clients : " + roundManager.getConnectedClients());
//                            System.out.println("Starting Progress...");
//                            isRunning = false;
//                            break;
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            inputThread.setDaemon(true);  // 백그라운드에서 실행되도록 설정
//            inputThread.start();

            while (true) {
                // 클라이언트의 연결을 기다림
                Socket clientSocket = serverSocket.accept();

                // Master Node Server.py 체크
                if (clientSocket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
                    new Thread(new MasterHandler(clientSocket, roundManager, clientList)).start();
                } else {
                    // 새로운 클라이언트 연결을 처리할 스레드 생성
                    new Thread(new FileHandler(clientSocket, roundManager)).start();
                    synchronized (clientList) {
                        clientList.add(clientSocket);
                    }
                    System.out.println("Client connected. Total connected clients: " + roundManager.getConnectedClients());
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
