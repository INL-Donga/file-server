package org.example;

import javax.naming.ldap.SortKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static final int client_count = 1;

    public static int client_id = 1;


    public static void main(String[] args) throws IOException {

         MasterHandler masterHandler = null;
         List<FileHandler> fileHandlerList = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(9090)) {
            System.out.println("[FileServer] : Server started. Waiting for connections...");


            for(int i =0; i<2;i++){
                Socket clientSocket = serverSocket.accept();

                if(clientSocket.getInetAddress().getHostAddress().equals("127.0.0.1")){
                    masterHandler = new MasterHandler(clientSocket,client_count) ;
                }
                else{
                    FileHandler fileHandler = new FileHandler(clientSocket);
                    fileHandlerList.add(fileHandler);

                    // 클라이언트별로 고유번호 할당하기
                    fileHandler.sendClientId(client_id);
                    client_id++;
                }
                System.out.println("[FileServer] : Accepted Socket: " + clientSocket.getInetAddress());
            }




        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("진짜 시작");

        int exit_code = 0;
        while(true){
            // 시작 메시지가 온 것을 확인

            while(true){
                String msg =masterHandler.getMessage();

                if(msg.equals("start")){    // mp.deploy_weight
                    for(FileHandler fileHandler : fileHandlerList){
                        fileHandler.sendUpdatePt("fileName");   // cp.getUpdatePT
                    }
                    break;
                }

                else if(msg.equals("end")){
                    for(FileHandler fileHandler : fileHandlerList){
                        fileHandler.sendEnd();
                    }
                    exit_code = 1;
                    break;
                }
            }

            if(exit_code == 1) {
                for(FileHandler fileHandler : fileHandlerList){
                    fileHandler.getEnd();
                }
            }

            // 클라이언트로부터 학습 완료 메시지 받기
            for(FileHandler fileHandler : fileHandlerList){
                String msg = fileHandler.getCompleteMessage();

            }


            // 볼륨에 새로운 가중치 파일이 있다고 MP에게 알린다. (mp.average_weight)
            masterHandler.alertMP("uploaded weight file");

            // MP에서 가중치 평균화 끝내는 것을 기다린다. mp.average_weight
            String msg = masterHandler.getCompleteMessage();
            System.out.println("가중치 계산 끝남!" + msg);
        }

    }
}