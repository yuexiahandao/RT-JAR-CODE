/*     */ package javax.management;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class MBeanFeatureInfo
/*     */   implements Serializable, DescriptorRead
/*     */ {
/*     */   static final long serialVersionUID = 3952882688968447265L;
/*     */   protected String name;
/*     */   protected String description;
/*     */   private transient Descriptor descriptor;
/*     */ 
/*     */   public MBeanFeatureInfo(String paramString1, String paramString2)
/*     */   {
/*  84 */     this(paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   public MBeanFeatureInfo(String paramString1, String paramString2, Descriptor paramDescriptor)
/*     */   {
/*  99 */     this.name = paramString1;
/* 100 */     this.description = paramString2;
/* 101 */     this.descriptor = paramDescriptor;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 119 */     return this.description;
/*     */   }
/*     */ 
/*     */   public Descriptor getDescriptor()
/*     */   {
/* 131 */     return (Descriptor)ImmutableDescriptor.nonNullDescriptor(this.descriptor).clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 146 */     if (paramObject == this)
/* 147 */       return true;
/* 148 */     if (!(paramObject instanceof MBeanFeatureInfo))
/* 149 */       return false;
/* 150 */     MBeanFeatureInfo localMBeanFeatureInfo = (MBeanFeatureInfo)paramObject;
/* 151 */     return (Objects.equals(localMBeanFeatureInfo.getName(), getName())) && (Objects.equals(localMBeanFeatureInfo.getDescription(), getDescription())) && (Objects.equals(localMBeanFeatureInfo.getDescriptor(), getDescriptor()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 157 */     return getName().hashCode() ^ getDescription().hashCode() ^ getDescriptor().hashCode();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 187 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 189 */     if ((this.descriptor != null) && (this.descriptor.getClass() == ImmutableDescriptor.class))
/*     */     {
/* 192 */       paramObjectOutputStream.write(1);
/*     */ 
/* 194 */       String[] arrayOfString = this.descriptor.getFieldNames();
/*     */ 
/* 196 */       paramObjectOutputStream.writeObject(arrayOfString);
/* 197 */       paramObjectOutputStream.writeObject(this.descriptor.getFieldValues(arrayOfString));
/*     */     } else {
/* 199 */       paramObjectOutputStream.write(0);
/*     */ 
/* 201 */       paramObjectOutputStream.writeObject(this.descriptor);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 237 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 239 */     switch (paramObjectInputStream.read()) {
/*     */     case 1:
/* 241 */       String[] arrayOfString = (String[])paramObjectInputStream.readObject();
/*     */ 
/* 243 */       if (arrayOfString.length == 0) {
/* 244 */         this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */       } else {
/* 246 */         Object[] arrayOfObject = (Object[])paramObjectInputStream.readObject();
/* 247 */         this.descriptor = new ImmutableDescriptor(arrayOfString, arrayOfObject);
/*     */       }
/*     */ 
/* 250 */       break;
/*     */     case 0:
/* 252 */       this.descriptor = ((Descriptor)paramObjectInputStream.readObject());
/*     */ 
/* 254 */       if (this.descriptor == null)
/* 255 */         this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR; break;
/*     */     case -1:
/* 260 */       this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */ 
/* 262 */       break;
/*     */     default:
/* 264 */       throw new StreamCorruptedException("Got unexpected byte.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanFeatureInfo
 * JD-Core Version:    0.6.2
 */