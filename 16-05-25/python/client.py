import socket

hostname = socket.gethostname() #Obtiene el nombre del equipo
HOST = socket.gethostbyname(hostname)
PORT = 12345
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((HOST, PORT))

while True:
    mensaje = client.recv(1024).decode()
    print(mensaje, end="")

    if "Cerrando" in mensaje:
        break

    entrada = input("> ")
    if not entrada.strip():
        entrada = " "
    client.send(entrada.encode())

client.close()