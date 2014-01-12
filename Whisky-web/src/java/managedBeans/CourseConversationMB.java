package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

@Named(value = "courseConversationMB")
@ConversationScoped
public class CourseConversationMB implements Serializable {
    @Inject
    Conversation conversation;
    private Long id;
    private String name;
    // TODO agregar nombre de la clase para referenciarla en el índice de lectures

    public CourseConversationMB() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
