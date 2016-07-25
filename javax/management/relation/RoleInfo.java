/*     */ package javax.management.relation;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ 
/*     */ public class RoleInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long oldSerialVersionUID = 7227256952085334351L;
/*     */   private static final long newSerialVersionUID = 2504952983494636987L;
/*  65 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("myName", String.class), new ObjectStreamField("myIsReadableFlg", Boolean.TYPE), new ObjectStreamField("myIsWritableFlg", Boolean.TYPE), new ObjectStreamField("myDescription", String.class), new ObjectStreamField("myMinDegree", Integer.TYPE), new ObjectStreamField("myMaxDegree", Integer.TYPE), new ObjectStreamField("myRefMBeanClassName", String.class) };
/*     */ 
/*  77 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("name", String.class), new ObjectStreamField("isReadable", Boolean.TYPE), new ObjectStreamField("isWritable", Boolean.TYPE), new ObjectStreamField("description", String.class), new ObjectStreamField("minDegree", Integer.TYPE), new ObjectStreamField("maxDegree", Integer.TYPE), new ObjectStreamField("referencedMBeanClassName", String.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 100 */   private static boolean compat = false;
/*     */   public static final int ROLE_CARDINALITY_INFINITY = -1;
/* 136 */   private String name = null;
/*     */   private boolean isReadable;
/*     */   private boolean isWritable;
/* 151 */   private String description = null;
/*     */   private int minDegree;
/*     */   private int maxDegree;
/* 166 */   private String referencedMBeanClassName = null;
/*     */ 
/*     */   public RoleInfo(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, String paramString3)
/*     */     throws IllegalArgumentException, InvalidRoleInfoException, ClassNotFoundException, NotCompliantMBeanException
/*     */   {
/* 215 */     init(paramString1, paramString2, paramBoolean1, paramBoolean2, paramInt1, paramInt2, paramString3);
/*     */   }
/*     */ 
/*     */   public RoleInfo(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws IllegalArgumentException, ClassNotFoundException, NotCompliantMBeanException
/*     */   {
/*     */     try
/*     */     {
/* 258 */       init(paramString1, paramString2, paramBoolean1, paramBoolean2, 1, 1, null);
/*     */     }
/*     */     catch (InvalidRoleInfoException localInvalidRoleInfoException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public RoleInfo(String paramString1, String paramString2)
/*     */     throws IllegalArgumentException, ClassNotFoundException, NotCompliantMBeanException
/*     */   {
/*     */     try
/*     */     {
/* 301 */       init(paramString1, paramString2, true, true, 1, 1, null);
/*     */     }
/*     */     catch (InvalidRoleInfoException localInvalidRoleInfoException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public RoleInfo(RoleInfo paramRoleInfo)
/*     */     throws IllegalArgumentException
/*     */   {
/* 326 */     if (paramRoleInfo == null)
/*     */     {
/* 328 */       String str = "Invalid parameter.";
/* 329 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */     try
/*     */     {
/* 333 */       init(paramRoleInfo.getName(), paramRoleInfo.getRefMBeanClassName(), paramRoleInfo.isReadable(), paramRoleInfo.isWritable(), paramRoleInfo.getMinDegree(), paramRoleInfo.getMaxDegree(), paramRoleInfo.getDescription());
/*     */     }
/*     */     catch (InvalidRoleInfoException localInvalidRoleInfoException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 357 */     return this.name;
/*     */   }
/*     */ 
/*     */   public boolean isReadable()
/*     */   {
/* 366 */     return this.isReadable;
/*     */   }
/*     */ 
/*     */   public boolean isWritable()
/*     */   {
/* 375 */     return this.isWritable;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 384 */     return this.description;
/*     */   }
/*     */ 
/*     */   public int getMinDegree()
/*     */   {
/* 393 */     return this.minDegree;
/*     */   }
/*     */ 
/*     */   public int getMaxDegree()
/*     */   {
/* 402 */     return this.maxDegree;
/*     */   }
/*     */ 
/*     */   public String getRefMBeanClassName()
/*     */   {
/* 412 */     return this.referencedMBeanClassName;
/*     */   }
/*     */ 
/*     */   public boolean checkMinDegree(int paramInt)
/*     */   {
/* 424 */     if ((paramInt >= -1) && ((this.minDegree == -1) || (paramInt >= this.minDegree)))
/*     */     {
/* 427 */       return true;
/*     */     }
/* 429 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean checkMaxDegree(int paramInt)
/*     */   {
/* 442 */     if ((paramInt >= -1) && ((this.maxDegree == -1) || ((paramInt != -1) && (paramInt <= this.maxDegree))))
/*     */     {
/* 446 */       return true;
/*     */     }
/* 448 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 458 */     StringBuilder localStringBuilder = new StringBuilder();
/* 459 */     localStringBuilder.append("role info name: " + this.name);
/* 460 */     localStringBuilder.append("; isReadable: " + this.isReadable);
/* 461 */     localStringBuilder.append("; isWritable: " + this.isWritable);
/* 462 */     localStringBuilder.append("; description: " + this.description);
/* 463 */     localStringBuilder.append("; minimum degree: " + this.minDegree);
/* 464 */     localStringBuilder.append("; maximum degree: " + this.maxDegree);
/* 465 */     localStringBuilder.append("; MBean class: " + this.referencedMBeanClassName);
/* 466 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void init(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, String paramString3)
/*     */     throws IllegalArgumentException, InvalidRoleInfoException
/*     */   {
/* 484 */     if ((paramString1 == null) || (paramString2 == null))
/*     */     {
/* 487 */       String str = "Invalid parameter.";
/* 488 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 491 */     this.name = paramString1;
/* 492 */     this.isReadable = paramBoolean1;
/* 493 */     this.isWritable = paramBoolean2;
/* 494 */     if (paramString3 != null) {
/* 495 */       this.description = paramString3;
/*     */     }
/*     */ 
/* 498 */     int i = 0;
/* 499 */     StringBuilder localStringBuilder = new StringBuilder();
/* 500 */     if ((paramInt2 != -1) && ((paramInt1 == -1) || (paramInt1 > paramInt2)))
/*     */     {
/* 504 */       localStringBuilder.append("Minimum degree ");
/* 505 */       localStringBuilder.append(paramInt1);
/* 506 */       localStringBuilder.append(" is greater than maximum degree ");
/* 507 */       localStringBuilder.append(paramInt2);
/* 508 */       i = 1;
/*     */     }
/* 510 */     else if ((paramInt1 < -1) || (paramInt2 < -1))
/*     */     {
/* 513 */       localStringBuilder.append("Minimum or maximum degree has an illegal value, must be [0, ROLE_CARDINALITY_INFINITY].");
/* 514 */       i = 1;
/*     */     }
/* 516 */     if (i != 0) {
/* 517 */       throw new InvalidRoleInfoException(localStringBuilder.toString());
/*     */     }
/* 519 */     this.minDegree = paramInt1;
/* 520 */     this.maxDegree = paramInt2;
/*     */ 
/* 522 */     this.referencedMBeanClassName = paramString2;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 532 */     if (compat)
/*     */     {
/* 536 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 537 */       this.name = ((String)localGetField.get("myName", null));
/* 538 */       if (localGetField.defaulted("myName"))
/*     */       {
/* 540 */         throw new NullPointerException("myName");
/*     */       }
/* 542 */       this.isReadable = localGetField.get("myIsReadableFlg", false);
/* 543 */       if (localGetField.defaulted("myIsReadableFlg"))
/*     */       {
/* 545 */         throw new NullPointerException("myIsReadableFlg");
/*     */       }
/* 547 */       this.isWritable = localGetField.get("myIsWritableFlg", false);
/* 548 */       if (localGetField.defaulted("myIsWritableFlg"))
/*     */       {
/* 550 */         throw new NullPointerException("myIsWritableFlg");
/*     */       }
/* 552 */       this.description = ((String)localGetField.get("myDescription", null));
/* 553 */       if (localGetField.defaulted("myDescription"))
/*     */       {
/* 555 */         throw new NullPointerException("myDescription");
/*     */       }
/* 557 */       this.minDegree = localGetField.get("myMinDegree", 0);
/* 558 */       if (localGetField.defaulted("myMinDegree"))
/*     */       {
/* 560 */         throw new NullPointerException("myMinDegree");
/*     */       }
/* 562 */       this.maxDegree = localGetField.get("myMaxDegree", 0);
/* 563 */       if (localGetField.defaulted("myMaxDegree"))
/*     */       {
/* 565 */         throw new NullPointerException("myMaxDegree");
/*     */       }
/* 567 */       this.referencedMBeanClassName = ((String)localGetField.get("myRefMBeanClassName", null));
/* 568 */       if (localGetField.defaulted("myRefMBeanClassName"))
/*     */       {
/* 570 */         throw new NullPointerException("myRefMBeanClassName");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 577 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 587 */     if (compat)
/*     */     {
/* 591 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 592 */       localPutField.put("myName", this.name);
/* 593 */       localPutField.put("myIsReadableFlg", this.isReadable);
/* 594 */       localPutField.put("myIsWritableFlg", this.isWritable);
/* 595 */       localPutField.put("myDescription", this.description);
/* 596 */       localPutField.put("myMinDegree", this.minDegree);
/* 597 */       localPutField.put("myMaxDegree", this.maxDegree);
/* 598 */       localPutField.put("myRefMBeanClassName", this.referencedMBeanClassName);
/* 599 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 605 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 103 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 104 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 105 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 109 */     if (compat) {
/* 110 */       serialPersistentFields = oldSerialPersistentFields;
/* 111 */       serialVersionUID = 7227256952085334351L;
/*     */     } else {
/* 113 */       serialPersistentFields = newSerialPersistentFields;
/* 114 */       serialVersionUID = 2504952983494636987L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RoleInfo
 * JD-Core Version:    0.6.2
 */