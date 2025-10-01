#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <asm-generic/socket.h>


#define PORT 8080


int main(int argc, char const* argv[]){
   int server_fd, new_socket;
   ssize_t valread;
   struct sockaddr_in address;
   int opt = 1;
   socklen_t addrlen = sizeof(address);
   char buffer[1024] = {0};
   char* hello = "Bienvenido al server";


   //Crea el socket (file descriptor)
   if((server_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
       perror("socket fallo");
       exit(EXIT_FAILURE);
   }


   if(setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt, sizeof(opt))) {
       perror("setsockop");
       exit(EXIT_FAILURE);
   }


   address.sin_family = AF_INET;
   address.sin_addr.s_addr = INADDR_ANY;
   address.sin_port = htons(PORT);


   if(bind(server_fd, (struct sockaddr*)&address, sizeof(address)) < 0) {
       perror("falloo");
       exit(EXIT_FAILURE);
   }


   if(listen(server_fd, 3) < 0) {
       perror("listen");
       exit(EXIT_FAILURE);
   }


   while (1) {
       if((new_socket = accept(server_fd, (struct sockaddr*)&address, &addrlen)) < 0) {
           perror("accept");
           exit(EXIT_FAILURE);
       }


       send(new_socket, hello, strlen(hello), 0);
       printf("Mensaje de bienvenida enviado\n");


       while (1) {
           //Recibe mensajes del cliente
           memset(buffer, 0, sizeof(buffer));
           ssize_t valread = read(new_socket, buffer, sizeof(buffer) - 1);


           if (valread <= 0) {
               printf("Cliente desconectado\n");
               break;
           }
           //Imprime el mensaje del cliente
           printf("%s\n", buffer);


           //Envio de mensaje al cliente
           char* mensaje = malloc(1);  //Empieza con 1 byte
           int tamaño = 0;
           char c;


           printf("Ingrese mensaje: ");
           while((c = getchar()) != '\n') {
               mensaje = realloc(mensaje, tamaño + 2);
               mensaje[tamaño++] = c;  //guarda caracter
           }


           mensaje[tamaño] = '\0'; //finaliza la cadena
           send(new_socket, mensaje, strlen(mensaje), 0);
           free(mensaje);
       }
       close(new_socket);
       printf("esperando nuevo cliente...\n");
   }


   close(server_fd);
   return 0;


}
