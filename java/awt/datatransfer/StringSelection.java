/*     */ package java.awt.datatransfer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ 
/*     */ public class StringSelection
/*     */   implements Transferable, ClipboardOwner
/*     */ {
/*     */   private static final int STRING = 0;
/*     */   private static final int PLAIN_TEXT = 1;
/*  49 */   private static final DataFlavor[] flavors = { DataFlavor.stringFlavor, DataFlavor.plainTextFlavor };
/*     */   private String data;
/*     */ 
/*     */   public StringSelection(String paramString)
/*     */   {
/*  61 */     this.data = paramString;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/*  77 */     return (DataFlavor[])flavors.clone();
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor)
/*     */   {
/*  93 */     for (int i = 0; i < flavors.length; i++) {
/*  94 */       if (paramDataFlavor.equals(flavors[i])) {
/*  95 */         return true;
/*     */       }
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor paramDataFlavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 129 */     if (paramDataFlavor.equals(flavors[0]))
/* 130 */       return this.data;
/* 131 */     if (paramDataFlavor.equals(flavors[1])) {
/* 132 */       return new StringReader(this.data == null ? "" : this.data);
/*     */     }
/* 134 */     throw new UnsupportedFlavorException(paramDataFlavor);
/*     */   }
/*     */ 
/*     */   public void lostOwnership(Clipboard paramClipboard, Transferable paramTransferable)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.StringSelection
 * JD-Core Version:    0.6.2
 */