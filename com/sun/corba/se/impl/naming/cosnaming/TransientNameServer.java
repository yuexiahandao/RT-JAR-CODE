/*     */ package com.sun.corba.se.impl.naming.cosnaming;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public class TransientNameServer
/*     */ {
/*  62 */   private static boolean debug = false;
/*  63 */   static NamingSystemException wrapper = NamingSystemException.get("naming");
/*     */ 
/*     */   public static void trace(String paramString)
/*     */   {
/*  67 */     if (debug)
/*  68 */       System.out.println(paramString);
/*     */   }
/*     */ 
/*     */   public static void initDebug(String[] paramArrayOfString)
/*     */   {
/*  74 */     if (debug) {
/*  75 */       return;
/*     */     }
/*  77 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*  78 */       if (paramArrayOfString[i].equalsIgnoreCase("-debug")) {
/*  79 */         debug = true;
/*  80 */         return;
/*     */       }
/*  82 */     debug = false;
/*     */   }
/*     */ 
/*     */   private static org.omg.CORBA.Object initializeRootNamingContext(org.omg.CORBA.ORB paramORB) {
/*  86 */     java.lang.Object localObject = null;
/*     */     try {
/*  88 */       com.sun.corba.se.spi.orb.ORB localORB = (com.sun.corba.se.spi.orb.ORB)paramORB;
/*     */ 
/*  91 */       TransientNameService localTransientNameService = new TransientNameService(localORB);
/*  92 */       return localTransientNameService.initialNamingContext();
/*     */     } catch (SystemException localSystemException) {
/*  94 */       throw wrapper.transNsCannotCreateInitialNcSys(localSystemException);
/*     */     } catch (Exception localException) {
/*  96 */       throw wrapper.transNsCannotCreateInitialNc(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 107 */     initDebug(paramArrayOfString);
/*     */ 
/* 109 */     int i = 0;
/* 110 */     int j = 0;
/*     */ 
/* 113 */     int k = 0;
/*     */     try {
/* 115 */       trace("Transient name server started with args " + paramArrayOfString);
/*     */ 
/* 118 */       Properties localProperties = System.getProperties();
/*     */ 
/* 120 */       localProperties.put("com.sun.CORBA.POA.ORBServerId", "1000000");
/* 121 */       localProperties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
/*     */       try
/*     */       {
/* 126 */         String str1 = System.getProperty("org.omg.CORBA.ORBInitialPort");
/* 127 */         if ((str1 != null) && (str1.length() > 0)) {
/* 128 */           k = Integer.parseInt(str1);
/*     */ 
/* 130 */           if (k == 0) {
/* 131 */             j = 1;
/* 132 */             throw wrapper.transientNameServerBadPort();
/*     */           }
/*     */         }
/* 135 */         localObject1 = System.getProperty("org.omg.CORBA.ORBInitialHost");
/*     */ 
/* 137 */         if (localObject1 != null) {
/* 138 */           i = 1;
/* 139 */           throw wrapper.transientNameServerBadHost();
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/*     */ 
/* 146 */       for (int m = 0; m < paramArrayOfString.length; m++)
/*     */       {
/* 148 */         if ((paramArrayOfString[m].equals("-ORBInitialPort")) && (m < paramArrayOfString.length - 1))
/*     */         {
/* 150 */           k = Integer.parseInt(paramArrayOfString[(m + 1)]);
/*     */ 
/* 152 */           if (k == 0) {
/* 153 */             j = 1;
/* 154 */             throw wrapper.transientNameServerBadPort();
/*     */           }
/*     */         }
/* 157 */         if (paramArrayOfString[m].equals("-ORBInitialHost")) {
/* 158 */           i = 1;
/* 159 */           throw wrapper.transientNameServerBadHost();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 165 */       if (k == 0) {
/* 166 */         k = 900;
/* 167 */         localProperties.put("org.omg.CORBA.ORBInitialPort", Integer.toString(k));
/*     */       }
/*     */ 
/* 173 */       localProperties.put("com.sun.CORBA.POA.ORBPersistentServerPort", Integer.toString(k));
/*     */ 
/* 176 */       org.omg.CORBA.ORB localORB = org.omg.CORBA.ORB.init(paramArrayOfString, localProperties);
/* 177 */       trace("ORB object returned from init: " + localORB);
/*     */ 
/* 179 */       java.lang.Object localObject1 = initializeRootNamingContext(localORB);
/* 180 */       ((com.sun.corba.se.org.omg.CORBA.ORB)localORB).register_initial_reference("NamingService", (org.omg.CORBA.Object)localObject1);
/*     */ 
/* 183 */       String str2 = null;
/*     */ 
/* 185 */       if (localObject1 != null) {
/* 186 */         str2 = localORB.object_to_string((org.omg.CORBA.Object)localObject1);
/*     */       } else {
/* 188 */         NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.exception", k));
/*     */ 
/* 190 */         NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.usage"));
/*     */ 
/* 192 */         System.exit(1);
/*     */       }
/*     */ 
/* 195 */       trace("name service created");
/*     */ 
/* 201 */       System.out.println(CorbaResourceUtil.getText("tnameserv.hs1", str2));
/*     */ 
/* 203 */       System.out.println(CorbaResourceUtil.getText("tnameserv.hs2", k));
/*     */ 
/* 205 */       System.out.println(CorbaResourceUtil.getText("tnameserv.hs3"));
/*     */ 
/* 208 */       java.lang.Object localObject2 = new java.lang.Object();
/* 209 */       synchronized (localObject2) { localObject2.wait(); }
/*     */     } catch (Exception localException) {
/* 211 */       if (i != 0)
/*     */       {
/* 214 */         NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.invalidhostoption"));
/*     */       }
/* 216 */       else if (j != 0)
/*     */       {
/* 219 */         NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.orbinitialport0"));
/*     */       }
/*     */       else {
/* 222 */         NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.exception", k));
/*     */ 
/* 224 */         NamingUtils.errprint(CorbaResourceUtil.getText("tnameserv.usage"));
/*     */       }
/*     */ 
/* 228 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.TransientNameServer
 * JD-Core Version:    0.6.2
 */