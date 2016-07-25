/*     */ package sun.awt.datatransfer;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ClipboardTransferable
/*     */   implements Transferable
/*     */ {
/*  58 */   private final HashMap flavorsToData = new HashMap();
/*  59 */   private DataFlavor[] flavors = new DataFlavor[0];
/*     */ 
/*     */   public ClipboardTransferable(SunClipboard paramSunClipboard)
/*     */   {
/*  78 */     paramSunClipboard.openClipboard(null);
/*     */     try
/*     */     {
/*  81 */       long[] arrayOfLong = paramSunClipboard.getClipboardFormats();
/*     */ 
/*  83 */       if ((arrayOfLong != null) && (arrayOfLong.length > 0))
/*     */       {
/*  87 */         HashMap localHashMap = new HashMap(arrayOfLong.length, 1.0F);
/*     */ 
/*  89 */         Map localMap = DataTransferer.getInstance().getFlavorsForFormats(arrayOfLong, SunClipboard.getDefaultFlavorTable());
/*     */ 
/*  91 */         Iterator localIterator = localMap.keySet().iterator();
/*  92 */         while (localIterator.hasNext())
/*     */         {
/*  94 */           DataFlavor localDataFlavor = (DataFlavor)localIterator.next();
/*  95 */           Long localLong = (Long)localMap.get(localDataFlavor);
/*     */ 
/*  97 */           fetchOneFlavor(paramSunClipboard, localDataFlavor, localLong, localHashMap);
/*     */         }
/*     */ 
/* 100 */         DataTransferer.getInstance(); this.flavors = DataTransferer.setToSortedDataFlavorArray(this.flavorsToData.keySet());
/*     */       }
/*     */     }
/*     */     finally {
/* 104 */       paramSunClipboard.closeClipboard();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean fetchOneFlavor(SunClipboard paramSunClipboard, DataFlavor paramDataFlavor, Long paramLong, HashMap paramHashMap)
/*     */   {
/* 111 */     if (!this.flavorsToData.containsKey(paramDataFlavor)) {
/* 112 */       long l = paramLong.longValue();
/* 113 */       Object localObject = null;
/*     */ 
/* 115 */       if (!paramHashMap.containsKey(paramLong)) {
/*     */         try {
/* 117 */           localObject = paramSunClipboard.getClipboardData(l);
/*     */         } catch (IOException localIOException) {
/* 119 */           localObject = localIOException;
/*     */         } catch (Throwable localThrowable) {
/* 121 */           localThrowable.printStackTrace();
/*     */         }
/*     */ 
/* 126 */         paramHashMap.put(paramLong, localObject);
/*     */       } else {
/* 128 */         localObject = paramHashMap.get(paramLong);
/*     */       }
/*     */ 
/* 134 */       if ((localObject instanceof IOException)) {
/* 135 */         this.flavorsToData.put(paramDataFlavor, localObject);
/* 136 */         return false;
/* 137 */       }if (localObject != null) {
/* 138 */         this.flavorsToData.put(paramDataFlavor, new DataFactory(l, (byte[])localObject));
/*     */ 
/* 140 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors() {
/* 148 */     return (DataFlavor[])this.flavors.clone();
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor) {
/* 152 */     return this.flavorsToData.containsKey(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor paramDataFlavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 158 */     if (!isDataFlavorSupported(paramDataFlavor)) {
/* 159 */       throw new UnsupportedFlavorException(paramDataFlavor);
/*     */     }
/* 161 */     Object localObject = this.flavorsToData.get(paramDataFlavor);
/* 162 */     if ((localObject instanceof IOException))
/*     */     {
/* 164 */       throw ((IOException)localObject);
/* 165 */     }if ((localObject instanceof DataFactory))
/*     */     {
/* 167 */       DataFactory localDataFactory = (DataFactory)localObject;
/* 168 */       localObject = localDataFactory.getTransferData(paramDataFlavor);
/*     */     }
/* 170 */     return localObject;
/*     */   }
/*     */ 
/*     */   private final class DataFactory
/*     */   {
/*     */     final long format;
/*     */     final byte[] data;
/*     */ 
/*     */     DataFactory(long arg2, byte[] arg4)
/*     */     {
/*  65 */       this.format = ???;
/*     */       Object localObject;
/*  66 */       this.data = localObject;
/*     */     }
/*     */ 
/*     */     public Object getTransferData(DataFlavor paramDataFlavor) throws IOException {
/*  70 */       return DataTransferer.getInstance().translateBytes(this.data, paramDataFlavor, this.format, ClipboardTransferable.this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.ClipboardTransferable
 * JD-Core Version:    0.6.2
 */