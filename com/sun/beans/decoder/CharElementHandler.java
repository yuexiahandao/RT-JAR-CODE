/*    */ package com.sun.beans.decoder;
/*    */ 
/*    */ final class CharElementHandler extends StringElementHandler
/*    */ {
/*    */   public void addAttribute(String paramString1, String paramString2)
/*    */   {
/* 68 */     if (paramString1.equals("code")) {
/* 69 */       int i = Integer.decode(paramString2).intValue();
/* 70 */       for (char c : Character.toChars(i))
/* 71 */         addCharacter(c);
/*    */     }
/*    */     else {
/* 74 */       super.addAttribute(paramString1, paramString2);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object getValue(String paramString)
/*    */   {
/* 87 */     if (paramString.length() != 1) {
/* 88 */       throw new IllegalArgumentException("Wrong characters count");
/*    */     }
/* 90 */     return Character.valueOf(paramString.charAt(0));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.CharElementHandler
 * JD-Core Version:    0.6.2
 */