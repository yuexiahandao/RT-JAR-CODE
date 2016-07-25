/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.IOP.TaggedProfileHelper;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public class GenericTaggedProfile extends GenericIdentifiable
/*     */   implements com.sun.corba.se.spi.ior.TaggedProfile
/*     */ {
/*     */   private ORB orb;
/*     */ 
/*     */   public GenericTaggedProfile(int paramInt, InputStream paramInputStream)
/*     */   {
/*  51 */     super(paramInt, paramInputStream);
/*  52 */     this.orb = ((ORB)paramInputStream.orb());
/*     */   }
/*     */ 
/*     */   public GenericTaggedProfile(ORB paramORB, int paramInt, byte[] paramArrayOfByte)
/*     */   {
/*  57 */     super(paramInt, paramArrayOfByte);
/*  58 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   public TaggedProfileTemplate getTaggedProfileTemplate()
/*     */   {
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   public ObjectId getObjectId()
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   public ObjectKeyTemplate getObjectKeyTemplate()
/*     */   {
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   public ObjectKey getObjectKey()
/*     */   {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isEquivalent(com.sun.corba.se.spi.ior.TaggedProfile paramTaggedProfile)
/*     */   {
/*  83 */     return equals(paramTaggedProfile);
/*     */   }
/*     */ 
/*     */   public void makeImmutable()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isLocal()
/*     */   {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   public org.omg.IOP.TaggedProfile getIOPProfile()
/*     */   {
/*  98 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.orb);
/*     */ 
/* 100 */     write(localEncapsOutputStream);
/* 101 */     InputStream localInputStream = (InputStream)localEncapsOutputStream.create_input_stream();
/* 102 */     return TaggedProfileHelper.read(localInputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.GenericTaggedProfile
 * JD-Core Version:    0.6.2
 */