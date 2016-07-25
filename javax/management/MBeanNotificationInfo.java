/*     */ package javax.management;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class MBeanNotificationInfo extends MBeanFeatureInfo
/*     */   implements Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -3888371564530107064L;
/*  66 */   private static final String[] NO_TYPES = new String[0];
/*     */ 
/*  68 */   static final MBeanNotificationInfo[] NO_NOTIFICATIONS = new MBeanNotificationInfo[0];
/*     */   private String[] types;
/*     */   private final transient boolean arrayGettersSafe;
/*     */ 
/*     */   public MBeanNotificationInfo(String[] paramArrayOfString, String paramString1, String paramString2)
/*     */   {
/*  92 */     this(paramArrayOfString, paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo(String[] paramArrayOfString, String paramString1, String paramString2, Descriptor paramDescriptor)
/*     */   {
/* 113 */     super(paramString1, paramString2, paramDescriptor);
/*     */ 
/* 121 */     this.types = ((paramArrayOfString != null) && (paramArrayOfString.length > 0) ? (String[])paramArrayOfString.clone() : NO_TYPES);
/*     */ 
/* 123 */     this.arrayGettersSafe = MBeanInfo.arrayGettersSafe(getClass(), MBeanNotificationInfo.class);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 138 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getNotifTypes()
/*     */   {
/* 154 */     if (this.types.length == 0) {
/* 155 */       return NO_TYPES;
/*     */     }
/* 157 */     return (String[])this.types.clone();
/*     */   }
/*     */ 
/*     */   private String[] fastGetNotifTypes() {
/* 161 */     if (this.arrayGettersSafe) {
/* 162 */       return this.types;
/*     */     }
/* 164 */     return getNotifTypes();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 168 */     return getClass().getName() + "[" + "description=" + getDescription() + ", " + "name=" + getName() + ", " + "notifTypes=" + Arrays.asList(fastGetNotifTypes()) + ", " + "descriptor=" + getDescriptor() + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 192 */     if (paramObject == this)
/* 193 */       return true;
/* 194 */     if (!(paramObject instanceof MBeanNotificationInfo))
/* 195 */       return false;
/* 196 */     MBeanNotificationInfo localMBeanNotificationInfo = (MBeanNotificationInfo)paramObject;
/* 197 */     return (Objects.equals(localMBeanNotificationInfo.getName(), getName())) && (Objects.equals(localMBeanNotificationInfo.getDescription(), getDescription())) && (Objects.equals(localMBeanNotificationInfo.getDescriptor(), getDescriptor())) && (Arrays.equals(localMBeanNotificationInfo.fastGetNotifTypes(), fastGetNotifTypes()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 204 */     int i = getName().hashCode();
/* 205 */     for (int j = 0; j < this.types.length; j++)
/* 206 */       i ^= this.types[j].hashCode();
/* 207 */     return i;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 211 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 212 */     String[] arrayOfString = (String[])localGetField.get("types", null);
/*     */ 
/* 214 */     this.types = ((arrayOfString != null) && (arrayOfString.length != 0) ? (String[])arrayOfString.clone() : NO_TYPES);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanNotificationInfo
 * JD-Core Version:    0.6.2
 */