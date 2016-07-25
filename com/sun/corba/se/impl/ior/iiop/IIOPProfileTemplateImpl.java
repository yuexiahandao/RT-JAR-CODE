/*     */ package com.sun.corba.se.impl.ior.iiop;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDROutputStream;
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.impl.ior.EncapsulationUtility;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfile;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfileTemplateBase;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public class IIOPProfileTemplateImpl extends TaggedProfileTemplateBase
/*     */   implements IIOPProfileTemplate
/*     */ {
/*     */   private ORB orb;
/*     */   private GIOPVersion giopVersion;
/*     */   private IIOPAddress primary;
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  70 */     if (!(paramObject instanceof IIOPProfileTemplateImpl)) {
/*  71 */       return false;
/*     */     }
/*  73 */     IIOPProfileTemplateImpl localIIOPProfileTemplateImpl = (IIOPProfileTemplateImpl)paramObject;
/*     */ 
/*  75 */     return (super.equals(paramObject)) && (this.giopVersion.equals(localIIOPProfileTemplateImpl.giopVersion)) && (this.primary.equals(localIIOPProfileTemplateImpl.primary));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  81 */     return super.hashCode() ^ this.giopVersion.hashCode() ^ this.primary.hashCode();
/*     */   }
/*     */ 
/*     */   public TaggedProfile create(ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId)
/*     */   {
/*  86 */     return IIOPFactories.makeIIOPProfile(this.orb, paramObjectKeyTemplate, paramObjectId, this);
/*     */   }
/*     */ 
/*     */   public GIOPVersion getGIOPVersion()
/*     */   {
/*  91 */     return this.giopVersion;
/*     */   }
/*     */ 
/*     */   public IIOPAddress getPrimaryAddress()
/*     */   {
/*  96 */     return this.primary;
/*     */   }
/*     */ 
/*     */   public IIOPProfileTemplateImpl(ORB paramORB, GIOPVersion paramGIOPVersion, IIOPAddress paramIIOPAddress)
/*     */   {
/* 101 */     this.orb = paramORB;
/* 102 */     this.giopVersion = paramGIOPVersion;
/* 103 */     this.primary = paramIIOPAddress;
/* 104 */     if (this.giopVersion.getMinor() == 0)
/*     */     {
/* 107 */       makeImmutable();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IIOPProfileTemplateImpl(InputStream paramInputStream) {
/* 112 */     byte b1 = paramInputStream.read_octet();
/* 113 */     byte b2 = paramInputStream.read_octet();
/* 114 */     this.giopVersion = GIOPVersion.getInstance(b1, b2);
/* 115 */     this.primary = new IIOPAddressImpl(paramInputStream);
/* 116 */     this.orb = ((ORB)paramInputStream.orb());
/*     */ 
/* 118 */     if (b2 > 0) {
/* 119 */       EncapsulationUtility.readIdentifiableSequence(this, this.orb.getTaggedComponentFactoryFinder(), paramInputStream);
/*     */     }
/*     */ 
/* 122 */     makeImmutable();
/*     */   }
/*     */ 
/*     */   public void write(ObjectKeyTemplate paramObjectKeyTemplate, ObjectId paramObjectId, OutputStream paramOutputStream)
/*     */   {
/* 127 */     this.giopVersion.write(paramOutputStream);
/* 128 */     this.primary.write(paramOutputStream);
/*     */ 
/* 135 */     EncapsOutputStream localEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB)paramOutputStream.orb(), ((CDROutputStream)paramOutputStream).isLittleEndian());
/*     */ 
/* 139 */     paramObjectKeyTemplate.write(paramObjectId, localEncapsOutputStream);
/* 140 */     EncapsulationUtility.writeOutputStream(localEncapsOutputStream, paramOutputStream);
/*     */ 
/* 142 */     if (this.giopVersion.getMinor() > 0)
/* 143 */       EncapsulationUtility.writeIdentifiableSequence(this, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void writeContents(OutputStream paramOutputStream)
/*     */   {
/* 150 */     this.giopVersion.write(paramOutputStream);
/* 151 */     this.primary.write(paramOutputStream);
/*     */ 
/* 153 */     if (this.giopVersion.getMinor() > 0)
/* 154 */       EncapsulationUtility.writeIdentifiableSequence(this, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public int getId()
/*     */   {
/* 159 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean isEquivalent(TaggedProfileTemplate paramTaggedProfileTemplate)
/*     */   {
/* 164 */     if (!(paramTaggedProfileTemplate instanceof IIOPProfileTemplateImpl)) {
/* 165 */       return false;
/*     */     }
/* 167 */     IIOPProfileTemplateImpl localIIOPProfileTemplateImpl = (IIOPProfileTemplateImpl)paramTaggedProfileTemplate;
/*     */ 
/* 169 */     return this.primary.equals(localIIOPProfileTemplateImpl.primary);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.IIOPProfileTemplateImpl
 * JD-Core Version:    0.6.2
 */