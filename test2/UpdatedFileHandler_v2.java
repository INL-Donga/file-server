
package org.example;

import java.io.*;
import java.net.Socket;

public class FileHandler implements Runnable {

    private Socket clientSocket;
    private RoundManager roundManager;

    public FileHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.roundManager = RoundManager.getInstance();  // RoundManager 인스턴스 가져오기
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String message;

            while (true) {
                // 라운드 정보 전송
                writer.println("Round " + roundManager.getRound());
                System.out.println("Sent round info to client: " + roundManager.getRound());

                // 클라이언트가 작업을 완료했는지 확인
                message = reader.readLine();
                if (message != null && message.trim().equals("complete")) {
                    System.out.println("Client completed round: " + roundManager.getRound());
                    roundManager.clientCompleted();
                    break;  // 클라이언트 작업 완료 시 종료
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
