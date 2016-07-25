/*     */ package javax.management.modelmbean;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.security.AccessController;
/*     */ 
/*     */ public class InvalidTargetObjectTypeException extends Exception
/*     */ {
/*     */   private static final long oldSerialVersionUID = 3711724570458346634L;
/*     */   private static final long newSerialVersionUID = 1190536278266811217L;
/*  66 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("msgStr", String.class), new ObjectStreamField("relatedExcept", Exception.class) };
/*     */ 
/*  73 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("exception", Exception.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/*  84 */   private static boolean compat = false;
/*     */   Exception exception;
/*     */ 
/*     */   public InvalidTargetObjectTypeException()
/*     */   {
/* 115 */     super("InvalidTargetObjectTypeException: ");
/* 116 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public InvalidTargetObjectTypeException(String paramString)
/*     */   {
/* 129 */     super("InvalidTargetObjectTypeException: " + paramString);
/* 130 */     this.exception = null;
/*     */   }
/*     */ 
/*     */   public InvalidTargetObjectTypeException(Exception paramException, String paramString)
/*     */   {
/* 147 */     super("InvalidTargetObjectTypeException: " + paramString + (paramException != null ? "\n\t triggered by:" + paramException.toString() : ""));
/*     */ 
/* 150 */     this.exception = paramException;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 158 */     if (compat)
/*     */     {
/* 162 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 163 */       this.exception = ((Exception)localGetField.get("relatedExcept", null));
/* 164 */       if (localGetField.defaulted("relatedExcept"))
/*     */       {
/* 166 */         throw new NullPointerException("relatedExcept");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 173 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 183 */     if (compat)
/*     */     {
/* 187 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 188 */       localPutField.put("relatedExcept", this.exception);
/* 189 */       localPutField.put("msgStr", this.exception != null ? this.exception.getMessage() : "");
/* 190 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 196 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  87 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  88 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  89 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  93 */     if (compat) {
/*  94 */       serialPersistentFields = oldSerialPersistentFields;
/*  95 */       serialVersionUID = 3711724570458346634L;
/*     */     } else {
/*  97 */       serialPersistentFields = newSerialPersistentFields;
/*  98 */       serialVersionUID = 1190536278266811217L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.InvalidTargetObjectTypeException
 * JD-Core Version:    0.6.2
 */