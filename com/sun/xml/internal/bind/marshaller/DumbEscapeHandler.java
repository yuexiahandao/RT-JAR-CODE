/*    */ package com.sun.xml.internal.bind.marshaller;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ 
/*    */ public class DumbEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/* 45 */   public static final CharacterEscapeHandler theInstance = new DumbEscapeHandler();
/*    */ 
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
/* 48 */     int limit = start + length;
/* 49 */     for (int i = start; i < limit; i++)
/* 50 */       switch (ch[i]) {
/*    */       case '&':
/* 52 */         out.write("&amp;");
/* 53 */         break;
/*    */       case '<':
/* 55 */         out.write("&lt;");
/* 56 */         break;
/*    */       case '>':
/* 58 */         out.write("&gt;");
/* 59 */         break;
/*    */       case '"':
/* 61 */         if (isAttVal)
/* 62 */           out.write("&quot;");
/*    */         else {
/* 64 */           out.write(34);
/*    */         }
/* 66 */         break;
/*    */       default:
/* 68 */         if (ch[i] > '') {
/* 69 */           out.write("&#");
/* 70 */           out.write(Integer.toString(ch[i]));
/* 71 */           out.write(59);
/*    */         } else {
/* 73 */           out.write(ch[i]);
/*    */         }
/*    */         break;
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.DumbEscapeHandler
 * JD-Core Version:    0.6.2
 */