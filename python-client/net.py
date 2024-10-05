import socket

file_path = 'model_1_state_dict.pt'
file_name = file_path.split('/')[-1]
server_address = ('localhost', 8080)

print("Sending file:", file_name)

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect(server_address)

    s.sendall((file_name + '\n').encode('utf-8'))

    # with open(file_path, 'rb') as file:
    #     buffer = file.read(8192)
    #     while buffer:
    #         s.sendall(buffer)
    #         buffer = file.read(8192)

    print("File transfer complete.")
    response = s.recv(1024)
    print("complete")
