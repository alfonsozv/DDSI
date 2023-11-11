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
                System.out.println("1. Inserción de stock");
                System.out.println("2. Dar de alta nuevo pedido");
                System.out.println("3. Mostrar contenido de las tablas de la base de datos");
                System.out.println("4. Salir del programa");

                int op = Integer.parseInt(System.console().readLine());
                
                switch (op) {
                    case 1:
                        
                        break;
                
                    case 2:
                        System.out.println("Seleccione una opción  para el pedido:");
                        System.out.println("1. Crear nuevo pedido");
                        System.out.println("2. Añadir detalle de producto");
                        System.out.println("3. Eliminar todos los detalles del producto");
                        System.out.println("4. Cancelar pedido");
                        System.out.println("5. Finalizar pedido");

                        // Leer la opción ingresada por el usuario
                        int opcion = Integer.parseInt(System.console().readLine());

                        switch (opcion) {
                            case 1:
                                //Crear nuevo pedido
                                System.out.println("Introduzca su código de cliente:");
                                String cliente = System.console().readLine();
                                System.out.println("Introduzca el código del pedido:");
                                String newpedido = System.console().readLine();
                                System.out.println("Introduzca la fecha del pedido:");
                                String fecha = System.console().readLine();
                                
                                //Comprobar si el pedido existe
                                rs = stmt.executeQuery("SELECT cpedido FROM pedido WHERE cpedido = '" + newpedido + "'");
                                if (rs.next()) {
                                    System.out.println("El pedido ya existe.");
                                    break;
                                }

                                // Insertar en Pedidos
                                stmt.executeUpdate("INSERT INTO pedido VALUES ('" + newpedido + "', '" + cliente + "',  TO_DATE('" + fecha + "', 'DD/MM/YYYY'))");
                                System.out.println("Pedido creado correctamente.");
                                break;
                                
                            case 2:
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
                                        System.out.println("Introduzca el código de su pedido:");
                                        String nuevopedido = System.console().readLine();
                                        
                                        //El pedido debe existir para poder añadirle un producto
                                        rs = stmt.executeQuery("SELECT cpedido FROM pedido WHERE cpedido = '" + nuevopedido + "'");
                                        if (!rs.next()) {
                                            System.out.println("El pedido no existe.");
                                            break;
                                        }
                                        System.out.println("1.");  
                                        //Introducimos los detalles del pedido
                                        stmt.executeUpdate("INSERT INTO detallepedido VALUES ('" + nuevopedido + "', '" + cproducto + "', " + cantidadped + ")");
                                        System.out.println("2");
                                        System.out.println("Producto añadido correctamente al pedido.");
                                    } else {
                                        System.out.println("No hay suficiente stock para el producto.");
                                    }
                                } else {
                                    System.out.println("El producto no existe.");
                                }
                                break;
                            case 3:
                                // Eliminar todos los detalles del producto
                                System.out.println("Introduzca el código del producto:");
                                String producto = System.console().readLine();
                                stmt.executeUpdate("DELETE detallepedido WHERE cproducto = '" + producto + "'");
                                System.out.println("Producto eliminado del pedido.\n");
                                break;
                            case 4:
                                // Eliminar pedido y todos sus detalles
                                System.out.println("Introduzca el código del pedido:");
                                String cpedido = System.console().readLine();
                                stmt.executeUpdate("DELETE detallepedido WHERE cpedido = '" + cpedido + "'");
                                stmt.executeUpdate("DELETE pedido WHERE cpedido = '" + cpedido + "'");
                                System.out.println("Pedido cancelado correctamente.\n");
                                break;
                            case 5:
                                // Hacer cambios permanentes
                                conn.commit();
                                System.out.println("Pedido finalizado.\n");
                                break;
                            default:
                                // Salir del programa
                                System.exit(0);
                                break;
                        }  
                        break;

                    case 3:
                        // Mostrar todas las tablas

                        System.out.println("1.  Para mostrar el contenido de la tabla stock");
                        System.out.println("2.  Para mostrar el contenido de la tabla DetallePedido");
                        System.out.println("3.  Para mostrar el contenido de la tabla Pedidos");

                        int caso = Integer.parseInt(System.console().readLine());

                        switch(caso) {

                            case 1:
                            // Mostrar contenido de la tabla stock
                            System.out.println("Contenido de la tabla stock:");
                            rs = stmt.executeQuery("SELECT * FROM stock");
                            while (rs.next()) {
                                System.out.println("Código de Producto: " + rs.getString("cproducto") +
                                                ", Cantidad en Stock: " + rs.getInt("cantidad"));
                            }
                            System.out.println();
                            break;

                            case 2:
                            // Mostrar contenido de la tabla DetallePedido
                            System.out.println("Contenido de la tabla DetallePedido:");
                            rs = stmt.executeQuery("SELECT * FROM detallepedido");
                            while (rs.next()) {
                                System.out.println("Código de Pedido: " + rs.getString("cpedido") +
                                                ", Código de Producto: " + rs.getString("cproducto") +
                                                ", Cantidad: " + rs.getInt("cantidad"));
                            }
                            System.out.println();
                            break;

                            case 3:
                            // Mostrar contenido de la tabla Pedidos
                            System.out.println("Contenido de la tabla Pedidos:");
                            rs = stmt.executeQuery("SELECT * FROM pedido");
                            while (rs.next()) {
                                System.out.println("Código de Pedido: " + rs.getString("cpedido") +
                                                ", Código de Cliente: " + rs.getString("ccliente") + ", Fecha: " + rs.getDate("fecha"));
                            }
                            System.out.println();
                            break;

                        };
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
