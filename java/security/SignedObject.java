/*     */ package java.security;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class SignedObject
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 720502720485447167L;
/*     */   private byte[] content;
/*     */   private byte[] signature;
/*     */   private String thealgorithm;
/*     */ 
/*     */   public SignedObject(Serializable paramSerializable, PrivateKey paramPrivateKey, Signature paramSignature)
/*     */     throws IOException, InvalidKeyException, SignatureException
/*     */   {
/* 150 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 151 */     ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
/*     */ 
/* 154 */     localObjectOutputStream.writeObject(paramSerializable);
/* 155 */     localObjectOutputStream.flush();
/* 156 */     localObjectOutputStream.close();
/* 157 */     this.content = localByteArrayOutputStream.toByteArray();
/* 158 */     localByteArrayOutputStream.close();
/*     */ 
/* 161 */     sign(paramPrivateKey, paramSignature);
/*     */   }
/*     */ 
/*     */   public Object getObject()
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 178 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.content);
/* 179 */     ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
/* 180 */     Object localObject = localObjectInputStream.readObject();
/* 181 */     localByteArrayInputStream.close();
/* 182 */     localObjectInputStream.close();
/* 183 */     return localObject;
/*     */   }
/*     */ 
/*     */   public byte[] getSignature()
/*     */   {
/* 194 */     return (byte[])this.signature.clone();
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 203 */     return this.thealgorithm;
/*     */   }
/*     */ 
/*     */   public boolean verify(PublicKey paramPublicKey, Signature paramSignature)
/*     */     throws InvalidKeyException, SignatureException
/*     */   {
/* 223 */     paramSignature.initVerify(paramPublicKey);
/* 224 */     paramSignature.update((byte[])this.content.clone());
/* 225 */     return paramSignature.verify((byte[])this.signature.clone());
/*     */   }
/*     */ 
/*     */   private void sign(PrivateKey paramPrivateKey, Signature paramSignature)
/*     */     throws InvalidKeyException, SignatureException
/*     */   {
/* 241 */     paramSignature.initSign(paramPrivateKey);
/* 242 */     paramSignature.update((byte[])this.content.clone());
/* 243 */     this.signature = ((byte[])paramSignature.sign().clone());
/* 244 */     this.thealgorithm = paramSignature.getAlgorithm();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 253 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 254 */     this.content = ((byte[])((byte[])localGetField.get("content", null)).clone());
/* 255 */     this.signature = ((byte[])((byte[])localGetField.get("signature", null)).clone());
/* 256 */     this.thealgorithm = ((String)localGetField.get("thealgorithm", null));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.SignedObject
 * JD-Core Version:    0.6.2
 */