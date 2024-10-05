import socket
import time


def send_msg_from_server():
    server_address = ('localhost', 9090)  # 서버 주소 및 포트 설정
    i = 0
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(server_address)

        for i in range(100):
            msg = f"{i}\n"
            s.sendall(msg.encode('utf-8'))  # 0을 전송 (예시)
            while (1):
                time.sleep(5)

                response1 = s.recv(1024)  # 최대 1024바이트 수신
                print(4)
                complete_message = response1.decode('utf-8').strip()  # 문자열 디코딩
                print(f"첫 번째 메시지: {complete_message}")
                i = i+1
                # 두 번째 데이터 (roundManager.getConnectedClients())
                response2 = s.recv(1024)  # 최대 1024바이트 수신 (크기에 맞게 수정 가능)
                connected_clients = int.from_bytes(
                    response2, 'big')  # 바이트 데이터를 정수로 변환
                print(f"연결된 클라이언트 수: {connected_clients}")


if __name__ == "__main__":
    send_msg_from_server()
