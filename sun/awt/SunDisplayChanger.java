/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.IllegalComponentStateException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class SunDisplayChanger
/*     */ {
/*  57 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.multiscreen.SunDisplayChanger");
/*     */ 
/*  62 */   private Map listeners = Collections.synchronizedMap(new WeakHashMap(1));
/*     */ 
/*     */   public void add(DisplayChangedListener paramDisplayChangedListener)
/*     */   {
/*  71 */     if ((log.isLoggable(500)) && 
/*  72 */       (paramDisplayChangedListener == null)) {
/*  73 */       log.fine("Assertion (theListener != null) failed");
/*     */     }
/*     */ 
/*  76 */     if (log.isLoggable(400)) {
/*  77 */       log.finer("Adding listener: " + paramDisplayChangedListener);
/*     */     }
/*  79 */     this.listeners.put(paramDisplayChangedListener, null);
/*     */   }
/*     */ 
/*     */   public void remove(DisplayChangedListener paramDisplayChangedListener)
/*     */   {
/*  86 */     if ((log.isLoggable(500)) && 
/*  87 */       (paramDisplayChangedListener == null)) {
/*  88 */       log.fine("Assertion (theListener != null) failed");
/*     */     }
/*     */ 
/*  91 */     if (log.isLoggable(400)) {
/*  92 */       log.finer("Removing listener: " + paramDisplayChangedListener);
/*     */     }
/*  94 */     this.listeners.remove(paramDisplayChangedListener);
/*     */   }
/*     */ 
/*     */   public void notifyListeners()
/*     */   {
/* 102 */     if (log.isLoggable(300))
/* 103 */       log.finest("notifyListeners");
/*     */     HashMap localHashMap;
/* 119 */     synchronized (this.listeners) {
/* 120 */       localHashMap = new HashMap(this.listeners);
/*     */     }
/*     */ 
/* 123 */     Set localSet = localHashMap.keySet();
/* 124 */     ??? = localSet.iterator();
/* 125 */     while (((Iterator)???).hasNext()) {
/* 126 */       DisplayChangedListener localDisplayChangedListener = (DisplayChangedListener)((Iterator)???).next();
/*     */       try
/*     */       {
/* 129 */         if (log.isLoggable(300)) {
/* 130 */           log.finest("displayChanged for listener: " + localDisplayChangedListener);
/*     */         }
/* 132 */         localDisplayChangedListener.displayChanged();
/*     */       }
/*     */       catch (IllegalComponentStateException localIllegalComponentStateException)
/*     */       {
/* 139 */         this.listeners.remove(localDisplayChangedListener);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void notifyPaletteChanged()
/*     */   {
/* 149 */     if (log.isLoggable(300))
/* 150 */       log.finest("notifyPaletteChanged");
/*     */     HashMap localHashMap;
/* 166 */     synchronized (this.listeners) {
/* 167 */       localHashMap = new HashMap(this.listeners);
/*     */     }
/* 169 */     Set localSet = localHashMap.keySet();
/* 170 */     ??? = localSet.iterator();
/* 171 */     while (((Iterator)???).hasNext()) {
/* 172 */       DisplayChangedListener localDisplayChangedListener = (DisplayChangedListener)((Iterator)???).next();
/*     */       try
/*     */       {
/* 175 */         if (log.isLoggable(300)) {
/* 176 */           log.finest("paletteChanged for listener: " + localDisplayChangedListener);
/*     */         }
/* 178 */         localDisplayChangedListener.paletteChanged();
/*     */       }
/*     */       catch (IllegalComponentStateException localIllegalComponentStateException)
/*     */       {
/* 185 */         this.listeners.remove(localDisplayChangedListener);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.SunDisplayChanger
 * JD-Core Version:    0.6.2
 */