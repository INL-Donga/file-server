package org.example;

import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MasterHandler implements Runnable {
    private final Socket clientSocket;
    private static final String BASE_DIRECTORY = "D:\\INL\\backend\\test1\\parameters"; // 파일 저장 경로
    private RoundManager roundManager;  // RoundManager 인스턴스
    private static List<Socket> clientList = new ArrayList<>();

    public MasterHandler(Socket socket, RoundManager roundManager, List<Socket> clientList) {
        this.clientSocket = socket;
        this.roundManager = roundManager;
        this.clientList = clientList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // localhost 의 .py 로 부터 .pt 업로드 완료 수신
                // 클라이언트로부터 파일을 수신
                System.out.println("test1");
                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String message = reader.readLine().trim();  // 메시지 수신
                System.out.println("Received message: " + message);


                if (Integer.parseInt(message) != roundManager.getRound()) {
                    System.out.println("round mismatch");
//                    System.exit(1); // 프로그램 비정상종료 종료
                    // 클라이언트로 보내기?
                }

                System.out.println("fdsfsdfds");
                // 문제점 1. 클라이언트 정보를 받아와야 돼
                for (Socket client : clientList) {
                    OutputStream outputStream = client.getOutputStream();
//                        String msg = Integer.toString(roundManager.getRound());
                    String msg = "fsdfsdfsd";
                    outputStream.write(msg.getBytes());
                    outputStream.flush();
                }

                while (true) {
                    if (clientList.size() == roundManager.getCompletedClients()) {
                        OutputStream outputStream = clientSocket.getOutputStream();
                        String msg = "complete ";
                        outputStream.write(msg.getBytes());
                        outputStream.write(roundManager.getConnectedClients());
                        outputStream.flush();
                        break;
                    }
                }

            }




        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
