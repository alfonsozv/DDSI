import java.sql.*;

public class MiPrograma {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es";
        String user = "x7244926";
        String password = "x7244926";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Reincio de tablas y stock");
                System.out.println("2. Dar de alta nuevo pedido");
                System.out.println("3. Mostrar contenido de las tablas de la base de datos");
                System.out.println("4. Salir del programa\n");

                int op = Integer.parseInt(System.console().readLine());

                switch (op) {
                    case 1:    
                    // Borrar las tablas si existen
                        
                        System.out.println("Borrando tablas...");
                        stmt.executeUpdate("DROP TABLE detallepedido");
                        stmt.executeUpdate("DROP TABLE pedido");
                        stmt.executeUpdate("DROP TABLE stock");
                        System.out.println("Tablas borradas correctamente.\n");
                        
                        // Crear las tablas
                        System.out.println("Creando tablas...");
                        stmt.executeUpdate("CREATE TABLE stock(cproducto VARCHAR2(15) CONSTRAINT cpro_clave_primaria PRIMARY KEY, cantidad NUMBER(5))");
                        stmt.executeUpdate("CREATE TABLE pedido(cpedido VARCHAR2(15) CONSTRAINT cped_clave_primaria PRIMARY KEY, cliente VARCHAR2(15) CONSTRAINT cliente_obligatorio NOT NULL, fechapedido DATE default sysdate)");
                        stmt.executeUpdate("CREATE TABLE detallepedido(cpedido REFERENCES pedido(cpedido), cproducto REFERENCES stock(cproducto), cantidad NUMBER(5), PRIMARY KEY(cpedido, cproducto))");
                        System.out.println("Tablas creadas correctamente.\n");
                           
                        // Insertar 10 tuplas predefinidas en la tabla Stock
                        System.out.println("Insertando tuplas predefinidas en la tabla Stock...");
                        for (int i = 1; i <= 10; i++) {
                            stmt.executeUpdate("INSERT INTO stock VALUES ('P" + i + "', " + (i * 10) + ")");
                        }
                        System.out.println("Tuplas insertadas correctamente\n");
                        break;
                
                        case 2:
                        System.out.println("Seleccione una opción para el pedido:");
                        int opcionPedido = Integer.parseInt(System.console().readLine());
                        Savepoint savepoint1 = null;
                        
                        try {
                            switch (opcionPedido) {
                                case 1:
                                    // Crear nuevo pedido
                                    System.out.println("Introduzca su código de cliente:");
                                    String cliente = System.console().readLine();
                                    System.out.println("Introduzca el código del pedido:");
                                    String newpedido = System.console().readLine();
                                    System.out.println("Introduzca la fecha del pedido:");
                                    String fecha = System.console().readLine();
                                    
                                    savepoint1 = conn.setSavepoint("SavepointCrearPedido");
                                    
                                    // Comprobar si el pedido existe
                                    rs = stmt.executeQuery("SELECT cpedido FROM pedido WHERE cpedido = '" + newpedido + "'");
                                    if (rs.next()) {
                                        System.out.println("El pedido ya existe.");
                                        break;
                                    }
                                    
                                    // Insertar en Pedidos
                                    stmt.executeUpdate("INSERT INTO pedido (cpedido, cliente, fechapedido) VALUES ('" + newpedido + "', '" + cliente + "', TO_DATE('" + fecha + "', 'DD/MM/YYYY'))");
                                    System.out.println("Pedido creado correctamente.\n");
                                    break;
                                
                                case 2:
                                    // Añadir detalle de producto al pedido
                                    System.out.println("Introduzca el código del pedido:");
                                    String pedidoDetalle = System.console().readLine();
                                    System.out.println("Introduzca el código del producto:");
                                    String cproducto = System.console().readLine();
                                    System.out.println("Introduzca la cantidad:");
                                    int cantidad = Integer.parseInt(System.console().readLine());
                                    
                                    savepoint1 = conn.setSavepoint("SavepointAnadirDetalle");
                                    
                                    // Insertar detalle de pedido
                                    stmt.executeUpdate("INSERT INTO detallepedido (cpedido, cproducto, cantidad) VALUES ('" + pedidoDetalle + "', '" + cproducto + "', " + cantidad + ")");
                                    System.out.println("Detalle de producto añadido correctamente al pedido.\n");
                                    break;
                                
                                case 3:
                                    // Eliminar todos los detalles del producto de un pedido
                                    System.out.println("Introduzca el código del pedido para eliminar detalles:");
                                    String pedidoEliminar = System.console().readLine();
                                    
                                    savepoint1 = conn.setSavepoint("SavepointEliminarDetalles");
                                    
                                    // Eliminar detalles del pedido
                                    stmt.executeUpdate("DELETE FROM detallepedido WHERE cpedido = '" + pedidoEliminar + "'");
                                    System.out.println("Detalles del pedido eliminados.\n");
                                    break;
                                
                                case 4:
                                    // Cancelar pedido
                                    System.out.println("Introduzca el código del pedido para cancelar:");
                                    String pedidoCancelar = System.console().readLine();
                                    
                                    savepoint1 = conn.setSavepoint("SavepointCancelarPedido");
                                    
                                    // Eliminar pedido y detalles del pedido
                                    stmt.executeUpdate("DELETE FROM detallepedido WHERE cpedido = '" + pedidoCancelar + "'");
                                    stmt.executeUpdate("DELETE FROM pedido WHERE cpedido = '" + pedidoCancelar + "'");
                                    System.out.println("Pedido cancelado correctamente.\n");
                                    break;
                                
                                case 5:
                                    // Confirmar el pedido y hacer los cambios permanentes
                                    conn.commit();
                                    System.out.println("Pedido finalizado y guardado con éxito.\n");
                                    break;
                                
                                default:
                                    System.out.println("Opción no válida.");
                                    break;
                            }
                        } catch (SQLException e) {
                            System.out.println("Se produjo un error en la base de datos: " + e.getMessage());
                            if (conn != null && savepoint1 != null) {
                                conn.rollback(savepoint1);
                                System.out.println("Los cambios han sido revertidos al último punto seguro.");
                            }
                        } finally {
                            if (rs != null) {
                                try {
                                    rs.close();
                                } catch (SQLException e) {
                                    System.out.println("Error al cerrar ResultSet: " + e.getMessage());
                                }
                            }
                        }
                        break;
                    case 3:
                        // Mostrar todas las tablas

                        System.out.println("CONTENIDO DE LAS TABLAS: \n");

                        // Mostrar contenido de la tabla stock
                        System.out.println("Contenido de la tabla stock:");
                        rs = stmt.executeQuery("SELECT * FROM stock");
                        while (rs.next()) {
                            System.out.println("Código de Producto: " + rs.getString("cproducto") +
                                            ", Cantidad en Stock: " + rs.getInt("cantidad"));
                        }
                        System.out.println("-----------------------------\n");

                        
                        // Mostrar contenido de la tabla Pedidos
                        System.out.println("Contenido de la tabla Pedidos:");
                        rs = stmt.executeQuery("SELECT * FROM pedido");
                        while (rs.next()) {
                            System.out.println("Código de Pedido: " + rs.getString("cpedido") +
                                            ", Código de Cliente: " + rs.getString("cliente") + ", Fecha: " + rs.getDate("fechapedido"));
                        }
                        
                        System.out.println("-----------------------------\n");

                        // Mostrar contenido de la tabla DetallePedido
                        System.out.println("Contenido de la tabla DetallePedido:");
                        rs = stmt.executeQuery("SELECT * FROM detallepedido");
                        while (rs.next()) {
                            System.out.println("Código de Pedido: " + rs.getString("cpedido") +
                                            ", Código de Producto: " + rs.getString("cproducto") +
                                            ", Cantidad: " + rs.getInt("cantidad"));
                        }
                        System.out.println("-----------------------------\n");

                        break;

                    case 4:
                        System.out.println("Saliendo del programa...");
                        if (conn != null) {
                            conn.commit();  // Asegurarse de confirmar cualquier transacción pendiente
                        }
                        return; // Cambiado de System.exit(0) para asegurarse de que finally se ejecute

                    default:
                        System.out.println("Opción no válida. Saliendo del programa...");
                        if (conn != null) {
                            conn.commit();  // Asegurarse de confirmar cualquier transacción pendiente
                        }
                        return; // Cambiado de System.exit(0) para asegurarse de que finally se ejecute
                }

                // Cerrar ResultSet después de cada ciclo si está abierto
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Hacer rollback de la transacción si hay un error
                }
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            // Cerrar recursos en el bloque finally
            try {
                if (rs != null && !rs.isClosed()) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
