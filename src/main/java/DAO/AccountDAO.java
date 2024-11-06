package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {


    /*
     * @param account: id is not needed
     * @return: account if insertion was successful, return null if not successful.
     */
    public Account insertAccount(Account account){
        /*
         * Account 
         *  id
         *  username
         *  password
         */

        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(1, account.getPassword());

            preparedStatement.executeUpdate();
            return account;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
