/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class StopParseException extends SAXException
/*    */ {
/*    */   static final long serialVersionUID = 210102479218258961L;
/*    */ 
/*    */   StopParseException()
/*    */   {
/* 40 */     super("Stylesheet PIs found, stop the parse");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StopParseException
 * JD-Core Version:    0.6.2
 */