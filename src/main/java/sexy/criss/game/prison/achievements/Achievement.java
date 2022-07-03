package sexy.criss.game.prison.achievements;

public enum Achievement {
    START("&lНачало проложенно", new String[] {"&6&lЗайдите в игру первый раз"}),
    PICKAXE("&lНастоящий шахтёр", new String[] {"&6&lПолучите свою первую лопату"}),
    FIRST_BOSS("&lПокоритель мностров", new String[] {"&6&lУбить своего первого босса"}),
    FIRST_TRANSIT("&lЮный торговец", new String[] {"&6&lСовершить свою первую сделку"})

    ;

    private String name;
    private String[] messages;

    Achievement(String name, String[] message) {
        this.name = name;
        this.messages = message;
    }

    public String getName() {
        return this.name;
    }

    public String[] getMessages() {
        return this.messages;
    }

}
