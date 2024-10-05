import socket
import time

def send_msg_from_server():
        server_address = ('localhost', 8080)  # 서버 주소 및 포트 설정
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect(server_address)
            # 서버에 데이터를 요청하는 과정 (필요에 따라 수정 가능)
            s.sendall((0).to_bytes(1, 'big'))  # 0을 전송 (예시)

            time.sleep(5)
            
            # 첫 번째 메시지인 "complete" 문자열 수신
            response1 = s.recv(1024)  # 최대 1024바이트 수신
            complete_message = response1.decode('utf-8').strip()  # 문자열 디코딩
            print(f"첫 번째 메시지: {complete_message}")
    
            # 두 번째 데이터 (roundManager.getConnectedClients())
            response2 = s.recv(1024)  # 최대 1024바이트 수신 (크기에 맞게 수정 가능)
            connected_clients = int.from_bytes(response2, 'big')  # 바이트 데이터를 정수로 변환
            print(f"연결된 클라이언트 수: {connected_clients}")
            


send_msg_from_server()