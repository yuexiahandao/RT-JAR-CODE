/*     */ package javax.management.modelmbean;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.security.AccessController;
/*     */ 
/*     */ public class XMLParseException extends Exception
/*     */ {
/*     */   private static final long oldSerialVersionUID = -7780049316655891976L;
/*     */   private static final long newSerialVersionUID = 3176664577895105181L;
/*  69 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("msgStr", String.class) };
/*     */ 
/*  75 */   private static final ObjectStreamField[] newSerialPersistentFields = new ObjectStreamField[0];
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/*  80 */   private static boolean compat = false;
/*     */ 
/*     */   public XMLParseException()
/*     */   {
/* 105 */     super("XML Parse Exception.");
/*     */   }
/*     */ 
/*     */   public XMLParseException(String paramString)
/*     */   {
/* 115 */     super("XML Parse Exception: " + paramString);
/*     */   }
/*     */ 
/*     */   public XMLParseException(Exception paramException, String paramString)
/*     */   {
/* 125 */     super("XML Parse Exception: " + paramString + ":" + paramException.toString());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 134 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 143 */     if (compat)
/*     */     {
/* 147 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 148 */       localPutField.put("msgStr", getMessage());
/* 149 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 155 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  83 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  84 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  85 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  89 */     if (compat) {
/*  90 */       serialPersistentFields = oldSerialPersistentFields;
/*  91 */       serialVersionUID = -7780049316655891976L;
/*     */     } else {
/*  93 */       serialPersistentFields = newSerialPersistentFields;
/*  94 */       serialVersionUID = 3176664577895105181L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.XMLParseException
 * JD-Core Version:    0.6.2
 */