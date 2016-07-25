/*     */ package javax.naming.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ import javax.naming.Binding;
/*     */ 
/*     */ public class NamingEvent extends EventObject
/*     */ {
/*     */   public static final int OBJECT_ADDED = 0;
/*     */   public static final int OBJECT_REMOVED = 1;
/*     */   public static final int OBJECT_RENAMED = 2;
/*     */   public static final int OBJECT_CHANGED = 3;
/*     */   protected Object changeInfo;
/*     */   protected int type;
/*     */   protected Binding oldBinding;
/*     */   protected Binding newBinding;
/*     */   private static final long serialVersionUID = -7126752885365133499L;
/*     */ 
/*     */   public NamingEvent(EventContext paramEventContext, int paramInt, Binding paramBinding1, Binding paramBinding2, Object paramObject)
/*     */   {
/* 174 */     super(paramEventContext);
/* 175 */     this.type = paramInt;
/* 176 */     this.oldBinding = paramBinding2;
/* 177 */     this.newBinding = paramBinding1;
/* 178 */     this.changeInfo = paramObject;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 190 */     return this.type;
/*     */   }
/*     */ 
/*     */   public EventContext getEventContext()
/*     */   {
/* 209 */     return (EventContext)getSource();
/*     */   }
/*     */ 
/*     */   public Binding getOldBinding()
/*     */   {
/* 231 */     return this.oldBinding;
/*     */   }
/*     */ 
/*     */   public Binding getNewBinding()
/*     */   {
/* 253 */     return this.newBinding;
/*     */   }
/*     */ 
/*     */   public Object getChangeInfo()
/*     */   {
/* 264 */     return this.changeInfo;
/*     */   }
/*     */ 
/*     */   public void dispatch(NamingListener paramNamingListener)
/*     */   {
/* 281 */     switch (this.type) {
/*     */     case 0:
/* 283 */       ((NamespaceChangeListener)paramNamingListener).objectAdded(this);
/* 284 */       break;
/*     */     case 1:
/* 287 */       ((NamespaceChangeListener)paramNamingListener).objectRemoved(this);
/* 288 */       break;
/*     */     case 2:
/* 291 */       ((NamespaceChangeListener)paramNamingListener).objectRenamed(this);
/* 292 */       break;
/*     */     case 3:
/* 295 */       ((ObjectChangeListener)paramNamingListener).objectChanged(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.event.NamingEvent
 * JD-Core Version:    0.6.2
 */