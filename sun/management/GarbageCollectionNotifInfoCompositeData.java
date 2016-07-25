/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.GarbageCollectionNotificationInfo;
/*     */ import com.sun.management.GcInfo;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ import javax.management.openmbean.OpenType;
/*     */ import javax.management.openmbean.SimpleType;
/*     */ 
/*     */ public class GarbageCollectionNotifInfoCompositeData extends LazyCompositeData
/*     */ {
/*     */   private final GarbageCollectionNotificationInfo gcNotifInfo;
/*     */   private static final String GC_NAME = "gcName";
/*     */   private static final String GC_ACTION = "gcAction";
/*     */   private static final String GC_CAUSE = "gcCause";
/*     */   private static final String GC_INFO = "gcInfo";
/* 137 */   private static final String[] gcNotifInfoItemNames = { "gcName", "gcAction", "gcCause", "gcInfo" };
/*     */ 
/* 143 */   private static HashMap<GcInfoBuilder, CompositeType> compositeTypeByBuilder = new HashMap();
/*     */ 
/* 194 */   private static CompositeType baseGcNotifInfoCompositeType = null;
/*     */   private static final long serialVersionUID = -1805123446483771292L;
/*     */ 
/*     */   public GarbageCollectionNotifInfoCompositeData(GarbageCollectionNotificationInfo paramGarbageCollectionNotificationInfo)
/*     */   {
/*  51 */     this.gcNotifInfo = paramGarbageCollectionNotificationInfo;
/*     */   }
/*     */ 
/*     */   public GarbageCollectionNotificationInfo getGarbageCollectionNotifInfo() {
/*  55 */     return this.gcNotifInfo;
/*     */   }
/*     */ 
/*     */   public static CompositeData toCompositeData(GarbageCollectionNotificationInfo paramGarbageCollectionNotificationInfo) {
/*  59 */     GarbageCollectionNotifInfoCompositeData localGarbageCollectionNotifInfoCompositeData = new GarbageCollectionNotifInfoCompositeData(paramGarbageCollectionNotificationInfo);
/*     */ 
/*  61 */     return localGarbageCollectionNotifInfoCompositeData.getCompositeData();
/*     */   }
/*     */ 
/*     */   private CompositeType getCompositeTypeByBuilder() {
/*  65 */     GcInfoBuilder localGcInfoBuilder = (GcInfoBuilder)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public GcInfoBuilder run() {
/*     */         try {
/*  68 */           Class localClass = Class.forName("com.sun.management.GcInfo");
/*  69 */           Field localField = localClass.getDeclaredField("builder");
/*  70 */           localField.setAccessible(true);
/*  71 */           return (GcInfoBuilder)localField.get(GarbageCollectionNotifInfoCompositeData.this.gcNotifInfo.getGcInfo());
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  73 */           return null;
/*     */         } catch (NoSuchFieldException localNoSuchFieldException) {
/*  75 */           return null; } catch (IllegalAccessException localIllegalAccessException) {
/*     */         }
/*  77 */         return null;
/*     */       }
/*     */     });
/*  81 */     CompositeType localCompositeType = null;
/*  82 */     synchronized (compositeTypeByBuilder) {
/*  83 */       localCompositeType = (CompositeType)compositeTypeByBuilder.get(localGcInfoBuilder);
/*  84 */       if (localCompositeType == null) {
/*  85 */         OpenType[] arrayOfOpenType = { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, localGcInfoBuilder.getGcInfoCompositeType() };
/*     */         try
/*     */         {
/*  94 */           localCompositeType = new CompositeType("sun.management.GarbageCollectionNotifInfoCompositeType", "CompositeType for GC notification info", gcNotifInfoItemNames, gcNotifInfoItemNames, arrayOfOpenType);
/*     */ 
/*  99 */           compositeTypeByBuilder.put(localGcInfoBuilder, localCompositeType);
/*     */         }
/*     */         catch (OpenDataException localOpenDataException) {
/* 102 */           throw Util.newException(localOpenDataException);
/*     */         }
/*     */       }
/*     */     }
/* 106 */     return localCompositeType;
/*     */   }
/*     */ 
/*     */   protected CompositeData getCompositeData()
/*     */   {
/* 113 */     Object[] arrayOfObject = { this.gcNotifInfo.getGcName(), this.gcNotifInfo.getGcAction(), this.gcNotifInfo.getGcCause(), GcInfoCompositeData.toCompositeData(this.gcNotifInfo.getGcInfo()) };
/*     */ 
/* 120 */     CompositeType localCompositeType = getCompositeTypeByBuilder();
/*     */     try
/*     */     {
/* 123 */       return new CompositeDataSupport(localCompositeType, gcNotifInfoItemNames, arrayOfObject);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/* 128 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getGcName(CompositeData paramCompositeData)
/*     */   {
/* 147 */     String str = getString(paramCompositeData, "gcName");
/* 148 */     if (str == null) {
/* 149 */       throw new IllegalArgumentException("Invalid composite data: Attribute gcName has null value");
/*     */     }
/*     */ 
/* 152 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getGcAction(CompositeData paramCompositeData) {
/* 156 */     String str = getString(paramCompositeData, "gcAction");
/* 157 */     if (str == null) {
/* 158 */       throw new IllegalArgumentException("Invalid composite data: Attribute gcAction has null value");
/*     */     }
/*     */ 
/* 161 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getGcCause(CompositeData paramCompositeData) {
/* 165 */     String str = getString(paramCompositeData, "gcCause");
/* 166 */     if (str == null) {
/* 167 */       throw new IllegalArgumentException("Invalid composite data: Attribute gcCause has null value");
/*     */     }
/*     */ 
/* 170 */     return str;
/*     */   }
/*     */ 
/*     */   public static GcInfo getGcInfo(CompositeData paramCompositeData) {
/* 174 */     CompositeData localCompositeData = (CompositeData)paramCompositeData.get("gcInfo");
/* 175 */     return GcInfo.from(localCompositeData);
/*     */   }
/*     */ 
/*     */   public static void validateCompositeData(CompositeData paramCompositeData)
/*     */   {
/* 183 */     if (paramCompositeData == null) {
/* 184 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 187 */     if (!isTypeMatched(getBaseGcNotifInfoCompositeType(), paramCompositeData.getCompositeType()))
/* 188 */       throw new IllegalArgumentException("Unexpected composite type for GarbageCollectionNotificationInfo");
/*     */   }
/*     */ 
/*     */   private static synchronized CompositeType getBaseGcNotifInfoCompositeType()
/*     */   {
/* 196 */     if (baseGcNotifInfoCompositeType == null) {
/*     */       try {
/* 198 */         OpenType[] arrayOfOpenType = { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, GcInfoCompositeData.getBaseGcInfoCompositeType() };
/*     */ 
/* 204 */         baseGcNotifInfoCompositeType = new CompositeType("sun.management.BaseGarbageCollectionNotifInfoCompositeType", "CompositeType for Base GarbageCollectionNotificationInfo", gcNotifInfoItemNames, gcNotifInfoItemNames, arrayOfOpenType);
/*     */       }
/*     */       catch (OpenDataException localOpenDataException)
/*     */       {
/* 212 */         throw Util.newException(localOpenDataException);
/*     */       }
/*     */     }
/* 215 */     return baseGcNotifInfoCompositeType;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.GarbageCollectionNotifInfoCompositeData
 * JD-Core Version:    0.6.2
 */