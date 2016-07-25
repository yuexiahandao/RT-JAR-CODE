/*      */ package org.omg.CORBA;
/*      */ 
/*      */ import com.sun.corba.se.impl.orb.ORBImpl;
/*      */ import com.sun.corba.se.impl.orb.ORBSingleton;
/*      */ import java.applet.Applet;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Properties;
/*      */ import org.omg.CORBA.ORBPackage.InconsistentTypeCode;
/*      */ import org.omg.CORBA.ORBPackage.InvalidName;
/*      */ import org.omg.CORBA.portable.OutputStream;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public abstract class ORB
/*      */ {
/*      */   private static final String ORBClassKey = "org.omg.CORBA.ORBClass";
/*      */   private static final String ORBSingletonClassKey = "org.omg.CORBA.ORBSingletonClass";
/*      */   private static ORB singleton;
/*      */ 
/*      */   private static String getSystemProperty(String paramString)
/*      */   {
/*  193 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public java.lang.Object run() {
/*  196 */         return System.getProperty(this.val$name);
/*      */       }
/*      */     });
/*  201 */     return str;
/*      */   }
/*      */ 
/*      */   private static String getPropertyFromFile(String paramString)
/*      */   {
/*  210 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       private Properties getFileProperties(String paramAnonymousString) {
/*      */         try {
/*  214 */           File localFile = new File(paramAnonymousString);
/*  215 */           if (!localFile.exists()) {
/*  216 */             return null;
/*      */           }
/*  218 */           Properties localProperties = new Properties();
/*  219 */           FileInputStream localFileInputStream = new FileInputStream(localFile);
/*      */           try {
/*  221 */             localProperties.load(localFileInputStream);
/*      */           } finally {
/*  223 */             localFileInputStream.close();
/*      */           }
/*      */ 
/*  226 */           return localProperties; } catch (Exception localException) {
/*      */         }
/*  228 */         return null;
/*      */       }
/*      */ 
/*      */       public java.lang.Object run()
/*      */       {
/*  233 */         String str1 = System.getProperty("user.home");
/*  234 */         String str2 = str1 + File.separator + "orb.properties";
/*      */ 
/*  236 */         Properties localProperties = getFileProperties(str2);
/*      */ 
/*  238 */         if (localProperties != null) {
/*  239 */           str3 = localProperties.getProperty(this.val$name);
/*  240 */           if (str3 != null) {
/*  241 */             return str3;
/*      */           }
/*      */         }
/*  244 */         String str3 = System.getProperty("java.home");
/*  245 */         str2 = str3 + File.separator + "lib" + File.separator + "orb.properties";
/*      */ 
/*  247 */         localProperties = getFileProperties(str2);
/*      */ 
/*  249 */         if (localProperties == null) {
/*  250 */           return null;
/*      */         }
/*  252 */         return localProperties.getProperty(this.val$name);
/*      */       }
/*      */     });
/*  257 */     return str;
/*      */   }
/*      */ 
/*      */   public static synchronized ORB init()
/*      */   {
/*  286 */     if (singleton == null) {
/*  287 */       String str = getSystemProperty("org.omg.CORBA.ORBSingletonClass");
/*  288 */       if (str == null)
/*  289 */         str = getPropertyFromFile("org.omg.CORBA.ORBSingletonClass");
/*  290 */       if ((str == null) || (str.equals("com.sun.corba.se.impl.orb.ORBSingleton")))
/*      */       {
/*  292 */         singleton = new ORBSingleton();
/*      */       }
/*  294 */       else singleton = create_impl(str);
/*      */     }
/*      */ 
/*  297 */     return singleton;
/*      */   }
/*      */ 
/*      */   private static ORB create_impl(String paramString) {
/*  301 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*  302 */     if (localClassLoader == null)
/*  303 */       localClassLoader = ClassLoader.getSystemClassLoader();
/*      */     java.lang.Object localObject;
/*      */     try {
/*  306 */       ReflectUtil.checkPackageAccess(paramString);
/*  307 */       ORB localORB = ORB.class;
/*  308 */       localObject = Class.forName(paramString, true, localClassLoader).asSubclass(localORB);
/*  309 */       return (ORB)((Class)localObject).newInstance();
/*      */     } catch (Throwable localThrowable) {
/*  311 */       localObject = new INITIALIZE("can't instantiate default ORB implementation " + paramString);
/*      */ 
/*  313 */       ((SystemException)localObject).initCause(localThrowable);
/*  314 */     }throw ((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   public static ORB init(String[] paramArrayOfString, Properties paramProperties)
/*      */   {
/*  338 */     String str = null;
/*      */ 
/*  341 */     if (paramProperties != null)
/*  342 */       str = paramProperties.getProperty("org.omg.CORBA.ORBClass");
/*  343 */     if (str == null)
/*  344 */       str = getSystemProperty("org.omg.CORBA.ORBClass");
/*  345 */     if (str == null)
/*  346 */       str = getPropertyFromFile("org.omg.CORBA.ORBClass");
/*      */     java.lang.Object localObject;
/*  347 */     if ((str == null) || (str.equals("com.sun.corba.se.impl.orb.ORBImpl")))
/*      */     {
/*  349 */       localObject = new ORBImpl();
/*      */     }
/*  351 */     else localObject = create_impl(str);
/*      */ 
/*  353 */     ((ORB)localObject).set_parameters(paramArrayOfString, paramProperties);
/*  354 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static ORB init(Applet paramApplet, Properties paramProperties)
/*      */   {
/*  370 */     String str = paramApplet.getParameter("org.omg.CORBA.ORBClass");
/*  371 */     if ((str == null) && (paramProperties != null))
/*  372 */       str = paramProperties.getProperty("org.omg.CORBA.ORBClass");
/*  373 */     if (str == null)
/*  374 */       str = getSystemProperty("org.omg.CORBA.ORBClass");
/*  375 */     if (str == null)
/*  376 */       str = getPropertyFromFile("org.omg.CORBA.ORBClass");
/*      */     java.lang.Object localObject;
/*  377 */     if ((str == null) || (str.equals("com.sun.corba.se.impl.orb.ORBImpl")))
/*      */     {
/*  379 */       localObject = new ORBImpl();
/*      */     }
/*  381 */     else localObject = create_impl(str);
/*      */ 
/*  383 */     ((ORB)localObject).set_parameters(paramApplet, paramProperties);
/*  384 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected abstract void set_parameters(String[] paramArrayOfString, Properties paramProperties);
/*      */ 
/*      */   protected abstract void set_parameters(Applet paramApplet, Properties paramProperties);
/*      */ 
/*      */   public void connect(Object paramObject)
/*      */   {
/*  432 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  454 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public void disconnect(Object paramObject)
/*      */   {
/*  476 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public abstract String[] list_initial_services();
/*      */ 
/*      */   public abstract Object resolve_initial_references(String paramString)
/*      */     throws InvalidName;
/*      */ 
/*      */   public abstract String object_to_string(Object paramObject);
/*      */ 
/*      */   public abstract Object string_to_object(String paramString);
/*      */ 
/*      */   public abstract NVList create_list(int paramInt);
/*      */ 
/*      */   public NVList create_operation_list(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/*  577 */       String str = "org.omg.CORBA.OperationDef";
/*  578 */       localObject = null;
/*      */ 
/*  580 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*  581 */       if (localClassLoader == null) {
/*  582 */         localClassLoader = ClassLoader.getSystemClassLoader();
/*      */       }
/*  584 */       localObject = Class.forName(str, true, localClassLoader);
/*      */ 
/*  588 */       Class[] arrayOfClass = { localObject };
/*  589 */       Method localMethod = getClass().getMethod("create_operation_list", arrayOfClass);
/*      */ 
/*  593 */       java.lang.Object[] arrayOfObject = { paramObject };
/*  594 */       return (NVList)localMethod.invoke(this, arrayOfObject);
/*      */     }
/*      */     catch (InvocationTargetException localInvocationTargetException) {
/*  597 */       java.lang.Object localObject = localInvocationTargetException.getTargetException();
/*  598 */       if ((localObject instanceof Error)) {
/*  599 */         throw ((Error)localObject);
/*      */       }
/*  601 */       if ((localObject instanceof RuntimeException)) {
/*  602 */         throw ((RuntimeException)localObject);
/*      */       }
/*      */ 
/*  605 */       throw new NO_IMPLEMENT();
/*      */     }
/*      */     catch (RuntimeException localRuntimeException)
/*      */     {
/*  609 */       throw localRuntimeException;
/*      */     } catch (Exception localException) {
/*      */     }
/*  612 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public abstract NamedValue create_named_value(String paramString, Any paramAny, int paramInt);
/*      */ 
/*      */   public abstract ExceptionList create_exception_list();
/*      */ 
/*      */   public abstract ContextList create_context_list();
/*      */ 
/*      */   public abstract Context get_default_context();
/*      */ 
/*      */   public abstract Environment create_environment();
/*      */ 
/*      */   public abstract OutputStream create_output_stream();
/*      */ 
/*      */   public abstract void send_multiple_requests_oneway(Request[] paramArrayOfRequest);
/*      */ 
/*      */   public abstract void send_multiple_requests_deferred(Request[] paramArrayOfRequest);
/*      */ 
/*      */   public abstract boolean poll_next_response();
/*      */ 
/*      */   public abstract Request get_next_response()
/*      */     throws WrongTransaction;
/*      */ 
/*      */   public abstract TypeCode get_primitive_tc(TCKind paramTCKind);
/*      */ 
/*      */   public abstract TypeCode create_struct_tc(String paramString1, String paramString2, StructMember[] paramArrayOfStructMember);
/*      */ 
/*      */   public abstract TypeCode create_union_tc(String paramString1, String paramString2, TypeCode paramTypeCode, UnionMember[] paramArrayOfUnionMember);
/*      */ 
/*      */   public abstract TypeCode create_enum_tc(String paramString1, String paramString2, String[] paramArrayOfString);
/*      */ 
/*      */   public abstract TypeCode create_alias_tc(String paramString1, String paramString2, TypeCode paramTypeCode);
/*      */ 
/*      */   public abstract TypeCode create_exception_tc(String paramString1, String paramString2, StructMember[] paramArrayOfStructMember);
/*      */ 
/*      */   public abstract TypeCode create_interface_tc(String paramString1, String paramString2);
/*      */ 
/*      */   public abstract TypeCode create_string_tc(int paramInt);
/*      */ 
/*      */   public abstract TypeCode create_wstring_tc(int paramInt);
/*      */ 
/*      */   public abstract TypeCode create_sequence_tc(int paramInt, TypeCode paramTypeCode);
/*      */ 
/*      */   @Deprecated
/*      */   public abstract TypeCode create_recursive_sequence_tc(int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract TypeCode create_array_tc(int paramInt, TypeCode paramTypeCode);
/*      */ 
/*      */   public TypeCode create_native_tc(String paramString1, String paramString2)
/*      */   {
/*  902 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public TypeCode create_abstract_interface_tc(String paramString1, String paramString2)
/*      */   {
/*  916 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public TypeCode create_fixed_tc(short paramShort1, short paramShort2)
/*      */   {
/*  930 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public TypeCode create_value_tc(String paramString1, String paramString2, short paramShort, TypeCode paramTypeCode, ValueMember[] paramArrayOfValueMember)
/*      */   {
/*  959 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public TypeCode create_recursive_tc(String paramString)
/*      */   {
/* 1003 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public TypeCode create_value_box_tc(String paramString1, String paramString2, TypeCode paramTypeCode)
/*      */   {
/* 1019 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public abstract Any create_any();
/*      */ 
/*      */   @Deprecated
/*      */   public Current get_current()
/*      */   {
/* 1050 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/* 1062 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public void shutdown(boolean paramBoolean)
/*      */   {
/* 1096 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public boolean work_pending()
/*      */   {
/* 1112 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public void perform_work()
/*      */   {
/* 1126 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public boolean get_service_information(short paramShort, ServiceInformationHolder paramServiceInformationHolder)
/*      */   {
/* 1156 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynAny create_dyn_any(Any paramAny)
/*      */   {
/* 1176 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynAny create_basic_dyn_any(TypeCode paramTypeCode)
/*      */     throws InconsistentTypeCode
/*      */   {
/* 1196 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynStruct create_dyn_struct(TypeCode paramTypeCode)
/*      */     throws InconsistentTypeCode
/*      */   {
/* 1216 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynSequence create_dyn_sequence(TypeCode paramTypeCode)
/*      */     throws InconsistentTypeCode
/*      */   {
/* 1236 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynArray create_dyn_array(TypeCode paramTypeCode)
/*      */     throws InconsistentTypeCode
/*      */   {
/* 1257 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynUnion create_dyn_union(TypeCode paramTypeCode)
/*      */     throws InconsistentTypeCode
/*      */   {
/* 1277 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public DynEnum create_dyn_enum(TypeCode paramTypeCode)
/*      */     throws InconsistentTypeCode
/*      */   {
/* 1297 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ 
/*      */   public Policy create_policy(int paramInt, Any paramAny)
/*      */     throws PolicyError
/*      */   {
/* 1323 */     throw new NO_IMPLEMENT();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ORB
 * JD-Core Version:    0.6.2
 */