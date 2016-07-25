/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ 
/*     */ public class MenuDragMouseEvent extends MouseEvent
/*     */ {
/*     */   private MenuElement[] path;
/*     */   private MenuSelectionManager manager;
/*     */ 
/*     */   public MenuDragMouseEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/*  82 */     super(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean);
/*  83 */     this.path = paramArrayOfMenuElement;
/*  84 */     this.manager = paramMenuSelectionManager;
/*     */   }
/*     */ 
/*     */   public MenuDragMouseEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, MenuElement[] paramArrayOfMenuElement, MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/* 121 */     super(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramBoolean, 0);
/*     */ 
/* 123 */     this.path = paramArrayOfMenuElement;
/* 124 */     this.manager = paramMenuSelectionManager;
/*     */   }
/*     */ 
/*     */   public MenuElement[] getPath()
/*     */   {
/* 133 */     return this.path;
/*     */   }
/*     */ 
/*     */   public MenuSelectionManager getMenuSelectionManager()
/*     */   {
/* 142 */     return this.manager;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.MenuDragMouseEvent
 * JD-Core Version:    0.6.2
 */