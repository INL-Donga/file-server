
package org.example;

import java.io.*;
import java.net.Socket;

public class MasterHandler {

    private RoundManager roundManager;
    private Socket clientSocket;

    public MasterHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.roundManager = RoundManager.getInstance();  // RoundManager 인스턴스 가져오기
    }

    public void startHandling() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                // 모든 클라이언트가 완료될 때까지 대기
                roundManager.awaitCompletion();

                // 라운드 완료 시 다음 라운드로 전환
                roundManager.nextRound();

                // 모든 클라이언트 완료 신호를 마스터 서버에 전송
                writer.println("All clients completed round: " + (roundManager.getRound() - 1));
                System.out.println("[MasterHandler] : 모든 클라이언트가 완료되었습니다.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
