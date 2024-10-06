package org.example;

import org.example.RoundManager;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class MasterHandler implements Runnable {
    private final Socket clientSocket;
    public static int round;
    private static CountDownLatch latch;
    private List<Socket> clientList;
    private RoundManager roundManager;

    public MasterHandler(Socket socket, RoundManager roundManager, List<Socket> clientList) {
        this.clientSocket = socket;
        this.roundManager = roundManager;
        this.clientList = clientList;
        latch = new CountDownLatch(clientList.size());
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while (true) {
                // 마스터로부터 라운드 수신
                String message = reader.readLine();
                if (message != null) {
                    message = message.trim();
                    System.out.println("[MasterHandler] : Server.py로 부터 라운드를 받았습니다: " + message);
                    round = Integer.parseInt(message);
                }

                System.out.println("================ round :" + round + " ================");

                // 모든 클라이언트가 완료될 때까지 대기
                latch.await();

                // Server.py 에 완료 신호 전송
                write(clientSocket, Integer.toString(roundManager.getConnectedClients()));
                write(clientSocket, "complete");
                System.out.println("[MasterHandler] : Server.py 에게 완료 신호를 보냈습니다.");

                // 다음 라운드로 진행
                latch = new CountDownLatch(clientList.size());
            }

        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void write(Socket clientSocket, String msg) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }

    public static void countDown() {
        latch.countDown();
    }
}
