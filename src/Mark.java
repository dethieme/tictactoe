public enum Mark {
    EMPTY(0),
    O(-1),
    X(1);

    public final int value;

    Mark(int value) {
        this.value = value;
    }
}
