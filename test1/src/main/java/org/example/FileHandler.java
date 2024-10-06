package org.example;

import org.example.MasterHandler;
import org.example.RoundManager;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class FileHandler implements Runnable {
    private final Socket clientSocket;
    private RoundManager roundManager;

    public FileHandler(Socket socket, RoundManager roundManager) {
        this.clientSocket = socket;
        this.roundManager = roundManager;
        this.roundManager.clientConnected();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            while (true) {
                write(clientSocket, Integer.toString(MasterHandler.round));
                System.out.println("[FileHandler] : 클라이언트에게 라운드 수 정보를 줍니다. : " + MasterHandler.round);

                String msg = reader.readLine().trim();
                System.out.println("[FileHandler] : 클라이언트로부터 온 데이터 : " + msg);

                if (msg.equals("complete")) {
                    roundManager.clientCompleted();
                    MasterHandler.countDown();  // 클라이언트 완료 시 카운트 감소
                    break;
                }
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
