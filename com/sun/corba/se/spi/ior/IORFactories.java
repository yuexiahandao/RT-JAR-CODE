/*     */ package com.sun.corba.se.spi.ior;
/*     */ 
/*     */ import com.sun.corba.se.impl.ior.IORImpl;
/*     */ import com.sun.corba.se.impl.ior.IORTemplateImpl;
/*     */ import com.sun.corba.se.impl.ior.IORTemplateListImpl;
/*     */ import com.sun.corba.se.impl.ior.ObjectIdImpl;
/*     */ import com.sun.corba.se.impl.ior.ObjectKeyFactoryImpl;
/*     */ import com.sun.corba.se.impl.ior.ObjectKeyImpl;
/*     */ import com.sun.corba.se.impl.ior.ObjectReferenceFactoryImpl;
/*     */ import com.sun.corba.se.impl.ior.ObjectReferenceProducerBase;
/*     */ import com.sun.corba.se.impl.ior.ObjectReferenceTemplateImpl;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.Serializable;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.portable.ValueFactory;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceFactory;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*     */ 
/*     */ public class IORFactories
/*     */ {
/*     */   public static ObjectId makeObjectId(byte[] paramArrayOfByte)
/*     */   {
/*  69 */     return new ObjectIdImpl(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public static ObjectKey makeObjectKey(ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId)
/*     */   {
/*  77 */     return new ObjectKeyImpl(paramObjectKeyTemplate, paramObjectId);
/*     */   }
/*     */ 
/*     */   public static IOR makeIOR(ORB paramORB, String paramString)
/*     */   {
/*  84 */     return new IORImpl(paramORB, paramString);
/*     */   }
/*     */ 
/*     */   public static IOR makeIOR(ORB paramORB)
/*     */   {
/*  91 */     return new IORImpl(paramORB);
/*     */   }
/*     */ 
/*     */   public static IOR makeIOR(InputStream paramInputStream)
/*     */   {
/*  98 */     return new IORImpl(paramInputStream);
/*     */   }
/*     */ 
/*     */   public static IORTemplate makeIORTemplate(ObjectKeyTemplate paramObjectKeyTemplate)
/*     */   {
/* 106 */     return new IORTemplateImpl(paramObjectKeyTemplate);
/*     */   }
/*     */ 
/*     */   public static IORTemplate makeIORTemplate(InputStream paramInputStream)
/*     */   {
/* 113 */     return new IORTemplateImpl(paramInputStream);
/*     */   }
/*     */ 
/*     */   public static IORTemplateList makeIORTemplateList()
/*     */   {
/* 118 */     return new IORTemplateListImpl();
/*     */   }
/*     */ 
/*     */   public static IORTemplateList makeIORTemplateList(InputStream paramInputStream)
/*     */   {
/* 123 */     return new IORTemplateListImpl(paramInputStream);
/*     */   }
/*     */ 
/*     */   public static IORFactory getIORFactory(ObjectReferenceTemplate paramObjectReferenceTemplate)
/*     */   {
/* 128 */     if ((paramObjectReferenceTemplate instanceof ObjectReferenceTemplateImpl)) {
/* 129 */       ObjectReferenceTemplateImpl localObjectReferenceTemplateImpl = (ObjectReferenceTemplateImpl)paramObjectReferenceTemplate;
/*     */ 
/* 131 */       return localObjectReferenceTemplateImpl.getIORFactory();
/*     */     }
/*     */ 
/* 134 */     throw new BAD_PARAM();
/*     */   }
/*     */ 
/*     */   public static IORTemplateList getIORTemplateList(ObjectReferenceFactory paramObjectReferenceFactory)
/*     */   {
/* 139 */     if ((paramObjectReferenceFactory instanceof ObjectReferenceProducerBase)) {
/* 140 */       ObjectReferenceProducerBase localObjectReferenceProducerBase = (ObjectReferenceProducerBase)paramObjectReferenceFactory;
/*     */ 
/* 142 */       return localObjectReferenceProducerBase.getIORTemplateList();
/*     */     }
/*     */ 
/* 145 */     throw new BAD_PARAM();
/*     */   }
/*     */ 
/*     */   public static ObjectReferenceTemplate makeObjectReferenceTemplate(ORB paramORB, IORTemplate paramIORTemplate)
/*     */   {
/* 151 */     return new ObjectReferenceTemplateImpl(paramORB, paramIORTemplate);
/*     */   }
/*     */ 
/*     */   public static ObjectReferenceFactory makeObjectReferenceFactory(ORB paramORB, IORTemplateList paramIORTemplateList)
/*     */   {
/* 157 */     return new ObjectReferenceFactoryImpl(paramORB, paramIORTemplateList);
/*     */   }
/*     */ 
/*     */   public static ObjectKeyFactory makeObjectKeyFactory(ORB paramORB)
/*     */   {
/* 162 */     return new ObjectKeyFactoryImpl(paramORB);
/*     */   }
/*     */ 
/*     */   public static IOR getIOR(org.omg.CORBA.Object paramObject)
/*     */   {
/* 167 */     return ORBUtility.getIOR(paramObject);
/*     */   }
/*     */ 
/*     */   public static org.omg.CORBA.Object makeObjectReference(IOR paramIOR)
/*     */   {
/* 172 */     return ORBUtility.makeObjectReference(paramIOR);
/*     */   }
/*     */ 
/*     */   public static void registerValueFactories(ORB paramORB)
/*     */   {
/* 183 */     java.lang.Object localObject = new ValueFactory()
/*     */     {
/*     */       public Serializable read_value(InputStream paramAnonymousInputStream) {
/* 186 */         return new ObjectReferenceTemplateImpl(paramAnonymousInputStream);
/*     */       }
/*     */     };
/* 190 */     paramORB.register_value_factory("IDL:com/sun/corba/se/impl/ior/ObjectReferenceTemplateImpl:1.0", (ValueFactory)localObject);
/*     */ 
/* 194 */     localObject = new ValueFactory()
/*     */     {
/*     */       public Serializable read_value(InputStream paramAnonymousInputStream) {
/* 197 */         return new ObjectReferenceFactoryImpl(paramAnonymousInputStream);
/*     */       }
/*     */     };
/* 201 */     paramORB.register_value_factory("IDL:com/sun/corba/se/impl/ior/ObjectReferenceFactoryImpl:1.0", (ValueFactory)localObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.IORFactories
 * JD-Core Version:    0.6.2
 */