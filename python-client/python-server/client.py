import socket
import multiprocessing


server_address = ('10.3.129.180', 30007)


def send():

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(server_address)

        # 서버 정보 출력
        server_info = s.getpeername()  # 서버의 IP 주소와 포트를 반환
        print(f"Connected to server at {server_info}")

        for i in range(100):
            msg = f"{i}\n"
            s.sendall(msg.encode('utf-8'))  # 0을 전송 (예시)

            print("File transfer complete.")
            response = s.recv(1024)
            print("Response from server:", response.decode('utf-8'))
            print("complete")


if __name__ == "__main__":
    processes = []
    for _ in range(3):
        p = multiprocessing.Process(target=send)
        processes.append(p)
        p.start()

    for p in processes:
        p.join()
