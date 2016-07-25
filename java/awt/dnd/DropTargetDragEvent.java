/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DropTargetDragEvent extends DropTargetEvent
/*     */ {
/*     */   private static final long serialVersionUID = -8422265619058953682L;
/*     */   private Point location;
/*     */   private int actions;
/*     */   private int dropAction;
/*     */ 
/*     */   public DropTargetDragEvent(DropTargetContext paramDropTargetContext, Point paramPoint, int paramInt1, int paramInt2)
/*     */   {
/*  99 */     super(paramDropTargetContext);
/*     */ 
/* 101 */     if (paramPoint == null) throw new NullPointerException("cursorLocn");
/*     */ 
/* 103 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 2) && (paramInt1 != 1073741824))
/*     */     {
/* 107 */       throw new IllegalArgumentException("dropAction" + paramInt1);
/*     */     }
/* 109 */     if ((paramInt2 & 0xBFFFFFFC) != 0) throw new IllegalArgumentException("srcActions");
/*     */ 
/* 111 */     this.location = paramPoint;
/* 112 */     this.actions = paramInt2;
/* 113 */     this.dropAction = paramInt1;
/*     */   }
/*     */ 
/*     */   public Point getLocation()
/*     */   {
/* 127 */     return this.location;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getCurrentDataFlavors()
/*     */   {
/* 139 */     return getDropTargetContext().getCurrentDataFlavors();
/*     */   }
/*     */ 
/*     */   public List<DataFlavor> getCurrentDataFlavorsAsList()
/*     */   {
/* 150 */     return getDropTargetContext().getCurrentDataFlavorsAsList();
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */   {
/* 163 */     return getDropTargetContext().isDataFlavorSupported(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public int getSourceActions()
/*     */   {
/* 171 */     return this.actions;
/*     */   }
/*     */ 
/*     */   public int getDropAction()
/*     */   {
/* 178 */     return this.dropAction;
/*     */   }
/*     */ 
/*     */   public Transferable getTransferable()
/*     */   {
/* 191 */     return getDropTargetContext().getTransferable();
/*     */   }
/*     */ 
/*     */   public void acceptDrag(int paramInt)
/*     */   {
/* 207 */     getDropTargetContext().acceptDrag(paramInt);
/*     */   }
/*     */ 
/*     */   public void rejectDrag()
/*     */   {
/* 216 */     getDropTargetContext().rejectDrag();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DropTargetDragEvent
 * JD-Core Version:    0.6.2
 */