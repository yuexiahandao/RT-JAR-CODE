/*     */ package java.awt.datatransfer;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.awt.EventListenerAggregate;
/*     */ 
/*     */ public class Clipboard
/*     */ {
/*     */   String name;
/*     */   protected ClipboardOwner owner;
/*     */   protected Transferable contents;
/*     */   private EventListenerAggregate flavorListeners;
/*     */   private Set currentDataFlavors;
/*     */ 
/*     */   public Clipboard(String paramString)
/*     */   {
/*  83 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  92 */     return this.name;
/*     */   }
/*     */ 
/*     */   public synchronized void setContents(Transferable paramTransferable, ClipboardOwner paramClipboardOwner)
/*     */   {
/* 121 */     final ClipboardOwner localClipboardOwner = this.owner;
/* 122 */     final Transferable localTransferable = this.contents;
/*     */ 
/* 124 */     this.owner = paramClipboardOwner;
/* 125 */     this.contents = paramTransferable;
/*     */ 
/* 127 */     if ((localClipboardOwner != null) && (localClipboardOwner != paramClipboardOwner)) {
/* 128 */       EventQueue.invokeLater(new Runnable() {
/*     */         public void run() {
/* 130 */           localClipboardOwner.lostOwnership(Clipboard.this, localTransferable);
/*     */         }
/*     */       });
/*     */     }
/* 134 */     fireFlavorsChanged();
/*     */   }
/*     */ 
/*     */   public synchronized Transferable getContents(Object paramObject)
/*     */   {
/* 152 */     return this.contents;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getAvailableDataFlavors()
/*     */   {
/* 170 */     Transferable localTransferable = getContents(null);
/* 171 */     if (localTransferable == null) {
/* 172 */       return new DataFlavor[0];
/*     */     }
/* 174 */     return localTransferable.getTransferDataFlavors();
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorAvailable(DataFlavor paramDataFlavor)
/*     */   {
/* 193 */     if (paramDataFlavor == null) {
/* 194 */       throw new NullPointerException("flavor");
/*     */     }
/*     */ 
/* 197 */     Transferable localTransferable = getContents(null);
/* 198 */     if (localTransferable == null) {
/* 199 */       return false;
/*     */     }
/* 201 */     return localTransferable.isDataFlavorSupported(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public Object getData(DataFlavor paramDataFlavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 228 */     if (paramDataFlavor == null) {
/* 229 */       throw new NullPointerException("flavor");
/*     */     }
/*     */ 
/* 232 */     Transferable localTransferable = getContents(null);
/* 233 */     if (localTransferable == null) {
/* 234 */       throw new UnsupportedFlavorException(paramDataFlavor);
/*     */     }
/* 236 */     return localTransferable.getTransferData(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public synchronized void addFlavorListener(FlavorListener paramFlavorListener)
/*     */   {
/* 255 */     if (paramFlavorListener == null) {
/* 256 */       return;
/*     */     }
/* 258 */     if (this.flavorListeners == null) {
/* 259 */       this.currentDataFlavors = getAvailableDataFlavorSet();
/* 260 */       this.flavorListeners = new EventListenerAggregate(FlavorListener.class);
/*     */     }
/* 262 */     this.flavorListeners.add(paramFlavorListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeFlavorListener(FlavorListener paramFlavorListener)
/*     */   {
/* 283 */     if ((paramFlavorListener == null) || (this.flavorListeners == null)) {
/* 284 */       return;
/*     */     }
/* 286 */     this.flavorListeners.remove(paramFlavorListener);
/*     */   }
/*     */ 
/*     */   public synchronized FlavorListener[] getFlavorListeners()
/*     */   {
/* 302 */     return this.flavorListeners == null ? new FlavorListener[0] : (FlavorListener[])this.flavorListeners.getListenersCopy();
/*     */   }
/*     */ 
/*     */   private void fireFlavorsChanged()
/*     */   {
/* 314 */     if (this.flavorListeners == null) {
/* 315 */       return;
/*     */     }
/* 317 */     Set localSet = this.currentDataFlavors;
/* 318 */     this.currentDataFlavors = getAvailableDataFlavorSet();
/* 319 */     if (localSet.equals(this.currentDataFlavors)) {
/* 320 */       return;
/*     */     }
/* 322 */     FlavorListener[] arrayOfFlavorListener = (FlavorListener[])this.flavorListeners.getListenersInternal();
/*     */ 
/* 324 */     for (int i = 0; i < arrayOfFlavorListener.length; i++) {
/* 325 */       final FlavorListener localFlavorListener = arrayOfFlavorListener[i];
/* 326 */       EventQueue.invokeLater(new Runnable() {
/*     */         public void run() {
/* 328 */           localFlavorListener.flavorsChanged(new FlavorEvent(Clipboard.this));
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   private Set getAvailableDataFlavorSet()
/*     */   {
/* 344 */     HashSet localHashSet = new HashSet();
/* 345 */     Transferable localTransferable = getContents(null);
/* 346 */     if (localTransferable != null) {
/* 347 */       DataFlavor[] arrayOfDataFlavor = localTransferable.getTransferDataFlavors();
/* 348 */       if (arrayOfDataFlavor != null) {
/* 349 */         localHashSet.addAll(Arrays.asList(arrayOfDataFlavor));
/*     */       }
/*     */     }
/* 352 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.Clipboard
 * JD-Core Version:    0.6.2
 */