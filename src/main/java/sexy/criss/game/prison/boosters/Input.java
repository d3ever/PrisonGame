/*
 * Decompiled with CFR 0.150.
 */
package sexy.criss.game.prison.boosters;

public class Input {
    InputType type;
    Object value;

    public Input(InputType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public InputType getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }
}

