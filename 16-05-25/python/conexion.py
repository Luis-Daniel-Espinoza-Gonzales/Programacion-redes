import mysql.connector

def crear_conexion():
    try:
        conexion = mysql.connector.connect(
			host = "localhost",
			password = "",
			user = "root",
			database = "chat_26"
		)

        print("funciono la conexion")
    except Exception as e:
        print(f"fallo la conexion a mysql: {e}")
    
    return conexion 
'''
def mostrar_datos(conexion, nickname):
	try:
		cursor=conexion.cursor()
		query_select= "SELECT * FROM usuarios"
		cursor.execute(query_select)
		datos = cursor.fetchall()
		for row in datos:
			print(f"id: {row[0]}, nickname: {row[1]}" )
	except Exception as e:
		print(f"fallo la busqueda de select: {e}")
		

nickname = "dann"

con = crear_conexion()

mostrar_datos(con, nickname)
'''