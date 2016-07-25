/*     */ package java.rmi;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import sun.rmi.server.MarshalInputStream;
/*     */ import sun.rmi.server.MarshalOutputStream;
/*     */ 
/*     */ public final class MarshalledObject<T>
/*     */   implements Serializable
/*     */ {
/*  77 */   private byte[] objBytes = null;
/*     */ 
/*  84 */   private byte[] locBytes = null;
/*     */   private int hash;
/*     */   private static final long serialVersionUID = 8988374069173025854L;
/*     */ 
/*     */   public MarshalledObject(T paramT)
/*     */     throws IOException
/*     */   {
/* 109 */     if (paramT == null) {
/* 110 */       this.hash = 13;
/* 111 */       return;
/*     */     }
/*     */ 
/* 114 */     ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
/* 115 */     ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
/* 116 */     MarshalledObjectOutputStream localMarshalledObjectOutputStream = new MarshalledObjectOutputStream(localByteArrayOutputStream1, localByteArrayOutputStream2);
/*     */ 
/* 118 */     localMarshalledObjectOutputStream.writeObject(paramT);
/* 119 */     localMarshalledObjectOutputStream.flush();
/* 120 */     this.objBytes = localByteArrayOutputStream1.toByteArray();
/*     */ 
/* 122 */     this.locBytes = (localMarshalledObjectOutputStream.hadAnnotations() ? localByteArrayOutputStream2.toByteArray() : null);
/*     */ 
/* 128 */     int i = 0;
/* 129 */     for (int j = 0; j < this.objBytes.length; j++) {
/* 130 */       i = 31 * i + this.objBytes[j];
/*     */     }
/* 132 */     this.hash = i;
/*     */   }
/*     */ 
/*     */   public T get()
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 150 */     if (this.objBytes == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     ByteArrayInputStream localByteArrayInputStream1 = new ByteArrayInputStream(this.objBytes);
/*     */ 
/* 155 */     ByteArrayInputStream localByteArrayInputStream2 = this.locBytes == null ? null : new ByteArrayInputStream(this.locBytes);
/*     */ 
/* 157 */     MarshalledObjectInputStream localMarshalledObjectInputStream = new MarshalledObjectInputStream(localByteArrayInputStream1, localByteArrayInputStream2);
/*     */ 
/* 159 */     Object localObject = localMarshalledObjectInputStream.readObject();
/* 160 */     localMarshalledObjectInputStream.close();
/* 161 */     return localObject;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 170 */     return this.hash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 189 */     if (paramObject == this) {
/* 190 */       return true;
/*     */     }
/* 192 */     if ((paramObject != null) && ((paramObject instanceof MarshalledObject))) {
/* 193 */       MarshalledObject localMarshalledObject = (MarshalledObject)paramObject;
/*     */ 
/* 196 */       if ((this.objBytes == null) || (localMarshalledObject.objBytes == null)) {
/* 197 */         return this.objBytes == localMarshalledObject.objBytes;
/*     */       }
/*     */ 
/* 200 */       if (this.objBytes.length != localMarshalledObject.objBytes.length) {
/* 201 */         return false;
/*     */       }
/*     */ 
/* 205 */       for (int i = 0; i < this.objBytes.length; i++) {
/* 206 */         if (this.objBytes[i] != localMarshalledObject.objBytes[i])
/* 207 */           return false;
/*     */       }
/* 209 */       return true;
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   private static class MarshalledObjectInputStream extends MarshalInputStream
/*     */   {
/*     */     private ObjectInputStream locIn;
/*     */ 
/*     */     MarshalledObjectInputStream(InputStream paramInputStream1, InputStream paramInputStream2)
/*     */       throws IOException
/*     */     {
/* 300 */       super();
/* 301 */       this.locIn = (paramInputStream2 == null ? null : new ObjectInputStream(paramInputStream2));
/*     */     }
/*     */ 
/*     */     protected Object readLocation()
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 312 */       return this.locIn == null ? null : this.locIn.readObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MarshalledObjectOutputStream extends MarshalOutputStream
/*     */   {
/*     */     private ObjectOutputStream locOut;
/*     */     private boolean hadAnnotations;
/*     */ 
/*     */     MarshalledObjectOutputStream(OutputStream paramOutputStream1, OutputStream paramOutputStream2)
/*     */       throws IOException
/*     */     {
/* 246 */       super();
/* 247 */       useProtocolVersion(2);
/* 248 */       this.locOut = new ObjectOutputStream(paramOutputStream2);
/* 249 */       this.hadAnnotations = false;
/*     */     }
/*     */ 
/*     */     boolean hadAnnotations()
/*     */     {
/* 257 */       return this.hadAnnotations;
/*     */     }
/*     */ 
/*     */     protected void writeLocation(String paramString)
/*     */       throws IOException
/*     */     {
/* 265 */       this.hadAnnotations |= paramString != null;
/* 266 */       this.locOut.writeObject(paramString);
/*     */     }
/*     */ 
/*     */     public void flush() throws IOException
/*     */     {
/* 271 */       super.flush();
/* 272 */       this.locOut.flush();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.MarshalledObject
 * JD-Core Version:    0.6.2
 */