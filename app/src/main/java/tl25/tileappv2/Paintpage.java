package tl25.tileappv2;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Sanket MIttal on 1/10/2019.
 */
public class Paintpage {
    public int color;
    public boolean emboss;
    public boolean blur;
    public int strokewidth;
    public Path path;

    public Paintpage(int color, boolean emboss, boolean blur, int strokewidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokewidth = strokewidth;
        this.path = path;
    }
}
