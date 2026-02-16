package com.smarthome.smarthome_budget.basedatos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* Clase encargada de gestionar la conexión física entre la aplicación Java y el motor de base de datos MySQL.*/
public class claseConexion 
{
    // Configuración de la cadena de conexión (URL de la base de datos local)
    static String BD = "jdbc:mysql://localhost:3306/SmartHome_BudgetBD";
    // Credenciales de acceso al servidor de base de datos
    static String usuario = "root";
    static String password = "Sali20302002";

    /* Establece una conexión con la base de datos MySQL utilizando los parámetro definidos en los atributos de la clase.*/
    public static Connection MetodoConectar() 
    {
        try 
        {
            // Cargamos el driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Validación preventiva, Verifica si las variables de configuración están inicializadas
            if (BD.isEmpty() && usuario.isEmpty() && password.isEmpty()) 
            {
                System.out.println("Los datos de conexión están incompletos");
            }
            
            // Intenta establecer la conexión con la base de datos usando los datos de acceso definidos arriba y Retorna la conexión activa.
            return DriverManager.getConnection(BD, usuario, password);

        } 
        catch (SQLException error) 
        {
            // Captura de excepciones específicas de SQL (Ej: error de contraseña, BD inexistente)
            System.err.println("Error de SQL: " + error.getMessage());
            error.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error: No se encontró el Driver de MySQL: " + e.getMessage());
            return null;
        }
    }
}
