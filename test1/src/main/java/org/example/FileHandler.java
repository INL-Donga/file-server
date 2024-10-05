package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class FileHandler implements Runnable {
    private final Socket clientSocket;
    private static final String BASE_DIRECTORY = "D:\\INL\\backend\\test1\\parameters"; // 파일 저장 경로
    private RoundManager roundManager;  // RoundManager 인스턴스

    public FileHandler(Socket socket, RoundManager roundManager) {
        this.clientSocket = socket; // 클라이언트 1, 클라이언트 2
        this.roundManager = roundManager;
        this.roundManager.clientConnected();  // 클라이언트가 연결될 때마다 클라이언트 수를 증가시킴
    }

    @Override
    public void run() {

        try {

            while (true) {

//                if(!FileServer.isRunning) continue;

                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String message;
                while ((message = reader.readLine()) != null) {  // null 체크를 통해 스트림의 끝을 확인
                    message = message.trim();  // null이 아닐 때만 trim() 호출

                    if (!message.isEmpty()) {
                        System.out.println("FileHandler Received message: " + message);

                        // 여기서 받은 메시지에 대한 추가 작업을 할 수 있습니다
                    }


                String clientIp = clientSocket.getInetAddress().getHostAddress();
                String[] ipParts = clientIp.split("\\.");  // IP 주소를 '.'으로 분리
                String lastPart = ipParts[ipParts.length - 1];  // 마지막 부분을 가져옴
                String lastThreeDigits = lastPart.length() > 3 ? lastPart.substring(lastPart.length() - 3) : lastPart;


                System.out.println("FileHandler End");

                Thread.sleep(5);
                // 클라이언트 완료 처리
                roundManager.clientCompleted();  // 라운드 매니저에 완료 알림
                    OutputStream outputStream = clientSocket.getOutputStream();
                    String msg = Integer.toString(roundManager.getRound());
                    outputStream.write(msg.getBytes());
                    outputStream.flush();
            }

        }
    } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }}
