package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    Message message;

    public SocialMediaController(){
        accountService = new AccountService();
        message = new Message();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccount);
        app.post("/login", this::postLogIn);
        app.post("/messages", this::postInsertMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::patchMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountId);


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

    private void postAccount(Context ctx) throws JsonProcessingException{
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

    private void postLogIn(Context ctx) throws JsonProcessingException {
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

    private void postInsertMessage(Context ctx){

    }

    private void getAllMessages(Context ctx){

    }

    private void getMessageById(Context ctx){

    }

    private void deleteMessageById(Context ctx){

    }

    private void patchMessageById(Context ctx){

    }

    private void getAllMessagesFromAccountId(Context ctx){

    }
}