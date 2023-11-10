import java.sql.*;

public class MiPrograma {
    public static void main(String[] args) {
        // Información de conexión a la base de datos Oracle
        String url = "jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es";
        String user = "x7244926";
        String password = "x7244926";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Establecer la conexión a la base de datos
            conn = DriverManager.getConnection(url, user, password);
            // Crear una declaración SQL para ejecutar comandos
            stmt = conn.createStatement();
            
            // Menú principal del programa
            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Mostrar tabla");
                System.out.println("2. Insertar registro");
                System.out.println("3. Modificar registro");
                System.out.println("4. Eliminar registro");
                System.out.println("5. Salir");
                // Leer la opción ingresada por el usuario
                int opcion = Integer.parseInt(System.console().readLine());
                
                switch (opcion) {
                    case 1:
                        // Mostrar todos los registros de la tabla
                        rs = stmt.executeQuery("SELECT * FROM tabla");
                        while (rs.next()) {
                            System.out.println(rs.getString(1) + " " + rs.getString(2));
                        }
                        break;
                    case 2:
                        // Insertar un nuevo registro en la tabla
                        System.out.println("Introduzca el valor del campo 1:");
                        String campo1 = System.console().readLine();
                        System.out.println("Introduzca el valor del campo 2:");
                        String campo2 = System.console().readLine();
                        stmt.executeUpdate("INSERT INTO tabla VALUES ('" + campo1 + "', '" + campo2 + "')");
                        break;
                    case 3:
                        // Modificar un registro existente en la tabla
                        System.out.println("Introduzca el valor del campo 1 del registro a modificar:");
                        String campo1Antiguo = System.console().readLine();
                        System.out.println("Introduzca el valor del campo 1 nuevo:");
                        String campo1Nuevo = System.console().readLine();
                        System.out.println("Introduzca el valor del campo 2 nuevo:");
                        String campo2Nuevo = System.console().readLine();
                        stmt.executeUpdate("UPDATE tabla SET campo1='" + campo1Nuevo + "', campo2='" + campo2Nuevo + "' WHERE campo1='" + campo1Antiguo + "'");
                        break;
                    case 4:
                        // Eliminar un registro de la tabla
                        System.out.println("Introduzca el valor del campo 1 del registro a eliminar:");
                        String campo1Eliminar = System.console().readLine();
                        stmt.executeUpdate("DELETE FROM tabla WHERE campo1='" + campo1Eliminar + "'");
                        break;
                    case 5:
                        // Salir del programa
                        System.exit(0);
                        break;
                    default:
                        // Mensaje para opciones no válidas
                        System.out.println("Opción no válida");
                        break;
                }
            }
        } catch (SQLException e) {
            // Manejar errores de SQL
            System.out.println("Error de SQL: " + e.getMessage());
        } finally {
            // Cerrar recursos (ResultSet, Statement, Connection) en el bloque finally
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
