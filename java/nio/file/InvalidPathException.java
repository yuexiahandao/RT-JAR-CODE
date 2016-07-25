/*     */ package java.nio.file;
/*     */ 
/*     */ public class InvalidPathException extends IllegalArgumentException
/*     */ {
/*     */   static final long serialVersionUID = 4355821422286746137L;
/*     */   private String input;
/*     */   private int index;
/*     */ 
/*     */   public InvalidPathException(String paramString1, String paramString2, int paramInt)
/*     */   {
/*  58 */     super(paramString2);
/*  59 */     if ((paramString1 == null) || (paramString2 == null))
/*  60 */       throw new NullPointerException();
/*  61 */     if (paramInt < -1)
/*  62 */       throw new IllegalArgumentException();
/*  63 */     this.input = paramString1;
/*  64 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public InvalidPathException(String paramString1, String paramString2)
/*     */   {
/*  78 */     this(paramString1, paramString2, -1);
/*     */   }
/*     */ 
/*     */   public String getInput()
/*     */   {
/*  87 */     return this.input;
/*     */   }
/*     */ 
/*     */   public String getReason()
/*     */   {
/*  96 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 106 */     return this.index;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 120 */     StringBuffer localStringBuffer = new StringBuffer();
/* 121 */     localStringBuffer.append(getReason());
/* 122 */     if (this.index > -1) {
/* 123 */       localStringBuffer.append(" at index ");
/* 124 */       localStringBuffer.append(this.index);
/*     */     }
/* 126 */     localStringBuffer.append(": ");
/* 127 */     localStringBuffer.append(this.input);
/* 128 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.InvalidPathException
 * JD-Core Version:    0.6.2
 */