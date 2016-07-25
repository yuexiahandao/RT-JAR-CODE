/*    */ package com.sun.org.apache.xerces.internal.xinclude;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class XInclude11TextReader extends XIncludeTextReader
/*    */ {
/*    */   public XInclude11TextReader(XMLInputSource source, XIncludeHandler handler, int bufferSize)
/*    */     throws IOException
/*    */   {
/* 50 */     super(source, handler, bufferSize);
/*    */   }
/*    */ 
/*    */   protected boolean isValid(int ch)
/*    */   {
/* 60 */     return XML11Char.isXML11Valid(ch);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.XInclude11TextReader
 * JD-Core Version:    0.6.2
 */