/*     */ package com.sun.corba.se.spi.ior.iiop;
/*     */ 
/*     */ import com.sun.corba.se.impl.ior.iiop.AlternateIIOPAddressComponentImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.CodeSetsComponentImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.IIOPAddressImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.IIOPProfileImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.IIOPProfileTemplateImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.JavaCodebaseComponentImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.JavaSerializationComponent;
/*     */ import com.sun.corba.se.impl.ior.iiop.MaxStreamFormatVersionComponentImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.ORBTypeComponentImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.RequestPartitioningComponentImpl;
/*     */ import com.sun.corba.se.spi.ior.EncapsulationFactoryBase;
/*     */ import com.sun.corba.se.spi.ior.Identifiable;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableFactory;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.IOP.TaggedProfile;
/*     */ 
/*     */ public abstract class IIOPFactories
/*     */ {
/*     */   public static IdentifiableFactory makeRequestPartitioningComponentFactory()
/*     */   {
/*  74 */     return new EncapsulationFactoryBase(1398099457)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/*  77 */         int i = paramAnonymousInputStream.read_ulong();
/*  78 */         RequestPartitioningComponentImpl localRequestPartitioningComponentImpl = new RequestPartitioningComponentImpl(i);
/*     */ 
/*  80 */         return localRequestPartitioningComponentImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static RequestPartitioningComponent makeRequestPartitioningComponent(int paramInt)
/*     */   {
/*  88 */     return new RequestPartitioningComponentImpl(paramInt);
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeAlternateIIOPAddressComponentFactory()
/*     */   {
/*  93 */     return new EncapsulationFactoryBase(3)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/*  96 */         IIOPAddressImpl localIIOPAddressImpl = new IIOPAddressImpl(paramAnonymousInputStream);
/*  97 */         AlternateIIOPAddressComponentImpl localAlternateIIOPAddressComponentImpl = new AlternateIIOPAddressComponentImpl(localIIOPAddressImpl);
/*     */ 
/*  99 */         return localAlternateIIOPAddressComponentImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static AlternateIIOPAddressComponent makeAlternateIIOPAddressComponent(IIOPAddress paramIIOPAddress)
/*     */   {
/* 107 */     return new AlternateIIOPAddressComponentImpl(paramIIOPAddress);
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeCodeSetsComponentFactory()
/*     */   {
/* 112 */     return new EncapsulationFactoryBase(1)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 115 */         return new CodeSetsComponentImpl(paramAnonymousInputStream);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static CodeSetsComponent makeCodeSetsComponent(ORB paramORB)
/*     */   {
/* 122 */     return new CodeSetsComponentImpl(paramORB);
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeJavaCodebaseComponentFactory()
/*     */   {
/* 127 */     return new EncapsulationFactoryBase(25)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 130 */         String str = paramAnonymousInputStream.read_string();
/* 131 */         JavaCodebaseComponentImpl localJavaCodebaseComponentImpl = new JavaCodebaseComponentImpl(str);
/* 132 */         return localJavaCodebaseComponentImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static JavaCodebaseComponent makeJavaCodebaseComponent(String paramString)
/*     */   {
/* 140 */     return new JavaCodebaseComponentImpl(paramString);
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeORBTypeComponentFactory()
/*     */   {
/* 145 */     return new EncapsulationFactoryBase(0)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 148 */         int i = paramAnonymousInputStream.read_ulong();
/* 149 */         ORBTypeComponentImpl localORBTypeComponentImpl = new ORBTypeComponentImpl(i);
/* 150 */         return localORBTypeComponentImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static ORBTypeComponent makeORBTypeComponent(int paramInt)
/*     */   {
/* 157 */     return new ORBTypeComponentImpl(paramInt);
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeMaxStreamFormatVersionComponentFactory()
/*     */   {
/* 162 */     return new EncapsulationFactoryBase(38)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 165 */         byte b = paramAnonymousInputStream.read_octet();
/* 166 */         MaxStreamFormatVersionComponentImpl localMaxStreamFormatVersionComponentImpl = new MaxStreamFormatVersionComponentImpl(b);
/* 167 */         return localMaxStreamFormatVersionComponentImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static MaxStreamFormatVersionComponent makeMaxStreamFormatVersionComponent()
/*     */   {
/* 174 */     return new MaxStreamFormatVersionComponentImpl();
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeJavaSerializationComponentFactory() {
/* 178 */     return new EncapsulationFactoryBase(1398099458)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 181 */         byte b = paramAnonymousInputStream.read_octet();
/* 182 */         JavaSerializationComponent localJavaSerializationComponent = new JavaSerializationComponent(b);
/* 183 */         return localJavaSerializationComponent;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static JavaSerializationComponent makeJavaSerializationComponent() {
/* 189 */     return JavaSerializationComponent.singleton();
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeIIOPProfileFactory()
/*     */   {
/* 194 */     return new EncapsulationFactoryBase(0)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 197 */         IIOPProfileImpl localIIOPProfileImpl = new IIOPProfileImpl(paramAnonymousInputStream);
/* 198 */         return localIIOPProfileImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static IIOPProfile makeIIOPProfile(ORB paramORB, ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId, IIOPProfileTemplate paramIIOPProfileTemplate)
/*     */   {
/* 206 */     return new IIOPProfileImpl(paramORB, paramObjectKeyTemplate, paramObjectId, paramIIOPProfileTemplate);
/*     */   }
/*     */ 
/*     */   public static IIOPProfile makeIIOPProfile(ORB paramORB, TaggedProfile paramTaggedProfile)
/*     */   {
/* 212 */     return new IIOPProfileImpl(paramORB, paramTaggedProfile);
/*     */   }
/*     */ 
/*     */   public static IdentifiableFactory makeIIOPProfileTemplateFactory()
/*     */   {
/* 217 */     return new EncapsulationFactoryBase(0)
/*     */     {
/*     */       public Identifiable readContents(InputStream paramAnonymousInputStream) {
/* 220 */         IIOPProfileTemplateImpl localIIOPProfileTemplateImpl = new IIOPProfileTemplateImpl(paramAnonymousInputStream);
/* 221 */         return localIIOPProfileTemplateImpl;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static IIOPProfileTemplate makeIIOPProfileTemplate(ORB paramORB, GIOPVersion paramGIOPVersion, IIOPAddress paramIIOPAddress)
/*     */   {
/* 229 */     return new IIOPProfileTemplateImpl(paramORB, paramGIOPVersion, paramIIOPAddress);
/*     */   }
/*     */ 
/*     */   public static IIOPAddress makeIIOPAddress(ORB paramORB, String paramString, int paramInt)
/*     */   {
/* 234 */     return new IIOPAddressImpl(paramORB, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public static IIOPAddress makeIIOPAddress(InputStream paramInputStream)
/*     */   {
/* 239 */     return new IIOPAddressImpl(paramInputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.ior.iiop.IIOPFactories
 * JD-Core Version:    0.6.2
 */