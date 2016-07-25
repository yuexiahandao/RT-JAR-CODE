/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactory;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableContainerBase;
/*     */ import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public class IORTemplateImpl extends IdentifiableContainerBase
/*     */   implements IORTemplate
/*     */ {
/*     */   private ObjectKeyTemplate oktemp;
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  58 */     if (paramObject == null) {
/*  59 */       return false;
/*     */     }
/*  61 */     if (!(paramObject instanceof IORTemplateImpl)) {
/*  62 */       return false;
/*     */     }
/*  64 */     IORTemplateImpl localIORTemplateImpl = (IORTemplateImpl)paramObject;
/*     */ 
/*  66 */     return (super.equals(paramObject)) && (this.oktemp.equals(localIORTemplateImpl.getObjectKeyTemplate()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  71 */     return super.hashCode() ^ this.oktemp.hashCode();
/*     */   }
/*     */ 
/*     */   public ObjectKeyTemplate getObjectKeyTemplate()
/*     */   {
/*  76 */     return this.oktemp;
/*     */   }
/*     */ 
/*     */   public IORTemplateImpl(ObjectKeyTemplate paramObjectKeyTemplate)
/*     */   {
/*  81 */     this.oktemp = paramObjectKeyTemplate;
/*     */   }
/*     */ 
/*     */   public IOR makeIOR(ORB paramORB, String paramString, ObjectId paramObjectId)
/*     */   {
/*  86 */     return new IORImpl(paramORB, paramString, this, paramObjectId);
/*     */   }
/*     */ 
/*     */   public boolean isEquivalent(IORFactory paramIORFactory)
/*     */   {
/*  91 */     if (!(paramIORFactory instanceof IORTemplate)) {
/*  92 */       return false;
/*     */     }
/*  94 */     IORTemplate localIORTemplate = (IORTemplate)paramIORFactory;
/*     */ 
/*  96 */     Iterator localIterator1 = iterator();
/*  97 */     Iterator localIterator2 = localIORTemplate.iterator();
/*  98 */     while ((localIterator1.hasNext()) && (localIterator2.hasNext())) {
/*  99 */       TaggedProfileTemplate localTaggedProfileTemplate1 = (TaggedProfileTemplate)localIterator1.next();
/*     */ 
/* 101 */       TaggedProfileTemplate localTaggedProfileTemplate2 = (TaggedProfileTemplate)localIterator2.next();
/*     */ 
/* 103 */       if (!localTaggedProfileTemplate1.isEquivalent(localTaggedProfileTemplate2)) {
/* 104 */         return false;
/*     */       }
/*     */     }
/* 107 */     return (localIterator1.hasNext() == localIterator2.hasNext()) && (getObjectKeyTemplate().equals(localIORTemplate.getObjectKeyTemplate()));
/*     */   }
/*     */ 
/*     */   public void makeImmutable()
/*     */   {
/* 117 */     makeElementsImmutable();
/* 118 */     super.makeImmutable();
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/* 123 */     this.oktemp.write(paramOutputStream);
/* 124 */     EncapsulationUtility.writeIdentifiableSequence(this, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public IORTemplateImpl(InputStream paramInputStream)
/*     */   {
/* 129 */     ORB localORB = (ORB)paramInputStream.orb();
/* 130 */     IdentifiableFactoryFinder localIdentifiableFactoryFinder = localORB.getTaggedProfileTemplateFactoryFinder();
/*     */ 
/* 133 */     this.oktemp = localORB.getObjectKeyFactory().createTemplate(paramInputStream);
/* 134 */     EncapsulationUtility.readIdentifiableSequence(this, localIdentifiableFactoryFinder, paramInputStream);
/*     */ 
/* 136 */     makeImmutable();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.IORTemplateImpl
 * JD-Core Version:    0.6.2
 */