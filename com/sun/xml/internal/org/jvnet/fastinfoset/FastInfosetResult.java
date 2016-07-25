/*    */ package com.sun.xml.internal.org.jvnet.fastinfoset;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializer;
/*    */ import java.io.OutputStream;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.ext.LexicalHandler;
/*    */ 
/*    */ public class FastInfosetResult extends SAXResult
/*    */ {
/*    */   OutputStream _outputStream;
/*    */ 
/*    */   public FastInfosetResult(OutputStream outputStream)
/*    */   {
/* 60 */     this._outputStream = outputStream;
/*    */   }
/*    */ 
/*    */   public ContentHandler getHandler() {
/* 64 */     ContentHandler handler = super.getHandler();
/* 65 */     if (handler == null) {
/* 66 */       handler = new SAXDocumentSerializer();
/* 67 */       setHandler(handler);
/*    */     }
/* 69 */     ((SAXDocumentSerializer)handler).setOutputStream(this._outputStream);
/* 70 */     return handler;
/*    */   }
/*    */ 
/*    */   public LexicalHandler getLexicalHandler() {
/* 74 */     return (LexicalHandler)getHandler();
/*    */   }
/*    */ 
/*    */   public OutputStream getOutputStream() {
/* 78 */     return this._outputStream;
/*    */   }
/*    */ 
/*    */   public void setOutputStream(OutputStream outputStream) {
/* 82 */     this._outputStream = outputStream;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetResult
 * JD-Core Version:    0.6.2
 */