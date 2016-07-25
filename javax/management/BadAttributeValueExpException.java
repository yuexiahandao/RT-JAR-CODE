/*    */ package javax.management;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectInputStream.GetField;
/*    */ 
/*    */ public class BadAttributeValueExpException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -3105272988410493376L;
/*    */   private Object val;
/*    */ 
/*    */   public BadAttributeValueExpException(Object paramObject)
/*    */   {
/* 57 */     this.val = (paramObject == null ? null : paramObject.toString());
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 65 */     return "BadAttributeValueException: " + this.val;
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 69 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 70 */     Object localObject = localGetField.get("val", null);
/*    */ 
/* 72 */     if (localObject == null)
/* 73 */       this.val = null;
/* 74 */     else if ((localObject instanceof String))
/* 75 */       this.val = localObject;
/* 76 */     else if ((System.getSecurityManager() == null) || ((localObject instanceof Long)) || ((localObject instanceof Integer)) || ((localObject instanceof Float)) || ((localObject instanceof Double)) || ((localObject instanceof Byte)) || ((localObject instanceof Short)) || ((localObject instanceof Boolean)))
/*    */     {
/* 84 */       this.val = localObject.toString();
/*    */     }
/* 86 */     else this.val = (System.identityHashCode(localObject) + "@" + localObject.getClass().getName());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.BadAttributeValueExpException
 * JD-Core Version:    0.6.2
 */