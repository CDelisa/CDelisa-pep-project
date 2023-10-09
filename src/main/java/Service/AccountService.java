package Service;

import DAO.AccountDAO;
import java.util.List;
import java.util.ArrayList;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public Account addAccount(Account account){
        if(account == null){
            return null;
        }
        else if(account.getUsername() == ""){
            return null;
        }
        else if(account.getPassword().length() < 4){
            return null;
        } 
        return accountDAO.insertAccount(account);
    }
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }
    public List<String> getAllUsernames(){
        List<String> list = new ArrayList<String>();
        getAllAccounts().forEach(x -> list.add(x.getUsername()));
        return list;
    }
    public Account getAccountByLogin(Account account)
    {
        return accountDAO.getAccountByLogin(account.getUsername(), account.getPassword());
    }

}
