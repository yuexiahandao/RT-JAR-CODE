/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*     */ import com.sun.corba.se.impl.logging.IORSystemException;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.IOException;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.OctetSeqHolder;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ 
/*     */ public class ObjectKeyFactoryImpl
/*     */   implements ObjectKeyFactory
/*     */ {
/*     */   public static final int MAGIC_BASE = -1347695874;
/*     */   public static final int JAVAMAGIC_OLD = -1347695874;
/*     */   public static final int JAVAMAGIC_NEW = -1347695873;
/*     */   public static final int JAVAMAGIC_NEWER = -1347695872;
/*     */   public static final int MAX_MAGIC = -1347695872;
/*     */   public static final byte JDK1_3_1_01_PATCH_LEVEL = 1;
/*     */   private final ORB orb;
/*     */   private IORSystemException wrapper;
/* 130 */   private Handler fullKey = new Handler()
/*     */   {
/*     */     public ObjectKeyTemplate handle(int paramAnonymousInt1, int paramAnonymousInt2, InputStream paramAnonymousInputStream, OctetSeqHolder paramAnonymousOctetSeqHolder) {
/* 133 */       Object localObject = null;
/*     */ 
/* 135 */       if ((paramAnonymousInt2 >= 32) && (paramAnonymousInt2 <= 63))
/*     */       {
/* 137 */         if (paramAnonymousInt1 >= -1347695872)
/* 138 */           localObject = new POAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream, paramAnonymousOctetSeqHolder);
/*     */         else
/* 140 */           localObject = new OldPOAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream, paramAnonymousOctetSeqHolder);
/* 141 */       } else if ((paramAnonymousInt2 >= 0) && (paramAnonymousInt2 < 32)) {
/* 142 */         if (paramAnonymousInt1 >= -1347695872)
/* 143 */           localObject = new JIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream, paramAnonymousOctetSeqHolder);
/*     */         else {
/* 145 */           localObject = new OldJIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream, paramAnonymousOctetSeqHolder);
/*     */         }
/*     */       }
/* 148 */       return localObject;
/*     */     }
/* 130 */   };
/*     */ 
/* 154 */   private Handler oktempOnly = new Handler()
/*     */   {
/*     */     public ObjectKeyTemplate handle(int paramAnonymousInt1, int paramAnonymousInt2, InputStream paramAnonymousInputStream, OctetSeqHolder paramAnonymousOctetSeqHolder) {
/* 157 */       Object localObject = null;
/*     */ 
/* 159 */       if ((paramAnonymousInt2 >= 32) && (paramAnonymousInt2 <= 63))
/*     */       {
/* 161 */         if (paramAnonymousInt1 >= -1347695872)
/* 162 */           localObject = new POAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream);
/*     */         else
/* 164 */           localObject = new OldPOAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream);
/* 165 */       } else if ((paramAnonymousInt2 >= 0) && (paramAnonymousInt2 < 32)) {
/* 166 */         if (paramAnonymousInt1 >= -1347695872)
/* 167 */           localObject = new JIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream);
/*     */         else {
/* 169 */           localObject = new OldJIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInputStream);
/*     */         }
/*     */       }
/* 172 */       return localObject;
/*     */     }
/* 154 */   };
/*     */ 
/*     */   public ObjectKeyFactoryImpl(ORB paramORB)
/*     */   {
/*  96 */     this.orb = paramORB;
/*  97 */     this.wrapper = IORSystemException.get(paramORB, "oa.ior");
/*     */   }
/*     */ 
/*     */   private boolean validMagic(int paramInt)
/*     */   {
/* 181 */     return (paramInt >= -1347695874) && (paramInt <= -1347695872);
/*     */   }
/*     */ 
/*     */   private ObjectKeyTemplate create(InputStream paramInputStream, Handler paramHandler, OctetSeqHolder paramOctetSeqHolder)
/*     */   {
/* 190 */     ObjectKeyTemplate localObjectKeyTemplate = null;
/*     */     try
/*     */     {
/* 193 */       paramInputStream.mark(0);
/* 194 */       int i = paramInputStream.read_long();
/*     */ 
/* 196 */       if (validMagic(i)) {
/* 197 */         int j = paramInputStream.read_long();
/* 198 */         localObjectKeyTemplate = paramHandler.handle(i, j, paramInputStream, paramOctetSeqHolder);
/*     */       }
/*     */     }
/*     */     catch (MARSHAL localMARSHAL)
/*     */     {
/*     */     }
/*     */ 
/* 205 */     if (localObjectKeyTemplate == null)
/*     */     {
/*     */       try
/*     */       {
/* 210 */         paramInputStream.reset();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/* 216 */     return localObjectKeyTemplate;
/*     */   }
/*     */ 
/*     */   public ObjectKey create(byte[] paramArrayOfByte)
/*     */   {
/* 221 */     OctetSeqHolder localOctetSeqHolder = new OctetSeqHolder();
/* 222 */     EncapsInputStream localEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, paramArrayOfByte, paramArrayOfByte.length);
/*     */ 
/* 224 */     Object localObject = create(localEncapsInputStream, this.fullKey, localOctetSeqHolder);
/* 225 */     if (localObject == null) {
/* 226 */       localObject = new WireObjectKeyTemplate(localEncapsInputStream, localOctetSeqHolder);
/*     */     }
/* 228 */     ObjectIdImpl localObjectIdImpl = new ObjectIdImpl(localOctetSeqHolder.value);
/* 229 */     return new ObjectKeyImpl((ObjectKeyTemplate)localObject, localObjectIdImpl);
/*     */   }
/*     */ 
/*     */   public ObjectKeyTemplate createTemplate(InputStream paramInputStream)
/*     */   {
/* 234 */     Object localObject = create(paramInputStream, this.oktempOnly, null);
/* 235 */     if (localObject == null) {
/* 236 */       localObject = new WireObjectKeyTemplate(this.orb);
/*     */     }
/* 238 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectKeyFactoryImpl
 * JD-Core Version:    0.6.2
 */