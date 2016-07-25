/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public class StubIORImpl
/*     */ {
/*     */   private int hashCode;
/*     */   private byte[] typeData;
/*     */   private int[] profileTags;
/*     */   private byte[][] profileData;
/*     */ 
/*     */   public StubIORImpl()
/*     */   {
/*  68 */     this.hashCode = 0;
/*  69 */     this.typeData = null;
/*  70 */     this.profileTags = null;
/*  71 */     this.profileData = ((byte[][])null);
/*     */   }
/*     */ 
/*     */   public String getRepositoryId()
/*     */   {
/*  76 */     if (this.typeData == null) {
/*  77 */       return null;
/*     */     }
/*  79 */     return new String(this.typeData);
/*     */   }
/*     */ 
/*     */   public StubIORImpl(org.omg.CORBA.Object paramObject)
/*     */   {
/*  85 */     OutputStream localOutputStream = StubAdapter.getORB(paramObject).create_output_stream();
/*  86 */     localOutputStream.write_Object(paramObject);
/*  87 */     InputStream localInputStream = localOutputStream.create_input_stream();
/*     */ 
/*  90 */     int i = localInputStream.read_long();
/*  91 */     this.typeData = new byte[i];
/*  92 */     localInputStream.read_octet_array(this.typeData, 0, i);
/*  93 */     int j = localInputStream.read_long();
/*  94 */     this.profileTags = new int[j];
/*  95 */     this.profileData = new byte[j][];
/*  96 */     for (int k = 0; k < j; k++) {
/*  97 */       this.profileTags[k] = localInputStream.read_long();
/*  98 */       this.profileData[k] = new byte[localInputStream.read_long()];
/*  99 */       localInputStream.read_octet_array(this.profileData[k], 0, this.profileData[k].length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Delegate getDelegate(ORB paramORB)
/*     */   {
/* 106 */     OutputStream localOutputStream = paramORB.create_output_stream();
/* 107 */     localOutputStream.write_long(this.typeData.length);
/* 108 */     localOutputStream.write_octet_array(this.typeData, 0, this.typeData.length);
/* 109 */     localOutputStream.write_long(this.profileTags.length);
/* 110 */     for (int i = 0; i < this.profileTags.length; i++) {
/* 111 */       localOutputStream.write_long(this.profileTags[i]);
/* 112 */       localOutputStream.write_long(this.profileData[i].length);
/* 113 */       localOutputStream.write_octet_array(this.profileData[i], 0, this.profileData[i].length);
/*     */     }
/*     */ 
/* 116 */     InputStream localInputStream = localOutputStream.create_input_stream();
/*     */ 
/* 119 */     org.omg.CORBA.Object localObject = localInputStream.read_Object();
/* 120 */     return StubAdapter.getDelegate(localObject);
/*     */   }
/*     */ 
/*     */   public void doRead(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 127 */     int i = paramObjectInputStream.readInt();
/* 128 */     this.typeData = new byte[i];
/* 129 */     paramObjectInputStream.readFully(this.typeData);
/* 130 */     int j = paramObjectInputStream.readInt();
/* 131 */     this.profileTags = new int[j];
/* 132 */     this.profileData = new byte[j][];
/* 133 */     for (int k = 0; k < j; k++) {
/* 134 */       this.profileTags[k] = paramObjectInputStream.readInt();
/* 135 */       this.profileData[k] = new byte[paramObjectInputStream.readInt()];
/* 136 */       paramObjectInputStream.readFully(this.profileData[k]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void doWrite(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 144 */     paramObjectOutputStream.writeInt(this.typeData.length);
/* 145 */     paramObjectOutputStream.write(this.typeData);
/* 146 */     paramObjectOutputStream.writeInt(this.profileTags.length);
/* 147 */     for (int i = 0; i < this.profileTags.length; i++) {
/* 148 */       paramObjectOutputStream.writeInt(this.profileTags[i]);
/* 149 */       paramObjectOutputStream.writeInt(this.profileData[i].length);
/* 150 */       paramObjectOutputStream.write(this.profileData[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized int hashCode()
/*     */   {
/* 161 */     if (this.hashCode == 0)
/*     */     {
/* 164 */       for (int i = 0; i < this.typeData.length; i++) {
/* 165 */         this.hashCode = (this.hashCode * 37 + this.typeData[i]);
/*     */       }
/*     */ 
/* 168 */       for (i = 0; i < this.profileTags.length; i++) {
/* 169 */         this.hashCode = (this.hashCode * 37 + this.profileTags[i]);
/* 170 */         for (int j = 0; j < this.profileData[i].length; j++) {
/* 171 */           this.hashCode = (this.hashCode * 37 + this.profileData[i][j]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 176 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   private boolean equalArrays(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 181 */     if (paramArrayOfInt1.length != paramArrayOfInt2.length) {
/* 182 */       return false;
/*     */     }
/* 184 */     for (int i = 0; i < paramArrayOfInt1.length; i++) {
/* 185 */       if (paramArrayOfInt1[i] != paramArrayOfInt2[i]) {
/* 186 */         return false;
/*     */       }
/*     */     }
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean equalArrays(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 194 */     if (paramArrayOfByte1.length != paramArrayOfByte2.length) {
/* 195 */       return false;
/*     */     }
/* 197 */     for (int i = 0; i < paramArrayOfByte1.length; i++) {
/* 198 */       if (paramArrayOfByte1[i] != paramArrayOfByte2[i]) {
/* 199 */         return false;
/*     */       }
/*     */     }
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean equalArrays(byte[][] paramArrayOfByte1, byte[][] paramArrayOfByte2)
/*     */   {
/* 207 */     if (paramArrayOfByte1.length != paramArrayOfByte2.length) {
/* 208 */       return false;
/*     */     }
/* 210 */     for (int i = 0; i < paramArrayOfByte1.length; i++) {
/* 211 */       if (!equalArrays(paramArrayOfByte1[i], paramArrayOfByte2[i])) {
/* 212 */         return false;
/*     */       }
/*     */     }
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(java.lang.Object paramObject)
/*     */   {
/* 220 */     if (this == paramObject) {
/* 221 */       return true;
/*     */     }
/*     */ 
/* 224 */     if (!(paramObject instanceof StubIORImpl)) {
/* 225 */       return false;
/*     */     }
/*     */ 
/* 228 */     StubIORImpl localStubIORImpl = (StubIORImpl)paramObject;
/* 229 */     if (localStubIORImpl.hashCode() != hashCode()) {
/* 230 */       return false;
/*     */     }
/*     */ 
/* 233 */     return (equalArrays(this.typeData, localStubIORImpl.typeData)) && (equalArrays(this.profileTags, localStubIORImpl.profileTags)) && (equalArrays(this.profileData, localStubIORImpl.profileData));
/*     */   }
/*     */ 
/*     */   private void appendByteArray(StringBuffer paramStringBuffer, byte[] paramArrayOfByte)
/*     */   {
/* 240 */     for (int i = 0; i < paramArrayOfByte.length; i++)
/* 241 */       paramStringBuffer.append(Integer.toHexString(paramArrayOfByte[i]));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 253 */     StringBuffer localStringBuffer = new StringBuffer();
/* 254 */     localStringBuffer.append("SimpleIORImpl[");
/* 255 */     String str = new String(this.typeData);
/* 256 */     localStringBuffer.append(str);
/* 257 */     for (int i = 0; i < this.profileTags.length; i++) {
/* 258 */       localStringBuffer.append(",(");
/* 259 */       localStringBuffer.append(this.profileTags[i]);
/* 260 */       localStringBuffer.append(")");
/* 261 */       appendByteArray(localStringBuffer, this.profileData[i]);
/*     */     }
/*     */ 
/* 264 */     localStringBuffer.append("]");
/* 265 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.StubIORImpl
 * JD-Core Version:    0.6.2
 */