/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Container;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ public class AncestorEvent extends AWTEvent
/*     */ {
/*     */   public static final int ANCESTOR_ADDED = 1;
/*     */   public static final int ANCESTOR_REMOVED = 2;
/*     */   public static final int ANCESTOR_MOVED = 3;
/*     */   Container ancestor;
/*     */   Container ancestorParent;
/*     */ 
/*     */   public AncestorEvent(JComponent paramJComponent, int paramInt, Container paramContainer1, Container paramContainer2)
/*     */   {
/*  76 */     super(paramJComponent, paramInt);
/*  77 */     this.ancestor = paramContainer1;
/*  78 */     this.ancestorParent = paramContainer2;
/*     */   }
/*     */ 
/*     */   public Container getAncestor()
/*     */   {
/*  85 */     return this.ancestor;
/*     */   }
/*     */ 
/*     */   public Container getAncestorParent()
/*     */   {
/*  94 */     return this.ancestorParent;
/*     */   }
/*     */ 
/*     */   public JComponent getComponent()
/*     */   {
/* 101 */     return (JComponent)getSource();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.AncestorEvent
 * JD-Core Version:    0.6.2
 */