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
            write(clientSocket,Integer.toString(roundManager.getRound()) );

            System.out.println("[FileHandler] : 클라이언트에게 라운드 수 정보를 줍니다. : "+ roundManager.getRound());
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            while (true) {


                if(!FileServer.isRunning) continue;



                String message = reader.readLine();
                if(message != null){
                    message = message.trim();
                    System.out.println("[FileHandler] : 클라이언트로부터 데이터를 읽었습니다 : " + message);
                }


//                String clientIp = clientSocket.getInetAddress().getHostAddress();
//                String[] ipParts = clientIp.split("\\.");  // IP 주소를 '.'으로 분리
//                String lastPart = ipParts[ipParts.length - 1];  // 마지막 부분을 가져옴
//                String lastThreeDigits = lastPart.length() > 3 ? lastPart.substring(lastPart.length() - 3) : lastPart;

                Thread.sleep(5);
                // 클라이언트 완료 처리
                roundManager.clientCompleted();  // 라운드 매니저에 완료 알림
                write(clientSocket,Integer.toString(1));
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(Socket clientSocket, String msg) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }

}
