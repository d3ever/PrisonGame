package sexy.criss.game.prison.prison_data.data.promocodes;

import com.google.common.collect.Sets;

import java.util.Set;

public class Promo {
    public static Set<Promo> promos = Sets.newHashSet();

    private String name, author;
    private int reward, reward_a;

    public Promo(String name, String author, int reward, int reward_a) {
        this.name = name;
        this.author = author;
        this.reward = reward;
        this.reward_a = reward_a;

        promos.add(this);
    }

    public String getOwn() {
        return this.author;
    }

    public int getReward() {
        return this.reward;
    }

    public int getRewardOwn() {
        return this.reward_a;
    }

    public String getName() {
        return this.name;
    }

    public static Promo getByOwn(String s) {
        return promos.stream().filter(x -> x.getOwn().equalsIgnoreCase(s)).findAny().orElse(null);
    }

    public static Promo getById(String s) {
        return promos.stream().filter(x -> x.getName().equalsIgnoreCase(s)).findAny().orElse(null);
    }

}
