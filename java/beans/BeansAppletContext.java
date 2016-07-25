/*     */ package java.beans;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.applet.AppletContext;
/*     */ import java.applet.AudioClip;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class BeansAppletContext
/*     */   implements AppletContext
/*     */ {
/*     */   Applet target;
/* 495 */   Hashtable imageCache = new Hashtable();
/*     */ 
/*     */   BeansAppletContext(Applet paramApplet) {
/* 498 */     this.target = paramApplet;
/*     */   }
/*     */ 
/*     */   public AudioClip getAudioClip(URL paramURL)
/*     */   {
/*     */     try
/*     */     {
/* 506 */       return (AudioClip)paramURL.getContent(); } catch (Exception localException) {
/*     */     }
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized Image getImage(URL paramURL)
/*     */   {
/* 513 */     Object localObject = this.imageCache.get(paramURL);
/* 514 */     if (localObject != null)
/* 515 */       return (Image)localObject;
/*     */     try
/*     */     {
/* 518 */       localObject = paramURL.getContent();
/* 519 */       if (localObject == null) {
/* 520 */         return null;
/*     */       }
/* 522 */       if ((localObject instanceof Image)) {
/* 523 */         this.imageCache.put(paramURL, localObject);
/* 524 */         return (Image)localObject;
/*     */       }
/*     */ 
/* 527 */       Image localImage = this.target.createImage((ImageProducer)localObject);
/* 528 */       this.imageCache.put(paramURL, localImage);
/* 529 */       return localImage;
/*     */     } catch (Exception localException) {
/*     */     }
/* 532 */     return null;
/*     */   }
/*     */ 
/*     */   public Applet getApplet(String paramString)
/*     */   {
/* 537 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration getApplets() {
/* 541 */     Vector localVector = new Vector();
/* 542 */     localVector.addElement(this.target);
/* 543 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public void showDocument(URL paramURL)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void showDocument(URL paramURL, String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void showStatus(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setStream(String paramString, InputStream paramInputStream) throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public InputStream getStream(String paramString)
/*     */   {
/* 564 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator getStreamKeys()
/*     */   {
/* 569 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.BeansAppletContext
 * JD-Core Version:    0.6.2
 */