import socket
import time

server_address = ('10.3.129.180', 9090)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(server_address)
id = ""


def getId():
    data = s.recv(1024).decode('utf-8')
    id = data
    print(f"client id : {id}")

def Learning():
    time.sleep(5)

def getUpdatedPT(): # 여기서 end 메시지도 받음
    msg = s.recv(1024).decode('utf-8')
    
    if(msg=="end"):
        return "end"
    
    print(msg)
    msg = "done learning\n"
    s.sendall(msg.encode('utf-8'))

if __name__ == "__main__":
    getId()
    while True:
        t = getUpdatedPT()
        if(t=="end"):
            print("종료 코드 수신")
            s.sendall(msg.encode('utf-8'))
            break
        
