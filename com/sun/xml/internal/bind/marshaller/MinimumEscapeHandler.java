/*    */ package com.sun.xml.internal.bind.marshaller;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ 
/*    */ public class MinimumEscapeHandler
/*    */   implements CharacterEscapeHandler
/*    */ {
/* 42 */   public static final CharacterEscapeHandler theInstance = new MinimumEscapeHandler();
/*    */ 
/*    */   public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out)
/*    */     throws IOException
/*    */   {
/* 48 */     int limit = start + length;
/* 49 */     for (int i = start; i < limit; i++) {
/* 50 */       char c = ch[i];
/* 51 */       if ((c == '&') || (c == '<') || (c == '>') || (c == '\r') || ((c == '"') && (isAttVal))) {
/* 52 */         if (i != start)
/* 53 */           out.write(ch, start, i - start);
/* 54 */         start = i + 1;
/* 55 */         switch (ch[i]) {
/*    */         case '&':
/* 57 */           out.write("&amp;");
/* 58 */           break;
/*    */         case '<':
/* 60 */           out.write("&lt;");
/* 61 */           break;
/*    */         case '>':
/* 63 */           out.write("&gt;");
/* 64 */           break;
/*    */         case '"':
/* 66 */           out.write("&quot;");
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 72 */     if (start != limit)
/* 73 */       out.write(ch, start, limit - start);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler
 * JD-Core Version:    0.6.2
 */