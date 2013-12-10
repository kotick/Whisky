/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

/**
 *
 * @author kotick
 */
@Named(value = "photoConversationMB")
@ConversationScoped
public class PhotoConversationMB implements Serializable {
    @Inject
    Conversation conversation;
    private Long id;
    /**
     * Creates a new instance of PhotoConversationMB
     */
    public PhotoConversationMB() {
    }
    
    public void beginConversation(){
        if (conversation.isTransient()){
            conversation.begin();
        }
    }
    public void endConversation(){
        if(!conversation.isTransient()){
            conversation.end();
        }
    }
    public Conversation getConversation(){
        return conversation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
