public enum Symbol {
    EMPTY(0),
    O(-1),
    X(1);

    public final int value;

    Symbol(int value) {
        this.value = value;
    }
}
