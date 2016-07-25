package com.sun.awt;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;

import sun.awt.AWTAccessor;
import sun.awt.AWTAccessor.ComponentAccessor;
import sun.awt.AWTAccessor.WindowAccessor;
import sun.awt.SunToolkit;

public final class AWTUtilities {
    public static boolean isTranslucencySupported(Translucency paramTranslucency) {
        switch (1. $SwitchMap$com$sun$awt$AWTUtilities$Translucency[paramTranslucency.ordinal()]){
            case 1:
                return isWindowShapingSupported();
            case 2:
                return isWindowOpacitySupported();
            case 3:
                return isWindowTranslucencySupported();
        }
        return false;
    }

    private static boolean isWindowOpacitySupported() {
        Toolkit localToolkit = Toolkit.getDefaultToolkit();
        if (!(localToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) localToolkit).isWindowOpacitySupported();
    }

    public static void setWindowOpacity(Window paramWindow, float paramFloat) {
        if (paramWindow == null) {
            throw new NullPointerException("The window argument should not be null.");
        }

        AWTAccessor.getWindowAccessor().setOpacity(paramWindow, paramFloat);
    }

    public static float getWindowOpacity(Window paramWindow) {
        if (paramWindow == null) {
            throw new NullPointerException("The window argument should not be null.");
        }

        return AWTAccessor.getWindowAccessor().getOpacity(paramWindow);
    }

    public static boolean isWindowShapingSupported() {
        Toolkit localToolkit = Toolkit.getDefaultToolkit();
        if (!(localToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) localToolkit).isWindowShapingSupported();
    }

    public static Shape getWindowShape(Window paramWindow) {
        if (paramWindow == null) {
            throw new NullPointerException("The window argument should not be null.");
        }

        return AWTAccessor.getWindowAccessor().getShape(paramWindow);
    }

    public static void setWindowShape(Window paramWindow, Shape paramShape) {
        if (paramWindow == null) {
            throw new NullPointerException("The window argument should not be null.");
        }

        AWTAccessor.getWindowAccessor().setShape(paramWindow, paramShape);
    }

    private static boolean isWindowTranslucencySupported() {
        Toolkit localToolkit = Toolkit.getDefaultToolkit();
        if (!(localToolkit instanceof SunToolkit)) {
            return false;
        }

        if (!((SunToolkit) localToolkit).isWindowTranslucencySupported()) {
            return false;
        }

        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        if (isTranslucencyCapable(localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration())) {
            return true;
        }

        GraphicsDevice[] arrayOfGraphicsDevice = localGraphicsEnvironment.getScreenDevices();

        for (int i = 0; i < arrayOfGraphicsDevice.length; i++) {
            GraphicsConfiguration[] arrayOfGraphicsConfiguration = arrayOfGraphicsDevice[i].getConfigurations();
            for (int j = 0; j < arrayOfGraphicsConfiguration.length; j++) {
                if (isTranslucencyCapable(arrayOfGraphicsConfiguration[j])) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void setWindowOpaque(Window paramWindow, boolean paramBoolean) {
        if (paramWindow == null) {
            throw new NullPointerException("The window argument should not be null.");
        }

        if ((!paramBoolean) && (!isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT))) {
            throw new UnsupportedOperationException("The PERPIXEL_TRANSLUCENT translucency kind is not supported");
        }

        AWTAccessor.getWindowAccessor().setOpaque(paramWindow, paramBoolean);
    }

    public static boolean isWindowOpaque(Window paramWindow) {
        if (paramWindow == null) {
            throw new NullPointerException("The window argument should not be null.");
        }

        return paramWindow.isOpaque();
    }

    public static boolean isTranslucencyCapable(GraphicsConfiguration paramGraphicsConfiguration) {
        if (paramGraphicsConfiguration == null) {
            throw new NullPointerException("The gc argument should not be null");
        }

        Toolkit localToolkit = Toolkit.getDefaultToolkit();
        if (!(localToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) localToolkit).isTranslucencyCapable(paramGraphicsConfiguration);
    }

    public static void setComponentMixingCutoutShape(Component paramComponent, Shape paramShape) {
        if (paramComponent == null) {
            throw new NullPointerException("The component argument should not be null.");
        }

        AWTAccessor.getComponentAccessor().setMixingCutoutShape(paramComponent, paramShape);
    }

    public static enum Translucency {
        PERPIXEL_TRANSPARENT,

        TRANSLUCENT,

        PERPIXEL_TRANSLUCENT;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.awt.AWTUtilities
 * JD-Core Version:    0.6.2
 */