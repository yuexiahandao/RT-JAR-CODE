/*     */ package javax.sql.rowset.spi;
/*     */ 
/*     */ import javax.sql.RowSetReader;
/*     */ import javax.sql.RowSetWriter;
/*     */ 
/*     */ class ProviderImpl extends SyncProvider
/*     */ {
/* 856 */   private String className = null;
/* 857 */   private String vendorName = null;
/* 858 */   private String ver = null;
/*     */   private int index;
/*     */ 
/*     */   public void setClassname(String paramString)
/*     */   {
/* 862 */     this.className = paramString;
/*     */   }
/*     */ 
/*     */   public String getClassname() {
/* 866 */     return this.className;
/*     */   }
/*     */ 
/*     */   public void setVendor(String paramString) {
/* 870 */     this.vendorName = paramString;
/*     */   }
/*     */ 
/*     */   public String getVendor() {
/* 874 */     return this.vendorName;
/*     */   }
/*     */ 
/*     */   public void setVersion(String paramString) {
/* 878 */     this.ver = paramString;
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 882 */     return this.ver;
/*     */   }
/*     */ 
/*     */   public void setIndex(int paramInt) {
/* 886 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 890 */     return this.index;
/*     */   }
/*     */ 
/*     */   public int getDataSourceLock() throws SyncProviderException
/*     */   {
/* 895 */     int i = 0;
/*     */     try {
/* 897 */       i = SyncFactory.getInstance(this.className).getDataSourceLock();
/*     */     }
/*     */     catch (SyncFactoryException localSyncFactoryException) {
/* 900 */       throw new SyncProviderException(localSyncFactoryException.getMessage());
/*     */     }
/*     */ 
/* 903 */     return i;
/*     */   }
/*     */ 
/*     */   public int getProviderGrade()
/*     */   {
/* 908 */     int i = 0;
/*     */     try
/*     */     {
/* 911 */       i = SyncFactory.getInstance(this.className).getProviderGrade();
/*     */     }
/*     */     catch (SyncFactoryException localSyncFactoryException)
/*     */     {
/*     */     }
/* 916 */     return i;
/*     */   }
/*     */ 
/*     */   public String getProviderID() {
/* 920 */     return this.className;
/*     */   }
/*     */ 
/*     */   public RowSetReader getRowSetReader()
/*     */   {
/* 935 */     RowSetReader localRowSetReader = null;
/*     */     try
/*     */     {
/* 938 */       localRowSetReader = SyncFactory.getInstance(this.className).getRowSetReader();
/*     */     }
/*     */     catch (SyncFactoryException localSyncFactoryException)
/*     */     {
/*     */     }
/* 943 */     return localRowSetReader;
/*     */   }
/*     */ 
/*     */   public RowSetWriter getRowSetWriter()
/*     */   {
/* 949 */     RowSetWriter localRowSetWriter = null;
/*     */     try {
/* 951 */       localRowSetWriter = SyncFactory.getInstance(this.className).getRowSetWriter();
/*     */     }
/*     */     catch (SyncFactoryException localSyncFactoryException)
/*     */     {
/*     */     }
/* 956 */     return localRowSetWriter;
/*     */   }
/*     */ 
/*     */   public void setDataSourceLock(int paramInt) throws SyncProviderException
/*     */   {
/*     */     try
/*     */     {
/* 963 */       SyncFactory.getInstance(this.className).setDataSourceLock(paramInt);
/*     */     }
/*     */     catch (SyncFactoryException localSyncFactoryException) {
/* 966 */       throw new SyncProviderException(localSyncFactoryException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int supportsUpdatableView()
/*     */   {
/* 972 */     int i = 0;
/*     */     try
/*     */     {
/* 975 */       i = SyncFactory.getInstance(this.className).supportsUpdatableView();
/*     */     }
/*     */     catch (SyncFactoryException localSyncFactoryException)
/*     */     {
/*     */     }
/* 980 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.spi.ProviderImpl
 * JD-Core Version:    0.6.2
 */