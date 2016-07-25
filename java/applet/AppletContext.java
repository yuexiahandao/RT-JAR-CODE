package java.applet;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

public abstract interface AppletContext
{
  public abstract AudioClip getAudioClip(URL paramURL);

  public abstract Image getImage(URL paramURL);

  public abstract Applet getApplet(String paramString);

  public abstract Enumeration<Applet> getApplets();

  public abstract void showDocument(URL paramURL);

  public abstract void showDocument(URL paramURL, String paramString);

  public abstract void showStatus(String paramString);

  public abstract void setStream(String paramString, InputStream paramInputStream)
    throws IOException;

  public abstract InputStream getStream(String paramString);

  public abstract Iterator<String> getStreamKeys();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.applet.AppletContext
 * JD-Core Version:    0.6.2
 */