/*    */ package javax.xml.crypto.dsig.spec;
/*    */ 
/*    */ public final class HMACParameterSpec
/*    */   implements SignatureMethodParameterSpec
/*    */ {
/*    */   private int outputLength;
/*    */ 
/*    */   public HMACParameterSpec(int paramInt)
/*    */   {
/* 64 */     this.outputLength = paramInt;
/*    */   }
/*    */ 
/*    */   public int getOutputLength()
/*    */   {
/* 73 */     return this.outputLength;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.spec.HMACParameterSpec
 * JD-Core Version:    0.6.2
 */