package neuvillette.AuroraUI.api.widget.layout;

public class Constraint {

    private int size;
    private Mode mode;

    private Constraint(){}

    public Constraint(int size, Mode mode) {
        this.size = size;
        this.mode = mode;
    }

    public static Constraint Length(int size) {
        return new Constraint(size, Mode.Length);
    }

    public static Constraint Fill(int size) {
        return new Constraint(size, Mode.Fill);
    }

    public Mode getMode() {
        return mode;
    }

    public int getSize() {
        return size;
    }

    public boolean isFill() {
        return mode == Mode.Fill;
    }
    public boolean isLength() {
        return mode == Mode.Length;
    }

    public enum Mode{
        Length,
        Fill,
    }
}
