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

            Class.forName("oracle.jdbc.driver.OracleDriver");


            // Establecer la conexión a la base de datos
            conn = DriverManager.getConnection(url, user, password);
            // Crear una declaración SQL para ejecutar comandos
            stmt = conn.createStatement();
            
            // Menú principal del programa
            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Mostrar contenido de las tablas");
                System.out.println("2. Dar de alta pedido");    
                System.out.println("3. Añadir 10 tuplas a Stock");
                System.out.println("4. Eliminar tuplas de Stock");        
                System.out.println("5. Salir");
                // Leer la opción ingresada por el usuario
                int opcion = Integer.parseInt(System.console().readLine());
                
                switch (opcion) {
                    case 1:
                        // Mostrar todos los registros de la tabla
                        rs = stmt.executeQuery("SELECT * FROM stock, detallepedido, pedido)");
                        while (rs.next()) {
                            System.out.println(rs.getString(1) + " " + rs.getString(2));
                        }
                        break;
                    case 2:
                        // Dar de alta un nuevo pedido en la tabla
                        System.out.println("Introduzca el código del producto:");
                        String campo1 = System.console().readLine();
                        System.out.println("Introduzca el código de cliente:");
                        String campo2 = System.console().readLine();
                        stmt.executeUpdate("INSERT INTO pedido VALUES ('" + campo1 + "', '" + campo2 + "')");
                        break;
                    case 3:
                        // Añadir 10 tuplas a Stock
                        for (int i = 0; i < 10; i++) {
                            stmt.executeUpdate("INSERT INTO stock VALUES ('"'P' + i + "', '" + i + "')");
                        }
                        
                    case 4:
                        // Eliminar tuplas de Stock
                        System.out.println("Introduzca el código producto:");
                        String campo1 = System.console().readLine();
                        System.out.println("Introduzca la cantidad:");
                        String campo2 = System.console().readLine();
                        stmt.executeUpdate("DELETE FROM stock WHERE cproducto = '" + campo1 + "' AND cantidad = '" + campo2 + "'");
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
