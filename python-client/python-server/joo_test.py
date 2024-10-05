import socket

server_address = ('10.3.129.169', 9090)

try:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(server_address)
    print('서버와 연결되었습니다.')

    round = 0

    while True:
        try:
            data = s.recv(1024)
            if not data:
                print('서버로부터 데이터를 수신할 수 없습니다.')
                break

            receive_round = int(data.decode('utf-8'))

            if receive_round != round:
                print("라운드 동기 오류")
            else:
                print(f"현재 라운드: {round}")

            round += 1

        except socket.error as e:
            print(f"소켓 오류 발생: {e}")

except socket.error as e:
    print(f"서버와의 연결에 실패했습니다: {e}")

finally:
    if s:
        s.close()
        print("소켓 닫음")
