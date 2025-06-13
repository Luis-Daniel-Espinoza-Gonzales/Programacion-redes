import socket
import threading

hostname = socket.gethostname() #Obtiene el nombre del equipo
HOST = socket.gethostbyname(hostname)
PORT = "12345"

def handle_client(conn, addr):
    while True:
        data = conn.recv(1024).decode()

        if data.lower() == "exit":
            print(f"El cliente {addr} ha terminado la conexión.")
            break

        message = input("Escribir mensaje al cliente: ")
        conn.sendall(message.encode())

        if message.lower() == "exit":
            print("Conexión terminada a petición del servidor.")
            break

def start_server():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()
        print("El servidor esta escuchando en", (HOST, PORT))

        while True:

            conn, addr = s.accept()
            print(f"Conectado desde {addr}")

            client_thread = threading.Thread(target=handle_client, args=(conn, addr))
            client_thread.start()
            client_thread.join()

start_server()