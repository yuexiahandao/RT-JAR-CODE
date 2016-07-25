/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.UnexpectedException;
/*     */ import org.omg.CORBA.UserException;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ 
/*     */ public class ExceptionHandlerImpl
/*     */   implements ExceptionHandler
/*     */ {
/*     */   private ExceptionRW[] rws;
/*     */   private final ORBUtilSystemException wrapper;
/*     */ 
/*     */   public ExceptionHandlerImpl(Class[] paramArrayOfClass)
/*     */   {
/* 183 */     this.wrapper = ORBUtilSystemException.get("rpc.presentation");
/*     */ 
/* 186 */     int i = 0;
/* 187 */     for (int j = 0; j < paramArrayOfClass.length; j++) {
/* 188 */       Class localClass1 = paramArrayOfClass[j];
/* 189 */       if (!RemoteException.class.isAssignableFrom(localClass1)) {
/* 190 */         i++;
/*     */       }
/*     */     }
/* 193 */     this.rws = new ExceptionRW[i];
/*     */ 
/* 195 */     j = 0;
/* 196 */     for (int k = 0; k < paramArrayOfClass.length; k++) {
/* 197 */       Class localClass2 = paramArrayOfClass[k];
/* 198 */       if (!RemoteException.class.isAssignableFrom(localClass2)) {
/* 199 */         Object localObject = null;
/* 200 */         if (UserException.class.isAssignableFrom(localClass2))
/* 201 */           localObject = new ExceptionRWIDLImpl(localClass2);
/*     */         else {
/* 203 */           localObject = new ExceptionRWRMIImpl(localClass2);
/*     */         }
/*     */ 
/* 224 */         this.rws[(j++)] = localObject;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int findDeclaredException(Class paramClass)
/*     */   {
/* 231 */     for (int i = 0; i < this.rws.length; i++) {
/* 232 */       Class localClass = this.rws[i].getExceptionClass();
/* 233 */       if (localClass.isAssignableFrom(paramClass)) {
/* 234 */         return i;
/*     */       }
/*     */     }
/* 237 */     return -1;
/*     */   }
/*     */ 
/*     */   private int findDeclaredException(String paramString)
/*     */   {
/* 242 */     for (int i = 0; i < this.rws.length; i++)
/*     */     {
/* 245 */       if (this.rws[i] == null) {
/* 246 */         return -1;
/*     */       }
/* 248 */       String str = this.rws[i].getId();
/* 249 */       if (paramString.equals(str)) {
/* 250 */         return i;
/*     */       }
/*     */     }
/* 253 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isDeclaredException(Class paramClass)
/*     */   {
/* 258 */     return findDeclaredException(paramClass) >= 0;
/*     */   }
/*     */ 
/*     */   public void writeException(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, Exception paramException)
/*     */   {
/* 263 */     int i = findDeclaredException(paramException.getClass());
/* 264 */     if (i < 0) {
/* 265 */       throw this.wrapper.writeUndeclaredException(paramException, paramException.getClass().getName());
/*     */     }
/*     */ 
/* 268 */     this.rws[i].write(paramOutputStream, paramException);
/*     */   }
/*     */ 
/*     */   public Exception readException(ApplicationException paramApplicationException)
/*     */   {
/* 277 */     org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramApplicationException.getInputStream();
/* 278 */     String str = paramApplicationException.getId();
/* 279 */     int i = findDeclaredException(str);
/* 280 */     if (i < 0) {
/* 281 */       str = localInputStream.read_string();
/* 282 */       UnexpectedException localUnexpectedException = new UnexpectedException(str);
/* 283 */       localUnexpectedException.initCause(paramApplicationException);
/* 284 */       return localUnexpectedException;
/*     */     }
/*     */ 
/* 287 */     return this.rws[i].read(localInputStream);
/*     */   }
/*     */ 
/*     */   public ExceptionRW getRMIExceptionRW(Class paramClass)
/*     */   {
/* 293 */     return new ExceptionRWRMIImpl(paramClass);
/*     */   }
/*     */ 
/*     */   public static abstract interface ExceptionRW
/*     */   {
/*     */     public abstract Class getExceptionClass();
/*     */ 
/*     */     public abstract String getId();
/*     */ 
/*     */     public abstract void write(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, Exception paramException);
/*     */ 
/*     */     public abstract Exception read(org.omg.CORBA_2_3.portable.InputStream paramInputStream);
/*     */   }
/*     */ 
/*     */   public abstract class ExceptionRWBase
/*     */     implements ExceptionHandlerImpl.ExceptionRW
/*     */   {
/*     */     private Class cls;
/*     */     private String id;
/*     */ 
/*     */     public ExceptionRWBase(Class arg2)
/*     */     {
/*     */       Object localObject;
/*  77 */       this.cls = localObject;
/*     */     }
/*     */ 
/*     */     public Class getExceptionClass()
/*     */     {
/*  82 */       return this.cls;
/*     */     }
/*     */ 
/*     */     public String getId()
/*     */     {
/*  87 */       return this.id;
/*     */     }
/*     */ 
/*     */     void setId(String paramString)
/*     */     {
/*  92 */       this.id = paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class ExceptionRWIDLImpl extends ExceptionHandlerImpl.ExceptionRWBase {
/*     */     private Method readMethod;
/*     */     private Method writeMethod;
/*     */ 
/* 103 */     public ExceptionRWIDLImpl(Class arg2) { super(localClass1);
/*     */ 
/* 105 */       String str = localClass1.getName() + "Helper";
/* 106 */       ClassLoader localClassLoader = localClass1.getClassLoader();
/*     */       Class localClass2;
/*     */       try {
/* 110 */         localClass2 = Class.forName(str, true, localClassLoader);
/* 111 */         Method localMethod = localClass2.getDeclaredMethod("id", (Class[])null);
/* 112 */         setId((String)localMethod.invoke(null, (Object[])null));
/*     */       } catch (Exception localException1) {
/* 114 */         throw ExceptionHandlerImpl.this.wrapper.badHelperIdMethod(localException1, str);
/*     */       }
/*     */       try
/*     */       {
/* 118 */         Class[] arrayOfClass1 = { org.omg.CORBA.portable.OutputStream.class, localClass1 };
/*     */ 
/* 120 */         this.writeMethod = localClass2.getDeclaredMethod("write", arrayOfClass1);
/*     */       }
/*     */       catch (Exception localException2) {
/* 123 */         throw ExceptionHandlerImpl.this.wrapper.badHelperWriteMethod(localException2, str);
/*     */       }
/*     */       try
/*     */       {
/* 127 */         Class[] arrayOfClass2 = { org.omg.CORBA.portable.InputStream.class };
/*     */ 
/* 129 */         this.readMethod = localClass2.getDeclaredMethod("read", arrayOfClass2);
/*     */       } catch (Exception localException3) {
/* 131 */         throw ExceptionHandlerImpl.this.wrapper.badHelperReadMethod(localException3, str);
/*     */       } }
/*     */ 
/*     */     public void write(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, Exception paramException)
/*     */     {
/*     */       try
/*     */       {
/* 138 */         Object[] arrayOfObject = { paramOutputStream, paramException };
/* 139 */         this.writeMethod.invoke(null, arrayOfObject);
/*     */       } catch (Exception localException) {
/* 141 */         throw ExceptionHandlerImpl.this.wrapper.badHelperWriteMethod(localException, this.writeMethod.getDeclaringClass().getName());
/*     */       }
/*     */     }
/*     */ 
/*     */     public Exception read(org.omg.CORBA_2_3.portable.InputStream paramInputStream)
/*     */     {
/*     */       try
/*     */       {
/* 149 */         Object[] arrayOfObject = { paramInputStream };
/* 150 */         return (Exception)this.readMethod.invoke(null, arrayOfObject);
/*     */       } catch (Exception localException) {
/* 152 */         throw ExceptionHandlerImpl.this.wrapper.badHelperReadMethod(localException, this.readMethod.getDeclaringClass().getName());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public class ExceptionRWRMIImpl extends ExceptionHandlerImpl.ExceptionRWBase
/*     */   {
/*     */     public ExceptionRWRMIImpl(Class arg2)
/*     */     {
/* 162 */       super(localClass);
/* 163 */       setId(IDLNameTranslatorImpl.getExceptionId(localClass));
/*     */     }
/*     */ 
/*     */     public void write(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, Exception paramException)
/*     */     {
/* 168 */       paramOutputStream.write_string(getId());
/* 169 */       paramOutputStream.write_value(paramException, getExceptionClass());
/*     */     }
/*     */ 
/*     */     public Exception read(org.omg.CORBA_2_3.portable.InputStream paramInputStream)
/*     */     {
/* 174 */       paramInputStream.read_string();
/* 175 */       return (Exception)paramInputStream.read_value(getExceptionClass());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl
 * JD-Core Version:    0.6.2
 */