package java.awt.peer;

public abstract interface TrayIconPeer
{
  public abstract void dispose();

  public abstract void setToolTip(String paramString);

  public abstract void updateImage();

  public abstract void displayMessage(String paramString1, String paramString2, String paramString3);

  public abstract void showPopupMenu(int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.peer.TrayIconPeer
 * JD-Core Version:    0.6.2
 */