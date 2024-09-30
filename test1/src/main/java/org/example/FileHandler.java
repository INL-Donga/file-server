package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

class FileHandler implements Runnable {
    private final Socket clientSocket;
    //    private static final String BASE_DIRECTORY = "/mnt/parameters"; // 볼륨 마운트 경로
    private static final String BASE_DIRECTORY = "D:\\INL\\backend\\test1\\parameters"; // 볼륨 마운트 경로
    private final AtomicInteger connectedClients; // 현재 연결된 클라이언트 수
    private final AtomicInteger completedClients; // 파일 전송 완료된 클라이언트 수

    public FileHandler(Socket socket, AtomicInteger connectedClients, AtomicInteger completedClients) {
        this.clientSocket = socket;
        this.connectedClients = connectedClients;
        this.completedClients = completedClients;
        connectedClients.incrementAndGet(); // 새로운 클라이언트가 연결될 때마다 클라이언트 수 증가
    }

    @Override
    public void run() {
        long fileSize = 0;
        String originalFileName = "";

        try {
            // 클라이언트로부터 파일을 수신
            InputStream inputStream = clientSocket.getInputStream();
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);

            // 파일 이름을 구분자 (줄바꿈 '\n') 까지 읽어들임
            if (scanner.hasNextLine()) {
                originalFileName = scanner.nextLine().trim();  // 파일 이름 수신
            }

            // 고유 파일 이름 생성
            String uniqueFileName = generateUniqueFileName(originalFileName);
            System.out.println("Saved as: " + uniqueFileName);

            // 파일 저장 경로 설정 (볼륨 마운트된 폴더에 저장)
            File directory = new File(BASE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs(); // 폴더가 존재하지 않으면 생성
            }

            // 파일 저장
            FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, uniqueFileName));

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                fileSize += bytesRead; // 파일 크기 측정
            }

            fileOutputStream.close();
            clientSocket.close();

            // 모니터링 정보 출력
            System.out.println(getCurrentTimestamp() + "Client IP: " + clientSocket.getInetAddress().getHostAddress());
            System.out.println(getCurrentTimestamp() + "File received: " + uniqueFileName + " (" + fileSize + " bytes)");

            // 파일 수신이 완료된 클라이언트 수 증가
            int completed = completedClients.incrementAndGet();
            System.out.println(getCurrentTimestamp() + "Completed clients: " + completed);

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

    // 시간 포맷팅 함수
    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + LocalDateTime.now().format(formatter) + "] ";
    }
}