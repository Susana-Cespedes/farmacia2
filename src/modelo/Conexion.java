
package modelo;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    public static Connection getConexion() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config/config.properties"));

            String url = CryptoUtil.decrypt(props.getProperty("db.url"));
            String user = CryptoUtil.decrypt(props.getProperty("db.user"));
            String password = CryptoUtil.decrypt(props.getProperty("db.password"));

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
