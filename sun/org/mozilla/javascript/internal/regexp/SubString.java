/*    */ package sun.org.mozilla.javascript.internal.regexp;
/*    */ 
/*    */ public class SubString
/*    */ {
/* 69 */   public static final SubString emptySubString = new SubString();
/*    */   char[] charArray;
/*    */   int index;
/*    */   int length;
/*    */ 
/*    */   public SubString()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SubString(String paramString)
/*    */   {
/* 48 */     this.index = 0;
/* 49 */     this.charArray = paramString.toCharArray();
/* 50 */     this.length = paramString.length();
/*    */   }
/*    */ 
/*    */   public SubString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*    */   {
/* 55 */     this.index = 0;
/* 56 */     this.length = paramInt2;
/* 57 */     this.charArray = new char[paramInt2];
/*    */ 
/* 59 */     System.arraycopy(paramArrayOfChar, paramInt1, this.charArray, 0, paramInt2);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 64 */     return this.charArray == null ? "" : new String(this.charArray, this.index, this.length);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.regexp.SubString
 * JD-Core Version:    0.6.2
 */