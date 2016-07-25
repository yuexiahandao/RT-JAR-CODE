/*    */ package sun.reflect;
/*    */ 
/*    */ public class SignatureIterator
/*    */ {
/*    */   private String sig;
/*    */   private int idx;
/*    */ 
/*    */   public SignatureIterator(String paramString)
/*    */   {
/* 35 */     this.sig = paramString;
/* 36 */     reset();
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 40 */     this.idx = 1;
/*    */   }
/*    */ 
/*    */   public boolean atEnd() {
/* 44 */     return this.sig.charAt(this.idx) == ')';
/*    */   }
/*    */ 
/*    */   public String next() {
/* 48 */     if (atEnd()) return null;
/* 49 */     int i = this.sig.charAt(this.idx);
/* 50 */     if ((i != 91) && (i != 76)) {
/* 51 */       this.idx += 1;
/* 52 */       return new String(new char[] { i });
/*    */     }
/*    */ 
/* 55 */     int j = this.idx;
/* 56 */     if (i == 91) {
/* 57 */       while ((i = this.sig.charAt(j)) == '[') {
/* 58 */         j++;
/*    */       }
/*    */     }
/*    */ 
/* 62 */     if (i == 76) {
/* 63 */       while (this.sig.charAt(j) != ';') {
/* 64 */         j++;
/*    */       }
/*    */     }
/*    */ 
/* 68 */     int k = this.idx;
/* 69 */     this.idx = (j + 1);
/* 70 */     return this.sig.substring(k, this.idx);
/*    */   }
/*    */ 
/*    */   public String returnType()
/*    */   {
/* 76 */     if (!atEnd()) {
/* 77 */       throw new InternalError("Illegal use of SignatureIterator");
/*    */     }
/* 79 */     return this.sig.substring(this.idx + 1, this.sig.length());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.SignatureIterator
 * JD-Core Version:    0.6.2
 */