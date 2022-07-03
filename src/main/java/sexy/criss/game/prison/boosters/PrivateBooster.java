package sexy.criss.game.prison.boosters;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class PrivateBooster
extends Booster {
    public PrivateBooster(String owner, double multiplier, int minutes) {
        super(owner, multiplier, minutes);
    }

    public PrivateBooster(Booster.BoosterType type, String owner, double multiplier, int minutes) {
        super(type, owner, multiplier, minutes);
    }

    public PrivateBooster(int id) throws ParseException {
        super(id);
    }

    @Override
    public List<String> getAppliedPlayers() {
        return Arrays.asList(this.owner);
    }

    @Override
    public String getMessage() {
        return "messages.pbooster-use." + this.type.toString();
    }
}

