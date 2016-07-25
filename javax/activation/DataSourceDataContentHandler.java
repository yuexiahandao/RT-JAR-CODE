/*     */ package javax.activation;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class DataSourceDataContentHandler
/*     */   implements DataContentHandler
/*     */ {
/* 744 */   private DataSource ds = null;
/* 745 */   private DataFlavor[] transferFlavors = null;
/* 746 */   private DataContentHandler dch = null;
/*     */ 
/*     */   public DataSourceDataContentHandler(DataContentHandler dch, DataSource ds)
/*     */   {
/* 752 */     this.ds = ds;
/* 753 */     this.dch = dch;
/*     */   }
/*     */ 
/*     */   public DataFlavor[] getTransferDataFlavors()
/*     */   {
/* 762 */     if (this.transferFlavors == null) {
/* 763 */       if (this.dch != null) {
/* 764 */         this.transferFlavors = this.dch.getTransferDataFlavors();
/*     */       } else {
/* 766 */         this.transferFlavors = new DataFlavor[1];
/* 767 */         this.transferFlavors[0] = new ActivationDataFlavor(this.ds.getContentType(), this.ds.getContentType());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 772 */     return this.transferFlavors;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor df, DataSource ds)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 784 */     if (this.dch != null)
/* 785 */       return this.dch.getTransferData(df, ds);
/* 786 */     if (df.equals(getTransferDataFlavors()[0])) {
/* 787 */       return ds.getInputStream();
/*     */     }
/* 789 */     throw new UnsupportedFlavorException(df);
/*     */   }
/*     */ 
/*     */   public Object getContent(DataSource ds) throws IOException
/*     */   {
/* 794 */     if (this.dch != null) {
/* 795 */       return this.dch.getContent(ds);
/*     */     }
/* 797 */     return ds.getInputStream();
/*     */   }
/*     */ 
/*     */   public void writeTo(Object obj, String mimeType, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 805 */     if (this.dch != null)
/* 806 */       this.dch.writeTo(obj, mimeType, os);
/*     */     else
/* 808 */       throw new UnsupportedDataTypeException("no DCH for content type " + this.ds.getContentType());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.DataSourceDataContentHandler
 * JD-Core Version:    0.6.2
 */