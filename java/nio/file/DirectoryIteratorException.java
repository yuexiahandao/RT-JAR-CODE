/*    */ package java.nio.file;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InvalidObjectException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.util.ConcurrentModificationException;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public final class DirectoryIteratorException extends ConcurrentModificationException
/*    */ {
/*    */   private static final long serialVersionUID = -6012699886086212874L;
/*    */ 
/*    */   public DirectoryIteratorException(IOException paramIOException)
/*    */   {
/* 59 */     super((Throwable)Objects.requireNonNull(paramIOException));
/*    */   }
/*    */ 
/*    */   public IOException getCause()
/*    */   {
/* 69 */     return (IOException)super.getCause();
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream)
/*    */     throws IOException, ClassNotFoundException
/*    */   {
/* 82 */     paramObjectInputStream.defaultReadObject();
/* 83 */     Throwable localThrowable = super.getCause();
/* 84 */     if (!(localThrowable instanceof IOException))
/* 85 */       throw new InvalidObjectException("Cause must be an IOException");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.DirectoryIteratorException
 * JD-Core Version:    0.6.2
 */