package lrj.game;

import lrj.core.Renderable;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Layer implements Renderable.Image {
    public final int
        w, h;
    public final BufferedImage
        image;
    public final int[]
        image_buffer;


    public Layer(int w, int h) {
        this.w = w;
        this.h = h;
        this.image = new BufferedImage(this.w, this.h, BufferedImage.TYPE_INT_RGB);
        this.image_buffer = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    @Override
    public void onRenderImage(ImageContext context) {
        int
            dx = Math.min(w, context.w),
            dy = Math.min(h, context.h);
        for(int x = 0; x < dx; x ++)
            for(int y = 0; y < dy; y ++) {
                int z = y * w + x;
                if((image_buffer[z] & 0xffffff) != 0) {
                    int w = y * context.w + x;
                    context.image_buffer[w] = image_buffer[z];
                }
            }
    }
}
