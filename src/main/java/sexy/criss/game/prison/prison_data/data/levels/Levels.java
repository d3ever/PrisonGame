package sexy.criss.game.prison.prison_data.data.levels;

import com.google.common.collect.Maps;

import java.util.Map;

public class Levels {
    private static Map<Integer, Levels> levels_map = Maps.newHashMap();

    int number;
    int extraDMG;
    int total_blocks;
    int gold;
    public Levels(int number, int extraDMG, int total_blocks, int gold) {
        this.number = number;
        this.extraDMG = extraDMG;
        this.total_blocks = total_blocks;
        this.gold = gold;

        levels_map.put(number, this);
    }

    public int getExtraDMG() {
        return extraDMG;
    }

    public int getTotal_blocks() {
        return total_blocks;
    }

    public int getGold() {
        return gold;
    }

    public static Levels getByInt(int i) {
        return levels_map.get(i);
    }

    public static void registerLeveling() {
        int i1 = 10;
        int i2 = 70;
        int extra = 0;
        for(int i = 0; i < 17; i++) {
            i1*=2;
            i2*=2;
            switch (i){
                case 3: extra = 1; break;
                case 6: extra = 2; break;
                case 9: extra = 3; break;
                case 12: extra = 4; break;
                case 15: extra = 5; break;
                case 17: extra = 6; break;
            }
            new Levels(i, extra, i2, i1);
        }
    }

    public static int getMaxLevel() {
        return levels_map.keySet().stream().mapToInt(Integer::intValue).max().orElse(17);
    }

}
