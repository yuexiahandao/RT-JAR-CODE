/*    */ package org.omg.IOP.CodecPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class InvalidTypeForEncoding extends UserException
/*    */ {
/*    */   public InvalidTypeForEncoding()
/*    */   {
/* 16 */     super(InvalidTypeForEncodingHelper.id());
/*    */   }
/*    */ 
/*    */   public InvalidTypeForEncoding(String paramString)
/*    */   {
/* 22 */     super(InvalidTypeForEncodingHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.CodecPackage.InvalidTypeForEncoding
 * JD-Core Version:    0.6.2
 */