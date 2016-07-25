/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DropTargetDropEvent extends DropTargetEvent
/*     */ {
/*     */   private static final long serialVersionUID = -1721911170440459322L;
/* 271 */   private static final Point zero = new Point(0, 0);
/*     */ 
/* 278 */   private Point location = zero;
/*     */ 
/* 285 */   private int actions = 0;
/*     */ 
/* 292 */   private int dropAction = 0;
/*     */ 
/* 299 */   private boolean isLocalTx = false;
/*     */ 
/*     */   public DropTargetDropEvent(DropTargetContext paramDropTargetContext, Point paramPoint, int paramInt1, int paramInt2)
/*     */   {
/* 105 */     super(paramDropTargetContext);
/*     */ 
/* 107 */     if (paramPoint == null) throw new NullPointerException("cursorLocn");
/*     */ 
/* 109 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 2) && (paramInt1 != 1073741824))
/*     */     {
/* 113 */       throw new IllegalArgumentException("dropAction = " + paramInt1);
/*     */     }
/* 115 */     if ((paramInt2 & 0xBFFFFFFC) != 0) throw new IllegalArgumentException("srcActions");
/*     */ 
/* 117 */     this.location = paramPoint;
/* 118 */     this.actions = paramInt2;
/* 119 */     this.dropAction = paramInt1;
/*     */   }
/*     */ 
/*     */   public DropTargetDropEvent(DropTargetContext paramDropTargetContext, Point paramPoint, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 149 */     this(paramDropTargetContext, paramPoint, paramInt1, paramInt2);
/*     */ 
/* 151 */     this.isLocalTx = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Point getLocation()
/*     */   {
/* 163 */     return this.location;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getCurrentDataFlavors()
/*     */   {
/* 174 */     return getDropTargetContext().getCurrentDataFlavors();
/*     */   }
/*     */ 
/*     */   public List<DataFlavor> getCurrentDataFlavorsAsList()
/*     */   {
/* 185 */     return getDropTargetContext().getCurrentDataFlavorsAsList();
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */   {
/* 199 */     return getDropTargetContext().isDataFlavorSupported(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public int getSourceActions()
/*     */   {
/* 207 */     return this.actions;
/*     */   }
/*     */ 
/*     */   public int getDropAction()
/*     */   {
/* 214 */     return this.dropAction;
/*     */   }
/*     */ 
/*     */   public Transferable getTransferable()
/*     */   {
/* 224 */     return getDropTargetContext().getTransferable();
/*     */   }
/*     */ 
/*     */   public void acceptDrop(int paramInt)
/*     */   {
/* 234 */     getDropTargetContext().acceptDrop(paramInt);
/*     */   }
/*     */ 
/*     */   public void rejectDrop()
/*     */   {
/* 242 */     getDropTargetContext().rejectDrop();
/*     */   }
/*     */ 
/*     */   public void dropComplete(boolean paramBoolean)
/*     */   {
/* 253 */     getDropTargetContext().dropComplete(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean isLocalTransfer()
/*     */   {
/* 264 */     return this.isLocalTx;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DropTargetDropEvent
 * JD-Core Version:    0.6.2
 */