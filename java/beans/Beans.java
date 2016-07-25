/*     */ package java.beans;
/*     */ 
/*     */ import com.sun.beans.finder.ClassFinder;
/*     */ import java.applet.Applet;
/*     */ import java.applet.AppletContext;
/*     */ import java.applet.AppletStub;
/*     */ import java.beans.beancontext.BeanContext;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class Beans
/*     */ {
/*     */   public static Object instantiate(ClassLoader paramClassLoader, String paramString)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  77 */     return instantiate(paramClassLoader, paramString, null, null);
/*     */   }
/*     */ 
/*     */   public static Object instantiate(ClassLoader paramClassLoader, String paramString, BeanContext paramBeanContext)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  98 */     return instantiate(paramClassLoader, paramString, paramBeanContext, null);
/*     */   }
/*     */ 
/*     */   public static Object instantiate(ClassLoader paramClassLoader, String paramString, BeanContext paramBeanContext, AppletInitializer paramAppletInitializer)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 156 */     Object localObject1 = null;
/* 157 */     Object localObject2 = null;
/* 158 */     int i = 0;
/* 159 */     Object localObject3 = null;
/*     */ 
/* 166 */     if (paramClassLoader == null) {
/*     */       try {
/* 168 */         paramClassLoader = ClassLoader.getSystemClassLoader();
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 176 */     String str1 = paramString.replace('.', '/').concat(".ser");
/*     */     InputStream localInputStream;
/* 177 */     if (paramClassLoader == null)
/* 178 */       localInputStream = ClassLoader.getSystemResourceAsStream(str1);
/*     */     else
/* 180 */       localInputStream = paramClassLoader.getResourceAsStream(str1);
/* 181 */     if (localInputStream != null)
/*     */       try {
/* 183 */         if (paramClassLoader == null)
/* 184 */           localObject1 = new ObjectInputStream(localInputStream);
/*     */         else {
/* 186 */           localObject1 = new ObjectInputStreamWithLoader(localInputStream, paramClassLoader);
/*     */         }
/* 188 */         localObject2 = ((ObjectInputStream)localObject1).readObject();
/* 189 */         i = 1;
/* 190 */         ((ObjectInputStream)localObject1).close();
/*     */       } catch (IOException localIOException) {
/* 192 */         localInputStream.close();
/*     */ 
/* 195 */         localObject3 = localIOException;
/*     */       } catch (ClassNotFoundException localClassNotFoundException1) {
/* 197 */         localInputStream.close();
/* 198 */         throw localClassNotFoundException1;
/*     */       }
/*     */     Object localObject4;
/* 202 */     if (localObject2 == null)
/*     */     {
/*     */       try
/*     */       {
/* 207 */         localObject4 = ClassFinder.findClass(paramString, paramClassLoader);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException2)
/*     */       {
/* 212 */         if (localObject3 != null) {
/* 213 */           throw localObject3;
/*     */         }
/* 215 */         throw localClassNotFoundException2;
/*     */       }
/*     */ 
/* 218 */       if (!Modifier.isPublic(((Class)localObject4).getModifiers())) {
/* 219 */         throw new ClassNotFoundException("" + localObject4 + " : no public access");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 227 */         localObject2 = ((Class)localObject4).newInstance();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 231 */         throw new ClassNotFoundException("" + localObject4 + " : " + localException, localException);
/*     */       }
/*     */     }
/*     */ 
/* 235 */     if (localObject2 != null)
/*     */     {
/* 239 */       localObject4 = null;
/*     */ 
/* 241 */       if ((localObject2 instanceof Applet)) {
/* 242 */         Applet localApplet = (Applet)localObject2;
/* 243 */         int j = paramAppletInitializer == null ? 1 : 0;
/*     */ 
/* 245 */         if (j != 0)
/*     */         {
/*     */           String str2;
/* 258 */           if (i != 0)
/*     */           {
/* 260 */             str2 = paramString.replace('.', '/').concat(".ser");
/*     */           }
/*     */           else {
/* 263 */             str2 = paramString.replace('.', '/').concat(".class");
/*     */           }
/*     */ 
/* 266 */           URL localURL1 = null;
/* 267 */           URL localURL2 = null;
/* 268 */           URL localURL3 = null;
/*     */ 
/* 271 */           if (paramClassLoader == null)
/* 272 */             localURL1 = ClassLoader.getSystemResource(str2);
/*     */           else {
/* 274 */             localURL1 = paramClassLoader.getResource(str2);
/*     */           }
/*     */ 
/* 284 */           if (localURL1 != null) {
/* 285 */             localObject5 = localURL1.toExternalForm();
/*     */ 
/* 287 */             if (((String)localObject5).endsWith(str2)) {
/* 288 */               int k = ((String)localObject5).length() - str2.length();
/* 289 */               localURL2 = new URL(((String)localObject5).substring(0, k));
/* 290 */               localURL3 = localURL2;
/*     */ 
/* 292 */               k = ((String)localObject5).lastIndexOf('/');
/*     */ 
/* 294 */               if (k >= 0) {
/* 295 */                 localURL3 = new URL(((String)localObject5).substring(0, k + 1));
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 301 */           Object localObject5 = new BeansAppletContext(localApplet);
/*     */ 
/* 303 */           localObject4 = new BeansAppletStub(localApplet, (AppletContext)localObject5, localURL2, localURL3);
/* 304 */           localApplet.setStub((AppletStub)localObject4);
/*     */         } else {
/* 306 */           paramAppletInitializer.initialize(localApplet, paramBeanContext);
/*     */         }
/*     */ 
/* 311 */         if (paramBeanContext != null) {
/* 312 */           paramBeanContext.add(localObject2);
/*     */         }
/*     */ 
/* 318 */         if (i == 0)
/*     */         {
/* 322 */           localApplet.setSize(100, 100);
/* 323 */           localApplet.init();
/*     */         }
/*     */ 
/* 326 */         if (j != 0)
/* 327 */           ((BeansAppletStub)localObject4).active = true;
/* 328 */         else paramAppletInitializer.activate(localApplet);
/*     */       }
/* 330 */       else if (paramBeanContext != null) { paramBeanContext.add(localObject2); }
/*     */ 
/*     */     }
/* 333 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public static Object getInstanceOf(Object paramObject, Class<?> paramClass)
/*     */   {
/* 353 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public static boolean isInstanceOf(Object paramObject, Class<?> paramClass)
/*     */   {
/* 368 */     return Introspector.isSubclass(paramObject.getClass(), paramClass);
/*     */   }
/*     */ 
/*     */   public static boolean isDesignTime()
/*     */   {
/* 381 */     return ThreadGroupContext.getContext().isDesignTime();
/*     */   }
/*     */ 
/*     */   public static boolean isGuiAvailable()
/*     */   {
/* 398 */     return ThreadGroupContext.getContext().isGuiAvailable();
/*     */   }
/*     */ 
/*     */   public static void setDesignTime(boolean paramBoolean)
/*     */     throws SecurityException
/*     */   {
/* 420 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 421 */     if (localSecurityManager != null) {
/* 422 */       localSecurityManager.checkPropertiesAccess();
/*     */     }
/* 424 */     ThreadGroupContext.getContext().setDesignTime(paramBoolean);
/*     */   }
/*     */ 
/*     */   public static void setGuiAvailable(boolean paramBoolean)
/*     */     throws SecurityException
/*     */   {
/* 446 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 447 */     if (localSecurityManager != null) {
/* 448 */       localSecurityManager.checkPropertiesAccess();
/*     */     }
/* 450 */     ThreadGroupContext.getContext().setGuiAvailable(paramBoolean);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.Beans
 * JD-Core Version:    0.6.2
 */