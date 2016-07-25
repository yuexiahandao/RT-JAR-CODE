/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORFactory;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.IORTemplateList;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public class IORTemplateListImpl extends FreezableList
/*     */   implements IORTemplateList
/*     */ {
/*     */   public Object set(int paramInt, Object paramObject)
/*     */   {
/*  52 */     if ((paramObject instanceof IORTemplate))
/*  53 */       return super.set(paramInt, paramObject);
/*  54 */     if ((paramObject instanceof IORTemplateList)) {
/*  55 */       Object localObject = remove(paramInt);
/*  56 */       add(paramInt, paramObject);
/*  57 */       return localObject;
/*     */     }
/*  59 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Object paramObject)
/*     */   {
/*  64 */     if ((paramObject instanceof IORTemplate)) {
/*  65 */       super.add(paramInt, paramObject);
/*  66 */     } else if ((paramObject instanceof IORTemplateList)) {
/*  67 */       IORTemplateList localIORTemplateList = (IORTemplateList)paramObject;
/*  68 */       addAll(paramInt, localIORTemplateList);
/*     */     } else {
/*  70 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IORTemplateListImpl() {
/*  75 */     super(new ArrayList());
/*     */   }
/*     */ 
/*     */   public IORTemplateListImpl(InputStream paramInputStream)
/*     */   {
/*  80 */     this();
/*  81 */     int i = paramInputStream.read_long();
/*  82 */     for (int j = 0; j < i; j++) {
/*  83 */       IORTemplate localIORTemplate = IORFactories.makeIORTemplate(paramInputStream);
/*  84 */       add(localIORTemplate);
/*     */     }
/*     */ 
/*  87 */     makeImmutable();
/*     */   }
/*     */ 
/*     */   public void makeImmutable()
/*     */   {
/*  92 */     makeElementsImmutable();
/*  93 */     super.makeImmutable();
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/*  98 */     paramOutputStream.write_long(size());
/*  99 */     Iterator localIterator = iterator();
/* 100 */     while (localIterator.hasNext()) {
/* 101 */       IORTemplate localIORTemplate = (IORTemplate)localIterator.next();
/* 102 */       localIORTemplate.write(paramOutputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public IOR makeIOR(ORB paramORB, String paramString, ObjectId paramObjectId)
/*     */   {
/* 108 */     return new IORImpl(paramORB, paramString, this, paramObjectId);
/*     */   }
/*     */ 
/*     */   public boolean isEquivalent(IORFactory paramIORFactory)
/*     */   {
/* 113 */     if (!(paramIORFactory instanceof IORTemplateList)) {
/* 114 */       return false;
/*     */     }
/* 116 */     IORTemplateList localIORTemplateList = (IORTemplateList)paramIORFactory;
/*     */ 
/* 118 */     Iterator localIterator1 = iterator();
/* 119 */     Iterator localIterator2 = localIORTemplateList.iterator();
/* 120 */     while ((localIterator1.hasNext()) && (localIterator2.hasNext())) {
/* 121 */       IORTemplate localIORTemplate1 = (IORTemplate)localIterator1.next();
/* 122 */       IORTemplate localIORTemplate2 = (IORTemplate)localIterator2.next();
/* 123 */       if (!localIORTemplate1.isEquivalent(localIORTemplate2)) {
/* 124 */         return false;
/*     */       }
/*     */     }
/* 127 */     return localIterator1.hasNext() == localIterator2.hasNext();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.IORTemplateListImpl
 * JD-Core Version:    0.6.2
 */