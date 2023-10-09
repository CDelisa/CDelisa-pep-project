package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
        accountDAO = new AccountDAO();
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public List<Message> getAllMessagesByID(int id){
        return messageDAO.getAllMessagesByID(id);
    }
    public Message deleteMessageByID(int id){
        return messageDAO.deleteMessageByID(id);
    }
    public Message addMessage(Message message){
        if(message.getMessage_text().length() == 0){
            return null;
        }
        else if(message.getMessage_text().length() >= 255){
            return null;

        }
        else if(accountDAO.getAccountByID(message.getPosted_by()) == null){
            return null;
        }
        return messageDAO.insertMessage(message);
    }
    public Message updateMessage(int id, String newText){
        Message updatedMessage = messageDAO.updateMessage(id, newText);
        if(messageDAO.getMessageByID(id) == null){
            return null;
        }
        if(updatedMessage.getMessage_text().length() == 0){
            return null;
        }
        if(updatedMessage.getMessage_text().length() >= 255){
            return null;
        }
        return updatedMessage;
    }
}
