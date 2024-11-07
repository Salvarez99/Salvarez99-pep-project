package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
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

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLogInHandler);
        app.post("/messages", this::postInsertMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountIdHandler);


        return app;
    }

    /*
     * Checks if account username is in valid format
     * @param account
     * @return true if valid format, false otherwise
     */
    private boolean hasUsername(Account account){
        return account.username != null && !account.username.trim().isEmpty();
    }

    /*
     * Checks if account password is in valid format
     * @param account
     * @return true if valid format, false otherwise
     */
    private boolean hasPassword(Account account){
        return account.password != null;
    }

    private boolean hasMessageText(Message message){
        return message.getMessage_text() != null && 
        !message.getMessage_text().trim().isEmpty() && 
        message.getMessage_text().length() < 255;
    }

    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        if(hasUsername(account) && hasPassword(account)){

            if(accountService.getAccountByUsername(account.username) == null && account.password.length() >= 4){
                Account addedAccount = accountService.addAccount(account);
                if(addedAccount != null){
                    ctx.json(mapper.writeValueAsString(addedAccount));
                    ctx.status(200);
                    return;
                }
            }
        }
        ctx.status(400);
    }

    private void postLogInHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        if(hasUsername(account) && hasPassword(account)){
            Account actual = accountService.getAccountByUsername(account.username);
            if(actual != null && actual.getUsername().equals(account.getUsername()) && actual.getPassword().equals(account.getPassword())){
                ctx.json(mapper.writeValueAsString(actual));
                ctx.status(200);
                return;
            }
        }
        ctx.status(401);
    }

    private void postInsertMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        if(hasMessageText(message)){

            if(accountService.getAccountById(message.getPosted_by()) != null){
                Message addedMessage = messageService.addMessage(message);
                if(addedMessage != null){
                    ctx.json(mapper.writeValueAsString(addedMessage));
                    ctx.status(200);
                    return;
                }
            }
        }
        ctx.status(400);
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message response = messageService.getMessageById(messageId);

        if(response != null){
            ctx.json(response);
            ctx.status(200);
        }else{
            ctx.json("");
            ctx.status(200);
        }
    }

    private void deleteMessageByIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message response = messageService.deletMessageById(messageId);

        if(response != null){
            ctx.json(response);
            ctx.status(200);
        }else{
            ctx.json("");
            ctx.status(200);
        }
    }

    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message updatedMessage = mapper.readValue(ctx.body(), Message.class);

        Message message = messageService.getMessageById(messageId);

        if( message != null && updatedMessage != null && hasMessageText(updatedMessage)){
            Message response = messageService.updateMessageById(messageId, updatedMessage);
            ctx.json(response);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesFromAccountIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccountId(messageId));
        ctx.status(200);
    }
}