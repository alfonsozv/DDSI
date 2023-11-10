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
                System.out.println("1. Añadir detalle de producto");
                System.out.println("2. Eliminar todos los detalles del producto");
                System.out.println("3. Cancelar pedido");
                System.out.println("4. Finalizar pedido");
                System.out.println("5. Salir");

                // Leer la opción ingresada por el usuario
                int opcion = Integer.parseInt(System.console().readLine());

                switch (opcion) {
                    case 1:
                        // Añadir detalle de producto
                        System.out.println("Introduzca el código del producto:");
                        String productCode = System.console().readLine();
                        System.out.println("Introduzca la cantidad:");
                        int quantity = Integer.parseInt(System.console().readLine());

                        // Comprobar si hay suficiente stock
                        rs = stmt.executeQuery("SELECT stock FROM stock WHERE product_code = '" + productCode + "'");
                        if (rs.next()) {
                            int stock = rs.getInt(1);
                            if (stock >= quantity) {
                                // Actualizar Stock
                                stmt.executeUpdate("UPDATE stock SET stock = stock - " + quantity + " WHERE product_code = '" + productCode + "'");

                                // Insert en Detalle-Pedido
                                stmt.executeUpdate("INSERT INTO detallepedido (product_code, quantity) VALUES ('" + productCode + "', " + quantity + ")");
                            } else {
                                System.out.println("No hay suficiente stock para el producto.");
                            }
                        } else {
                            System.out.println("El producto no existe.");
                        }
                        break;
                    case 2:
                        // Eliminar todos los detalles del producto
                        System.out.println("Introduzca el código del producto:");
                        String productCodeToDelete = System.console().readLine();
                        stmt.executeUpdate("DELETE FROM detallepedido WHERE product_code = '" + productCodeToDelete + "'");
                        break;
                    case 3:
                        // Añadir 10 tuplas a Stock
                        for (int i = 0; i < 10; i++) {
                            stmt.executeUpdate("INSERT INTO stock VALUES ('P" + i + "', " + i + ")");
                        }
                        break; // Se agregó la declaración break para salir del case 3
                    case 4:
                        // Eliminar tuplas de Stock
                        System.out.println("Introduzca el código producto:");
                        String codeToDelete = System.console().readLine();
                        System.out.println("Introduzca la cantidad:");
                        int quantityToDelete = Integer.parseInt(System.console().readLine());
                        stmt.executeUpdate("DELETE FROM stock WHERE product_code = '" + codeToDelete + "' AND stock = " + quantityToDelete);
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
        } catch (ClassNotFoundException | SQLException e) {
            // Manejar errores de carga del controlador y SQL
            System.out.println("Error: " + e.getMessage());
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
