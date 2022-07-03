package sexy.criss.game.prison.boosters;

public class Transaction {
    long timestamp;
    int items;
    double money;
    SellEvent.Type type;

    public Transaction(String value) {
        this.timestamp = Long.parseLong(value.split(" __ ")[0]);
        this.type = SellEvent.Type.valueOf(value.split(" __ ")[1]);
        this.items = Integer.parseInt(value.split(" __ ")[2]);
        this.money = Double.parseDouble(value.split(" __ ")[3]);
    }

    public Transaction(long timestamp, SellEvent.Type type, int soldItems, double money) {
        this.timestamp = timestamp;
        this.type = type;
        this.items = soldItems;
        this.money = money;
    }

    public int getItemsSold() {
        return this.items;
    }

    public double getMoney() {
        return this.money;
    }

    public static int getItemsSold(String string) {
        return Integer.parseInt(string.split(" __ ")[2]);
    }

    public static double getMoney(String string) {
        return Double.parseDouble(string.split(" __ ")[3]);
    }
}

