/*     */ package java.sql;
/*     */ 
/*     */ public class DataTruncation extends SQLWarning
/*     */ {
/*     */   private int index;
/*     */   private boolean parameter;
/*     */   private boolean read;
/*     */   private int dataSize;
/*     */   private int transferSize;
/*     */   private static final long serialVersionUID = 6464298989504059473L;
/*     */ 
/*     */   public DataTruncation(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
/*     */   {
/*  63 */     super("Data truncation", paramBoolean2 == true ? "01004" : "22001");
/*  64 */     this.index = paramInt1;
/*  65 */     this.parameter = paramBoolean1;
/*  66 */     this.read = paramBoolean2;
/*  67 */     this.dataSize = paramInt2;
/*  68 */     this.transferSize = paramInt3;
/*     */   }
/*     */ 
/*     */   public DataTruncation(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3, Throwable paramThrowable)
/*     */   {
/*  96 */     super("Data truncation", paramBoolean2 == true ? "01004" : "22001", paramThrowable);
/*  97 */     this.index = paramInt1;
/*  98 */     this.parameter = paramBoolean1;
/*  99 */     this.read = paramBoolean2;
/* 100 */     this.dataSize = paramInt2;
/* 101 */     this.transferSize = paramInt3;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 113 */     return this.index;
/*     */   }
/*     */ 
/*     */   public boolean getParameter()
/*     */   {
/* 124 */     return this.parameter;
/*     */   }
/*     */ 
/*     */   public boolean getRead()
/*     */   {
/* 134 */     return this.read;
/*     */   }
/*     */ 
/*     */   public int getDataSize()
/*     */   {
/* 145 */     return this.dataSize;
/*     */   }
/*     */ 
/*     */   public int getTransferSize()
/*     */   {
/* 155 */     return this.transferSize;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.DataTruncation
 * JD-Core Version:    0.6.2
 */