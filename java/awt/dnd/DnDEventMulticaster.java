/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.AWTEventMulticaster;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EventListener;
/*     */ 
/*     */ class DnDEventMulticaster extends AWTEventMulticaster
/*     */   implements DragSourceListener, DragSourceMotionListener
/*     */ {
/*     */   protected DnDEventMulticaster(EventListener paramEventListener1, EventListener paramEventListener2)
/*     */   {
/*  56 */     super(paramEventListener1, paramEventListener2);
/*     */   }
/*     */ 
/*     */   public void dragEnter(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/*  66 */     ((DragSourceMotionListener)this.a).dragEnter(paramDragSourceDragEvent);
/*  67 */     ((DragSourceMotionListener)this.b).dragEnter(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   public void dragOver(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/*  77 */     ((DragSourceMotionListener)this.a).dragOver(paramDragSourceDragEvent);
/*  78 */     ((DragSourceMotionListener)this.b).dragOver(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   public void dropActionChanged(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/*  88 */     ((DragSourceMotionListener)this.a).dropActionChanged(paramDragSourceDragEvent);
/*  89 */     ((DragSourceMotionListener)this.b).dropActionChanged(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   public void dragExit(DragSourceEvent paramDragSourceEvent)
/*     */   {
/*  99 */     ((DragSourceMotionListener)this.a).dragExit(paramDragSourceEvent);
/* 100 */     ((DragSourceMotionListener)this.b).dragExit(paramDragSourceEvent);
/*     */   }
/*     */ 
/*     */   public void dragDropEnd(DragSourceDropEvent paramDragSourceDropEvent)
/*     */   {
/* 110 */     ((DragSourceMotionListener)this.a).dragDropEnd(paramDragSourceDropEvent);
/* 111 */     ((DragSourceMotionListener)this.b).dragDropEnd(paramDragSourceDropEvent);
/*     */   }
/*     */ 
/*     */   public void dragMouseMoved(DragSourceDragEvent paramDragSourceDragEvent)
/*     */   {
/* 121 */     ((DragSourceMotionListener)this.a).dragMouseMoved(paramDragSourceDragEvent);
/* 122 */     ((DragSourceMotionListener)this.b).dragMouseMoved(paramDragSourceDragEvent);
/*     */   }
/*     */ 
/*     */   public static DragSourceListener add(DragSourceListener paramDragSourceListener1, DragSourceListener paramDragSourceListener2)
/*     */   {
/* 134 */     return (DragSourceMotionListener)addInternal(paramDragSourceListener1, paramDragSourceListener2);
/*     */   }
/*     */ 
/*     */   public static DragSourceMotionListener add(DragSourceMotionListener paramDragSourceMotionListener1, DragSourceMotionListener paramDragSourceMotionListener2)
/*     */   {
/* 146 */     return (DragSourceMotionListener)addInternal(paramDragSourceMotionListener1, paramDragSourceMotionListener2);
/*     */   }
/*     */ 
/*     */   public static DragSourceListener remove(DragSourceListener paramDragSourceListener1, DragSourceListener paramDragSourceListener2)
/*     */   {
/* 158 */     return (DragSourceMotionListener)removeInternal(paramDragSourceListener1, paramDragSourceListener2);
/*     */   }
/*     */ 
/*     */   public static DragSourceMotionListener remove(DragSourceMotionListener paramDragSourceMotionListener1, DragSourceMotionListener paramDragSourceMotionListener2)
/*     */   {
/* 171 */     return (DragSourceMotionListener)removeInternal(paramDragSourceMotionListener1, paramDragSourceMotionListener2);
/*     */   }
/*     */ 
/*     */   protected static EventListener addInternal(EventListener paramEventListener1, EventListener paramEventListener2)
/*     */   {
/* 185 */     if (paramEventListener1 == null) return paramEventListener2;
/* 186 */     if (paramEventListener2 == null) return paramEventListener1;
/* 187 */     return new DnDEventMulticaster(paramEventListener1, paramEventListener2);
/*     */   }
/*     */ 
/*     */   protected EventListener remove(EventListener paramEventListener)
/*     */   {
/* 196 */     if (paramEventListener == this.a) return this.b;
/* 197 */     if (paramEventListener == this.b) return this.a;
/* 198 */     EventListener localEventListener1 = removeInternal(this.a, paramEventListener);
/* 199 */     EventListener localEventListener2 = removeInternal(this.b, paramEventListener);
/* 200 */     if ((localEventListener1 == this.a) && (localEventListener2 == this.b)) {
/* 201 */       return this;
/*     */     }
/* 203 */     return addInternal(localEventListener1, localEventListener2);
/*     */   }
/*     */ 
/*     */   protected static EventListener removeInternal(EventListener paramEventListener1, EventListener paramEventListener2)
/*     */   {
/* 218 */     if ((paramEventListener1 == paramEventListener2) || (paramEventListener1 == null))
/* 219 */       return null;
/* 220 */     if ((paramEventListener1 instanceof DnDEventMulticaster)) {
/* 221 */       return ((DnDEventMulticaster)paramEventListener1).remove(paramEventListener2);
/*     */     }
/* 223 */     return paramEventListener1;
/*     */   }
/*     */ 
/*     */   protected static void save(ObjectOutputStream paramObjectOutputStream, String paramString, EventListener paramEventListener)
/*     */     throws IOException
/*     */   {
/* 229 */     AWTEventMulticaster.save(paramObjectOutputStream, paramString, paramEventListener);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DnDEventMulticaster
 * JD-Core Version:    0.6.2
 */