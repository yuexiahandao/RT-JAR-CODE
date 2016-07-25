/*     */ package java.beans;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.applet.AppletContext;
/*     */ import java.applet.AppletStub;
/*     */ import java.net.URL;
/*     */ 
/*     */ class BeansAppletStub
/*     */   implements AppletStub
/*     */ {
/*     */   transient boolean active;
/*     */   transient Applet target;
/*     */   transient AppletContext context;
/*     */   transient URL codeBase;
/*     */   transient URL docBase;
/*     */ 
/*     */   BeansAppletStub(Applet paramApplet, AppletContext paramAppletContext, URL paramURL1, URL paramURL2)
/*     */   {
/* 587 */     this.target = paramApplet;
/* 588 */     this.context = paramAppletContext;
/* 589 */     this.codeBase = paramURL1;
/* 590 */     this.docBase = paramURL2;
/*     */   }
/*     */ 
/*     */   public boolean isActive() {
/* 594 */     return this.active;
/*     */   }
/*     */ 
/*     */   public URL getDocumentBase()
/*     */   {
/* 599 */     return this.docBase;
/*     */   }
/*     */ 
/*     */   public URL getCodeBase()
/*     */   {
/* 604 */     return this.codeBase;
/*     */   }
/*     */ 
/*     */   public String getParameter(String paramString) {
/* 608 */     return null;
/*     */   }
/*     */ 
/*     */   public AppletContext getAppletContext() {
/* 612 */     return this.context;
/*     */   }
/*     */ 
/*     */   public void appletResize(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.BeansAppletStub
 * JD-Core Version:    0.6.2
 */