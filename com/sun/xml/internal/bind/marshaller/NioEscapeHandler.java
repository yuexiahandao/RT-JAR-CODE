/*    */ package com.sun.xml.internal.bind.marshaller;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ public class NioEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/*    */   private final CharsetEncoder encoder;
/*    */ 
/*    */   public NioEscapeHandler(String charsetName)
/*    */   {
/* 60 */     this.encoder = Charset.forName(charsetName).newEncoder();
/*    */   }
/*    */ 
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
/* 64 */     int limit = start + length;
/* 65 */     for (int i = start; i < limit; i++)
/* 66 */       switch (ch[i]) {
/*    */       case '&':
/* 68 */         out.write("&amp;");
/* 69 */         break;
/*    */       case '<':
/* 71 */         out.write("&lt;");
/* 72 */         break;
/*    */       case '>':
/* 74 */         out.write("&gt;");
/* 75 */         break;
/*    */       case '"':
/* 77 */         if (isAttVal)
/* 78 */           out.write("&quot;");
/*    */         else {
/* 80 */           out.write(34);
/*    */         }
/* 82 */         break;
/*    */       default:
/* 84 */         if (this.encoder.canEncode(ch[i])) {
/* 85 */           out.write(ch[i]);
/*    */         } else {
/* 87 */           out.write("&#");
/* 88 */           out.write(Integer.toString(ch[i]));
/* 89 */           out.write(59);
/*    */         }
/*    */         break;
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.NioEscapeHandler
 * JD-Core Version:    0.6.2
 */