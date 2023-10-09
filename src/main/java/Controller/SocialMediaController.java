package Controller;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Message;
import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    public SocialMediaController(MessageService messageService){
        this.messageService = messageService;
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::postMessageHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    /* 
    private void exampleHandler(Context context) {
        context.json("sample text");
    }*/

    private void postAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        
        if(addedAccount != null){
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else{
            context.status(400);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage != null){
            context.json(mapper.writeValueAsString(addedMessage));
        }
        else{
            context.status(400);
        }
    }
    private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account lAccount = accountService.getAccountByLogin(account);
        if(lAccount != null){
            context.json(mapper.writeValueAsString(lAccount));
        }
        else{
            context.status(401);
        }
    }
    private void getAllMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    private void getMessageByIDHandler(Context context){
        int id = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        Message message = messageService.getMessageByID(id);
        if(message == null){
            context.status(200).result("");
        }       
        else
            context.json(message);
    }
    private void getAllMessagesByIDHandler(Context context){
        int id = Integer.parseInt(Objects.requireNonNull(context.pathParam("account_id")));
        List<Message> messages = messageService.getAllMessagesByID(id);
        if(messages != null)
            context.json(messages);
    }
    private void deleteMessageByIDHandler(Context context){
        int id = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        Message message = messageService.deleteMessageByID(id);
        if(message == null){
            context.status(200).result("");
        }
        else
            context.json(message);
    }
    private void updateMessageByIDHandler(Context context){
        int id = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        Message m = context.bodyAsClass(Message.class);
        String newText = m.message_text;
        Message message = messageService.updateMessage(id, newText);
        if(message == null){
            context.status(400).result("");
        }
        else
            context.json(message);
    }

}