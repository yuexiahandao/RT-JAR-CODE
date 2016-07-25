/*     */ package java.net;
/*     */ 
/*     */ public class URISyntaxException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 2137979680897488891L;
/*     */   private String input;
/*     */   private int index;
/*     */ 
/*     */   public URISyntaxException(String paramString1, String paramString2, int paramInt)
/*     */   {
/*  62 */     super(paramString2);
/*  63 */     if ((paramString1 == null) || (paramString2 == null))
/*  64 */       throw new NullPointerException();
/*  65 */     if (paramInt < -1)
/*  66 */       throw new IllegalArgumentException();
/*  67 */     this.input = paramString1;
/*  68 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public URISyntaxException(String paramString1, String paramString2)
/*     */   {
/*  82 */     this(paramString1, paramString2, -1);
/*     */   }
/*     */ 
/*     */   public String getInput()
/*     */   {
/*  91 */     return this.input;
/*     */   }
/*     */ 
/*     */   public String getReason()
/*     */   {
/* 100 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 110 */     return this.index;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 124 */     StringBuffer localStringBuffer = new StringBuffer();
/* 125 */     localStringBuffer.append(getReason());
/* 126 */     if (this.index > -1) {
/* 127 */       localStringBuffer.append(" at index ");
/* 128 */       localStringBuffer.append(this.index);
/*     */     }
/* 130 */     localStringBuffer.append(": ");
/* 131 */     localStringBuffer.append(this.input);
/* 132 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URISyntaxException
 * JD-Core Version:    0.6.2
 */