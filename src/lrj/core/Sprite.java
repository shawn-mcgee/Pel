package lrj.core;

import lrj.util.Debug;
import lrj.util.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class Sprite implements Renderable.Image {
    protected Sprite.Atlas
        atlas;

    public float
        x, y,
        w, h;
    public float
        frame;

    public Sprite(
        Atlas atlas
    ) {
        this.atlas = atlas;
        w = atlas.frame_w;
        h = atlas.frame_h;
    }

    @Override
    public void onRenderImage(ImageContext context) {
        float
            sx = (float)atlas.frame_w / w,
            sy = (float)atlas.frame_h / h;
        for(int i = 0; i < w; i ++ )
            for(int j = 0; j < h; j ++) {
                int
                    x = (int)(i * sx),
                    y = (int)(j * sy),
                    rgb = atlas.frame_buffer[(int)frame][y * atlas.frame_w + x];
                if(rgb != 0)
                    context.rgb(
                        (int)(i + this.x),
                        (int)(j + this.y),
                        rgb
                    );
            }
    }

    public static class Atlas {
        protected static final HashMap<String, Atlas>
            NAME_INDEX = new HashMap<String, Atlas>(),
            PATH_INDEX = new HashMap<String, Atlas>();

        protected final Resource
            resource;
        protected final String
            path_string,
            name_string;

        protected final BufferedImage
            image;

        protected final int[]
            atlas_buffer;
        protected final int
            atlas_w,
            atlas_h;

        protected final int[][]
            frame_buffer;
        protected final int
            frame_w,
            frame_h;

        public Atlas(
            Resource resource,
            String path_string,
            String name_string,
            BufferedImage image,
            int[] atlas_buffer,
            int atlas_w,
            int atlas_h,
            int[][] frame_buffer,
            int frame_w,
            int frame_h
        ) {
            this.resource = resource;
            this.path_string = path_string;
            this.name_string = name_string;
            this.image = image;
            this.atlas_buffer = atlas_buffer;
            this.atlas_w = atlas_w;
            this.atlas_h = atlas_h;
            this.frame_buffer = frame_buffer;
            this.frame_w = frame_w;
            this.frame_h = frame_h;
        }

        public static Sprite.Atlas load(Resource resource, String name, int frame_w, int frame_h) {
            try(InputStream in = Resource.newInputStream(resource)) {
                String
                    path_string = Objects.toString(resource),
                    name_string = Objects.toString(name    );
                BufferedImage input = ImageIO.read(in);
                BufferedImage image = new BufferedImage(
                    input.getWidth() ,
                    input.getHeight(),
                    BufferedImage.TYPE_INT_RGB
                );
                Graphics2D g = image.createGraphics();
                g.drawImage(input, 0, 0, null);
                g.dispose();

                int[] atlas_buffer = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
                int
                    atlas_w = input.getWidth(),
                    atlas_h = input.getHeight(),
                    w = atlas_w / frame_w,
                    h = atlas_h / frame_h;
                int[][] frame_buffer = new int[w * h][frame_w * frame_h];

                for(int i = 0; i < w; i ++)
                    for(int j = 0; j < h; j ++) {
                        int k = j * w + i;
                        for(int x = 0; x < frame_w; x ++)
                            for(int y = 0; y < frame_h; y ++) {
                                int
                                    z0 = y * frame_w + x,
                                    z1 = (frame_h * j + y) * atlas_w + (frame_w * i + x);
                                frame_buffer[k][z0] = atlas_buffer[z1];
                            }
                    }

                Sprite.Atlas atlas = new Sprite.Atlas(
                    resource,
                    path_string,
                    name_string,
                    image,
                    atlas_buffer,
                    atlas_w,
                    atlas_h,
                    frame_buffer,
                    frame_w,
                    frame_h
                );
                if(PATH_INDEX.get(path_string) != null)
                    Debug.warn(new Object() { }, "Sprite.Atlas with path '" + path_string + "' already exists.");
                if(NAME_INDEX.get(name_string) != null)
                    Debug.warn(new Object() { }, "Sprite.Atlas with name '" + name_string + "' already exists.");
                PATH_INDEX.put(path_string, atlas);
                NAME_INDEX.put(name_string, atlas);
                return atlas;
            } catch(Exception na) {
                Debug.warn(new Object() { }, na);
                return null;
            }
        }

        public static Sprite.Atlas load(String   resource, String name, int frame_w, int frame_h) {
            return load(new Resource(resource), name, frame_w, frame_h);
        }

        public static Sprite.Atlas load(Class<?> from, String path, String name, int frame_w, int frame_h) {
            return load(new Resource(from, path), name, frame_w, frame_h);
        }

        public static Sprite.Atlas load(String   from, String path, String name, int frame_w, int frame_h) {
            return load(new Resource(from, path), name, frame_w, frame_h);
        }

        public static Sprite.Atlas getByPath(String path) {
            Sprite.Atlas atlas = PATH_INDEX.get(path);
            if(atlas == null)
                Debug.warn(new Object() { }, "Sprite.Atlas with path '" + path + "' does not exist");
            return atlas;
        }

        public static Sprite.Atlas getByName(String name) {
            Sprite.Atlas atlas = NAME_INDEX.get(name);
            if(atlas == null)
                Debug.warn(new Object() { }, "Sprite.Atlas with name '" + name + "' does not exist");
            return atlas;
        }
    }
}
