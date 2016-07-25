/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchEvent.Kind;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ 
/*     */ abstract class AbstractWatchKey
/*     */   implements WatchKey
/*     */ {
/*     */   static final int MAX_EVENT_LIST_SIZE = 512;
/*  45 */   static final Event<Object> OVERFLOW_EVENT = new Event(StandardWatchEventKinds.OVERFLOW, null);
/*     */   private final AbstractWatchService watcher;
/*     */   private final Path dir;
/*     */   private State state;
/*     */   private List<WatchEvent<?>> events;
/*     */   private Map<Object, WatchEvent<?>> lastModifyEvents;
/*     */ 
/*     */   protected AbstractWatchKey(Path paramPath, AbstractWatchService paramAbstractWatchService)
/*     */   {
/*  70 */     this.watcher = paramAbstractWatchService;
/*  71 */     this.dir = paramPath;
/*  72 */     this.state = State.READY;
/*  73 */     this.events = new ArrayList();
/*  74 */     this.lastModifyEvents = new HashMap();
/*     */   }
/*     */ 
/*     */   final AbstractWatchService watcher() {
/*  78 */     return this.watcher;
/*     */   }
/*     */ 
/*     */   public Path watchable()
/*     */   {
/*  86 */     return this.dir;
/*     */   }
/*     */ 
/*     */   final void signal()
/*     */   {
/*  93 */     synchronized (this) {
/*  94 */       if (this.state == State.READY) {
/*  95 */         this.state = State.SIGNALLED;
/*  96 */         this.watcher.enqueueKey(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final void signalEvent(WatchEvent.Kind<?> paramKind, Object paramObject)
/*     */   {
/* 106 */     int i = paramKind == StandardWatchEventKinds.ENTRY_MODIFY ? 1 : 0;
/* 107 */     synchronized (this) {
/* 108 */       int j = this.events.size();
/* 109 */       if (j > 0)
/*     */       {
/* 112 */         localObject1 = (WatchEvent)this.events.get(j - 1);
/* 113 */         if ((((WatchEvent)localObject1).kind() == StandardWatchEventKinds.OVERFLOW) || ((paramKind == ((WatchEvent)localObject1).kind()) && (Objects.equals(paramObject, ((WatchEvent)localObject1).context()))))
/*     */         {
/* 117 */           ((Event)localObject1).increment();
/* 118 */           return;
/*     */         }
/*     */ 
/* 123 */         if (!this.lastModifyEvents.isEmpty()) {
/* 124 */           if (i != 0) {
/* 125 */             WatchEvent localWatchEvent = (WatchEvent)this.lastModifyEvents.get(paramObject);
/* 126 */             if (localWatchEvent != null) {
/* 127 */               assert (localWatchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY);
/* 128 */               ((Event)localWatchEvent).increment();
/* 129 */               return;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 134 */             this.lastModifyEvents.remove(paramObject);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 140 */         if (j >= 512) {
/* 141 */           paramKind = StandardWatchEventKinds.OVERFLOW;
/* 142 */           i = 0;
/* 143 */           paramObject = null;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 148 */       Object localObject1 = new Event(paramKind, paramObject);
/*     */ 
/* 150 */       if (i != 0) {
/* 151 */         this.lastModifyEvents.put(paramObject, localObject1);
/* 152 */       } else if (paramKind == StandardWatchEventKinds.OVERFLOW)
/*     */       {
/* 154 */         this.events.clear();
/* 155 */         this.lastModifyEvents.clear();
/*     */       }
/* 157 */       this.events.add(localObject1);
/* 158 */       signal();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final List<WatchEvent<?>> pollEvents()
/*     */   {
/* 164 */     synchronized (this) {
/* 165 */       List localList = this.events;
/* 166 */       this.events = new ArrayList();
/* 167 */       this.lastModifyEvents.clear();
/* 168 */       return localList;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean reset()
/*     */   {
/* 174 */     synchronized (this) {
/* 175 */       if ((this.state == State.SIGNALLED) && (isValid())) {
/* 176 */         if (this.events.isEmpty()) {
/* 177 */           this.state = State.READY;
/*     */         }
/*     */         else {
/* 180 */           this.watcher.enqueueKey(this);
/*     */         }
/*     */       }
/* 183 */       return isValid();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Event<T>
/*     */     implements WatchEvent<T>
/*     */   {
/*     */     private final WatchEvent.Kind<T> kind;
/*     */     private final T context;
/*     */     private int count;
/*     */ 
/*     */     Event(WatchEvent.Kind<T> paramKind, T paramT)
/*     */     {
/* 198 */       this.kind = paramKind;
/* 199 */       this.context = paramT;
/* 200 */       this.count = 1;
/*     */     }
/*     */ 
/*     */     public WatchEvent.Kind<T> kind()
/*     */     {
/* 205 */       return this.kind;
/*     */     }
/*     */ 
/*     */     public T context()
/*     */     {
/* 210 */       return this.context;
/*     */     }
/*     */ 
/*     */     public int count()
/*     */     {
/* 215 */       return this.count;
/*     */     }
/*     */ 
/*     */     void increment()
/*     */     {
/* 220 */       this.count += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum State
/*     */   {
/*  51 */     READY, SIGNALLED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.AbstractWatchKey
 * JD-Core Version:    0.6.2
 */