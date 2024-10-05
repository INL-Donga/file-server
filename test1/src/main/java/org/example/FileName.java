package org.example;

public class FileName {
    private String io; // input or output
    private String client_name;
    private int round;


    public FileName(String io, String client_name, int round) {
        this.io = io;
        this.client_name = client_name;
        this.round = round;
    }

    public FileName(int round){
        this.io = "output";
        this.client_name = "master";
        this.round = round;
    }

    public void setIo(String io) {
        this.io = io;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getIo() {
        return io;
    }

    public String getClient_name() {
        return client_name;
    }

    public int getRound() {
        return round;
    }

    public String getFileName(){
        return this.io + "_" + this.client_name + "_" + this.round;
    }
}
