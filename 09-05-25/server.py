import socket
import sys

clave = {
    "usuario" : "daniel",
    "contraseña" : "den1"
}

#Obtetncion de nombre e IP del equipo
hostname = socket.gethostname() #Obtiene el nombre del equipo
ip = socket.gethostbyname(hostname) #Traduce el nombre en una IP
print("El nombre de su computadora es: " + hostname)
print("La dirección IP de su computadora es: " + ip)

#Configuraciones del servidor
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   #AF.INET = protocolo IPv4, SOCK_STREAM = tipo de socket TCP.
server_socket.bind((ip, 12345)) #se asigna ip y puerto definido donde se escucha
server_socket.listen(1) #Indica cuantas conexiones puede mantener

print("server escuchando en el puerto 12345")

while True:

    #Crea socket
    client_socket, address = server_socket.accept()
    print("se establecio una conexion con " + str(address))

    #Envio saludo
    client_socket.send("hola, te envio saludo desde el servidor.".encode())

    #Envio pregunta
    pregunta_00 = "Ingrese su usuario:"
    client_socket.send(pregunta_00.encode())

    #Recibir repuesta
    respuesta_00 = client_socket.recv(1024).decode()
    print("Usuario del cliente: ", respuesta_00)

    #Envio pregunta
    pregunta_01 = "Ingrese su contraseña:"
    client_socket.send(pregunta_01.encode())

    #Recibir repuesta
    respuesta_01 = client_socket.recv(1024).decode()
    print("Contraseña del cliente: ", respuesta_01)

    #verifica el usuario y contraseña
    if respuesta_00 == clave["usuario"] and respuesta_01 == clave["contraseña"]:
        print("acceso concedido.")
        client_socket.send("Acceso concedido. Bienvenido.".encode())
    else:
        print("acceso denegado.")
        client_socket.send("Acceso denegado. Cerrando conexión.".encode())
        client_socket.close()

    #Cierre de conexion
    client_socket.close()