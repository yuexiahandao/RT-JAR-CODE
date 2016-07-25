/*     */ package com.sun.corba.se.impl.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.Operation;
/*     */ import com.sun.corba.se.spi.orb.StringPair;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PrefixParserAction extends ParserActionBase
/*     */ {
/*     */   private Class componentType;
/*     */   private ORBUtilSystemException wrapper;
/*     */ 
/*     */   public PrefixParserAction(String paramString1, Operation paramOperation, String paramString2, Class paramClass)
/*     */   {
/*  51 */     super(paramString1, true, paramOperation, paramString2);
/*  52 */     this.componentType = paramClass;
/*  53 */     this.wrapper = ORBUtilSystemException.get("orb.lifecycle");
/*     */   }
/*     */ 
/*     */   public Object apply(Properties paramProperties)
/*     */   {
/*  65 */     String str1 = getPropertyName();
/*  66 */     int i = str1.length();
/*  67 */     if (str1.charAt(i - 1) != '.') {
/*  68 */       str1 = str1 + '.';
/*  69 */       i++;
/*     */     }
/*     */ 
/*  72 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/*  75 */     Iterator localIterator1 = paramProperties.keySet().iterator();
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  76 */     while (localIterator1.hasNext()) {
/*  77 */       String str2 = (String)localIterator1.next();
/*  78 */       if (str2.startsWith(str1)) {
/*  79 */         localObject1 = str2.substring(i);
/*  80 */         String str3 = paramProperties.getProperty(str2);
/*  81 */         StringPair localStringPair = new StringPair((String)localObject1, str3);
/*  82 */         localObject2 = getOperation().operate(localStringPair);
/*  83 */         localLinkedList.add(localObject2);
/*     */       }
/*     */     }
/*     */ 
/*  87 */     int j = localLinkedList.size();
/*  88 */     if (j > 0)
/*     */     {
/*  93 */       localObject1 = null;
/*     */       try {
/*  95 */         localObject1 = Array.newInstance(this.componentType, j);
/*     */       } catch (Throwable localThrowable1) {
/*  97 */         throw this.wrapper.couldNotCreateArray(localThrowable1, getPropertyName(), this.componentType, new Integer(j));
/*     */       }
/*     */ 
/* 102 */       Iterator localIterator2 = localLinkedList.iterator();
/* 103 */       int k = 0;
/* 104 */       while (localIterator2.hasNext()) {
/* 105 */         localObject2 = localIterator2.next();
/*     */         try
/*     */         {
/* 108 */           Array.set(localObject1, k, localObject2);
/*     */         } catch (Throwable localThrowable2) {
/* 110 */           throw this.wrapper.couldNotSetArray(localThrowable2, getPropertyName(), new Integer(k), this.componentType, new Integer(j), localObject2.toString());
/*     */         }
/*     */ 
/* 115 */         k++;
/*     */       }
/*     */ 
/* 118 */       return localObject1;
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.PrefixParserAction
 * JD-Core Version:    0.6.2
 */