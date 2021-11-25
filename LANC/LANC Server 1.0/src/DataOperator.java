import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.sql.*;

public class DataOperator {
    String sql;
    String driver="com.mysql.cj.jdbc.Driver";
    String lib="jdbc:MySQL://localhost/DancUsers";
    Connection conn;
    PreparedStatement statement;
    ResultSet set;
    public void loadDatabase(){
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("无法加载驱动");
        }
    }
    public void connect(){
        try {
            conn=DriverManager.getConnection(lib,"root","Zhonjc13579");
        } catch (SQLException e) {
            System.out.println("无法连接数据库");
        }
    }
    public String userQuery(String user,String pass){
        sql="SELECT * FROM users WHERE userName=? AND password=?";
        try {
            statement=conn.prepareStatement(sql);
            statement.setString(1,user);
            statement.setString(2,pass);
            set=statement.executeQuery();
            if(set.next())return set.getString(1);
            else return "-1";
        } catch (SQLException e) {
            System.out.println("数据库请求错误"+e);
            return "0";
        }
    }
    public String signupQuery(String user, String pass){
        sql="INSERT INTO users VALUES (?,?)";
        try {
            statement=conn.prepareStatement(sql);
            statement.setString(1,user);
            statement.setString(2,pass);
            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            System.out.println("数据库请求错误或已存在该账户");
            return "ERROR";
        }
    }


}
