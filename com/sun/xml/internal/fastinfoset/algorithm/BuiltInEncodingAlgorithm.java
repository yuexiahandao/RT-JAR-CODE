/*    */ package com.sun.xml.internal.fastinfoset.algorithm;
/*    */ 
/*    */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*    */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*    */ import java.nio.CharBuffer;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public abstract class BuiltInEncodingAlgorithm
/*    */   implements EncodingAlgorithm
/*    */ {
/* 37 */   protected static final Pattern SPACE_PATTERN = Pattern.compile("\\s");
/*    */ 
/*    */   public abstract int getPrimtiveLengthFromOctetLength(int paramInt)
/*    */     throws EncodingAlgorithmException;
/*    */ 
/*    */   public abstract int getOctetLengthFromPrimitiveLength(int paramInt);
/*    */ 
/*    */   public abstract void encodeToBytes(Object paramObject, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3);
/*    */ 
/*    */   public void matchWhiteSpaceDelimnatedWords(CharBuffer cb, WordListener wl)
/*    */   {
/* 50 */     Matcher m = SPACE_PATTERN.matcher(cb);
/* 51 */     int i = 0;
/* 52 */     int s = 0;
/* 53 */     while (m.find()) {
/* 54 */       s = m.start();
/* 55 */       if (s != i) {
/* 56 */         wl.word(i, s);
/*    */       }
/* 58 */       i = m.end();
/*    */     }
/* 60 */     if (i != cb.length())
/* 61 */       wl.word(i, cb.length());
/*    */   }
/*    */ 
/*    */   public StringBuffer removeWhitespace(char[] ch, int start, int length) {
/* 65 */     StringBuffer buf = new StringBuffer();
/* 66 */     int firstNonWS = 0;
/* 67 */     for (int idx = 0; 
/* 68 */       idx < length; idx++) {
/* 69 */       if (Character.isWhitespace(ch[(idx + start)])) {
/* 70 */         if (firstNonWS < idx) {
/* 71 */           buf.append(ch, firstNonWS + start, idx - firstNonWS);
/*    */         }
/* 73 */         firstNonWS = idx + 1;
/*    */       }
/*    */     }
/* 76 */     if (firstNonWS < idx) {
/* 77 */       buf.append(ch, firstNonWS + start, idx - firstNonWS);
/*    */     }
/* 79 */     return buf;
/*    */   }
/*    */ 
/*    */   public static abstract interface WordListener
/*    */   {
/*    */     public abstract void word(int paramInt1, int paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm
 * JD-Core Version:    0.6.2
 */