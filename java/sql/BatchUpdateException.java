/*     */ package java.sql;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class BatchUpdateException extends SQLException
/*     */ {
/*     */   private final int[] updateCounts;
/*     */   private static final long serialVersionUID = 5977529877145521757L;
/*     */ 
/*     */   public BatchUpdateException(String paramString1, String paramString2, int paramInt, int[] paramArrayOfInt)
/*     */   {
/*  82 */     super(paramString1, paramString2, paramInt);
/*  83 */     this.updateCounts = (paramArrayOfInt == null ? null : Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length));
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(String paramString1, String paramString2, int[] paramArrayOfInt)
/*     */   {
/* 110 */     this(paramString1, paramString2, 0, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(String paramString, int[] paramArrayOfInt)
/*     */   {
/* 136 */     this(paramString, null, 0, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(int[] paramArrayOfInt)
/*     */   {
/* 159 */     this(null, null, 0, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException()
/*     */   {
/* 174 */     this(null, null, 0, null);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(Throwable paramThrowable)
/*     */   {
/* 192 */     this(paramThrowable == null ? null : paramThrowable.toString(), null, 0, null, paramThrowable);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(int[] paramArrayOfInt, Throwable paramThrowable)
/*     */   {
/* 218 */     this(paramThrowable == null ? null : paramThrowable.toString(), null, 0, paramArrayOfInt, paramThrowable);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(String paramString, int[] paramArrayOfInt, Throwable paramThrowable)
/*     */   {
/* 242 */     this(paramString, null, 0, paramArrayOfInt, paramThrowable);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(String paramString1, String paramString2, int[] paramArrayOfInt, Throwable paramThrowable)
/*     */   {
/* 267 */     this(paramString1, paramString2, 0, paramArrayOfInt, paramThrowable);
/*     */   }
/*     */ 
/*     */   public BatchUpdateException(String paramString1, String paramString2, int paramInt, int[] paramArrayOfInt, Throwable paramThrowable)
/*     */   {
/* 294 */     super(paramString1, paramString2, paramInt, paramThrowable);
/* 295 */     this.updateCounts = (paramArrayOfInt == null ? null : Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length));
/*     */   }
/*     */ 
/*     */   public int[] getUpdateCounts()
/*     */   {
/* 329 */     return this.updateCounts == null ? null : Arrays.copyOf(this.updateCounts, this.updateCounts.length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.BatchUpdateException
 * JD-Core Version:    0.6.2
 */