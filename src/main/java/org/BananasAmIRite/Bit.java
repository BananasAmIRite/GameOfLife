package org.BananasAmIRite;

public class Bit {
    private boolean isLit;

    public Bit() {
        this.isLit = false;
    }

    public Bit(boolean isLit) {
        this.isLit = isLit;
    }

    public void flip() {
        this.isLit = !this.isLit;
    }

    public boolean isLit() {
        return this.isLit;
    }

    @Override
    public String toString() {
        return "Bit(" + (isLit ? '+' : '-') + ")";
    }
}
