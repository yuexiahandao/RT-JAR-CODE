/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class DataHandlerDataSource
/*     */   implements DataSource
/*     */ {
/* 694 */   DataHandler dataHandler = null;
/*     */ 
/*     */   public DataHandlerDataSource(DataHandler dh)
/*     */   {
/* 700 */     this.dataHandler = dh;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 708 */     return this.dataHandler.getInputStream();
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 716 */     return this.dataHandler.getOutputStream();
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 724 */     return this.dataHandler.getContentType();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 732 */     return this.dataHandler.getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.DataHandlerDataSource
 * JD-Core Version:    0.6.2
 */