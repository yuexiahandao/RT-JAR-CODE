package sun.applet;

import java.awt.MenuBar;
import java.net.URL;
import java.util.Hashtable;

public abstract interface AppletViewerFactory
{
  public abstract AppletViewer createAppletViewer(int paramInt1, int paramInt2, URL paramURL, Hashtable paramHashtable);

  public abstract MenuBar getBaseMenuBar();

  public abstract boolean isStandalone();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletViewerFactory
 * JD-Core Version:    0.6.2
 */