/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.nio.file.LinkOption;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ class Util
/*    */ {
/*    */   static String[] split(String paramString, char paramChar)
/*    */   {
/* 44 */     int i = 0;
/* 45 */     for (int j = 0; j < paramString.length(); j++) {
/* 46 */       if (paramString.charAt(j) == paramChar)
/* 47 */         i++;
/*    */     }
/* 49 */     String[] arrayOfString = new String[i + 1];
/* 50 */     int k = 0;
/* 51 */     int m = 0;
/* 52 */     for (int n = 0; n < paramString.length(); n++) {
/* 53 */       if (paramString.charAt(n) == paramChar) {
/* 54 */         arrayOfString[(k++)] = paramString.substring(m, n);
/* 55 */         m = n + 1;
/*    */       }
/*    */     }
/* 58 */     arrayOfString[k] = paramString.substring(m, paramString.length());
/* 59 */     return arrayOfString;
/*    */   }
/*    */ 
/*    */   static <E> Set<E> newSet(E[] paramArrayOfE)
/*    */   {
/* 66 */     HashSet localHashSet = new HashSet();
/* 67 */     for (E ? : paramArrayOfE) {
/* 68 */       localHashSet.add(?);
/*    */     }
/* 70 */     return localHashSet;
/*    */   }
/*    */ 
/*    */   static <E> Set<E> newSet(Set<E> paramSet, E[] paramArrayOfE)
/*    */   {
/* 78 */     HashSet localHashSet = new HashSet(paramSet);
/* 79 */     for (E ? : paramArrayOfE) {
/* 80 */       localHashSet.add(?);
/*    */     }
/* 82 */     return localHashSet;
/*    */   }
/*    */ 
/*    */   static boolean followLinks(LinkOption[] paramArrayOfLinkOption)
/*    */   {
/* 89 */     boolean bool = true;
/* 90 */     for (LinkOption localLinkOption : paramArrayOfLinkOption) {
/* 91 */       if (localLinkOption == LinkOption.NOFOLLOW_LINKS) {
/* 92 */         bool = false; } else {
/* 93 */         if (localLinkOption == null) {
/* 94 */           throw new NullPointerException();
/*    */         }
/* 96 */         throw new AssertionError("Should not get here");
/*    */       }
/*    */     }
/* 99 */     return bool;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.Util
 * JD-Core Version:    0.6.2
 */