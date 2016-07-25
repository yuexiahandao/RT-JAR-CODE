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
/*     */ public class Role
/*     */   implements Serializable
/*     */ {
/*     */   private static final long oldSerialVersionUID = -1959486389343113026L;
/*     */   private static final long newSerialVersionUID = -279985518429862552L;
/*  70 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("myName", String.class), new ObjectStreamField("myObjNameList", ArrayList.class) };
/*     */ 
/*  77 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("name", String.class), new ObjectStreamField("objectNameList", List.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/*  90 */   private static boolean compat = false;
/*     */ 
/* 117 */   private String name = null;
/*     */ 
/* 122 */   private List<ObjectName> objectNameList = new ArrayList();
/*     */ 
/*     */   public Role(String paramString, List<ObjectName> paramList)
/*     */     throws IllegalArgumentException
/*     */   {
/* 143 */     if ((paramString == null) || (paramList == null)) {
/* 144 */       String str = "Invalid parameter";
/* 145 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 148 */     setRoleName(paramString);
/* 149 */     setRoleValue(paramList);
/*     */   }
/*     */ 
/*     */   public String getRoleName()
/*     */   {
/* 166 */     return this.name;
/*     */   }
/*     */ 
/*     */   public List<ObjectName> getRoleValue()
/*     */   {
/* 177 */     return this.objectNameList;
/*     */   }
/*     */ 
/*     */   public void setRoleName(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 192 */     if (paramString == null) {
/* 193 */       String str = "Invalid parameter.";
/* 194 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 197 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public void setRoleValue(List<ObjectName> paramList)
/*     */     throws IllegalArgumentException
/*     */   {
/* 214 */     if (paramList == null) {
/* 215 */       String str = "Invalid parameter.";
/* 216 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 219 */     this.objectNameList = new ArrayList(paramList);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 229 */     StringBuilder localStringBuilder = new StringBuilder();
/* 230 */     localStringBuilder.append("role name: " + this.name + "; role value: ");
/* 231 */     Iterator localIterator = this.objectNameList.iterator();
/* 232 */     while (localIterator.hasNext()) {
/* 233 */       ObjectName localObjectName = (ObjectName)localIterator.next();
/* 234 */       localStringBuilder.append(localObjectName.toString());
/* 235 */       if (localIterator.hasNext()) {
/* 236 */         localStringBuilder.append(", ");
/*     */       }
/*     */     }
/* 239 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 254 */       return new Role(this.name, this.objectNameList); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   public static String roleValueToString(List<ObjectName> paramList)
/*     */     throws IllegalArgumentException
/*     */   {
/* 273 */     if (paramList == null) {
/* 274 */       localObject = "Invalid parameter";
/* 275 */       throw new IllegalArgumentException((String)localObject);
/*     */     }
/*     */ 
/* 278 */     Object localObject = new StringBuilder();
/* 279 */     for (ObjectName localObjectName : paramList) {
/* 280 */       if (((StringBuilder)localObject).length() > 0)
/* 281 */         ((StringBuilder)localObject).append("\n");
/* 282 */       ((StringBuilder)localObject).append(localObjectName.toString());
/*     */     }
/* 284 */     return ((StringBuilder)localObject).toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 292 */     if (compat)
/*     */     {
/* 296 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 297 */       this.name = ((String)localGetField.get("myName", null));
/* 298 */       if (localGetField.defaulted("myName"))
/*     */       {
/* 300 */         throw new NullPointerException("myName");
/*     */       }
/* 302 */       this.objectNameList = ((List)Util.cast(localGetField.get("myObjNameList", null)));
/* 303 */       if (localGetField.defaulted("myObjNameList"))
/*     */       {
/* 305 */         throw new NullPointerException("myObjNameList");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 312 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 322 */     if (compat)
/*     */     {
/* 326 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 327 */       localPutField.put("myName", this.name);
/* 328 */       localPutField.put("myObjNameList", this.objectNameList);
/* 329 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 335 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  93 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  94 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  95 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  99 */     if (compat) {
/* 100 */       serialPersistentFields = oldSerialPersistentFields;
/* 101 */       serialVersionUID = -1959486389343113026L;
/*     */     } else {
/* 103 */       serialPersistentFields = newSerialPersistentFields;
/* 104 */       serialVersionUID = -279985518429862552L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.Role
 * JD-Core Version:    0.6.2
 */