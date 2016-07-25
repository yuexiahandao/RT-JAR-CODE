/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.security.AccessController;
/*     */ import java.util.Date;
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class Notification extends EventObject
/*     */ {
/*     */   private static final long oldSerialVersionUID = 1716977971058914352L;
/*     */   private static final long newSerialVersionUID = -7516092053498031989L;
/*  72 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("message", String.class), new ObjectStreamField("sequenceNumber", Long.TYPE), new ObjectStreamField("source", Object.class), new ObjectStreamField("sourceObjectName", ObjectName.class), new ObjectStreamField("timeStamp", Long.TYPE), new ObjectStreamField("type", String.class), new ObjectStreamField("userData", Object.class) };
/*     */ 
/*  84 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("message", String.class), new ObjectStreamField("sequenceNumber", Long.TYPE), new ObjectStreamField("source", Object.class), new ObjectStreamField("timeStamp", Long.TYPE), new ObjectStreamField("type", String.class), new ObjectStreamField("userData", Object.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 112 */   private static boolean compat = false;
/*     */   private String type;
/*     */   private long sequenceNumber;
/*     */   private long timeStamp;
/* 157 */   private Object userData = null;
/*     */ 
/* 162 */   private String message = "";
/*     */ 
/* 171 */   protected Object source = null;
/*     */ 
/*     */   public Notification(String paramString, Object paramObject, long paramLong)
/*     */   {
/* 184 */     super(paramObject);
/* 185 */     this.source = paramObject;
/* 186 */     this.type = paramString;
/* 187 */     this.sequenceNumber = paramLong;
/* 188 */     this.timeStamp = new Date().getTime();
/*     */   }
/*     */ 
/*     */   public Notification(String paramString1, Object paramObject, long paramLong, String paramString2)
/*     */   {
/* 202 */     super(paramObject);
/* 203 */     this.source = paramObject;
/* 204 */     this.type = paramString1;
/* 205 */     this.sequenceNumber = paramLong;
/* 206 */     this.timeStamp = new Date().getTime();
/* 207 */     this.message = paramString2;
/*     */   }
/*     */ 
/*     */   public Notification(String paramString, Object paramObject, long paramLong1, long paramLong2)
/*     */   {
/* 220 */     super(paramObject);
/* 221 */     this.source = paramObject;
/* 222 */     this.type = paramString;
/* 223 */     this.sequenceNumber = paramLong1;
/* 224 */     this.timeStamp = paramLong2;
/*     */   }
/*     */ 
/*     */   public Notification(String paramString1, Object paramObject, long paramLong1, long paramLong2, String paramString2)
/*     */   {
/* 238 */     super(paramObject);
/* 239 */     this.source = paramObject;
/* 240 */     this.type = paramString1;
/* 241 */     this.sequenceNumber = paramLong1;
/* 242 */     this.timeStamp = paramLong2;
/* 243 */     this.message = paramString2;
/*     */   }
/*     */ 
/*     */   public void setSource(Object paramObject)
/*     */   {
/* 254 */     this.source = paramObject;
/* 255 */     this.source = paramObject;
/*     */   }
/*     */ 
/*     */   public long getSequenceNumber()
/*     */   {
/* 269 */     return this.sequenceNumber;
/*     */   }
/*     */ 
/*     */   public void setSequenceNumber(long paramLong)
/*     */   {
/* 282 */     this.sequenceNumber = paramLong;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 294 */     return this.type;
/*     */   }
/*     */ 
/*     */   public long getTimeStamp()
/*     */   {
/* 305 */     return this.timeStamp;
/*     */   }
/*     */ 
/*     */   public void setTimeStamp(long paramLong)
/*     */   {
/* 316 */     this.timeStamp = paramLong;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 326 */     return this.message;
/*     */   }
/*     */ 
/*     */   public Object getUserData()
/*     */   {
/* 338 */     return this.userData;
/*     */   }
/*     */ 
/*     */   public void setUserData(Object paramObject)
/*     */   {
/* 351 */     this.userData = paramObject;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 361 */     return super.toString() + "[type=" + this.type + "][message=" + this.message + "]";
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 370 */     paramObjectInputStream.defaultReadObject();
/* 371 */     this.source = this.source;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 380 */     if (compat)
/*     */     {
/* 383 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 384 */       localPutField.put("type", this.type);
/* 385 */       localPutField.put("sequenceNumber", this.sequenceNumber);
/* 386 */       localPutField.put("timeStamp", this.timeStamp);
/* 387 */       localPutField.put("userData", this.userData);
/* 388 */       localPutField.put("message", this.message);
/* 389 */       localPutField.put("source", this.source);
/* 390 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 394 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 115 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 116 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 117 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 121 */     if (compat) {
/* 122 */       serialPersistentFields = oldSerialPersistentFields;
/* 123 */       serialVersionUID = 1716977971058914352L;
/*     */     } else {
/* 125 */       serialPersistentFields = newSerialPersistentFields;
/* 126 */       serialVersionUID = -7516092053498031989L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.Notification
 * JD-Core Version:    0.6.2
 */