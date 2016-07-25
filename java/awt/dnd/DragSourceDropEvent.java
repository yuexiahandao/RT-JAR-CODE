/*     */ package java.awt.dnd;
/*     */ 
/*     */ public class DragSourceDropEvent extends DragSourceEvent
/*     */ {
/*     */   private static final long serialVersionUID = -5571321229470821891L;
/*     */   private boolean dropSuccess;
/* 167 */   private int dropAction = 0;
/*     */ 
/*     */   public DragSourceDropEvent(DragSourceContext paramDragSourceContext, int paramInt, boolean paramBoolean)
/*     */   {
/*  72 */     super(paramDragSourceContext);
/*     */ 
/*  74 */     this.dropSuccess = paramBoolean;
/*  75 */     this.dropAction = paramInt;
/*     */   }
/*     */ 
/*     */   public DragSourceDropEvent(DragSourceContext paramDragSourceContext, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3)
/*     */   {
/* 100 */     super(paramDragSourceContext, paramInt2, paramInt3);
/*     */ 
/* 102 */     this.dropSuccess = paramBoolean;
/* 103 */     this.dropAction = paramInt1;
/*     */   }
/*     */ 
/*     */   public DragSourceDropEvent(DragSourceContext paramDragSourceContext)
/*     */   {
/* 121 */     super(paramDragSourceContext);
/*     */ 
/* 123 */     this.dropSuccess = false;
/*     */   }
/*     */ 
/*     */   public boolean getDropSuccess()
/*     */   {
/* 137 */     return this.dropSuccess;
/*     */   }
/*     */ 
/*     */   public int getDropAction()
/*     */   {
/* 149 */     return this.dropAction;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DragSourceDropEvent
 * JD-Core Version:    0.6.2
 */