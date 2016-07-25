/*     */ package javax.management.relation;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class RoleUnresolved
/*     */   implements Serializable
/*     */ {
/*     */   private static final long oldSerialVersionUID = -9026457686611660144L;
/*     */   private static final long newSerialVersionUID = -48350262537070138L;
/*  71 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("myRoleName", String.class), new ObjectStreamField("myRoleValue", ArrayList.class), new ObjectStreamField("myPbType", Integer.TYPE) };
/*     */ 
/*  79 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("roleName", String.class), new ObjectStreamField("roleValue", List.class), new ObjectStreamField("problemType", Integer.TYPE) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/*  93 */   private static boolean compat = false;
/*     */ 
/* 120 */   private String roleName = null;
/*     */ 
/* 125 */   private List<ObjectName> roleValue = null;
/*     */   private int problemType;
/*     */ 
/*     */   public RoleUnresolved(String paramString, List<ObjectName> paramList, int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/* 153 */     if (paramString == null) {
/* 154 */       String str = "Invalid parameter.";
/* 155 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 158 */     setRoleName(paramString);
/* 159 */     setRoleValue(paramList);
/*     */ 
/* 161 */     setProblemType(paramInt);
/*     */   }
/*     */ 
/*     */   public String getRoleName()
/*     */   {
/* 177 */     return this.roleName;
/*     */   }
/*     */ 
/*     */   public List<ObjectName> getRoleValue()
/*     */   {
/* 190 */     return this.roleValue;
/*     */   }
/*     */ 
/*     */   public int getProblemType()
/*     */   {
/* 202 */     return this.problemType;
/*     */   }
/*     */ 
/*     */   public void setRoleName(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 217 */     if (paramString == null) {
/* 218 */       String str = "Invalid parameter.";
/* 219 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 222 */     this.roleName = paramString;
/*     */   }
/*     */ 
/*     */   public void setRoleValue(List<ObjectName> paramList)
/*     */   {
/* 236 */     if (paramList != null)
/* 237 */       this.roleValue = new ArrayList(paramList);
/*     */     else
/* 239 */       this.roleValue = null;
/*     */   }
/*     */ 
/*     */   public void setProblemType(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/* 257 */     if (!RoleStatus.isRoleStatus(paramInt)) {
/* 258 */       String str = "Incorrect problem type.";
/* 259 */       throw new IllegalArgumentException(str);
/*     */     }
/* 261 */     this.problemType = paramInt;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 272 */       return new RoleUnresolved(this.roleName, this.roleValue, this.problemType); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 274 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 284 */     StringBuilder localStringBuilder = new StringBuilder();
/* 285 */     localStringBuilder.append("role name: " + this.roleName);
/* 286 */     if (this.roleValue != null) {
/* 287 */       localStringBuilder.append("; value: ");
/* 288 */       Iterator localIterator = this.roleValue.iterator();
/* 289 */       while (localIterator.hasNext()) {
/* 290 */         ObjectName localObjectName = (ObjectName)localIterator.next();
/* 291 */         localStringBuilder.append(localObjectName.toString());
/* 292 */         if (localIterator.hasNext()) {
/* 293 */           localStringBuilder.append(", ");
/*     */         }
/*     */       }
/*     */     }
/* 297 */     localStringBuilder.append("; problem type: " + this.problemType);
/* 298 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 306 */     if (compat)
/*     */     {
/* 310 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 311 */       this.roleName = ((String)localGetField.get("myRoleName", null));
/* 312 */       if (localGetField.defaulted("myRoleName"))
/*     */       {
/* 314 */         throw new NullPointerException("myRoleName");
/*     */       }
/* 316 */       this.roleValue = ((List)Util.cast(localGetField.get("myRoleValue", null)));
/* 317 */       if (localGetField.defaulted("myRoleValue"))
/*     */       {
/* 319 */         throw new NullPointerException("myRoleValue");
/*     */       }
/* 321 */       this.problemType = localGetField.get("myPbType", 0);
/* 322 */       if (localGetField.defaulted("myPbType"))
/*     */       {
/* 324 */         throw new NullPointerException("myPbType");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 331 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 341 */     if (compat)
/*     */     {
/* 345 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 346 */       localPutField.put("myRoleName", this.roleName);
/* 347 */       localPutField.put("myRoleValue", this.roleValue);
/* 348 */       localPutField.put("myPbType", this.problemType);
/* 349 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 355 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  96 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  97 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  98 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 102 */     if (compat) {
/* 103 */       serialPersistentFields = oldSerialPersistentFields;
/* 104 */       serialVersionUID = -9026457686611660144L;
/*     */     } else {
/* 106 */       serialPersistentFields = newSerialPersistentFields;
/* 107 */       serialVersionUID = -48350262537070138L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RoleUnresolved
 * JD-Core Version:    0.6.2
 */