public class Service {
    private static DataOperator operator=new DataOperator();
    public static String login(String user,String pass){
        operator.loadDatabase();
        operator.connect();
        return operator.userQuery(user,pass);
    }
    public static String signup(String user,String pass){
        operator.loadDatabase();
        operator.connect();
        return operator.signupQuery(user,pass);
    }
}
