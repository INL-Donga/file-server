package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class FileHandler implements Runnable {
    private Socket clientSocket;
    private static final String BASE_DIRECTORY = "/mnt/parameters"; // 볼륨 마운트 경로


    public FileHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        long fileSize = 0;
        String originalFileName = "";

        try {
            // 클라이언트로부터 파일을 수신
            InputStream inputStream = clientSocket.getInputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8");

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
            System.out.println(getNow() + " File received: " + uniqueFileName);
            System.out.println(getNow() + " File size: " + fileSize + " bytes");
            System.out.println(getNow() + " Client IP: " + clientSocket.getInetAddress().getHostAddress());
            System.out.println(getNow() + " Client Port: " + clientSocket.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 고유 파일 이름 생성 (원래 이름 + 타임스탬프)
    private String generateUniqueFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex != -1) {
            // 확장자가 있을 경우, 파일명 + 타임스탬프 + 확장자 형태로 파일 이름을 생성
            return originalFileName.substring(0, dotIndex) + "_" + timestamp + originalFileName.substring(dotIndex);
        } else {
            // 확장자가 없을 경우, 파일명 + 타임스탬프
            return originalFileName + "_" + timestamp;
        }
    }

    private String getNow(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss"));
        return "[" + now.format(formatter) + "]";
    }
}
