import socket
import time

server_address = ('localhost', 9090)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(server_address)


def deploy_weight():
    startmsg = "start\n"
    s.sendall(startmsg.encode('utf-8'))
    print("deploy")


def average_weight():

    print("average_weight")
    filename = s.recv(1024)
    print(filename.decode("utf-8'"))
    # load and 평균화
    filename = "global_model.pt\n"
    s.sendall(filename.encode('utf-8'))


def send_end_message():
    msg = "end"
    print(msg)
    s.sendall(msg.encode('utf-8'))


if __name__ == "__main__":

    for i in range(100):
        deploy_weight()
        average_weight()

    send_end_message()
