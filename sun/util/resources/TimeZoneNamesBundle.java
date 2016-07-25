/*    */ package sun.util.resources;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public abstract class TimeZoneNamesBundle extends OpenListResourceBundle
/*    */ {
/*    */   public Object handleGetObject(String paramString)
/*    */   {
/* 75 */     String[] arrayOfString1 = (String[])super.handleGetObject(paramString);
/* 76 */     if (arrayOfString1 == null) {
/* 77 */       return null;
/*    */     }
/*    */ 
/* 80 */     int i = arrayOfString1.length;
/* 81 */     String[] arrayOfString2 = new String[i + 1];
/* 82 */     arrayOfString2[0] = paramString;
/* 83 */     for (int j = 0; j < i; j++) {
/* 84 */       arrayOfString2[(j + 1)] = arrayOfString1[j];
/*    */     }
/* 86 */     return arrayOfString2;
/*    */   }
/*    */ 
/*    */   protected Map createMap(int paramInt)
/*    */   {
/* 93 */     return new LinkedHashMap(paramInt);
/*    */   }
/*    */ 
/*    */   protected abstract Object[][] getContents();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.resources.TimeZoneNamesBundle
 * JD-Core Version:    0.6.2
 */