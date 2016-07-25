/*     */ package sun.awt.datatransfer;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.ClipboardOwner;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.FlavorEvent;
/*     */ import java.awt.datatransfer.FlavorListener;
/*     */ import java.awt.datatransfer.FlavorTable;
/*     */ import java.awt.datatransfer.SystemFlavorMap;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.EventListenerAggregate;
/*     */ import sun.awt.PeerEvent;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public abstract class SunClipboard extends Clipboard
/*     */   implements PropertyChangeListener
/*     */ {
/*  67 */   private AppContext contentsContext = null;
/*     */   private final Object CLIPBOARD_FLAVOR_LISTENER_KEY;
/*  75 */   private volatile int numberOfFlavorListeners = 0;
/*     */   private volatile Set currentDataFlavors;
/*     */ 
/*     */   public SunClipboard(String paramString)
/*     */   {
/*  86 */     super(paramString);
/*  87 */     this.CLIPBOARD_FLAVOR_LISTENER_KEY = new StringBuffer(paramString + "_CLIPBOARD_FLAVOR_LISTENER_KEY");
/*     */   }
/*     */ 
/*     */   public synchronized void setContents(Transferable paramTransferable, ClipboardOwner paramClipboardOwner)
/*     */   {
/*  94 */     if (paramTransferable == null) {
/*  95 */       throw new NullPointerException("contents");
/*     */     }
/*     */ 
/*  98 */     initContext();
/*     */ 
/* 100 */     final ClipboardOwner localClipboardOwner = this.owner;
/* 101 */     final Transferable localTransferable = this.contents;
/*     */     try
/*     */     {
/* 104 */       this.owner = paramClipboardOwner;
/* 105 */       this.contents = new TransferableProxy(paramTransferable, true);
/*     */ 
/* 107 */       setContentsNative(paramTransferable);
/*     */ 
/* 109 */       if ((localClipboardOwner != null) && (localClipboardOwner != paramClipboardOwner))
/* 110 */         EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 112 */             localClipboardOwner.lostOwnership(SunClipboard.this, localTransferable);
/*     */           }
/*     */         });
/*     */     }
/*     */     finally
/*     */     {
/* 109 */       if ((localClipboardOwner != null) && (localClipboardOwner != paramClipboardOwner))
/* 110 */         EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 112 */             localClipboardOwner.lostOwnership(SunClipboard.this, localTransferable);
/*     */           }
/*     */         });
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void initContext()
/*     */   {
/* 120 */     AppContext localAppContext = AppContext.getAppContext();
/*     */ 
/* 122 */     if (this.contentsContext != localAppContext)
/*     */     {
/* 125 */       synchronized (localAppContext) {
/* 126 */         if (localAppContext.isDisposed()) {
/* 127 */           throw new IllegalStateException("Can't set contents from disposed AppContext");
/*     */         }
/* 129 */         localAppContext.addPropertyChangeListener("disposed", this);
/*     */       }
/*     */ 
/* 132 */       if (this.contentsContext != null) {
/* 133 */         this.contentsContext.removePropertyChangeListener("disposed", this);
/*     */       }
/*     */ 
/* 136 */       this.contentsContext = localAppContext;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized Transferable getContents(Object paramObject) {
/* 141 */     if (this.contents != null) {
/* 142 */       return this.contents;
/*     */     }
/* 144 */     return new ClipboardTransferable(this);
/*     */   }
/*     */ 
/*     */   private synchronized Transferable getContextContents()
/*     */   {
/* 154 */     AppContext localAppContext = AppContext.getAppContext();
/* 155 */     return localAppContext == this.contentsContext ? this.contents : null;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getAvailableDataFlavors()
/*     */   {
/* 164 */     Transferable localTransferable = getContextContents();
/* 165 */     if (localTransferable != null) {
/* 166 */       return localTransferable.getTransferDataFlavors();
/*     */     }
/*     */ 
/* 169 */     long[] arrayOfLong = getClipboardFormatsOpenClose();
/*     */ 
/* 171 */     return DataTransferer.getInstance().getFlavorsForFormatsAsArray(arrayOfLong, getDefaultFlavorTable());
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorAvailable(DataFlavor paramDataFlavor)
/*     */   {
/* 180 */     if (paramDataFlavor == null) {
/* 181 */       throw new NullPointerException("flavor");
/*     */     }
/*     */ 
/* 184 */     Transferable localTransferable = getContextContents();
/* 185 */     if (localTransferable != null) {
/* 186 */       return localTransferable.isDataFlavorSupported(paramDataFlavor);
/*     */     }
/*     */ 
/* 189 */     long[] arrayOfLong = getClipboardFormatsOpenClose();
/*     */ 
/* 191 */     return formatArrayAsDataFlavorSet(arrayOfLong).contains(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public Object getData(DataFlavor paramDataFlavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 200 */     if (paramDataFlavor == null) {
/* 201 */       throw new NullPointerException("flavor");
/*     */     }
/*     */ 
/* 204 */     Transferable localTransferable1 = getContextContents();
/* 205 */     if (localTransferable1 != null) {
/* 206 */       return localTransferable1.getTransferData(paramDataFlavor);
/*     */     }
/*     */ 
/* 209 */     long l = 0L;
/* 210 */     byte[] arrayOfByte = null;
/* 211 */     Transferable localTransferable2 = null;
/*     */     try
/*     */     {
/* 214 */       openClipboard(null);
/*     */ 
/* 216 */       long[] arrayOfLong = getClipboardFormats();
/* 217 */       Long localLong = (Long)DataTransferer.getInstance().getFlavorsForFormats(arrayOfLong, getDefaultFlavorTable()).get(paramDataFlavor);
/*     */ 
/* 220 */       if (localLong == null) {
/* 221 */         throw new UnsupportedFlavorException(paramDataFlavor);
/*     */       }
/*     */ 
/* 224 */       l = localLong.longValue();
/* 225 */       arrayOfByte = getClipboardData(l);
/*     */ 
/* 227 */       if (DataTransferer.getInstance().isLocaleDependentTextFormat(l))
/* 228 */         localTransferable2 = createLocaleTransferable(arrayOfLong);
/*     */     }
/*     */     finally
/*     */     {
/* 232 */       closeClipboard();
/*     */     }
/*     */ 
/* 235 */     return DataTransferer.getInstance().translateBytes(arrayOfByte, paramDataFlavor, l, localTransferable2);
/*     */   }
/*     */ 
/*     */   protected Transferable createLocaleTransferable(long[] paramArrayOfLong)
/*     */     throws IOException
/*     */   {
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   public void openClipboard(SunClipboard paramSunClipboard) {
/*     */   }
/*     */ 
/*     */   public void closeClipboard() {
/*     */   }
/*     */ 
/*     */   public abstract long getID();
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 257 */     if (("disposed".equals(paramPropertyChangeEvent.getPropertyName())) && (Boolean.TRUE.equals(paramPropertyChangeEvent.getNewValue())))
/*     */     {
/* 259 */       AppContext localAppContext = (AppContext)paramPropertyChangeEvent.getSource();
/* 260 */       lostOwnershipLater(localAppContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void lostOwnershipImpl() {
/* 265 */     lostOwnershipLater(null);
/*     */   }
/*     */ 
/*     */   protected void lostOwnershipLater(final AppContext paramAppContext)
/*     */   {
/* 279 */     AppContext localAppContext = this.contentsContext;
/* 280 */     if (localAppContext == null) {
/* 281 */       return;
/*     */     }
/*     */ 
/* 284 */     Runnable local2 = new Runnable() {
/*     */       public void run() {
/* 286 */         SunClipboard localSunClipboard = SunClipboard.this;
/* 287 */         ClipboardOwner localClipboardOwner = null;
/* 288 */         Transferable localTransferable = null;
/*     */ 
/* 290 */         synchronized (localSunClipboard) {
/* 291 */           AppContext localAppContext = localSunClipboard.contentsContext;
/*     */ 
/* 293 */           if (localAppContext == null) {
/* 294 */             return;
/*     */           }
/*     */ 
/* 297 */           if ((paramAppContext == null) || (localAppContext == paramAppContext)) {
/* 298 */             localClipboardOwner = localSunClipboard.owner;
/* 299 */             localTransferable = localSunClipboard.contents;
/* 300 */             localSunClipboard.contentsContext = null;
/* 301 */             localSunClipboard.owner = null;
/* 302 */             localSunClipboard.contents = null;
/* 303 */             localSunClipboard.clearNativeContext();
/* 304 */             localAppContext.removePropertyChangeListener("disposed", localSunClipboard);
/*     */           }
/*     */           else {
/* 307 */             return;
/*     */           }
/*     */         }
/* 310 */         if (localClipboardOwner != null)
/* 311 */           localClipboardOwner.lostOwnership(localSunClipboard, localTransferable);
/*     */       }
/*     */     };
/* 316 */     SunToolkit.postEvent(localAppContext, new PeerEvent(this, local2, 1L));
/*     */   }
/*     */ 
/*     */   protected abstract void clearNativeContext();
/*     */ 
/*     */   protected abstract void setContentsNative(Transferable paramTransferable);
/*     */ 
/*     */   protected long[] getClipboardFormatsOpenClose()
/*     */   {
/*     */     try
/*     */     {
/* 329 */       openClipboard(null);
/* 330 */       return getClipboardFormats();
/*     */     } finally {
/* 332 */       closeClipboard();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract long[] getClipboardFormats();
/*     */ 
/*     */   protected abstract byte[] getClipboardData(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   private static Set formatArrayAsDataFlavorSet(long[] paramArrayOfLong)
/*     */   {
/* 347 */     return paramArrayOfLong == null ? null : DataTransferer.getInstance().getFlavorsForFormatsAsSet(paramArrayOfLong, getDefaultFlavorTable());
/*     */   }
/*     */ 
/*     */   public synchronized void addFlavorListener(FlavorListener paramFlavorListener)
/*     */   {
/* 354 */     if (paramFlavorListener == null) {
/* 355 */       return;
/*     */     }
/* 357 */     AppContext localAppContext = AppContext.getAppContext();
/* 358 */     EventListenerAggregate localEventListenerAggregate = (EventListenerAggregate)localAppContext.get(this.CLIPBOARD_FLAVOR_LISTENER_KEY);
/*     */ 
/* 360 */     if (localEventListenerAggregate == null) {
/* 361 */       localEventListenerAggregate = new EventListenerAggregate(FlavorListener.class);
/* 362 */       localAppContext.put(this.CLIPBOARD_FLAVOR_LISTENER_KEY, localEventListenerAggregate);
/*     */     }
/* 364 */     localEventListenerAggregate.add(paramFlavorListener);
/*     */ 
/* 366 */     if (this.numberOfFlavorListeners++ == 0) {
/* 367 */       long[] arrayOfLong = null;
/*     */       try {
/* 369 */         openClipboard(null);
/* 370 */         arrayOfLong = getClipboardFormats();
/*     */       } catch (IllegalStateException localIllegalStateException) {
/*     */       } finally {
/* 373 */         closeClipboard();
/*     */       }
/* 375 */       this.currentDataFlavors = formatArrayAsDataFlavorSet(arrayOfLong);
/*     */ 
/* 377 */       registerClipboardViewerChecked();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void removeFlavorListener(FlavorListener paramFlavorListener) {
/* 382 */     if (paramFlavorListener == null) {
/* 383 */       return;
/*     */     }
/* 385 */     AppContext localAppContext = AppContext.getAppContext();
/* 386 */     EventListenerAggregate localEventListenerAggregate = (EventListenerAggregate)localAppContext.get(this.CLIPBOARD_FLAVOR_LISTENER_KEY);
/*     */ 
/* 388 */     if (localEventListenerAggregate == null)
/*     */     {
/* 390 */       return;
/*     */     }
/* 392 */     if ((localEventListenerAggregate.remove(paramFlavorListener)) && (--this.numberOfFlavorListeners == 0))
/*     */     {
/* 394 */       unregisterClipboardViewerChecked();
/* 395 */       this.currentDataFlavors = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized FlavorListener[] getFlavorListeners() {
/* 400 */     EventListenerAggregate localEventListenerAggregate = (EventListenerAggregate)AppContext.getAppContext().get(this.CLIPBOARD_FLAVOR_LISTENER_KEY);
/*     */ 
/* 402 */     return localEventListenerAggregate == null ? new FlavorListener[0] : (FlavorListener[])localEventListenerAggregate.getListenersCopy();
/*     */   }
/*     */ 
/*     */   public boolean areFlavorListenersRegistered()
/*     */   {
/* 407 */     return this.numberOfFlavorListeners > 0;
/*     */   }
/*     */ 
/*     */   protected abstract void registerClipboardViewerChecked();
/*     */ 
/*     */   protected abstract void unregisterClipboardViewerChecked();
/*     */ 
/*     */   public void checkChange(long[] paramArrayOfLong)
/*     */   {
/* 425 */     Set localSet = this.currentDataFlavors;
/* 426 */     this.currentDataFlavors = formatArrayAsDataFlavorSet(paramArrayOfLong);
/*     */ 
/* 428 */     if ((localSet != null) && (this.currentDataFlavors != null) && (localSet.equals(this.currentDataFlavors)))
/*     */     {
/* 433 */       return;
/*     */     }
/*     */ 
/* 450 */     for (Iterator localIterator = AppContext.getAppContexts().iterator(); localIterator.hasNext(); ) {
/* 451 */       AppContext localAppContext = (AppContext)localIterator.next();
/* 452 */       if ((localAppContext != null) && (!localAppContext.isDisposed()))
/*     */       {
/* 455 */         EventListenerAggregate localEventListenerAggregate = (EventListenerAggregate)localAppContext.get(this.CLIPBOARD_FLAVOR_LISTENER_KEY);
/*     */ 
/* 457 */         if (localEventListenerAggregate != null) {
/* 458 */           FlavorListener[] arrayOfFlavorListener = (FlavorListener[])localEventListenerAggregate.getListenersInternal();
/*     */ 
/* 460 */           for (int i = 0; i < arrayOfFlavorListener.length; i++)
/* 461 */             SunToolkit.postEvent(localAppContext, new PeerEvent(this, new Runnable()
/*     */             {
/*     */               private final FlavorListener flavorListener;
/*     */ 
/*     */               public void run()
/*     */               {
/* 444 */                 if (this.flavorListener != null)
/* 445 */                   this.flavorListener.flavorsChanged(new FlavorEvent(SunClipboard.this));
/*     */               }
/*     */             }
/*     */             , 1L));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static FlavorTable getDefaultFlavorTable()
/*     */   {
/* 470 */     return (FlavorTable)SystemFlavorMap.getDefaultFlavorMap();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.SunClipboard
 * JD-Core Version:    0.6.2
 */