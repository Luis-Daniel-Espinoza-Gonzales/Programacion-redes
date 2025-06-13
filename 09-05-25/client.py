import socket
import sys

hostname = socket.gethostname()
ip = socket.gethostbyname(hostname)
print("El nombre de su computadora es: " + hostname)
print("La dirección IP de su computadora es: " + ip)

#Crea socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)   #AF.INET = IPv4, SOCK_STREAM = TCP.
client_socket.connect((ip, 12345))  #se conecta a una ip, y un puerto definido que se usa para conectarse

#Recibe pregunta del servidor
respuesta = client_socket.recv(1024).decode()
print(respuesta)

#Recibe pregunta del servidor
pregunta_00 = client_socket.recv(1024).decode()
respuesta_00 = input(pregunta_00 + " ")

#Envio respuesta
client_socket.send(respuesta_00.encode())

#Recibe pregunta del servidor
pregunta_01 = client_socket.recv(1024).decode()
respuesta_01 = input(pregunta_01 + " ")

#Envio respuesta
client_socket.send(respuesta_01.encode())

mensaje_00 = client_socket.recv(1024).decode()
print(mensaje_00)

#cierre de conexion
client_socket.close()