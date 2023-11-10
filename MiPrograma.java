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

                // Leer la opción ingresada por el usuario
                int opcion = Integer.parseInt(System.console().readLine());

                switch (opcion) {
                    case 1:
                        // Añadir detalle de producto
                        System.out.println("Introduzca el código del producto:");
                        String cproducto = System.console().readLine();
                        System.out.println("Introduzca la cantidad:");
                        int cantidadped = Integer.parseInt(System.console().readLine());

                        // Comprobar si hay suficiente stock
                        rs = stmt.executeQuery("SELECT cantidad FROM stock WHERE cproducto = '" + cproducto + "'");
                        if (rs.next()) {
                            int stock = rs.getInt(1);
                            if (stock >= cantidadped) {
                                // Actualizar Stock
                                stmt.executeUpdate("UPDATE stock SET cantidad = cantidad - " + cantidadped + " WHERE cproducto = '" + cproducto + "'");

                                // Insert en Detalle-Pedido
                                System.out.println("Introduzca el código del pedido:");
                                String nuevopedido = System.console().readLine();
                                stmt.executeUpdate("INSERT INTO detallepedido VALUES ('" + nuevopedido + "', '" + cproducto + "', " + cantidadped + ")");
                            } else {
                                System.out.println("No hay suficiente stock para el producto.");
                            }
                        } else {
                            System.out.println("El producto no existe.");
                        }
                        break;
                    case 2:
                        // Eliminar todos los detalles del producto
                        System.out.println("Introduzca el código del pedido:");
                        String pedido = System.console().readLine();
                        stmt.executeUpdate("DELETE FROM Detalle-Pedido WHERE cpedido = '" + pedido + "'");
                    case 3:
                        // Eliminar pedido y todos sus detalles
                        System.out.println("Introduzca el código del pedido:");
                        String cpedido = System.console().readLine();
                        stmt.executeUpdate("DELETE FROM Detalle-Pedido WHERE cpedido = '" + cpedido + "'");
                        stmt.executeUpdate("DELETE FROM Pedidos WHERE cpedido = '" + cpedido + "'");
                        break;
                    case 4:
                        // Hacer cambios permanentes
                        conn.commit();
                        break;
                    default:
                        // Salir del programa
                        System.exit(0);
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
