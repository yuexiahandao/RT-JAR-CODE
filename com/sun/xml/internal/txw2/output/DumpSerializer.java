/*    */ package com.sun.xml.internal.txw2.output;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class DumpSerializer
/*    */   implements XmlSerializer
/*    */ {
/*    */   private final PrintStream out;
/*    */ 
/*    */   public DumpSerializer(PrintStream out)
/*    */   {
/* 41 */     this.out = out;
/*    */   }
/*    */ 
/*    */   public void beginStartTag(String uri, String localName, String prefix) {
/* 45 */     this.out.println('<' + prefix + ':' + localName);
/*    */   }
/*    */ 
/*    */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/* 49 */     this.out.println('@' + prefix + ':' + localName + '=' + value);
/*    */   }
/*    */ 
/*    */   public void writeXmlns(String prefix, String uri) {
/* 53 */     this.out.println("xmlns:" + prefix + '=' + uri);
/*    */   }
/*    */ 
/*    */   public void endStartTag(String uri, String localName, String prefix) {
/* 57 */     this.out.println('>');
/*    */   }
/*    */ 
/*    */   public void endTag() {
/* 61 */     this.out.println("</  >");
/*    */   }
/*    */ 
/*    */   public void text(StringBuilder text) {
/* 65 */     this.out.println(text);
/*    */   }
/*    */ 
/*    */   public void cdata(StringBuilder text) {
/* 69 */     this.out.println("<![CDATA[");
/* 70 */     this.out.println(text);
/* 71 */     this.out.println("]]>");
/*    */   }
/*    */ 
/*    */   public void comment(StringBuilder comment) {
/* 75 */     this.out.println("<!--");
/* 76 */     this.out.println(comment);
/* 77 */     this.out.println("-->");
/*    */   }
/*    */ 
/*    */   public void startDocument() {
/* 81 */     this.out.println("<?xml?>");
/*    */   }
/*    */ 
/*    */   public void endDocument() {
/* 85 */     this.out.println("done");
/*    */   }
/*    */ 
/*    */   public void flush() {
/* 89 */     this.out.println("flush");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.DumpSerializer
 * JD-Core Version:    0.6.2
 */