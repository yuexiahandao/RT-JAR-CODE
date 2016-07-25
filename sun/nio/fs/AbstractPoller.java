/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.ClosedWatchServiceException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent.Kind;
/*     */ import java.nio.file.WatchEvent.Modifier;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ 
/*     */ abstract class AbstractPoller
/*     */   implements Runnable
/*     */ {
/*     */   private final LinkedList<Request> requestList;
/*     */   private boolean shutdown;
/*     */ 
/*     */   protected AbstractPoller()
/*     */   {
/*  50 */     this.requestList = new LinkedList();
/*  51 */     this.shutdown = false;
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  58 */     final AbstractPoller localAbstractPoller = this;
/*  59 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  62 */         Thread localThread = new Thread(localAbstractPoller);
/*  63 */         localThread.setDaemon(true);
/*  64 */         localThread.start();
/*  65 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   abstract void wakeup()
/*     */     throws IOException;
/*     */ 
/*     */   abstract Object implRegister(Path paramPath, Set<? extends WatchEvent.Kind<?>> paramSet, WatchEvent.Modifier[] paramArrayOfModifier);
/*     */ 
/*     */   abstract void implCancelKey(WatchKey paramWatchKey);
/*     */ 
/*     */   abstract void implCloseAll();
/*     */ 
/*     */   final WatchKey register(Path paramPath, WatchEvent.Kind<?>[] paramArrayOfKind, WatchEvent.Modifier[] paramArrayOfModifier)
/*     */     throws IOException
/*     */   {
/* 101 */     if (paramPath == null)
/* 102 */       throw new NullPointerException();
/* 103 */     if (paramArrayOfKind.length == 0)
/* 104 */       throw new IllegalArgumentException("No events to register");
/* 105 */     HashSet localHashSet = new HashSet(paramArrayOfKind.length);
/* 106 */     for (WatchEvent.Kind<?> localKind : paramArrayOfKind)
/*     */     {
/* 108 */       if ((localKind == StandardWatchEventKinds.ENTRY_CREATE) || (localKind == StandardWatchEventKinds.ENTRY_MODIFY) || (localKind == StandardWatchEventKinds.ENTRY_DELETE))
/*     */       {
/* 112 */         localHashSet.add(localKind);
/*     */       }
/* 117 */       else if (localKind == StandardWatchEventKinds.OVERFLOW) {
/* 118 */         if (paramArrayOfKind.length == 1) {
/* 119 */           throw new IllegalArgumentException("No events to register");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 124 */         if (localKind == null)
/* 125 */           throw new NullPointerException("An element in event set is 'null'");
/* 126 */         throw new UnsupportedOperationException(localKind.name());
/*     */       }
/*     */     }
/* 128 */     return (WatchKey)invoke(RequestType.REGISTER, new Object[] { paramPath, localHashSet, paramArrayOfModifier });
/*     */   }
/*     */ 
/*     */   final void cancel(WatchKey paramWatchKey)
/*     */   {
/*     */     try
/*     */     {
/* 136 */       invoke(RequestType.CANCEL, new Object[] { paramWatchKey });
/*     */     }
/*     */     catch (IOException localIOException) {
/* 139 */       throw new AssertionError(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   final void close()
/*     */     throws IOException
/*     */   {
/* 147 */     invoke(RequestType.CLOSE, new Object[0]);
/*     */   }
/*     */ 
/*     */   private Object invoke(RequestType paramRequestType, Object[] paramArrayOfObject)
/*     */     throws IOException
/*     */   {
/* 213 */     Request localRequest = new Request(paramRequestType, paramArrayOfObject);
/* 214 */     synchronized (this.requestList) {
/* 215 */       if (this.shutdown) {
/* 216 */         throw new ClosedWatchServiceException();
/*     */       }
/* 218 */       this.requestList.add(localRequest);
/*     */     }
/*     */ 
/* 222 */     wakeup();
/*     */ 
/* 225 */     ??? = localRequest.awaitResult();
/*     */ 
/* 227 */     if ((??? instanceof RuntimeException))
/* 228 */       throw ((RuntimeException)???);
/* 229 */     if ((??? instanceof IOException))
/* 230 */       throw ((IOException)???);
/* 231 */     return ???;
/*     */   }
/*     */ 
/*     */   boolean processRequests()
/*     */   {
/* 241 */     synchronized (this.requestList)
/*     */     {
/*     */       Request localRequest;
/* 243 */       while ((localRequest = (Request)this.requestList.poll()) != null)
/*     */       {
/* 245 */         if (this.shutdown)
/* 246 */           localRequest.release(new ClosedWatchServiceException());
/*     */         Object[] arrayOfObject;
/*     */         Object localObject1;
/* 249 */         switch (2.$SwitchMap$sun$nio$fs$AbstractPoller$RequestType[localRequest.type().ordinal()])
/*     */         {
/*     */         case 1:
/* 254 */           arrayOfObject = localRequest.parameters();
/* 255 */           localObject1 = (Path)arrayOfObject[0];
/* 256 */           Set localSet = (Set)arrayOfObject[1];
/*     */ 
/* 258 */           WatchEvent.Modifier[] arrayOfModifier = (WatchEvent.Modifier[])arrayOfObject[2];
/*     */ 
/* 260 */           localRequest.release(implRegister((Path)localObject1, localSet, arrayOfModifier));
/* 261 */           break;
/*     */         case 2:
/* 267 */           arrayOfObject = localRequest.parameters();
/* 268 */           localObject1 = (WatchKey)arrayOfObject[0];
/* 269 */           implCancelKey((WatchKey)localObject1);
/* 270 */           localRequest.release(null);
/* 271 */           break;
/*     */         case 3:
/* 277 */           implCloseAll();
/* 278 */           localRequest.release(null);
/* 279 */           this.shutdown = true;
/* 280 */           break;
/*     */         default:
/* 284 */           localRequest.release(new IOException("request not recognized"));
/*     */         }
/*     */       }
/*     */     }
/* 288 */     return this.shutdown;
/*     */   }
/*     */ 
/*     */   private static class Request
/*     */   {
/*     */     private final AbstractPoller.RequestType type;
/*     */     private final Object[] params;
/* 166 */     private boolean completed = false;
/* 167 */     private Object result = null;
/*     */ 
/*     */     Request(AbstractPoller.RequestType paramRequestType, Object[] paramArrayOfObject) {
/* 170 */       this.type = paramRequestType;
/* 171 */       this.params = paramArrayOfObject;
/*     */     }
/*     */ 
/*     */     AbstractPoller.RequestType type() {
/* 175 */       return this.type;
/*     */     }
/*     */ 
/*     */     Object[] parameters() {
/* 179 */       return this.params;
/*     */     }
/*     */ 
/*     */     void release(Object paramObject) {
/* 183 */       synchronized (this) {
/* 184 */         this.completed = true;
/* 185 */         this.result = paramObject;
/* 186 */         notifyAll();
/*     */       }
/*     */     }
/*     */ 
/*     */     Object awaitResult()
/*     */     {
/* 195 */       synchronized (this) {
/* 196 */         while (!this.completed)
/*     */           try {
/* 198 */             wait();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/* 203 */         return this.result;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum RequestType
/*     */   {
/* 154 */     REGISTER, 
/* 155 */     CANCEL, 
/* 156 */     CLOSE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractPoller
 * JD-Core Version:    0.6.2
 */