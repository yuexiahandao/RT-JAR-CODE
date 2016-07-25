/*     */ package java.awt.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.dnd.peer.DropTargetContextPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import sun.awt.datatransfer.TransferableProxy;
/*     */ 
/*     */ public class DropTargetContext
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -634158968993743371L;
/*     */   private DropTarget dropTarget;
/*     */   private transient DropTargetContextPeer dropTargetContextPeer;
/*     */   private transient Transferable transferable;
/*     */ 
/*     */   DropTargetContext(DropTarget paramDropTarget)
/*     */   {
/*  71 */     this.dropTarget = paramDropTarget;
/*     */   }
/*     */ 
/*     */   public DropTarget getDropTarget()
/*     */   {
/*  81 */     return this.dropTarget;
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/*  90 */     return this.dropTarget.getComponent();
/*     */   }
/*     */ 
/*     */   public void addNotify(DropTargetContextPeer paramDropTargetContextPeer)
/*     */   {
/*  99 */     this.dropTargetContextPeer = paramDropTargetContextPeer;
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 107 */     this.dropTargetContextPeer = null;
/* 108 */     this.transferable = null;
/*     */   }
/*     */ 
/*     */   protected void setTargetActions(int paramInt)
/*     */   {
/* 119 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 120 */     if (localDropTargetContextPeer != null)
/* 121 */       synchronized (localDropTargetContextPeer) {
/* 122 */         localDropTargetContextPeer.setTargetActions(paramInt);
/* 123 */         getDropTarget().doSetDefaultActions(paramInt);
/*     */       }
/*     */     else
/* 126 */       getDropTarget().doSetDefaultActions(paramInt);
/*     */   }
/*     */ 
/*     */   protected int getTargetActions()
/*     */   {
/* 138 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 139 */     return localDropTargetContextPeer != null ? localDropTargetContextPeer.getTargetActions() : this.dropTarget.getDefaultActions();
/*     */   }
/*     */ 
/*     */   public void dropComplete(boolean paramBoolean)
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 155 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 156 */     if (localDropTargetContextPeer != null)
/* 157 */       localDropTargetContextPeer.dropComplete(paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void acceptDrag(int paramInt)
/*     */   {
/* 168 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 169 */     if (localDropTargetContextPeer != null)
/* 170 */       localDropTargetContextPeer.acceptDrag(paramInt);
/*     */   }
/*     */ 
/*     */   protected void rejectDrag()
/*     */   {
/* 179 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 180 */     if (localDropTargetContextPeer != null)
/* 181 */       localDropTargetContextPeer.rejectDrag();
/*     */   }
/*     */ 
/*     */   protected void acceptDrop(int paramInt)
/*     */   {
/* 194 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 195 */     if (localDropTargetContextPeer != null)
/* 196 */       localDropTargetContextPeer.acceptDrop(paramInt);
/*     */   }
/*     */ 
/*     */   protected void rejectDrop()
/*     */   {
/* 206 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 207 */     if (localDropTargetContextPeer != null)
/* 208 */       localDropTargetContextPeer.rejectDrop();
/*     */   }
/*     */ 
/*     */   protected DataFlavor[] getCurrentDataFlavors()
/*     */   {
/* 222 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 223 */     return localDropTargetContextPeer != null ? localDropTargetContextPeer.getTransferDataFlavors() : new DataFlavor[0];
/*     */   }
/*     */ 
/*     */   protected List<DataFlavor> getCurrentDataFlavorsAsList()
/*     */   {
/* 236 */     return Arrays.asList(getCurrentDataFlavors());
/*     */   }
/*     */ 
/*     */   protected boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */   {
/* 250 */     return getCurrentDataFlavorsAsList().contains(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   protected Transferable getTransferable()
/*     */     throws InvalidDnDOperationException
/*     */   {
/* 262 */     DropTargetContextPeer localDropTargetContextPeer = getDropTargetContextPeer();
/* 263 */     if (localDropTargetContextPeer == null) {
/* 264 */       throw new InvalidDnDOperationException();
/*     */     }
/* 266 */     if (this.transferable == null) {
/* 267 */       Transferable localTransferable = localDropTargetContextPeer.getTransferable();
/* 268 */       boolean bool = localDropTargetContextPeer.isTransferableJVMLocal();
/* 269 */       synchronized (this) {
/* 270 */         if (this.transferable == null) {
/* 271 */           this.transferable = createTransferableProxy(localTransferable, bool);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 276 */     return this.transferable;
/*     */   }
/*     */ 
/*     */   DropTargetContextPeer getDropTargetContextPeer()
/*     */   {
/* 287 */     return this.dropTargetContextPeer;
/*     */   }
/*     */ 
/*     */   protected Transferable createTransferableProxy(Transferable paramTransferable, boolean paramBoolean)
/*     */   {
/* 300 */     return new TransferableProxy(paramTransferable, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected class TransferableProxy
/*     */     implements Transferable
/*     */   {
/*     */     protected Transferable transferable;
/*     */     protected boolean isLocal;
/*     */     private TransferableProxy proxy;
/*     */ 
/*     */     TransferableProxy(Transferable paramBoolean, boolean arg3)
/*     */     {
/*     */       boolean bool;
/* 331 */       this.proxy = new TransferableProxy(paramBoolean, bool);
/* 332 */       this.transferable = paramBoolean;
/* 333 */       this.isLocal = bool;
/*     */     }
/*     */ 
/*     */     public DataFlavor[] getTransferDataFlavors()
/*     */     {
/* 344 */       return this.proxy.getTransferDataFlavors();
/*     */     }
/*     */ 
/*     */     public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */     {
/* 355 */       return this.proxy.isDataFlavorSupported(paramDataFlavor);
/*     */     }
/*     */ 
/*     */     public Object getTransferData(DataFlavor paramDataFlavor)
/*     */       throws UnsupportedFlavorException, IOException
/*     */     {
/* 376 */       return this.proxy.getTransferData(paramDataFlavor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.DropTargetContext
 * JD-Core Version:    0.6.2
 */