/*     */ package javax.rmi.CORBA;
/*     */ 
/*     */ import com.sun.corba.se.impl.javax.rmi.CORBA.StubDelegateImpl;
/*     */ import com.sun.corba.se.impl.orbutil.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.security.AccessController;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.INITIALIZE;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA_2_3.portable.ObjectImpl;
/*     */ 
/*     */ public abstract class Stub extends ObjectImpl
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1087775603798577179L;
/*  61 */   private transient StubDelegate stubDelegate = null;
/*  62 */   private static Class stubDelegateClass = null;
/*     */   private static final String StubClassKey = "javax.rmi.CORBA.StubClass";
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  79 */     if (this.stubDelegate == null) {
/*  80 */       setDefaultDelegate();
/*     */     }
/*     */ 
/*  83 */     if (this.stubDelegate != null) {
/*  84 */       return this.stubDelegate.hashCode(this);
/*     */     }
/*     */ 
/*  87 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  99 */     if (this.stubDelegate == null) {
/* 100 */       setDefaultDelegate();
/*     */     }
/*     */ 
/* 103 */     if (this.stubDelegate != null) {
/* 104 */       return this.stubDelegate.equals(this, paramObject);
/*     */     }
/*     */ 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     if (this.stubDelegate == null) {
/* 119 */       setDefaultDelegate();
/*     */     }
/*     */ 
/* 123 */     if (this.stubDelegate != null) {
/* 124 */       String str = this.stubDelegate.toString(this);
/* 125 */       if (str == null) {
/* 126 */         return super.toString();
/*     */       }
/* 128 */       return str;
/*     */     }
/*     */ 
/* 131 */     return super.toString();
/*     */   }
/*     */ 
/*     */   public void connect(ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 147 */     if (this.stubDelegate == null) {
/* 148 */       setDefaultDelegate();
/*     */     }
/*     */ 
/* 151 */     if (this.stubDelegate != null)
/* 152 */       this.stubDelegate.connect(this, paramORB);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 163 */     if (this.stubDelegate == null) {
/* 164 */       setDefaultDelegate();
/*     */     }
/*     */ 
/* 167 */     if (this.stubDelegate != null)
/* 168 */       this.stubDelegate.readObject(this, paramObjectInputStream);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 183 */     if (this.stubDelegate == null) {
/* 184 */       setDefaultDelegate();
/*     */     }
/*     */ 
/* 187 */     if (this.stubDelegate != null)
/* 188 */       this.stubDelegate.writeObject(this, paramObjectOutputStream);
/*     */   }
/*     */ 
/*     */   private void setDefaultDelegate()
/*     */   {
/* 193 */     if (stubDelegateClass != null)
/*     */       try {
/* 195 */         this.stubDelegate = ((StubDelegate)stubDelegateClass.newInstance());
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   private static Object createDelegate(String paramString)
/*     */   {
/* 209 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/*     */ 
/* 211 */     if (str == null) {
/* 212 */       Properties localProperties = getORBPropertiesFile();
/* 213 */       if (localProperties != null) {
/* 214 */         str = localProperties.getProperty(paramString);
/*     */       }
/*     */     }
/*     */ 
/* 218 */     if (str == null)
/* 219 */       return new StubDelegateImpl();
/*     */     INITIALIZE localINITIALIZE;
/*     */     try
/*     */     {
/* 223 */       return loadDelegateClass(str).newInstance();
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 225 */       localINITIALIZE = new INITIALIZE("Cannot instantiate " + str);
/* 226 */       localINITIALIZE.initCause(localClassNotFoundException);
/* 227 */       throw localINITIALIZE;
/*     */     } catch (Exception localException) {
/* 229 */       localINITIALIZE = new INITIALIZE("Error while instantiating" + str);
/* 230 */       localINITIALIZE.initCause(localException);
/* 231 */     }throw localINITIALIZE;
/*     */   }
/*     */ 
/*     */   private static Class loadDelegateClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 239 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 240 */       return Class.forName(paramString, false, localClassLoader);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*     */       try
/*     */       {
/* 246 */         return RMIClassLoader.loadClass(paramString);
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 248 */         String str = "Could not load " + paramString + ": " + localMalformedURLException.toString();
/* 249 */         ClassNotFoundException localClassNotFoundException2 = new ClassNotFoundException(str);
/* 250 */         throw localClassNotFoundException2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Properties getORBPropertiesFile()
/*     */   {
/* 258 */     return (Properties)AccessController.doPrivileged(new GetORBPropertiesFileAction());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  66 */     Object localObject = createDelegate("javax.rmi.CORBA.StubClass");
/*  67 */     if (localObject != null)
/*  68 */       stubDelegateClass = localObject.getClass();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.CORBA.Stub
 * JD-Core Version:    0.6.2
 */