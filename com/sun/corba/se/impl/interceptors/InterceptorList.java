/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.InterceptorsSystemException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.omg.PortableInterceptor.ClientRequestInterceptor;
/*     */ import org.omg.PortableInterceptor.IORInterceptor;
/*     */ import org.omg.PortableInterceptor.Interceptor;
/*     */ import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
/*     */ import org.omg.PortableInterceptor.ServerRequestInterceptor;
/*     */ 
/*     */ public class InterceptorList
/*     */ {
/*     */   static final int INTERCEPTOR_TYPE_CLIENT = 0;
/*     */   static final int INTERCEPTOR_TYPE_SERVER = 1;
/*     */   static final int INTERCEPTOR_TYPE_IOR = 2;
/*     */   static final int NUM_INTERCEPTOR_TYPES = 3;
/*  66 */   static final Class[] classTypes = { ClientRequestInterceptor.class, ServerRequestInterceptor.class, IORInterceptor.class };
/*     */ 
/*  73 */   private boolean locked = false;
/*     */   private InterceptorsSystemException wrapper;
/*  80 */   private Interceptor[][] interceptors = new Interceptor[3][];
/*     */ 
/*     */   InterceptorList(InterceptorsSystemException paramInterceptorsSystemException)
/*     */   {
/*  88 */     this.wrapper = paramInterceptorsSystemException;
/*     */ 
/*  90 */     initInterceptorArrays();
/*     */   }
/*     */ 
/*     */   void register_interceptor(Interceptor paramInterceptor, int paramInt)
/*     */     throws DuplicateName
/*     */   {
/* 109 */     if (this.locked) {
/* 110 */       throw this.wrapper.interceptorListLocked();
/*     */     }
/*     */ 
/* 114 */     String str = paramInterceptor.name();
/* 115 */     boolean bool = str.equals("");
/* 116 */     int i = 0;
/* 117 */     Interceptor[] arrayOfInterceptor = this.interceptors[paramInt];
/*     */ 
/* 121 */     if (!bool) {
/* 122 */       int j = arrayOfInterceptor.length;
/*     */ 
/* 126 */       for (int k = 0; k < j; k++) {
/* 127 */         Interceptor localInterceptor = arrayOfInterceptor[k];
/* 128 */         if (localInterceptor.name().equals(str)) {
/* 129 */           i = 1;
/* 130 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 135 */     if (i == 0) {
/* 136 */       growInterceptorArray(paramInt);
/* 137 */       this.interceptors[paramInt][(this.interceptors[paramInt].length - 1)] = paramInterceptor;
/*     */     }
/*     */     else {
/* 140 */       throw new DuplicateName(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   void lock()
/*     */   {
/* 150 */     this.locked = true;
/*     */   }
/*     */ 
/*     */   Interceptor[] getInterceptors(int paramInt)
/*     */   {
/* 158 */     return this.interceptors[paramInt];
/*     */   }
/*     */ 
/*     */   boolean hasInterceptorsOfType(int paramInt)
/*     */   {
/* 166 */     return this.interceptors[paramInt].length > 0;
/*     */   }
/*     */ 
/*     */   private void initInterceptorArrays()
/*     */   {
/* 174 */     for (int i = 0; i < 3; i++) {
/* 175 */       Class localClass = classTypes[i];
/*     */ 
/* 178 */       this.interceptors[i] = ((Interceptor[])(Interceptor[])Array.newInstance(localClass, 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void growInterceptorArray(int paramInt)
/*     */   {
/* 187 */     Class localClass = classTypes[paramInt];
/* 188 */     int i = this.interceptors[paramInt].length;
/*     */ 
/* 193 */     Interceptor[] arrayOfInterceptor = (Interceptor[])Array.newInstance(localClass, i + 1);
/*     */ 
/* 195 */     System.arraycopy(this.interceptors[paramInt], 0, arrayOfInterceptor, 0, i);
/*     */ 
/* 197 */     this.interceptors[paramInt] = arrayOfInterceptor;
/*     */   }
/*     */ 
/*     */   void destroyAll()
/*     */   {
/* 205 */     int i = this.interceptors.length;
/*     */ 
/* 207 */     for (int j = 0; j < i; j++) {
/* 208 */       int k = this.interceptors[j].length;
/* 209 */       for (int m = 0; m < k; m++)
/* 210 */         this.interceptors[j][m].destroy();
/*     */     }
/*     */   }
/*     */ 
/*     */   void sortInterceptors()
/*     */   {
/* 219 */     ArrayList localArrayList1 = null;
/* 220 */     ArrayList localArrayList2 = null;
/*     */ 
/* 222 */     int i = this.interceptors.length;
/*     */ 
/* 224 */     for (int j = 0; j < i; j++) {
/* 225 */       int k = this.interceptors[j].length;
/* 226 */       if (k > 0)
/*     */       {
/* 228 */         localArrayList1 = new ArrayList();
/* 229 */         localArrayList2 = new ArrayList();
/*     */       }
/*     */       Object localObject;
/* 231 */       for (int m = 0; m < k; m++) {
/* 232 */         localObject = this.interceptors[j][m];
/* 233 */         if ((localObject instanceof Comparable))
/* 234 */           localArrayList1.add(localObject);
/*     */         else {
/* 236 */           localArrayList2.add(localObject);
/*     */         }
/*     */       }
/* 239 */       if ((k > 0) && (localArrayList1.size() > 0))
/*     */       {
/* 243 */         Collections.sort(localArrayList1);
/* 244 */         Iterator localIterator = localArrayList1.iterator();
/* 245 */         localObject = localArrayList2.iterator();
/* 246 */         for (int n = 0; n < k; n++)
/* 247 */           if (localIterator.hasNext()) {
/* 248 */             this.interceptors[j][n] = ((Interceptor)localIterator.next());
/*     */           }
/* 250 */           else if (((Iterator)localObject).hasNext()) {
/* 251 */             this.interceptors[j][n] = ((Interceptor)((Iterator)localObject).next());
/*     */           }
/*     */           else
/* 254 */             throw this.wrapper.sortSizeMismatch();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.InterceptorList
 * JD-Core Version:    0.6.2
 */