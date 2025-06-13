import mysql.connector

conexion = None
try:
    conexion = mysql.connector.connect(user = 'root', password = '', host = 'localhost', database = 'chat_26')
    print('se ha conectado a mysql')
except Exception as e:
    print(f'fallo en la conexion a mysql: {e}')