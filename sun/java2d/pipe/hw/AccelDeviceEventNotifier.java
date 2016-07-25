/*     */ package sun.java2d.pipe.hw;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class AccelDeviceEventNotifier
/*     */ {
/*     */   private static AccelDeviceEventNotifier theInstance;
/*     */   public static final int DEVICE_RESET = 0;
/*     */   public static final int DEVICE_DISPOSED = 1;
/*     */   private final Map<AccelDeviceEventListener, Integer> listeners;
/*     */ 
/*     */   private AccelDeviceEventNotifier()
/*     */   {
/*  58 */     this.listeners = Collections.synchronizedMap(new HashMap(1));
/*     */   }
/*     */ 
/*     */   private static synchronized AccelDeviceEventNotifier getInstance(boolean paramBoolean)
/*     */   {
/*  75 */     if ((theInstance == null) && (paramBoolean)) {
/*  76 */       theInstance = new AccelDeviceEventNotifier();
/*     */     }
/*  78 */     return theInstance;
/*     */   }
/*     */ 
/*     */   public static final void eventOccured(int paramInt1, int paramInt2)
/*     */   {
/*  93 */     AccelDeviceEventNotifier localAccelDeviceEventNotifier = getInstance(false);
/*  94 */     if (localAccelDeviceEventNotifier != null)
/*  95 */       localAccelDeviceEventNotifier.notifyListeners(paramInt2, paramInt1);
/*     */   }
/*     */ 
/*     */   public static final void addListener(AccelDeviceEventListener paramAccelDeviceEventListener, int paramInt)
/*     */   {
/* 110 */     getInstance(true).add(paramAccelDeviceEventListener, paramInt);
/*     */   }
/*     */ 
/*     */   public static final void removeListener(AccelDeviceEventListener paramAccelDeviceEventListener)
/*     */   {
/* 119 */     getInstance(true).remove(paramAccelDeviceEventListener);
/*     */   }
/*     */ 
/*     */   private final void add(AccelDeviceEventListener paramAccelDeviceEventListener, int paramInt) {
/* 123 */     this.listeners.put(paramAccelDeviceEventListener, Integer.valueOf(paramInt));
/*     */   }
/*     */   private final void remove(AccelDeviceEventListener paramAccelDeviceEventListener) {
/* 126 */     this.listeners.remove(paramAccelDeviceEventListener);
/*     */   }
/*     */ 
/*     */   private final void notifyListeners(int paramInt1, int paramInt2)
/*     */   {
/*     */     HashMap localHashMap;
/* 146 */     synchronized (this.listeners) {
/* 147 */       localHashMap = new HashMap(this.listeners);
/*     */     }
/*     */ 
/* 151 */     Set localSet = localHashMap.keySet();
/* 152 */     ??? = localSet.iterator();
/* 153 */     while (((Iterator)???).hasNext()) {
/* 154 */       AccelDeviceEventListener localAccelDeviceEventListener = (AccelDeviceEventListener)((Iterator)???).next();
/* 155 */       Integer localInteger = (Integer)localHashMap.get(localAccelDeviceEventListener);
/*     */ 
/* 157 */       if ((localInteger == null) || (localInteger.intValue() == paramInt2))
/*     */       {
/* 160 */         if (paramInt1 == 0)
/* 161 */           localAccelDeviceEventListener.onDeviceReset();
/* 162 */         else if (paramInt1 == 1)
/* 163 */           localAccelDeviceEventListener.onDeviceDispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.hw.AccelDeviceEventNotifier
 * JD-Core Version:    0.6.2
 */