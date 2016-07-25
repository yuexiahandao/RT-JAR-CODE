/*     */ package java.applet;
/*     */ 
/*     */ import java.awt.AWTPermission;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Panel.AccessibleAWTPanel;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import sun.applet.AppletAudioClip;
/*     */ 
/*     */ public class Applet extends Panel
/*     */ {
/*     */   private transient AppletStub stub;
/*     */   private static final long serialVersionUID = -5836846270535785031L;
/* 540 */   AccessibleContext accessibleContext = null;
/*     */ 
/*     */   public Applet()
/*     */     throws HeadlessException
/*     */   {
/*  66 */     if (GraphicsEnvironment.isHeadless())
/*  67 */       throw new HeadlessException();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/*  98 */     if (GraphicsEnvironment.isHeadless()) {
/*  99 */       throw new HeadlessException();
/*     */     }
/* 101 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   public final void setStub(AppletStub paramAppletStub)
/*     */   {
/* 114 */     if (this.stub != null) {
/* 115 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 116 */       if (localSecurityManager != null) {
/* 117 */         localSecurityManager.checkPermission(new AWTPermission("setAppletStub"));
/*     */       }
/*     */     }
/* 120 */     this.stub = paramAppletStub;
/*     */   }
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 134 */     if (this.stub != null) {
/* 135 */       return this.stub.isActive();
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   public URL getDocumentBase()
/*     */   {
/* 158 */     return this.stub.getDocumentBase();
/*     */   }
/*     */ 
/*     */   public URL getCodeBase()
/*     */   {
/* 169 */     return this.stub.getCodeBase();
/*     */   }
/*     */ 
/*     */   public String getParameter(String paramString)
/*     */   {
/* 191 */     return this.stub.getParameter(paramString);
/*     */   }
/*     */ 
/*     */   public AppletContext getAppletContext()
/*     */   {
/* 204 */     return this.stub.getAppletContext();
/*     */   }
/*     */ 
/*     */   public void resize(int paramInt1, int paramInt2)
/*     */   {
/* 214 */     Dimension localDimension = size();
/* 215 */     if ((localDimension.width != paramInt1) || (localDimension.height != paramInt2)) {
/* 216 */       super.resize(paramInt1, paramInt2);
/* 217 */       if (this.stub != null)
/* 218 */         this.stub.appletResize(paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resize(Dimension paramDimension)
/*     */   {
/* 229 */     resize(paramDimension.width, paramDimension.height);
/*     */   }
/*     */ 
/*     */   public boolean isValidateRoot()
/*     */   {
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */   public void showStatus(String paramString)
/*     */   {
/* 256 */     getAppletContext().showStatus(paramString);
/*     */   }
/*     */ 
/*     */   public Image getImage(URL paramURL)
/*     */   {
/* 274 */     return getAppletContext().getImage(paramURL);
/*     */   }
/*     */ 
/*     */   public Image getImage(URL paramURL, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 296 */       return getImage(new URL(paramURL, paramString)); } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   public static final AudioClip newAudioClip(URL paramURL)
/*     */   {
/* 311 */     return new AppletAudioClip(paramURL);
/*     */   }
/*     */ 
/*     */   public AudioClip getAudioClip(URL paramURL)
/*     */   {
/* 327 */     return getAppletContext().getAudioClip(paramURL);
/*     */   }
/*     */ 
/*     */   public AudioClip getAudioClip(URL paramURL, String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 347 */       return getAudioClip(new URL(paramURL, paramString)); } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 349 */     return null;
/*     */   }
/*     */ 
/*     */   public String getAppletInfo()
/*     */   {
/* 365 */     return null;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 378 */     Locale localLocale = super.getLocale();
/* 379 */     if (localLocale == null) {
/* 380 */       return Locale.getDefault();
/*     */     }
/* 382 */     return localLocale;
/*     */   }
/*     */ 
/*     */   public String[][] getParameterInfo()
/*     */   {
/* 407 */     return (String[][])null;
/*     */   }
/*     */ 
/*     */   public void play(URL paramURL)
/*     */   {
/* 417 */     AudioClip localAudioClip = getAudioClip(paramURL);
/* 418 */     if (localAudioClip != null)
/* 419 */       localAudioClip.play();
/*     */   }
/*     */ 
/*     */   public void play(URL paramURL, String paramString)
/*     */   {
/* 433 */     AudioClip localAudioClip = getAudioClip(paramURL, paramString);
/* 434 */     if (localAudioClip != null)
/* 435 */       localAudioClip.play();
/*     */   }
/*     */ 
/*     */   public void init()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 553 */     if (this.accessibleContext == null) {
/* 554 */       this.accessibleContext = new AccessibleApplet();
/*     */     }
/* 556 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleApplet extends Panel.AccessibleAWTPanel
/*     */   {
/*     */     private static final long serialVersionUID = 8127374778187708896L;
/*     */ 
/*     */     protected AccessibleApplet()
/*     */     {
/* 565 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 576 */       return AccessibleRole.FRAME;
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 587 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 588 */       localAccessibleStateSet.add(AccessibleState.ACTIVE);
/* 589 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.applet.Applet
 * JD-Core Version:    0.6.2
 */