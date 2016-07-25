/*    */ package java.awt.dnd;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ final class SerializationTester
/*    */ {
/*    */   private static ObjectOutputStream stream;
/*    */ 
/*    */   static boolean test(Object paramObject)
/*    */   {
/* 51 */     if (!(paramObject instanceof Serializable)) {
/* 52 */       return false;
/*    */     }
/*    */     try
/*    */     {
/* 56 */       stream.writeObject(paramObject);
/*    */     } catch (IOException localIOException2) {
/* 58 */       return false;
/*    */     }
/*    */     finally
/*    */     {
/*    */       try
/*    */       {
/* 64 */         stream.reset();
/*    */       }
/*    */       catch (IOException localIOException4) {
/*    */       }
/*    */     }
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 43 */       stream = new ObjectOutputStream(new OutputStream()
/*    */       {
/*    */         public void write(int paramAnonymousInt)
/*    */         {
/*    */         }
/*    */       });
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.dnd.SerializationTester
 * JD-Core Version:    0.6.2
 */