package connections;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConnectDb {
    private String driverName;
    private String connectionString;
    private String login;
    private String password;

    FileInputStream fis=new FileInputStream("src/main/resources/properties/connect.prop");
    Properties p = new Properties ();

    public ConnectDb() throws IOException {
        p.load(fis);

        this.driverName = (String) p.get("driverName");
        this.connectionString = (String) p.get("connectionString");
        this.login = (String) p.get("login");
        this.password = (String) p.get("password");
    }

    public String getDriverName() {
        return driverName;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
