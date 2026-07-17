package neuvillette.AuroraUI.api.color;

public record RGBAColor(int red, int green, int blue, int alpha) {

    public int toARGB() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public int toRGBA(){
        return (red << 24) | (green << 16) | (blue << 8) | alpha;
    }

    public RGBColor toRGBAColor(){
        return new RGBColor(toRGBA(), toARGB(), toRGBA());
    }
}
