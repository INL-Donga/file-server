package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileServer {

    public static int client_count = 0;

    public static boolean isRunning = false;

    private static List<Socket> clientList = new ArrayList<>();  // 클라이언트 리스트 전역 변수
    // 전역변수 배열로 client List => 마스터 핸들러에 주기
    public static void main(String[] args) {
        RoundManager roundManager = RoundManager.getInstance();  // RoundManager 인스턴스 생성

        try (ServerSocket serverSocket = new ServerSocket(9090)) {
            System.out.println("Server started. Waiting for connections...");
            System.out.println("Please enter 'start' to execute.");
            while (true) {
                // 클라이언트의 연결을 기다림
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted Socket: " + clientSocket.getInetAddress());

                // Master Node Server.py 체크
                if (clientSocket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
                    new Thread(new MasterHandler(clientSocket, roundManager, clientList)).start();
                    FileServer.isRunning=true;
                } else {
                    // 새로운 클라이언트 연결을 처리할 스레드 생성
                    new Thread(new FileHandler(clientSocket, roundManager)).start();
                    synchronized (clientList) {
                        clientList.add(clientSocket);
                    }


//                    OutputStream outputStream = clientSocket.getOutputStream();
//                    String msg = Integer.toString(FileServer.client_count);
//                    FileServer.client_count++;
//                    outputStream.write(msg.getBytes());
//                    outputStream.flush();

                    System.out.println("Client connected. Total connected clients: " + roundManager.getConnectedClients());
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
