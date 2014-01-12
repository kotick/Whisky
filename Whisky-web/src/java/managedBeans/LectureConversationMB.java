package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

@Named(value = "lectureConversationMB")
@ConversationScoped
public class LectureConversationMB implements Serializable {
    @Inject
    Conversation conversation;
    private Long idCourse;
    private Long idLecture;
                // TODO F: yani, puedes darme la fecha y el nombre del ramo en strings? para ponerlo en la lista de lectures? <3

    public LectureConversationMB() {
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

    public Long getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Long id) {
        this.idCourse = id;
    }

    public Long getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(Long idLecture) {
        this.idLecture = idLecture;
    }
    

}
