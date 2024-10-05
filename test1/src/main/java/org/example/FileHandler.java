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
        long fileSize = 0;
        String originalFileName = "";

        try {
            // localhost 의 .py 로 부터 .pt 업로드 완료 수신
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            int number = Integer.parseInt(message);

            // 클라이언트로부터 텍스트(Round Number)를 수신
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String message = in.readLine();

//            // 클라이언트로부터 파일을 수신
//            InputStream inputStream = clientSocket.getInputStream();
//            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
//
//            // 파일 이름을 구분자 (줄바꿈 '\n') 까지 읽어들임
//            if (scanner.hasNextLine()) {
//                originalFileName = scanner.nextLine().trim();  // 파일 이름 수신
//            }
            // Client IP 정보 수집
            String clientIp = clientSocket.getInetAddress().getHostAddress();
            String[] ipParts = clientIp.split("\\.");  // IP 주소를 '.'으로 분리
            String lastPart = ipParts[ipParts.length - 1];  // 마지막 부분을 가져옴

// 마지막 3자리만 가져옴 (3자리가 안 될 경우는 그대로 출력)
            String lastThreeDigits = lastPart.length() > 3 ? lastPart.substring(lastPart.length() - 3) : lastPart;
            // Python 통신 코드



//            FileName inputFile = new FileName("input",lastThreeDigits, roundManager.getRound());
//
//            // 고유 파일 이름 생성
//            String uniqueFileName = inputFile.getFileName();
//            System.out.println("Saved as: " + uniqueFileName);
//
//            // 파일 저장
//            File directory = new File(BASE_DIRECTORY);
//            if (!directory.exists()) {
//                directory.mkdirs(); // 폴더가 존재하지 않으면 생성
//            }

//            FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, uniqueFileName));
//
//            byte[] buffer = new byte[8192];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, bytesRead);
//                fileSize += bytesRead; // 파일 크기 측정
//            }
//
//            fileOutputStream.close();

            // 응답 코드
            OutputStream outputStream = clientSocket.getOutputStream();
            String msg = "ok";
            outputStream.write(msg.getBytes());
            outputStream.flush();

//            clientSocket.close();

            System.out.println("test");

            // 클라이언트 완료 처리
            roundManager.clientCompleted();  // 라운드 매니저에 완료 알림

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
