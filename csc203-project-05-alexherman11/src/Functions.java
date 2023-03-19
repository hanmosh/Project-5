import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions {
    public static final Random rand = new Random();

    private static final int COLOR_MASK = 0xffffff;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right", "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));


    public static boolean adjacent(Point p1, Point p2) { // stays
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

    public static int getIntFromRange(int max, int min) { //stays
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static double getNumFromRange(double max, double min) { //stays in functions
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }


    // health starts at 0 and builds up until ready to convert to Tree

    public static void setBackgroundCell(WorldModel world, Point pos, Background background) { //keeper
        world.background[pos.y][pos.x] = background;
    }

    public static int clamp(int value, int low, int high) { // stay
        return Math.min(high, Math.max(value, low));
    }

    /*
      Called with color for which alpha should be set and alpha value.
      setAlpha(img, color(255, 255, 255), 0));
    */
    public static void setAlpha(PImage img, int maskColor, int alpha) { // stay
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    public static int distanceSquared(Point p1, Point p2) { //stay
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;
        return deltaX * deltaX + deltaY * deltaY;
    }

}
