package takeQuizLogic;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class
gifAnimate extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int pixel_width;
    private final int pixel_height;

    private int lastIndex;

    public gifAnimate (
            ImageView imageView,
            Duration duration,
            int count,   int columns,
            int offsetX, int offsetY,
            int width,   int height) {
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.pixel_width     = width;
        this.pixel_height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double val) {
        final int index = Math.min((int) Math.floor(val * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * pixel_width  + offsetX;
            final int y = (index / columns) * pixel_height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, pixel_width, pixel_height));
            lastIndex = index;
        }
    }
}
