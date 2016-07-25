/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class PKCS10Attributes
/*     */   implements DerEncoder
/*     */ {
/*  52 */   private Hashtable<String, PKCS10Attribute> map = new Hashtable(3);
/*     */ 
/*     */   public PKCS10Attributes()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PKCS10Attributes(PKCS10Attribute[] paramArrayOfPKCS10Attribute)
/*     */   {
/*  66 */     for (int i = 0; i < paramArrayOfPKCS10Attribute.length; i++)
/*  67 */       this.map.put(paramArrayOfPKCS10Attribute[i].getAttributeId().toString(), paramArrayOfPKCS10Attribute[i]);
/*     */   }
/*     */ 
/*     */   public PKCS10Attributes(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  79 */     DerValue[] arrayOfDerValue = paramDerInputStream.getSet(3, true);
/*     */ 
/*  81 */     if (arrayOfDerValue == null)
/*  82 */       throw new IOException("Illegal encoding of attributes");
/*  83 */     for (int i = 0; i < arrayOfDerValue.length; i++) {
/*  84 */       PKCS10Attribute localPKCS10Attribute = new PKCS10Attribute(arrayOfDerValue[i]);
/*  85 */       this.map.put(localPKCS10Attribute.getAttributeId().toString(), localPKCS10Attribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  96 */     derEncode(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void derEncode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 108 */     Collection localCollection = this.map.values();
/* 109 */     PKCS10Attribute[] arrayOfPKCS10Attribute = (PKCS10Attribute[])localCollection.toArray(new PKCS10Attribute[this.map.size()]);
/*     */ 
/* 112 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 113 */     localDerOutputStream.putOrderedSetOf(DerValue.createTag((byte)-128, true, (byte)0), arrayOfPKCS10Attribute);
/*     */ 
/* 116 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void setAttribute(String paramString, Object paramObject)
/*     */   {
/* 123 */     if ((paramObject instanceof PKCS10Attribute))
/* 124 */       this.map.put(paramString, (PKCS10Attribute)paramObject);
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString)
/*     */   {
/* 132 */     return this.map.get(paramString);
/*     */   }
/*     */ 
/*     */   public void deleteAttribute(String paramString)
/*     */   {
/* 139 */     this.map.remove(paramString);
/*     */   }
/*     */ 
/*     */   public Enumeration<PKCS10Attribute> getElements()
/*     */   {
/* 147 */     return this.map.elements();
/*     */   }
/*     */ 
/*     */   public Collection<PKCS10Attribute> getAttributes()
/*     */   {
/* 155 */     return Collections.unmodifiableCollection(this.map.values());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 169 */     if (this == paramObject)
/* 170 */       return true;
/* 171 */     if (!(paramObject instanceof PKCS10Attributes)) {
/* 172 */       return false;
/*     */     }
/* 174 */     Collection localCollection = ((PKCS10Attributes)paramObject).getAttributes();
/*     */ 
/* 176 */     PKCS10Attribute[] arrayOfPKCS10Attribute = (PKCS10Attribute[])localCollection.toArray(new PKCS10Attribute[localCollection.size()]);
/*     */ 
/* 178 */     int i = arrayOfPKCS10Attribute.length;
/* 179 */     if (i != this.map.size()) {
/* 180 */       return false;
/*     */     }
/* 182 */     String str = null;
/* 183 */     for (int j = 0; j < i; j++) {
/* 184 */       PKCS10Attribute localPKCS10Attribute2 = arrayOfPKCS10Attribute[j];
/* 185 */       str = localPKCS10Attribute2.getAttributeId().toString();
/*     */ 
/* 187 */       if (str == null)
/* 188 */         return false;
/* 189 */       PKCS10Attribute localPKCS10Attribute1 = (PKCS10Attribute)this.map.get(str);
/* 190 */       if (localPKCS10Attribute1 == null)
/* 191 */         return false;
/* 192 */       if (!localPKCS10Attribute1.equals(localPKCS10Attribute2))
/* 193 */         return false;
/*     */     }
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 204 */     return this.map.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 216 */     String str = this.map.size() + "\n" + this.map.toString();
/* 217 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS10Attributes
 * JD-Core Version:    0.6.2
 */