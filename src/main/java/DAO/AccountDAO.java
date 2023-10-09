package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try{
            String sql = "SELECT * FROM account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Account account = new Account(resultSet.getInt("account_id"),                
                resultSet.getString("username"), 
                resultSet.getString("password"));
                
                accounts.add(account);
            } 
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByID(int id){

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account.account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), 
                rs.getString("username"),
                rs.getString("password"));
                
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Account getAccountByLogin(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account.username = ? AND account.password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"account_id"});
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int generated_account_id = rs.getInt(1);
                Account account = new Account(generated_account_id, rs.getString("username"), rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account(username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"account_id"});

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
          
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            
            if(resultSet.next()){
                int generated_account_id = resultSet.getInt(1);
                Account ret = new Account(generated_account_id, account.getUsername(), account.getPassword());
                return ret;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
