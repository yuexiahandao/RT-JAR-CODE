/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
/*    */ import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class IntData extends Pcdata
/*    */ {
/*    */   private int data;
/*    */   private int length;
/* 60 */   private static final int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, 2147483647 };
/*    */ 
/*    */   public void reset(int i)
/*    */   {
/* 53 */     this.data = i;
/* 54 */     if (i == -2147483648)
/* 55 */       this.length = 11;
/*    */     else
/* 57 */       this.length = (i < 0 ? stringSizeOfInt(-i) + 1 : stringSizeOfInt(i));
/*    */   }
/*    */ 
/*    */   private static int stringSizeOfInt(int x)
/*    */   {
/* 65 */     for (int i = 0; ; i++)
/* 66 */       if (x <= sizeTable[i])
/* 67 */         return i + 1;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 71 */     return String.valueOf(this.data);
/*    */   }
/*    */ 
/*    */   public int length()
/*    */   {
/* 76 */     return this.length;
/*    */   }
/*    */ 
/*    */   public char charAt(int index) {
/* 80 */     return toString().charAt(index);
/*    */   }
/*    */ 
/*    */   public CharSequence subSequence(int start, int end) {
/* 84 */     return toString().substring(start, end);
/*    */   }
/*    */ 
/*    */   public void writeTo(UTF8XmlOutput output) throws IOException {
/* 88 */     output.text(this.data);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntData
 * JD-Core Version:    0.6.2
 */