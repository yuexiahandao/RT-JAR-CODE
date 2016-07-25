/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORFactory;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.IORTemplateList;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.StreamableValue;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplateHelper;
/*     */ 
/*     */ public class ObjectReferenceTemplateImpl extends ObjectReferenceProducerBase
/*     */   implements ObjectReferenceTemplate, StreamableValue
/*     */ {
/*     */   private transient IORTemplate iorTemplate;
/*     */   public static final String repositoryId = "IDL:com/sun/corba/se/impl/ior/ObjectReferenceTemplateImpl:1.0";
/*     */ 
/*     */   public ObjectReferenceTemplateImpl(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/*  69 */     super((ORB)paramInputStream.orb());
/*  70 */     _read(paramInputStream);
/*     */   }
/*     */ 
/*     */   public ObjectReferenceTemplateImpl(ORB paramORB, IORTemplate paramIORTemplate)
/*     */   {
/*  75 */     super(paramORB);
/*  76 */     this.iorTemplate = paramIORTemplate;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  81 */     if (!(paramObject instanceof ObjectReferenceTemplateImpl)) {
/*  82 */       return false;
/*     */     }
/*  84 */     ObjectReferenceTemplateImpl localObjectReferenceTemplateImpl = (ObjectReferenceTemplateImpl)paramObject;
/*     */ 
/*  86 */     return (this.iorTemplate != null) && (this.iorTemplate.equals(localObjectReferenceTemplateImpl.iorTemplate));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  92 */     return this.iorTemplate.hashCode();
/*     */   }
/*     */ 
/*     */   public String[] _truncatable_ids()
/*     */   {
/* 105 */     return new String[] { "IDL:com/sun/corba/se/impl/ior/ObjectReferenceTemplateImpl:1.0" };
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 110 */     return ObjectReferenceTemplateHelper.type();
/*     */   }
/*     */ 
/*     */   public void _read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 118 */     org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream;
/*     */ 
/* 120 */     this.iorTemplate = IORFactories.makeIORTemplate(localInputStream);
/* 121 */     this.orb = ((ORB)localInputStream.orb());
/*     */   }
/*     */ 
/*     */   public void _write(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*     */   {
/* 128 */     org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream;
/*     */ 
/* 131 */     this.iorTemplate.write(localOutputStream);
/*     */   }
/*     */ 
/*     */   public String server_id()
/*     */   {
/* 136 */     int i = this.iorTemplate.getObjectKeyTemplate().getServerId();
/* 137 */     return Integer.toString(i);
/*     */   }
/*     */ 
/*     */   public String orb_id()
/*     */   {
/* 142 */     return this.iorTemplate.getObjectKeyTemplate().getORBId();
/*     */   }
/*     */ 
/*     */   public String[] adapter_name()
/*     */   {
/* 147 */     ObjectAdapterId localObjectAdapterId = this.iorTemplate.getObjectKeyTemplate().getObjectAdapterId();
/*     */ 
/* 150 */     return localObjectAdapterId.getAdapterName();
/*     */   }
/*     */ 
/*     */   public IORFactory getIORFactory()
/*     */   {
/* 155 */     return this.iorTemplate;
/*     */   }
/*     */ 
/*     */   public IORTemplateList getIORTemplateList()
/*     */   {
/* 160 */     IORTemplateList localIORTemplateList = IORFactories.makeIORTemplateList();
/* 161 */     localIORTemplateList.add(this.iorTemplate);
/* 162 */     localIORTemplateList.makeImmutable();
/* 163 */     return localIORTemplateList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectReferenceTemplateImpl
 * JD-Core Version:    0.6.2
 */