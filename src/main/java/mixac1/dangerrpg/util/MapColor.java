package mixac1.dangerrpg.util;

public enum MapColor {
    RED(0xff0000),
    GREEN(0x00ff00),
    BLUE(0x0000ff),
    YELLOW(0xffff00),
    ORANGE(0xffa500),
    BLACK(0x000000),
    PURPLE(0x960096),
    WHITE(0xffffff); // Value by default

    private final int color;

    MapColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public static int fromString(String color) {
        try {
            return MapColor.valueOf(color.toUpperCase()).getColor();
        } catch (IllegalArgumentException e) {
            return WHITE.getColor();
        }
    }
}
