/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ public class XMLStringFactoryDefault extends XMLStringFactory
/*    */ {
/* 32 */   private static final XMLStringDefault EMPTY_STR = new XMLStringDefault("");
/*    */ 
/*    */   public XMLString newstr(String string)
/*    */   {
/* 44 */     return new XMLStringDefault(string);
/*    */   }
/*    */ 
/*    */   public XMLString newstr(FastStringBuffer fsb, int start, int length)
/*    */   {
/* 59 */     return new XMLStringDefault(fsb.getString(start, length));
/*    */   }
/*    */ 
/*    */   public XMLString newstr(char[] string, int start, int length)
/*    */   {
/* 74 */     return new XMLStringDefault(new String(string, start, length));
/*    */   }
/*    */ 
/*    */   public XMLString emptystr()
/*    */   {
/* 84 */     return EMPTY_STR;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XMLStringFactoryDefault
 * JD-Core Version:    0.6.2
 */