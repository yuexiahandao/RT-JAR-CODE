/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.HotSpotDiagnosticMXBean;
/*     */ import com.sun.management.VMOption;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class HotSpotDiagnostic
/*     */   implements HotSpotDiagnosticMXBean
/*     */ {
/*     */   public void dumpHeap(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  44 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  45 */     if (localSecurityManager != null) {
/*  46 */       localSecurityManager.checkWrite(paramString);
/*  47 */       Util.checkControlAccess();
/*     */     }
/*     */ 
/*  50 */     dumpHeap0(paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   private native void dumpHeap0(String paramString, boolean paramBoolean) throws IOException;
/*     */ 
/*     */   public List<VMOption> getDiagnosticOptions() {
/*  56 */     List localList = Flag.getAllFlags();
/*  57 */     ArrayList localArrayList = new ArrayList();
/*  58 */     for (Flag localFlag : localList) {
/*  59 */       if ((localFlag.isWriteable()) && (localFlag.isExternal())) {
/*  60 */         localArrayList.add(localFlag.getVMOption());
/*     */       }
/*     */     }
/*  63 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public VMOption getVMOption(String paramString) {
/*  67 */     if (paramString == null) {
/*  68 */       throw new NullPointerException("name cannot be null");
/*     */     }
/*     */ 
/*  71 */     Flag localFlag = Flag.getFlag(paramString);
/*  72 */     if (localFlag == null) {
/*  73 */       throw new IllegalArgumentException("VM option \"" + paramString + "\" does not exist");
/*     */     }
/*     */ 
/*  76 */     return localFlag.getVMOption();
/*     */   }
/*     */ 
/*     */   public void setVMOption(String paramString1, String paramString2) {
/*  80 */     if (paramString1 == null) {
/*  81 */       throw new NullPointerException("name cannot be null");
/*     */     }
/*  83 */     if (paramString2 == null) {
/*  84 */       throw new NullPointerException("value cannot be null");
/*     */     }
/*     */ 
/*  87 */     Util.checkControlAccess();
/*  88 */     Flag localFlag = Flag.getFlag(paramString1);
/*  89 */     if (localFlag == null) {
/*  90 */       throw new IllegalArgumentException("VM option \"" + paramString1 + "\" does not exist");
/*     */     }
/*     */ 
/*  93 */     if (!localFlag.isWriteable()) {
/*  94 */       throw new IllegalArgumentException("VM Option \"" + paramString1 + "\" is not writeable");
/*     */     }
/*     */ 
/*  99 */     Object localObject = localFlag.getValue();
/* 100 */     if ((localObject instanceof Long)) {
/*     */       try {
/* 102 */         long l = Long.parseLong(paramString2);
/* 103 */         Flag.setLongValue(paramString1, l);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 105 */         IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Invalid value: VM Option \"" + paramString1 + "\"" + " expects numeric value");
/*     */ 
/* 109 */         localIllegalArgumentException.initCause(localNumberFormatException);
/* 110 */         throw localIllegalArgumentException;
/*     */       }
/* 112 */     } else if ((localObject instanceof Boolean)) {
/* 113 */       if ((!paramString2.equalsIgnoreCase("true")) && (!paramString2.equalsIgnoreCase("false")))
/*     */       {
/* 115 */         throw new IllegalArgumentException("Invalid value: VM Option \"" + paramString1 + "\"" + " expects \"true\" or \"false\".");
/*     */       }
/*     */ 
/* 119 */       Flag.setBooleanValue(paramString1, Boolean.parseBoolean(paramString2));
/* 120 */     } else if ((localObject instanceof String)) {
/* 121 */       Flag.setStringValue(paramString1, paramString2);
/*     */     } else {
/* 123 */       throw new IllegalArgumentException("VM Option \"" + paramString1 + "\" is of an unsupported type: " + localObject.getClass().getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName()
/*     */   {
/* 130 */     return Util.newObjectName("com.sun.management:type=HotSpotDiagnostic");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotSpotDiagnostic
 * JD-Core Version:    0.6.2
 */