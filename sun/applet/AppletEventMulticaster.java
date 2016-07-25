/*     */ package sun.applet;
/*     */ 
/*     */ public class AppletEventMulticaster
/*     */   implements AppletListener
/*     */ {
/*     */   private final AppletListener a;
/*     */   private final AppletListener b;
/*     */ 
/*     */   public AppletEventMulticaster(AppletListener paramAppletListener1, AppletListener paramAppletListener2)
/*     */   {
/*  45 */     this.a = paramAppletListener1; this.b = paramAppletListener2;
/*     */   }
/*     */ 
/*     */   public void appletStateChanged(AppletEvent paramAppletEvent) {
/*  49 */     this.a.appletStateChanged(paramAppletEvent);
/*  50 */     this.b.appletStateChanged(paramAppletEvent);
/*     */   }
/*     */ 
/*     */   public static AppletListener add(AppletListener paramAppletListener1, AppletListener paramAppletListener2)
/*     */   {
/*  60 */     return addInternal(paramAppletListener1, paramAppletListener2);
/*     */   }
/*     */ 
/*     */   public static AppletListener remove(AppletListener paramAppletListener1, AppletListener paramAppletListener2)
/*     */   {
/*  70 */     return removeInternal(paramAppletListener1, paramAppletListener2);
/*     */   }
/*     */ 
/*     */   private static AppletListener addInternal(AppletListener paramAppletListener1, AppletListener paramAppletListener2)
/*     */   {
/*  84 */     if (paramAppletListener1 == null) return paramAppletListener2;
/*  85 */     if (paramAppletListener2 == null) return paramAppletListener1;
/*  86 */     return new AppletEventMulticaster(paramAppletListener1, paramAppletListener2);
/*     */   }
/*     */ 
/*     */   protected AppletListener remove(AppletListener paramAppletListener)
/*     */   {
/*  96 */     if (paramAppletListener == this.a) return this.b;
/*  97 */     if (paramAppletListener == this.b) return this.a;
/*  98 */     AppletListener localAppletListener1 = removeInternal(this.a, paramAppletListener);
/*  99 */     AppletListener localAppletListener2 = removeInternal(this.b, paramAppletListener);
/* 100 */     if ((localAppletListener1 == this.a) && (localAppletListener2 == this.b)) {
/* 101 */       return this;
/*     */     }
/* 103 */     return addInternal(localAppletListener1, localAppletListener2);
/*     */   }
/*     */ 
/*     */   private static AppletListener removeInternal(AppletListener paramAppletListener1, AppletListener paramAppletListener2)
/*     */   {
/* 119 */     if ((paramAppletListener1 == paramAppletListener2) || (paramAppletListener1 == null))
/* 120 */       return null;
/* 121 */     if ((paramAppletListener1 instanceof AppletEventMulticaster)) {
/* 122 */       return ((AppletEventMulticaster)paramAppletListener1).remove(paramAppletListener2);
/*     */     }
/* 124 */     return paramAppletListener1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletEventMulticaster
 * JD-Core Version:    0.6.2
 */