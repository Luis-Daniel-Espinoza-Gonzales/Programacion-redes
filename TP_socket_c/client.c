#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>


#define PORT 8080


int main(int argc, char const* argv[]) {
   int status, valread, client_fd;
   struct sockaddr_in serv_addr;
   char buffer[1024] = {0};
   if ((client_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
       printf("\n Socket error \n");
       return -1;
   }


   serv_addr.sin_family = AF_INET;
   serv_addr.sin_port = htons(PORT);


   if (inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr) <= 0) {
       printf("\nDireccion invalida \n");
       return -1;
   }


   if ((status = connect(client_fd, (struct sockaddr*)&serv_addr, sizeof(serv_addr))) < 0) {
       printf("\nFalla en la conexion \n");
       return -1;
   }


   while (1) {
       //Recibe mensajes del servidor
       memset(buffer, 0, sizeof(buffer));
       ssize_t valread = read(client_fd, buffer, sizeof(buffer) - 1);


       if (valread <= 0) {
           printf("Servidor desconectado\n");
           break;
       }
       //Imprime el mensaje del servidor
       printf("%s\n", buffer);


       //Envio de mensajes al servidor
       char* mensaje = malloc(1);  //Empieza con 1 byte
       int tamaño = 0;
       char c;


       printf("Ingrese mensaje: ");
       while((c = getchar()) != '\n') {
           mensaje = realloc(mensaje, tamaño + 2);
           mensaje[tamaño++] = c;  //guarda caracter
       }


       mensaje[tamaño] = '\0'; //finaliza la cadena
       send(client_fd, mensaje, strlen(mensaje), 0);
       free(mensaje);
   }


   // closing the connected socket
   close(client_fd);
   return 0;
}
