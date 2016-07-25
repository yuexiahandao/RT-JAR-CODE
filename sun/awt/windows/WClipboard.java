/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import sun.awt.datatransfer.DataTransferer;
/*     */ import sun.awt.datatransfer.SunClipboard;
/*     */ 
/*     */ public class WClipboard extends SunClipboard
/*     */ {
/*     */   private boolean isClipboardViewerRegistered;
/*     */ 
/*     */   public WClipboard()
/*     */   {
/*  58 */     super("System");
/*     */   }
/*     */ 
/*     */   public long getID() {
/*  62 */     return 0L;
/*     */   }
/*     */ 
/*     */   protected void setContentsNative(Transferable paramTransferable)
/*     */   {
/*  73 */     SortedMap localSortedMap = WDataTransferer.getInstance().getFormatsForTransferable(paramTransferable, getDefaultFlavorTable());
/*     */ 
/*  76 */     openClipboard(this);
/*     */     try
/*     */     {
/*  79 */       Iterator localIterator = localSortedMap.keySet().iterator();
/*  80 */       while (localIterator.hasNext()) {
/*  81 */         Long localLong = (Long)localIterator.next();
/*  82 */         long l = localLong.longValue();
/*  83 */         DataFlavor localDataFlavor = (DataFlavor)localSortedMap.get(localLong);
/*     */         try
/*     */         {
/*  86 */           byte[] arrayOfByte = WDataTransferer.getInstance().translateTransferable(paramTransferable, localDataFlavor, l);
/*     */ 
/*  88 */           publishClipboardData(l, arrayOfByte);
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*  93 */           if ((localDataFlavor.isMimeTypeEqual("application/x-java-jvm-local-objectref")) && ((localIOException instanceof NotSerializableException)));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 100 */       closeClipboard();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void lostSelectionOwnershipImpl() {
/* 105 */     lostOwnershipImpl();
/*     */   }
/*     */ 
/*     */   protected void clearNativeContext()
/*     */   {
/*     */   }
/*     */ 
/*     */   public native void openClipboard(SunClipboard paramSunClipboard)
/*     */     throws IllegalStateException;
/*     */ 
/*     */   public native void closeClipboard();
/*     */ 
/*     */   private native void publishClipboardData(long paramLong, byte[] paramArrayOfByte);
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   protected native long[] getClipboardFormats();
/*     */ 
/*     */   protected native byte[] getClipboardData(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   protected void registerClipboardViewerChecked()
/*     */   {
/* 140 */     if (!this.isClipboardViewerRegistered) {
/* 141 */       registerClipboardViewer();
/* 142 */       this.isClipboardViewerRegistered = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void registerClipboardViewer();
/*     */ 
/*     */   protected void unregisterClipboardViewerChecked()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void handleContentsChanged()
/*     */   {
/* 159 */     if (!areFlavorListenersRegistered()) {
/* 160 */       return;
/*     */     }
/*     */ 
/* 163 */     long[] arrayOfLong = null;
/*     */     try {
/* 165 */       openClipboard(null);
/* 166 */       arrayOfLong = getClipboardFormats();
/*     */     } catch (IllegalStateException localIllegalStateException) {
/*     */     }
/*     */     finally {
/* 170 */       closeClipboard();
/*     */     }
/* 172 */     checkChange(arrayOfLong);
/*     */   }
/*     */ 
/*     */   protected Transferable createLocaleTransferable(long[] paramArrayOfLong)
/*     */     throws IOException
/*     */   {
/* 181 */     int i = 0;
/* 182 */     for (int j = 0; j < paramArrayOfLong.length; j++) {
/* 183 */       if (paramArrayOfLong[j] == 16L) {
/* 184 */         i = 1;
/* 185 */         break;
/*     */       }
/*     */     }
/* 188 */     if (i == 0) {
/* 189 */       return null;
/*     */     }
/*     */ 
/* 192 */     byte[] arrayOfByte1 = null;
/*     */     try {
/* 194 */       arrayOfByte1 = getClipboardData(16L);
/*     */     } catch (IOException localIOException) {
/* 196 */       return null;
/*     */     }
/*     */ 
/* 199 */     final byte[] arrayOfByte2 = arrayOfByte1;
/*     */ 
/* 201 */     return new Transferable() {
/*     */       public DataFlavor[] getTransferDataFlavors() {
/* 203 */         return new DataFlavor[] { DataTransferer.javaTextEncodingFlavor };
/*     */       }
/*     */       public boolean isDataFlavorSupported(DataFlavor paramAnonymousDataFlavor) {
/* 206 */         return paramAnonymousDataFlavor.equals(DataTransferer.javaTextEncodingFlavor);
/*     */       }
/*     */       public Object getTransferData(DataFlavor paramAnonymousDataFlavor) throws UnsupportedFlavorException {
/* 209 */         if (isDataFlavorSupported(paramAnonymousDataFlavor)) {
/* 210 */           return arrayOfByte2;
/*     */         }
/* 212 */         throw new UnsupportedFlavorException(paramAnonymousDataFlavor);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 133 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WClipboard
 * JD-Core Version:    0.6.2
 */