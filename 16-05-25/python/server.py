import socket
import threading
from conexion import crear_conexion

hostname = socket.gethostname() #Obtiene el nombre del equipo
HOST = socket.gethostbyname(hostname)
PORT = 12345

clientes_conectados = []

def handle_client(client, addr, conexion_db):

    cursor = conexion_db.cursor()
    client.send(b"\n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")
    usuario_actual = None

    while True:

        try:
            option = client.recv(1024).decode().strip()
            print(f"DEBUG - option recibida: '{option}'")

            if option == "1":
                client.send(b"Ingrese su nickname: ")
                nickname = client.recv(1024).decode().strip()

                client.send(b"Ingrese su password: ")
                password = client.recv(1024).decode().strip()

                cursor.execute("SELECT id FROM usuarios WHERE nickname = %s AND password = %s", (nickname, password))
                resultado = cursor.fetchall()

                if resultado:
                    usuario_actual = resultado[0][0]
                    client.send(b"Login exitoso. \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")
                else:
                    client.send(b"Credenciales incorrectas. \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")

            elif option == "2":
                if not usuario_actual:
                    client.send(b"Primero inicie sesion. \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")
                    continue

                client.send(b"Ingrese el nickname del receptor: ")
                receptor = client.recv(1024).decode().strip()

                cursor.execute("SELECT id FROM usuarios WHERE nickname = %s", (receptor,))
                receptor_resultado = cursor.fetchall()
                id_receptor = receptor_resultado[0][0]

                if receptor_resultado:

                    id_receptor = receptor_resultado[0][0]
                    client.send(b"Ingrese un mensaje: ")
                    mensaje = client.recv(1024).decode().strip()
                    cursor.execute("INSERT INTO chat (id_usuario, receptor, mensaje) VALUES (%s, %s, %s)", (usuario_actual, id_receptor, mensaje))
                    conexion_db.commit()    #Guarda los cambios de forma permanente en la base de datos
                    client.send(b"Mensaje enviado \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")
                
                else:
                    client.send(b"Receptor no encontrado. \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")

            elif option == "3":
                if not usuario_actual:
                    client.send(b"Primero inicie sesion. \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")
                    continue

                client.send(b"Ingrese el mensaje para todos: ")
                mensaje = client.recv(1024).decode().strip()

                cursor.execute("SELECT id FROM usuarios WHERE id != %s", (usuario_actual,))
                todos = cursor.fetchall()

                for receptor_id in todos:
                    cursor.execute("INSERT INTO chat (id_usuario, receptor, mensaje) VALUES (%s, %s, %s)", (usuario_actual, receptor_id[0], mensaje))
                conexion_db.commit()
                client.send(b"Mensaje enviados a todos \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")

            elif option == "4":
                client.send(b"Sesion finalizada. Cerrando conexion... \n")
                break

            elif option not in ["1", "2", "3", "4"]:
                client.send(b"Opcion no valida. Por favor, elija una opcion valida. \n \n=== Bienvenido al Chat ===\n1. Login\n2. Enviar mensaje a un usuario\n3. Enviar mensaje a todos\n4. Salir\nElige una opcion: ")
                continue

        except Exception as e:
            print(f"Error con {addr}: {e}")
            break

def start_server():
    conexion_db = crear_conexion()

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()
        print("El servidor esta escuchando en", (HOST, PORT))

        while True:

            client, addr = s.accept()
            print(f"Cliente conectado desde {addr}")

            client_thread = threading.Thread(target=handle_client, args=(client, addr, conexion_db))
            client_thread.start()
            clientes_conectados.append(client)

start_server()