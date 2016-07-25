/*    */ package sun.misc;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class ASCIICaseInsensitiveComparator
/*    */   implements Comparator<String>
/*    */ {
/* 37 */   public static final Comparator<String> CASE_INSENSITIVE_ORDER = new ASCIICaseInsensitiveComparator();
/*    */ 
/*    */   public int compare(String paramString1, String paramString2)
/*    */   {
/* 41 */     int i = paramString1.length(); int j = paramString2.length();
/* 42 */     int k = i < j ? i : j;
/* 43 */     for (int m = 0; m < k; m++) {
/* 44 */       int n = paramString1.charAt(m);
/* 45 */       int i1 = paramString2.charAt(m);
/* 46 */       assert ((n <= 127) && (i1 <= 127));
/* 47 */       if (n != i1) {
/* 48 */         n = (char)toLower(n);
/* 49 */         i1 = (char)toLower(i1);
/* 50 */         if (n != i1) {
/* 51 */           return n - i1;
/*    */         }
/*    */       }
/*    */     }
/* 55 */     return i - j;
/*    */   }
/*    */ 
/*    */   public static int lowerCaseHashCode(String paramString)
/*    */   {
/* 73 */     int i = 0;
/* 74 */     int j = paramString.length();
/*    */ 
/* 76 */     for (int k = 0; k < j; k++) {
/* 77 */       i = 31 * i + toLower(paramString.charAt(k));
/*    */     }
/*    */ 
/* 80 */     return i;
/*    */   }
/*    */ 
/*    */   static boolean isLower(int paramInt)
/*    */   {
/* 85 */     return (paramInt - 97 | 122 - paramInt) >= 0;
/*    */   }
/*    */ 
/*    */   static boolean isUpper(int paramInt) {
/* 89 */     return (paramInt - 65 | 90 - paramInt) >= 0;
/*    */   }
/*    */ 
/*    */   static int toLower(int paramInt) {
/* 93 */     return isUpper(paramInt) ? paramInt + 32 : paramInt;
/*    */   }
/*    */ 
/*    */   static int toUpper(int paramInt) {
/* 97 */     return isLower(paramInt) ? paramInt - 32 : paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.ASCIICaseInsensitiveComparator
 * JD-Core Version:    0.6.2
 */