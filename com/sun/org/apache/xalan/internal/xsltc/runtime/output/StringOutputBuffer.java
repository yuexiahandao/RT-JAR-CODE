/*    */ package com.sun.org.apache.xalan.internal.xsltc.runtime.output;
/*    */ 
/*    */ class StringOutputBuffer
/*    */   implements OutputBuffer
/*    */ {
/*    */   private StringBuffer _buffer;
/*    */ 
/*    */   public StringOutputBuffer()
/*    */   {
/* 34 */     this._buffer = new StringBuffer();
/*    */   }
/*    */ 
/*    */   public String close() {
/* 38 */     return this._buffer.toString();
/*    */   }
/*    */ 
/*    */   public OutputBuffer append(String s) {
/* 42 */     this._buffer.append(s);
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */   public OutputBuffer append(char[] s, int from, int to) {
/* 47 */     this._buffer.append(s, from, to);
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */   public OutputBuffer append(char ch) {
/* 52 */     this._buffer.append(ch);
/* 53 */     return this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.output.StringOutputBuffer
 * JD-Core Version:    0.6.2
 */