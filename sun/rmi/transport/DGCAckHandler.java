/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.rmi.server.UID;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.rmi.runtime.RuntimeUtil;
/*     */ import sun.rmi.runtime.RuntimeUtil.GetInstanceAction;
/*     */ import sun.security.action.GetLongAction;
/*     */ 
/*     */ public class DGCAckHandler
/*     */ {
/*  67 */   private static final long dgcAckTimeout = ((Long)AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.ackTimeout", 300000L))).longValue();
/*     */ 
/*  72 */   private static final ScheduledExecutorService scheduler = ((RuntimeUtil)AccessController.doPrivileged(new RuntimeUtil.GetInstanceAction())).getScheduler();
/*     */ 
/*  77 */   private static final Map<UID, DGCAckHandler> idTable = Collections.synchronizedMap(new HashMap());
/*     */   private final UID id;
/*  81 */   private List<Object> objList = new ArrayList();
/*  82 */   private Future<?> task = null;
/*     */ 
/*     */   DGCAckHandler(UID paramUID)
/*     */   {
/*  96 */     this.id = paramUID;
/*  97 */     if (paramUID != null) {
/*  98 */       assert (!idTable.containsKey(paramUID));
/*  99 */       idTable.put(paramUID, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void add(Object paramObject)
/*     */   {
/* 107 */     if (this.objList != null)
/* 108 */       this.objList.add(paramObject);
/*     */   }
/*     */ 
/*     */   synchronized void startTimer()
/*     */   {
/* 118 */     if ((this.objList != null) && (this.task == null))
/* 119 */       this.task = scheduler.schedule(new Runnable() {
/*     */         public void run() {
/* 121 */           DGCAckHandler.this.release();
/*     */         }
/*     */       }
/*     */       , dgcAckTimeout, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   synchronized void release()
/*     */   {
/* 131 */     if (this.task != null) {
/* 132 */       this.task.cancel(false);
/* 133 */       this.task = null;
/*     */     }
/* 135 */     this.objList = null;
/*     */   }
/*     */ 
/*     */   public static void received(UID paramUID)
/*     */   {
/* 143 */     DGCAckHandler localDGCAckHandler = (DGCAckHandler)idTable.remove(paramUID);
/* 144 */     if (localDGCAckHandler != null)
/* 145 */       localDGCAckHandler.release();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.DGCAckHandler
 * JD-Core Version:    0.6.2
 */