
// String url = "jdbc:oracle:thin:@oracle0.ugr.es:1521:practbd";

import java.sql.*;

public class MiPrograma {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@practbd.oracle0.ugr.es:1521:practbd";
        String user = "x7244926";
        String password = "x7244926";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            while (true) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Mostrar tabla");
                System.out.println("2. Insertar registro");
                System.out.println("3. Modificar registro");
                System.out.println("4. Eliminar registro");
                System.out.println("5. Salir");
                int opcion = Integer.parseInt(System.console().readLine());
                switch (opcion) {
                    case 1:
                        rs = stmt.executeQuery("SELECT * FROM tabla");
                        while (rs.next()) {
                            System.out.println(rs.getString(1) + " " + rs.getString(2));
                        }
                        break;
                    case 2:
                        System.out.println("Introduzca el valor del campo 1:");
                        String campo1 = System.console().readLine();
                        System.out.println("Introduzca el valor del campo 2:");
                        String campo2 = System.console().readLine();
                        stmt.executeUpdate("INSERT INTO tabla VALUES ('" + campo1 + "', '" + campo2 + "')");
                        break;
                    case 3:
                        System.out.println("Introduzca el valor del campo 1 del registro a modificar:");
                        String campo1Antiguo = System.console().readLine();
                        System.out.println("Introduzca el valor del campo 1 nuevo:");
                        String campo1Nuevo = System.console().readLine();
                        System.out.println("Introduzca el valor del campo 2 nuevo:");
                        String campo2Nuevo = System.console().readLine();
                        stmt.executeUpdate("UPDATE tabla SET campo1='" + campo1Nuevo + "', campo2='" + campo2Nuevo + "' WHERE campo1='" + campo1Antiguo + "'");
                        break;
                    case 4:
                        System.out.println("Introduzca el valor del campo 1 del registro a eliminar:");
                        String campo1Eliminar = System.console().readLine();
                        stmt.executeUpdate("DELETE FROM tabla WHERE campo1='" + campo1Eliminar + "'");
                        break;
                    case 5:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } finally {
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
