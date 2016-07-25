/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.IllegalComponentStateException;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.MemoryImageSource;
/*     */ import java.awt.image.PixelGrabber;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.beans.Transient;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.URL;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Locale;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleIcon;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class ImageIcon
/*     */   implements Icon, Serializable, Accessible
/*     */ {
/*     */   private transient String filename;
/*     */   private transient URL location;
/*     */   transient Image image;
/*  79 */   transient int loadStatus = 0;
/*     */   ImageObserver imageObserver;
/*  81 */   String description = null;
/*     */ 
/*  88 */   protected static final Component component = (Component)AccessController.doPrivileged(new PrivilegedAction() {
/*     */     public Component run() {
/*     */       try {
/*  91 */         Component localComponent = ImageIcon.access$000();
/*     */ 
/*  94 */         Field localField = Component.class.getDeclaredField("appContext");
/*     */ 
/*  97 */         localField.setAccessible(true);
/*  98 */         localField.set(localComponent, null);
/*     */ 
/* 100 */         return localComponent;
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 104 */         localThrowable.printStackTrace();
/* 105 */       }return null;
/*     */     }
/*     */   });
/*     */ 
/* 109 */   protected static final MediaTracker tracker = new MediaTracker(component);
/*     */   private static int mediaTrackerID;
/* 133 */   private static final Object TRACKER_KEY = new StringBuilder("TRACKER_KEY");
/*     */ 
/* 135 */   int width = -1;
/* 136 */   int height = -1;
/*     */ 
/* 525 */   private AccessibleImageIcon accessibleContext = null;
/*     */ 
/*     */   private static Component createNoPermsComponent()
/*     */   {
/* 115 */     return (Component)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Component run() {
/* 118 */         return new Component()
/*     */         {
/*     */         };
/*     */       }
/*     */     }
/*     */     , new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, null) }));
/*     */   }
/*     */ 
/*     */   public ImageIcon(String paramString1, String paramString2)
/*     */   {
/* 147 */     this.image = Toolkit.getDefaultToolkit().getImage(paramString1);
/* 148 */     if (this.image == null) {
/* 149 */       return;
/*     */     }
/* 151 */     this.filename = paramString1;
/* 152 */     this.description = paramString2;
/* 153 */     loadImage(this.image);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"description"})
/*     */   public ImageIcon(String paramString)
/*     */   {
/* 174 */     this(paramString, paramString);
/*     */   }
/*     */ 
/*     */   public ImageIcon(URL paramURL, String paramString)
/*     */   {
/* 186 */     this.image = Toolkit.getDefaultToolkit().getImage(paramURL);
/* 187 */     if (this.image == null) {
/* 188 */       return;
/*     */     }
/* 190 */     this.location = paramURL;
/* 191 */     this.description = paramString;
/* 192 */     loadImage(this.image);
/*     */   }
/*     */ 
/*     */   public ImageIcon(URL paramURL)
/*     */   {
/* 205 */     this(paramURL, paramURL.toExternalForm());
/*     */   }
/*     */ 
/*     */   public ImageIcon(Image paramImage, String paramString)
/*     */   {
/* 214 */     this(paramImage);
/* 215 */     this.description = paramString;
/*     */   }
/*     */ 
/*     */   public ImageIcon(Image paramImage)
/*     */   {
/* 227 */     this.image = paramImage;
/* 228 */     Object localObject = paramImage.getProperty("comment", this.imageObserver);
/* 229 */     if ((localObject instanceof String)) {
/* 230 */       this.description = ((String)localObject);
/*     */     }
/* 232 */     loadImage(paramImage);
/*     */   }
/*     */ 
/*     */   public ImageIcon(byte[] paramArrayOfByte, String paramString)
/*     */   {
/* 249 */     this.image = Toolkit.getDefaultToolkit().createImage(paramArrayOfByte);
/* 250 */     if (this.image == null) {
/* 251 */       return;
/*     */     }
/* 253 */     this.description = paramString;
/* 254 */     loadImage(this.image);
/*     */   }
/*     */ 
/*     */   public ImageIcon(byte[] paramArrayOfByte)
/*     */   {
/* 274 */     this.image = Toolkit.getDefaultToolkit().createImage(paramArrayOfByte);
/* 275 */     if (this.image == null) {
/* 276 */       return;
/*     */     }
/* 278 */     Object localObject = this.image.getProperty("comment", this.imageObserver);
/* 279 */     if ((localObject instanceof String)) {
/* 280 */       this.description = ((String)localObject);
/*     */     }
/* 282 */     loadImage(this.image);
/*     */   }
/*     */ 
/*     */   public ImageIcon()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void loadImage(Image paramImage)
/*     */   {
/* 296 */     MediaTracker localMediaTracker = getTracker();
/* 297 */     synchronized (localMediaTracker) {
/* 298 */       int i = getNextID();
/*     */ 
/* 300 */       localMediaTracker.addImage(paramImage, i);
/*     */       try {
/* 302 */         localMediaTracker.waitForID(i, 0L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 304 */         System.out.println("INTERRUPTED while loading Image");
/*     */       }
/* 306 */       this.loadStatus = localMediaTracker.statusID(i, false);
/* 307 */       localMediaTracker.removeImage(paramImage, i);
/*     */ 
/* 309 */       this.width = paramImage.getWidth(this.imageObserver);
/* 310 */       this.height = paramImage.getHeight(this.imageObserver);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getNextID()
/*     */   {
/* 318 */     synchronized (getTracker()) {
/* 319 */       return ++mediaTrackerID;
/*     */     }
/*     */   }
/*     */ 
/*     */   private MediaTracker getTracker()
/*     */   {
/* 329 */     AppContext localAppContext = AppContext.getAppContext();
/*     */     Object localObject1;
/* 332 */     synchronized (localAppContext) {
/* 333 */       localObject1 = localAppContext.get(TRACKER_KEY);
/* 334 */       if (localObject1 == null) {
/* 335 */         Component local3 = new Component()
/*     */         {
/*     */         };
/* 336 */         localObject1 = new MediaTracker(local3);
/* 337 */         localAppContext.put(TRACKER_KEY, localObject1);
/*     */       }
/*     */     }
/* 340 */     return (MediaTracker)localObject1;
/*     */   }
/*     */ 
/*     */   public int getImageLoadStatus()
/*     */   {
/* 351 */     return this.loadStatus;
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public Image getImage()
/*     */   {
/* 360 */     return this.image;
/*     */   }
/*     */ 
/*     */   public void setImage(Image paramImage)
/*     */   {
/* 368 */     this.image = paramImage;
/* 369 */     loadImage(paramImage);
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 382 */     return this.description;
/*     */   }
/*     */ 
/*     */   public void setDescription(String paramString)
/*     */   {
/* 393 */     this.description = paramString;
/*     */   }
/*     */ 
/*     */   public synchronized void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 412 */     if (this.imageObserver == null)
/* 413 */       paramGraphics.drawImage(this.image, paramInt1, paramInt2, paramComponent);
/*     */     else
/* 415 */       paramGraphics.drawImage(this.image, paramInt1, paramInt2, this.imageObserver);
/*     */   }
/*     */ 
/*     */   public int getIconWidth()
/*     */   {
/* 425 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getIconHeight()
/*     */   {
/* 434 */     return this.height;
/*     */   }
/*     */ 
/*     */   public void setImageObserver(ImageObserver paramImageObserver)
/*     */   {
/* 451 */     this.imageObserver = paramImageObserver;
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public ImageObserver getImageObserver()
/*     */   {
/* 461 */     return this.imageObserver;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 470 */     if (this.description != null) {
/* 471 */       return this.description;
/*     */     }
/* 473 */     return super.toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 479 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 481 */     int i = paramObjectInputStream.readInt();
/* 482 */     int j = paramObjectInputStream.readInt();
/* 483 */     int[] arrayOfInt = (int[])paramObjectInputStream.readObject();
/*     */ 
/* 485 */     if (arrayOfInt != null) {
/* 486 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 487 */       ColorModel localColorModel = ColorModel.getRGBdefault();
/* 488 */       this.image = localToolkit.createImage(new MemoryImageSource(i, j, localColorModel, arrayOfInt, 0, i));
/* 489 */       loadImage(this.image);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 497 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 499 */     int i = getIconWidth();
/* 500 */     int j = getIconHeight();
/* 501 */     int[] arrayOfInt = this.image != null ? new int[i * j] : null;
/*     */ 
/* 503 */     if (this.image != null) {
/*     */       try {
/* 505 */         PixelGrabber localPixelGrabber = new PixelGrabber(this.image, 0, 0, i, j, arrayOfInt, 0, i);
/* 506 */         localPixelGrabber.grabPixels();
/* 507 */         if ((localPixelGrabber.getStatus() & 0x80) != 0)
/* 508 */           throw new IOException("failed to load image contents");
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/* 512 */         throw new IOException("image load interrupted");
/*     */       }
/*     */     }
/*     */ 
/* 516 */     paramObjectOutputStream.writeInt(i);
/* 517 */     paramObjectOutputStream.writeInt(j);
/* 518 */     paramObjectOutputStream.writeObject(arrayOfInt);
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 541 */     if (this.accessibleContext == null) {
/* 542 */       this.accessibleContext = new AccessibleImageIcon();
/*     */     }
/* 544 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleImageIcon extends AccessibleContext
/*     */     implements AccessibleIcon, Serializable
/*     */   {
/*     */     protected AccessibleImageIcon()
/*     */     {
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 578 */       return AccessibleRole.ICON;
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 589 */       return null;
/*     */     }
/*     */ 
/*     */     public Accessible getAccessibleParent()
/*     */     {
/* 601 */       return null;
/*     */     }
/*     */ 
/*     */     public int getAccessibleIndexInParent()
/*     */     {
/* 612 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getAccessibleChildrenCount()
/*     */     {
/* 623 */       return 0;
/*     */     }
/*     */ 
/*     */     public Accessible getAccessibleChild(int paramInt)
/*     */     {
/* 633 */       return null;
/*     */     }
/*     */ 
/*     */     public Locale getLocale()
/*     */       throws IllegalComponentStateException
/*     */     {
/* 642 */       return null;
/*     */     }
/*     */ 
/*     */     public String getAccessibleIconDescription()
/*     */     {
/* 658 */       return ImageIcon.this.getDescription();
/*     */     }
/*     */ 
/*     */     public void setAccessibleIconDescription(String paramString)
/*     */     {
/* 670 */       ImageIcon.this.setDescription(paramString);
/*     */     }
/*     */ 
/*     */     public int getAccessibleIconHeight()
/*     */     {
/* 679 */       return ImageIcon.this.height;
/*     */     }
/*     */ 
/*     */     public int getAccessibleIconWidth()
/*     */     {
/* 688 */       return ImageIcon.this.width;
/*     */     }
/*     */ 
/*     */     private void readObject(ObjectInputStream paramObjectInputStream)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 694 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */ 
/*     */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */       throws IOException
/*     */     {
/* 700 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ImageIcon
 * JD-Core Version:    0.6.2
 */