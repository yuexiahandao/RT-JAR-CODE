/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ class NPrintWriter extends PrintWriter
/*     */ {
/* 168 */   private int numLines = 5;
/* 169 */   private int numPrinted = 0;
/*     */ 
/*     */   public NPrintWriter(int paramInt) {
/* 172 */     super(System.out);
/* 173 */     this.numLines = paramInt;
/*     */   }
/*     */ 
/*     */   public void println(char[] paramArrayOfChar) {
/* 177 */     if (this.numPrinted >= this.numLines) {
/* 178 */       return;
/*     */     }
/*     */ 
/* 181 */     Object localObject = null;
/*     */ 
/* 183 */     for (int i = 0; i < paramArrayOfChar.length; i++) {
/* 184 */       if (paramArrayOfChar[i] == '\n') {
/* 185 */         this.numPrinted += 1;
/*     */       }
/*     */ 
/* 188 */       if (this.numPrinted == this.numLines) {
/* 189 */         System.arraycopy(paramArrayOfChar, 0, localObject, 0, i);
/*     */       }
/*     */     }
/*     */ 
/* 193 */     if (localObject != null) {
/* 194 */       super.print(localObject);
/*     */     }
/*     */ 
/* 197 */     if (this.numPrinted == this.numLines) {
/* 198 */       return;
/*     */     }
/*     */ 
/* 201 */     super.println(paramArrayOfChar);
/* 202 */     this.numPrinted += 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.NPrintWriter
 * JD-Core Version:    0.6.2
 */