package com.smarthome.smarthome_budget.basedatos;
// Importación de clases necesarias para la conexión con la base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /* Clase: claseConexión.
       Propósito: Gestionar la conexión física entre la aplicación Java y el motor de base de datos MySQL.
    */

public class claseConexion 
{
    // Atributos
     // URL de conexión a la base de datos MySQL (incluye host, puerto y nombre de la BD)
    static String BD = "jdbc:mysql://localhost:3306/SmartHome_BudgetBD";
    // Usuario para acceder a la base de datos
    static String usuario = "root";
    // Contraseña del usuario de la base de datos
    static String password = "Sali20302002";

    /* Método: MetodoConectar
        Propósito: Establecer una conexión con la base de datos MySQL utilizando
        los parámetros definidos en los atributos de la clase.
        @return Connection -> Retorna un objeto Connection si la conexión es exitosa,
        o null si ocurre algún error.
    */
    public static Connection MetodoConectar() 
    {
        try 
        {
            // Se carga dinámicamente el driver de MySQL necesario para la conexión.
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Verifica si los datos de conexión están vacíos antes de intentar conectar
            if (BD.isEmpty() && usuario.isEmpty() && password.isEmpty()) 
            {
                System.out.println("Los datos de conexión están incompletos");
            }
            
            // Intenta conectarse a la base de datos con los datos proporcionados
            // Si la conexión es exitosa, retorna el objeto Connection activo
            return DriverManager.getConnection(BD, usuario, password);

        } 
        catch (SQLException error) 
        {
            // Captura errores relacionados con la base de datos (credenciales, BD inexistente, etc.)
            System.err.println("Error de SQL: " + error.getMessage());
            // Imprime el detalle completo del error para depuración
            error.printStackTrace();
            // Retorna null indicando que la conexión falló
            return null;
        }
        catch (ClassNotFoundException e)
        {
            // Ocurre si el driver de MySQL no está disponible en el proyecto.
            System.err.println("Error: No se encontró el Driver de MySQL: " + e.getMessage());
            // Retorna null indicando que no se pudo establecer la conexión.
            return null;
        }
    }
}
