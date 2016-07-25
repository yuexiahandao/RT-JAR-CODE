/*     */ package sun.java2d;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import sun.misc.ThreadGroupUtils;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class Disposer
/*     */   implements Runnable
/*     */ {
/*  54 */   private static final ReferenceQueue queue = new ReferenceQueue();
/*  55 */   private static final Hashtable records = new Hashtable();
/*     */   private static Disposer disposerInstance;
/*     */   public static final int WEAK = 0;
/*     */   public static final int PHANTOM = 1;
/*  60 */   public static int refType = 1;
/*     */ 
/* 167 */   private static ArrayList<DisposerRecord> deferredRecords = null;
/*     */ 
/* 187 */   public static volatile boolean pollingQueue = false;
/*     */ 
/*     */   public static void addRecord(Object paramObject, long paramLong1, long paramLong2)
/*     */   {
/* 107 */     disposerInstance.add(paramObject, new DefaultDisposerRecord(paramLong1, paramLong2));
/*     */   }
/*     */ 
/*     */   public static void addRecord(Object paramObject, DisposerRecord paramDisposerRecord)
/*     */   {
/* 118 */     disposerInstance.add(paramObject, paramDisposerRecord);
/*     */   }
/*     */ 
/*     */   synchronized void add(Object paramObject, DisposerRecord paramDisposerRecord)
/*     */   {
/* 130 */     if ((paramObject instanceof DisposerTarget))
/* 131 */       paramObject = ((DisposerTarget)paramObject).getDisposerReferent();
/*     */     Object localObject;
/* 134 */     if (refType == 1)
/* 135 */       localObject = new PhantomReference(paramObject, queue);
/*     */     else {
/* 137 */       localObject = new WeakReference(paramObject, queue);
/*     */     }
/* 139 */     records.put(localObject, paramDisposerRecord);
/*     */   }
/*     */ 
/*     */   public void run() {
/*     */     while (true)
/*     */       try {
/* 145 */         Reference localReference = queue.remove();
/* 146 */         ((Reference)localReference).clear();
/* 147 */         DisposerRecord localDisposerRecord = (DisposerRecord)records.remove(localReference);
/* 148 */         localDisposerRecord.dispose();
/* 149 */         localReference = null;
/* 150 */         localDisposerRecord = null;
/* 151 */         clearDeferredRecords();
/*     */       } catch (Exception localException) {
/* 153 */         System.out.println("Exception while removing reference.");
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void clearDeferredRecords()
/*     */   {
/* 170 */     if ((deferredRecords == null) || (deferredRecords.isEmpty())) {
/* 171 */       return;
/*     */     }
/* 173 */     for (int i = 0; i < deferredRecords.size(); i++) {
/*     */       try {
/* 175 */         DisposerRecord localDisposerRecord = (DisposerRecord)deferredRecords.get(i);
/* 176 */         localDisposerRecord.dispose();
/*     */       } catch (Exception localException) {
/* 178 */         System.out.println("Exception while disposing deferred rec.");
/*     */       }
/*     */     }
/* 181 */     deferredRecords.clear();
/*     */   }
/*     */ 
/*     */   public static void pollRemove()
/*     */   {
/* 200 */     if (pollingQueue) {
/* 201 */       return;
/*     */     }
/*     */ 
/* 204 */     pollingQueue = true;
/* 205 */     int i = 0;
/* 206 */     int j = 0;
/*     */     try
/*     */     {
/*     */       Reference localReference;
/* 209 */       while (((localReference = queue.poll()) != null) && (i < 10000) && (j < 100)) {
/* 210 */         i++;
/* 211 */         ((Reference)localReference).clear();
/* 212 */         DisposerRecord localDisposerRecord = (DisposerRecord)records.remove(localReference);
/* 213 */         if ((localDisposerRecord instanceof PollDisposable)) {
/* 214 */           localDisposerRecord.dispose();
/* 215 */           localReference = null;
/* 216 */           localDisposerRecord = null;
/*     */         }
/* 218 */         else if (localDisposerRecord != null)
/*     */         {
/* 221 */           j++;
/* 222 */           if (deferredRecords == null) {
/* 223 */             deferredRecords = new ArrayList(5);
/*     */           }
/* 225 */           deferredRecords.add(localDisposerRecord);
/*     */         }
/*     */       }
/*     */     } catch (Exception localException) {
/* 229 */       System.out.println("Exception while removing reference.");
/*     */     } finally {
/* 231 */       pollingQueue = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public static void addReference(Reference paramReference, DisposerRecord paramDisposerRecord)
/*     */   {
/* 246 */     records.put(paramReference, paramDisposerRecord);
/*     */   }
/*     */ 
/*     */   public static void addObjectRecord(Object paramObject, DisposerRecord paramDisposerRecord) {
/* 250 */     records.put(new WeakReference(paramObject, queue), paramDisposerRecord);
/*     */   }
/*     */ 
/*     */   public static ReferenceQueue getQueue()
/*     */   {
/* 256 */     return queue;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  63 */     AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*     */ 
/*  65 */     initIDs();
/*  66 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.reftype"));
/*     */ 
/*  68 */     if (str != null) {
/*  69 */       if (str.equals("weak")) {
/*  70 */         refType = 0;
/*  71 */         System.err.println("Using WEAK refs");
/*     */       } else {
/*  73 */         refType = 1;
/*  74 */         System.err.println("Using PHANTOM refs");
/*     */       }
/*     */     }
/*  77 */     disposerInstance = new Disposer();
/*  78 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/*  85 */         ThreadGroup localThreadGroup = ThreadGroupUtils.getRootThreadGroup();
/*  86 */         Thread localThread = new Thread(localThreadGroup, Disposer.disposerInstance, "Java2D Disposer");
/*  87 */         localThread.setContextClassLoader(null);
/*  88 */         localThread.setDaemon(true);
/*  89 */         localThread.setPriority(10);
/*  90 */         localThread.start();
/*  91 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static abstract interface PollDisposable
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.Disposer
 * JD-Core Version:    0.6.2
 */