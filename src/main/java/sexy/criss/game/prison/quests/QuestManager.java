package sexy.criss.game.prison.quests;

public class QuestManager {

    public Quest getQuestClass(Class<? extends Quest> cl) {
        try {
            return cl.newInstance();
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
