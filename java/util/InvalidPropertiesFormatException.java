/*    */ package java.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.NotSerializableException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ 
/*    */ public class InvalidPropertiesFormatException extends IOException
/*    */ {
/*    */   public InvalidPropertiesFormatException(Throwable paramThrowable)
/*    */   {
/* 55 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 56 */     initCause(paramThrowable);
/*    */   }
/*    */ 
/*    */   public InvalidPropertiesFormatException(String paramString)
/*    */   {
/* 67 */     super(paramString);
/*    */   }
/*    */ 
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*    */     throws NotSerializableException
/*    */   {
/* 77 */     throw new NotSerializableException("Not serializable.");
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream)
/*    */     throws NotSerializableException
/*    */   {
/* 87 */     throw new NotSerializableException("Not serializable.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.InvalidPropertiesFormatException
 * JD-Core Version:    0.6.2
 */