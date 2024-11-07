package Service;
import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public Message deletMessageById(int id){
        return messageDAO.deletMessageById(id);
    }

    public Message updateMessageById(int id, Message updatedMessage){
        return messageDAO.updateMessageByMessageId(id, updatedMessage);
    }

    public List<Message> getAllMessagesByAccountId(int id){
        return messageDAO.getAllMessagesByAccountId(id);
    }
    
}
