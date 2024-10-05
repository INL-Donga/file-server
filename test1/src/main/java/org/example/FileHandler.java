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


                System.out.println("test2");
                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String message = reader.readLine().trim();  // 메시지 수신
                System.out.println("Received message: " + message);

                String clientIp = clientSocket.getInetAddress().getHostAddress();
                String[] ipParts = clientIp.split("\\.");  // IP 주소를 '.'으로 분리
                String lastPart = ipParts[ipParts.length - 1];  // 마지막 부분을 가져옴
                String lastThreeDigits = lastPart.length() > 3 ? lastPart.substring(lastPart.length() - 3) : lastPart;


                System.out.println("test");

                // 클라이언트 완료 처리
                roundManager.clientCompleted();  // 라운드 매니저에 완료 알림
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 고유 파일 이름 생성 (원래 이름 + 타임스탬프)
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return originalFileName.substring(0, dotIndex) + "_" + timestamp + originalFileName.substring(dotIndex);
        } else {
            return originalFileName + "_" + timestamp;
        }
    }
}
