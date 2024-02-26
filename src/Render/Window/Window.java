package Render.Window;

import Tests.*;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Calendar;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {
    private long windowPtr;
    private static Window window;
    public static Vector2i dim = new Vector2i(1200, 900);

    private Test currentTest;
    private String test;


    public Window() {
    }

    public Window(String test) {
        this.test = test;
    }

    public Window(Vector2i dim) {
        Window.dim = dim;
    }

    public Window(String test, Vector2i dim) {
        this.test = test;
        Window.dim = dim;
    }

    public void init() {
        initWindow();
        initTest();
        window = this;
    }

    public void initWindow() {
        // Setup an error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        windowPtr = glfwCreateWindow(dim.x, dim.y, "Open Gl - Testing Enviroment", NULL, NULL);
        if (windowPtr == NULL)
            throw new RuntimeException("Failed to create the GLFW window");


        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowPtr, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop

            // Pass to <currentTest>
            if (currentTest != null) currentTest.OnKeyInput(window, key, scancode, action, mods);
        });

        // maintain starting aspect ratio of viewport on resize
        glfwSetFramebufferSizeCallback(windowPtr, (window, width, height) -> {
            float targetAspect = (float) dim.x / dim.y;
            dim.x = width;
            dim.y = height;
            float newAspect =  (float) width / height;
            if (newAspect > targetAspect) {
                dim.x = (int) (dim.y * targetAspect);
            } else {
                dim.y = (int) (dim.x / targetAspect);
            }
            glViewport(0, 0, dim.x, dim.y);
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowPtr, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowPtr,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

        } // the stack frame is popped automatically

        glfwMakeContextCurrent(windowPtr);
        // Enable v-sync
        glfwSwapInterval(0);
        glfwShowWindow(windowPtr);

        GL.createCapabilities();
    }

    public void initTest() {
        if (test != null) {
            switch (test.toLowerCase()) {
                case "clearcolour", "cc":
                    currentTest = new TestClearColour();
                    break;
                case "textures", "texturing", "tex":
                    currentTest = new TestTextures();
                    break;
                case "multipledrawcalls", "mdc":
                    currentTest = new TestMultipleDrawCalls();
                    break;
                case "camera", "c":
                    currentTest = new TestCamera();
                    break;
                case "pong":
                    currentTest = new TestPong();
                    break;
                case "objparser", "objmodelparser", "op":
                    currentTest = new TestObjModelParser();
                    break;
                case "objrendering", "or", "mr":
                    currentTest = new TestObjRendering();
                    break;
                case "batchrendering", "batching", "b", "br":
                    currentTest = new TestBatchRendering();
                    break;
                case "instancedrendering", "instancing", "i", "ir":
                    currentTest = new TestInstancedRendering();
                    break;
                case "square":
                    currentTest = new TestSquare();
                    break;
                case "spin", "3d":
                    currentTest = new Test3Dspin();
                    break;
                case "collision":
                    currentTest = new TestCollision();
                    break;
                case "normalisation", "n":
                    currentTest = new TestNormalisation();
                    break;
                case "primitive", "primitives", "p":
                    currentTest = new TestPrimitives();
                    break;
                case "game", "g":
                    currentTest = new TestGame();
                    break;
            }
        } else // default case
            currentTest = new Test();

        glfwSetCursorPosCallback(windowPtr, (window, xpos, ypos) -> {
            currentTest.mousePos.x = (float) xpos;
            currentTest.mousePos.y = (float) ypos;
        });
        glfwSetMouseButtonCallback(windowPtr, (window, button, action, mods) -> currentTest.OnKeyInput(window, button, -1, action, mods));

    }

    public void run() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //

        Calendar test = Calendar.getInstance();
        int lastSecond = test.get(Calendar.SECOND);
        int fps = 0;

        long ms = System.currentTimeMillis();

        // rendering loop
        while (!glfwWindowShouldClose(windowPtr)) {
            // Delta Time
            float dt = (System.currentTimeMillis() - ms) / 1000.f;
            ms = System.currentTimeMillis();

            // FPS  ||   ms per frame
            if (lastSecond != test.get(Calendar.SECOND)) {
                lastSecond = test.get(Calendar.SECOND);
                System.out.println(fps + "fps" + "   " + 1000.f / fps + "ms per frame");
                fps = 0;
            } else {
                fps++;
                test = Calendar.getInstance();
            }


            if (currentTest != null) {
                currentTest.OnRender();
                currentTest.OnUpdate(dt);
            }

            glfwSwapBuffers(windowPtr);
            glfwPollEvents();

            GlCheckError();
        }
    }

    public void destroy() {
        if (currentTest != null) currentTest.OnClose();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);


        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    static public void GlClearError() {
        while (glGetError() != GL_NO_ERROR) {
        }
    }

    static public void GlCheckError() {
        while (glGetError() != 0) {
            System.out.println("[OpenGL error:] " + Integer.toHexString(glGetError()));
        }
    }

    public static Window getWindow() {
        return window;
    }
    public static long getWindowPtr() {
        return window.windowPtr;
    }
}
