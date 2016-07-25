/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORFactory;
/*     */ import com.sun.corba.se.spi.ior.IORTemplateList;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.StreamableValue;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceFactory;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceFactoryHelper;
/*     */ 
/*     */ public class ObjectReferenceFactoryImpl extends ObjectReferenceProducerBase
/*     */   implements ObjectReferenceFactory, StreamableValue
/*     */ {
/*     */   private transient IORTemplateList iorTemplates;
/*     */   public static final String repositoryId = "IDL:com/sun/corba/se/impl/ior/ObjectReferenceFactoryImpl:1.0";
/*     */ 
/*     */   public ObjectReferenceFactoryImpl(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/*  68 */     super((ORB)paramInputStream.orb());
/*  69 */     _read(paramInputStream);
/*     */   }
/*     */ 
/*     */   public ObjectReferenceFactoryImpl(ORB paramORB, IORTemplateList paramIORTemplateList)
/*     */   {
/*  74 */     super(paramORB);
/*  75 */     this.iorTemplates = paramIORTemplateList;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  80 */     if (!(paramObject instanceof ObjectReferenceFactoryImpl)) {
/*  81 */       return false;
/*     */     }
/*  83 */     ObjectReferenceFactoryImpl localObjectReferenceFactoryImpl = (ObjectReferenceFactoryImpl)paramObject;
/*     */ 
/*  85 */     return (this.iorTemplates != null) && (this.iorTemplates.equals(localObjectReferenceFactoryImpl.iorTemplates));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  91 */     return this.iorTemplates.hashCode();
/*     */   }
/*     */ 
/*     */   public String[] _truncatable_ids()
/*     */   {
/* 104 */     return new String[] { "IDL:com/sun/corba/se/impl/ior/ObjectReferenceFactoryImpl:1.0" };
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 109 */     return ObjectReferenceFactoryHelper.type();
/*     */   }
/*     */ 
/*     */   public void _read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 117 */     org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream;
/*     */ 
/* 120 */     this.iorTemplates = IORFactories.makeIORTemplateList(localInputStream);
/*     */   }
/*     */ 
/*     */   public void _write(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*     */   {
/* 127 */     org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream;
/*     */ 
/* 130 */     this.iorTemplates.write(localOutputStream);
/*     */   }
/*     */ 
/*     */   public IORFactory getIORFactory()
/*     */   {
/* 135 */     return this.iorTemplates;
/*     */   }
/*     */ 
/*     */   public IORTemplateList getIORTemplateList()
/*     */   {
/* 140 */     return this.iorTemplates;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectReferenceFactoryImpl
 * JD-Core Version:    0.6.2
 */