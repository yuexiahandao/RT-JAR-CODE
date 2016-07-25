/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.GcInfo;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Map;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ import javax.management.openmbean.OpenType;
/*     */ import javax.management.openmbean.SimpleType;
/*     */ import javax.management.openmbean.TabularData;
/*     */ 
/*     */ public class GcInfoCompositeData extends LazyCompositeData
/*     */ {
/*     */   private final GcInfo info;
/*     */   private final GcInfoBuilder builder;
/*     */   private final Object[] gcExtItemValues;
/*     */   private static final String ID = "id";
/*     */   private static final String START_TIME = "startTime";
/*     */   private static final String END_TIME = "endTime";
/*     */   private static final String DURATION = "duration";
/*     */   private static final String MEMORY_USAGE_BEFORE_GC = "memoryUsageBeforeGc";
/*     */   private static final String MEMORY_USAGE_AFTER_GC = "memoryUsageAfterGc";
/* 167 */   private static final String[] baseGcInfoItemNames = { "id", "startTime", "endTime", "duration", "memoryUsageBeforeGc", "memoryUsageAfterGc" };
/*     */   private static MappedMXBeanType memoryUsageMapType;
/* 196 */   private static OpenType[] baseGcInfoItemTypes = null;
/*     */ 
/* 274 */   private static CompositeType baseGcInfoCompositeType = null;
/*     */   private static final long serialVersionUID = -5716428894085882742L;
/*     */ 
/*     */   public GcInfoCompositeData(GcInfo paramGcInfo, GcInfoBuilder paramGcInfoBuilder, Object[] paramArrayOfObject)
/*     */   {
/*  62 */     this.info = paramGcInfo;
/*  63 */     this.builder = paramGcInfoBuilder;
/*  64 */     this.gcExtItemValues = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   public GcInfo getGcInfo() {
/*  68 */     return this.info;
/*     */   }
/*     */ 
/*     */   public static CompositeData toCompositeData(GcInfo paramGcInfo) {
/*  72 */     GcInfoBuilder localGcInfoBuilder = (GcInfoBuilder)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public GcInfoBuilder run() {
/*     */         try {
/*  75 */           Class localClass = Class.forName("com.sun.management.GcInfo");
/*  76 */           Field localField = localClass.getDeclaredField("builder");
/*  77 */           localField.setAccessible(true);
/*  78 */           return (GcInfoBuilder)localField.get(this.val$info);
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  80 */           return null;
/*     */         } catch (NoSuchFieldException localNoSuchFieldException) {
/*  82 */           return null; } catch (IllegalAccessException localIllegalAccessException) {
/*     */         }
/*  84 */         return null;
/*     */       }
/*     */     });
/*  88 */     Object[] arrayOfObject = (Object[])AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object[] run() {
/*     */         try {
/*  91 */           Class localClass = Class.forName("com.sun.management.GcInfo");
/*  92 */           Field localField = localClass.getDeclaredField("extAttributes");
/*  93 */           localField.setAccessible(true);
/*  94 */           return (Object[])localField.get(this.val$info);
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  96 */           return null;
/*     */         } catch (NoSuchFieldException localNoSuchFieldException) {
/*  98 */           return null; } catch (IllegalAccessException localIllegalAccessException) {
/*     */         }
/* 100 */         return null;
/*     */       }
/*     */     });
/* 104 */     GcInfoCompositeData localGcInfoCompositeData = new GcInfoCompositeData(paramGcInfo, localGcInfoBuilder, arrayOfObject);
/*     */ 
/* 106 */     return localGcInfoCompositeData.getCompositeData();
/*     */   }
/*     */ 
/*     */   protected CompositeData getCompositeData()
/*     */   {
/*     */     Object[] arrayOfObject1;
/*     */     try
/*     */     {
/* 115 */       arrayOfObject1 = new Object[] { new Long(this.info.getId()), new Long(this.info.getStartTime()), new Long(this.info.getEndTime()), new Long(this.info.getDuration()), memoryUsageMapType.toOpenTypeData(this.info.getMemoryUsageBeforeGc()), memoryUsageMapType.toOpenTypeData(this.info.getMemoryUsageAfterGc()) };
/*     */     }
/*     */     catch (OpenDataException localOpenDataException1)
/*     */     {
/* 125 */       throw new AssertionError(localOpenDataException1);
/*     */     }
/*     */ 
/* 129 */     int i = this.builder.getGcExtItemCount();
/* 130 */     if ((i == 0) && (this.gcExtItemValues != null) && (this.gcExtItemValues.length != 0))
/*     */     {
/* 132 */       throw new AssertionError("Unexpected Gc Extension Item Values");
/*     */     }
/*     */ 
/* 135 */     if ((i > 0) && ((this.gcExtItemValues == null) || (i != this.gcExtItemValues.length)))
/*     */     {
/* 137 */       throw new AssertionError("Unmatched Gc Extension Item Values");
/*     */     }
/*     */ 
/* 140 */     Object[] arrayOfObject2 = new Object[arrayOfObject1.length + i];
/*     */ 
/* 142 */     System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, arrayOfObject1.length);
/*     */ 
/* 145 */     if (i > 0) {
/* 146 */       System.arraycopy(this.gcExtItemValues, 0, arrayOfObject2, arrayOfObject1.length, i);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 151 */       return new CompositeDataSupport(this.builder.getGcInfoCompositeType(), this.builder.getItemNames(), arrayOfObject2);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException2)
/*     */     {
/* 156 */       throw new AssertionError(localOpenDataException2);
/*     */     }
/*     */   }
/*     */ 
/*     */   static String[] getBaseGcInfoItemNames()
/*     */   {
/* 193 */     return baseGcInfoItemNames;
/*     */   }
/*     */ 
/*     */   static synchronized OpenType[] getBaseGcInfoItemTypes()
/*     */   {
/* 198 */     if (baseGcInfoItemTypes == null) {
/* 199 */       OpenType localOpenType = memoryUsageMapType.getOpenType();
/* 200 */       baseGcInfoItemTypes = new OpenType[] { SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, localOpenType, localOpenType };
/*     */     }
/*     */ 
/* 210 */     return baseGcInfoItemTypes;
/*     */   }
/*     */ 
/*     */   public static long getId(CompositeData paramCompositeData) {
/* 214 */     return getLong(paramCompositeData, "id");
/*     */   }
/*     */   public static long getStartTime(CompositeData paramCompositeData) {
/* 217 */     return getLong(paramCompositeData, "startTime");
/*     */   }
/*     */   public static long getEndTime(CompositeData paramCompositeData) {
/* 220 */     return getLong(paramCompositeData, "endTime");
/*     */   }
/*     */ 
/*     */   public static Map<String, MemoryUsage> getMemoryUsageBeforeGc(CompositeData paramCompositeData)
/*     */   {
/*     */     try {
/* 226 */       TabularData localTabularData = (TabularData)paramCompositeData.get("memoryUsageBeforeGc");
/* 227 */       return cast(memoryUsageMapType.toJavaTypeData(localTabularData));
/*     */     }
/*     */     catch (InvalidObjectException localInvalidObjectException) {
/* 230 */       throw new AssertionError(localInvalidObjectException);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException) {
/* 233 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Map<String, MemoryUsage> cast(Object paramObject)
/*     */   {
/* 239 */     return (Map)paramObject;
/*     */   }
/*     */ 
/*     */   public static Map<String, MemoryUsage> getMemoryUsageAfterGc(CompositeData paramCompositeData) {
/*     */     try {
/* 244 */       TabularData localTabularData = (TabularData)paramCompositeData.get("memoryUsageAfterGc");
/*     */ 
/* 246 */       return cast(memoryUsageMapType.toJavaTypeData(localTabularData));
/*     */     }
/*     */     catch (InvalidObjectException localInvalidObjectException) {
/* 249 */       throw new AssertionError(localInvalidObjectException);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException) {
/* 252 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void validateCompositeData(CompositeData paramCompositeData)
/*     */   {
/* 262 */     if (paramCompositeData == null) {
/* 263 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 266 */     if (!isTypeMatched(getBaseGcInfoCompositeType(), paramCompositeData.getCompositeType()))
/*     */     {
/* 268 */       throw new IllegalArgumentException("Unexpected composite type for GcInfo");
/*     */     }
/*     */   }
/*     */ 
/*     */   static synchronized CompositeType getBaseGcInfoCompositeType()
/*     */   {
/* 276 */     if (baseGcInfoCompositeType == null) {
/*     */       try {
/* 278 */         baseGcInfoCompositeType = new CompositeType("sun.management.BaseGcInfoCompositeType", "CompositeType for Base GcInfo", getBaseGcInfoItemNames(), getBaseGcInfoItemNames(), getBaseGcInfoItemTypes());
/*     */       }
/*     */       catch (OpenDataException localOpenDataException)
/*     */       {
/* 286 */         throw Util.newException(localOpenDataException);
/*     */       }
/*     */     }
/* 289 */     return baseGcInfoCompositeType;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 180 */       Method localMethod = GcInfo.class.getMethod("getMemoryUsageBeforeGc", new Class[0]);
/* 181 */       memoryUsageMapType = MappedMXBeanType.getMappedType(localMethod.getGenericReturnType());
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/* 185 */       throw new AssertionError(localNoSuchMethodException);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException) {
/* 188 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.GcInfoCompositeData
 * JD-Core Version:    0.6.2
 */