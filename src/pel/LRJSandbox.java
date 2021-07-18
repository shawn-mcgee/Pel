package pel;

import pel.core.Engine;
import pel.core.Input;
import pel.core.Renderable;
import pel.core.Scene;
import pel.util.Debug;

public class LRJSandbox {
    public static void main(String[] args) {
        Debug.info(new Object() { }, Pel.VERSION);

        // configure engine for lowrezjam
        Engine.configure(
            Engine.DEBUG        , true              ,
            Engine.CANVAS_LAYOUT, "w0: 64u, h0: 64u",
            Engine.WINDOW_LAYOUT, "w0:640u, h0:480u"
        );
        
        // alternative method to configure engine
//        Engine.setProperty(Engine.DEBUG        , true            );
//        Engine.setProperty(Engine.CANVAS_LAYOUT, "w0:64u, h0:64u");
        
        // initialize the engine thread
        Engine.init();
    
        // set the current scene
        Engine.setCurrentScene(new Scene() {
            
            @Override
            public void onAttach() {
            }
            
            @Override
            public void onRenderImage(Renderable.ImageContext context) {
                for(int i = 0; i < context.image_buffer.length; i ++)
                    context.image_buffer[i] += i;
            }
        });
        
        Engine.attach(Input.KEY_EVENT, (event) -> {
            Debug.info(event.key);
        });
    }
}
