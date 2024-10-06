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

    public static boolean isRunning = true;

    public static int round;
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

            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while (true) {
                // localhost 의 .py 로 부터 .pt 업로드 완료 수신
                // 마스터로부터 수신


                String message = reader.readLine();  // 메시지 수신
                if(message != null){
                    message = message.trim();
                    System.out.println("[MasterHandler] : Server.py로 부터 라운드를 받았습니다: " + message);
                    round = Integer.parseInt(message);
                    isRunning= false;
                }

                System.out.println("================ round :" + round + " ================" );

                while (true){
                    if(isRunning) break;
                }

                while(true) {
                    if (roundManager.getConnectedClients() == roundManager.getCompletedClients() && clientList.size() != 0) {
                        // Server.py 에게 완료된 클라이언트 수 주기
                        isRunning = true;
                        write(clientSocket, Integer.toString(roundManager.getConnectedClients()));
                        write(clientSocket, "complete");
                        System.out.println("[MasterHandler] : Server.py 에게 완료 신호를 보냈습니다.");
                        System.out.println(isRunning);
                        isRunning = false;
                        System.out.println(isRunning);
                        break;
                    }
                }

//                while (true){
//                    if(isRunning) break;
//                }
//
//                System.out.println("after while");

            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(Socket clientSocket, String msg) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }
}
