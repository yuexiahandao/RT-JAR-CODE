/*     */ package javax.activation;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ 
/*     */ class ObjectDataContentHandler
/*     */   implements DataContentHandler
/*     */ {
/* 821 */   private DataFlavor[] transferFlavors = null;
/*     */   private Object obj;
/*     */   private String mimeType;
/* 824 */   private DataContentHandler dch = null;
/*     */ 
/*     */   public ObjectDataContentHandler(DataContentHandler dch, Object obj, String mimeType)
/*     */   {
/* 831 */     this.obj = obj;
/* 832 */     this.mimeType = mimeType;
/* 833 */     this.dch = dch;
/*     */   }
/*     */ 
/*     */   public DataContentHandler getDCH()
/*     */   {
/* 841 */     return this.dch;
/*     */   }
/*     */ 
/*     */   public synchronized DataFlavor[] getTransferDataFlavors()
/*     */   {
/* 849 */     if (this.transferFlavors == null) {
/* 850 */       if (this.dch != null) {
/* 851 */         this.transferFlavors = this.dch.getTransferDataFlavors();
/*     */       } else {
/* 853 */         this.transferFlavors = new DataFlavor[1];
/* 854 */         this.transferFlavors[0] = new ActivationDataFlavor(this.obj.getClass(), this.mimeType, this.mimeType);
/*     */       }
/*     */     }
/*     */ 
/* 858 */     return this.transferFlavors;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor df, DataSource ds)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 870 */     if (this.dch != null)
/* 871 */       return this.dch.getTransferData(df, ds);
/* 872 */     if (df.equals(getTransferDataFlavors()[0])) {
/* 873 */       return this.obj;
/*     */     }
/* 875 */     throw new UnsupportedFlavorException(df);
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource ds)
/*     */   {
/* 880 */     return this.obj;
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String mimeType, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 888 */     if (this.dch != null) {
/* 889 */       this.dch.writeTo(obj, mimeType, os);
/* 890 */     } else if ((obj instanceof byte[])) {
/* 891 */       os.write((byte[])obj);
/* 892 */     } else if ((obj instanceof String)) {
/* 893 */       OutputStreamWriter osw = new OutputStreamWriter(os);
/* 894 */       osw.write((String)obj);
/* 895 */       osw.flush(); } else {
/* 896 */       throw new UnsupportedDataTypeException("no object DCH for MIME type " + this.mimeType);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.ObjectDataContentHandler
 * JD-Core Version:    0.6.2
 */