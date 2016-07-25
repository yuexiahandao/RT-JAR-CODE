package java.awt.peer;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import sun.awt.CausedFocusEvent.Cause;
import sun.java2d.pipe.Region;

public abstract interface ComponentPeer
{
  public static final int SET_LOCATION = 1;
  public static final int SET_SIZE = 2;
  public static final int SET_BOUNDS = 3;
  public static final int SET_CLIENT_SIZE = 4;
  public static final int RESET_OPERATION = 5;
  public static final int NO_EMBEDDED_CHECK = 16384;
  public static final int DEFAULT_OPERATION = 3;

  public abstract boolean isObscured();

  public abstract boolean canDetermineObscurity();

  public abstract void setVisible(boolean paramBoolean);

  public abstract void setEnabled(boolean paramBoolean);

  public abstract void paint(Graphics paramGraphics);

  public abstract void print(Graphics paramGraphics);

  public abstract void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

  public abstract void handleEvent(AWTEvent paramAWTEvent);

  public abstract void coalescePaintEvent(PaintEvent paramPaintEvent);

  public abstract Point getLocationOnScreen();

  public abstract Dimension getPreferredSize();

  public abstract Dimension getMinimumSize();

  public abstract ColorModel getColorModel();

  public abstract Toolkit getToolkit();

  public abstract Graphics getGraphics();

  public abstract FontMetrics getFontMetrics(Font paramFont);

  public abstract void dispose();

  public abstract void setForeground(Color paramColor);

  public abstract void setBackground(Color paramColor);

  public abstract void setFont(Font paramFont);

  public abstract void updateCursorImmediately();

  public abstract boolean requestFocus(Component paramComponent, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause);

  public abstract boolean isFocusable();

  public abstract Image createImage(ImageProducer paramImageProducer);

  public abstract Image createImage(int paramInt1, int paramInt2);

  public abstract VolatileImage createVolatileImage(int paramInt1, int paramInt2);

  public abstract boolean prepareImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver);

  public abstract int checkImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver);

  public abstract GraphicsConfiguration getGraphicsConfiguration();

  public abstract boolean handlesWheelScrolling();

  public abstract void createBuffers(int paramInt, BufferCapabilities paramBufferCapabilities)
    throws AWTException;

  public abstract Image getBackBuffer();

  public abstract void flip(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferCapabilities.FlipContents paramFlipContents);

  public abstract void destroyBuffers();

  public abstract void reparent(ContainerPeer paramContainerPeer);

  public abstract boolean isReparentSupported();

  public abstract void layout();

  public abstract void applyShape(Region paramRegion);

  public abstract void setZOrder(ComponentPeer paramComponentPeer);

  public abstract boolean updateGraphicsData(GraphicsConfiguration paramGraphicsConfiguration);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.ComponentPeer
 * JD-Core Version:    0.6.2
 */