/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LDC_W extends LDC
/*    */ {
/*    */   LDC_W()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LDC_W(int index)
/*    */   {
/* 78 */     super(index);
/*    */   }
/*    */ 
/*    */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*    */     throws IOException
/*    */   {
/* 87 */     setIndex(bytes.readUnsignedShort());
/*    */ 
/* 89 */     this.opcode = 19;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LDC_W
 * JD-Core Version:    0.6.2
 */