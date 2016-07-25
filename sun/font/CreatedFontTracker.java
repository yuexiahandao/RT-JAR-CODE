/*     */ package sun.font;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.awt.AppContext;
/*     */ import sun.misc.ThreadGroupUtils;
/*     */ 
/*     */ public class CreatedFontTracker
/*     */ {
/*     */   public static final int MAX_FILE_SIZE = 33554432;
/*     */   public static final int MAX_TOTAL_BYTES = 335544320;
/*     */   static CreatedFontTracker tracker;
/*     */   int numBytes;
/*     */ 
/*     */   public static synchronized CreatedFontTracker getTracker()
/*     */   {
/*  49 */     if (tracker == null) {
/*  50 */       tracker = new CreatedFontTracker();
/*     */     }
/*  52 */     return tracker;
/*     */   }
/*     */ 
/*     */   private CreatedFontTracker() {
/*  56 */     this.numBytes = 0;
/*     */   }
/*     */ 
/*     */   public synchronized int getNumBytes() {
/*  60 */     return this.numBytes;
/*     */   }
/*     */ 
/*     */   public synchronized void addBytes(int paramInt) {
/*  64 */     this.numBytes += paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void subBytes(int paramInt) {
/*  68 */     this.numBytes -= paramInt;
/*     */   }
/*     */ 
/*     */   private static synchronized Semaphore getCS()
/*     */   {
/*  75 */     AppContext localAppContext = AppContext.getAppContext();
/*  76 */     Semaphore localSemaphore = (Semaphore)localAppContext.get(CreatedFontTracker.class);
/*  77 */     if (localSemaphore == null)
/*     */     {
/*  80 */       localSemaphore = new Semaphore(5, true);
/*  81 */       localAppContext.put(CreatedFontTracker.class, localSemaphore);
/*     */     }
/*  83 */     return localSemaphore;
/*     */   }
/*     */ 
/*     */   public boolean acquirePermit() throws InterruptedException
/*     */   {
/*  88 */     return getCS().tryAcquire(120L, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */   public void releasePermit() {
/*  92 */     getCS().release();
/*     */   }
/*     */ 
/*     */   public void add(File paramFile) {
/*  96 */     TempFileDeletionHook.add(paramFile);
/*     */   }
/*     */ 
/*     */   public void set(File paramFile, OutputStream paramOutputStream) {
/* 100 */     TempFileDeletionHook.set(paramFile, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void remove(File paramFile) {
/* 104 */     TempFileDeletionHook.remove(paramFile);
/*     */   }
/*     */ 
/*     */   private static class TempFileDeletionHook
/*     */   {
/* 112 */     private static HashMap<File, OutputStream> files = new HashMap();
/*     */ 
/* 114 */     private static Thread t = null;
/*     */ 
/* 116 */     static void init() { if (t == null)
/*     */       {
/* 118 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run()
/*     */           {
/* 125 */             ThreadGroup localThreadGroup = ThreadGroupUtils.getRootThreadGroup();
/* 126 */             CreatedFontTracker.TempFileDeletionHook.access$002(new Thread(localThreadGroup, new Runnable()
/*     */             {
/*     */               public void run() {
/* 129 */                 CreatedFontTracker.TempFileDeletionHook.runHooks();
/*     */               }
/*     */             }));
/* 132 */             CreatedFontTracker.TempFileDeletionHook.t.setContextClassLoader(null);
/* 133 */             Runtime.getRuntime().addShutdownHook(CreatedFontTracker.TempFileDeletionHook.t);
/* 134 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */ 
/*     */     static synchronized void add(File paramFile)
/*     */     {
/* 143 */       init();
/* 144 */       files.put(paramFile, null);
/*     */     }
/*     */ 
/*     */     static synchronized void set(File paramFile, OutputStream paramOutputStream) {
/* 148 */       files.put(paramFile, paramOutputStream);
/*     */     }
/*     */ 
/*     */     static synchronized void remove(File paramFile) {
/* 152 */       files.remove(paramFile);
/*     */     }
/*     */ 
/*     */     static synchronized void runHooks() {
/* 156 */       if (files.isEmpty()) {
/* 157 */         return;
/*     */       }
/*     */ 
/* 160 */       for (Map.Entry localEntry : files.entrySet())
/*     */       {
/*     */         try {
/* 163 */           if (localEntry.getValue() != null)
/* 164 */             ((OutputStream)localEntry.getValue()).close();
/*     */         } catch (Exception localException) {
/*     */         }
/* 167 */         ((File)localEntry.getKey()).delete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CreatedFontTracker
 * JD-Core Version:    0.6.2
 */