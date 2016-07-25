/*    */ package com.sun.beans.finder;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class PrimitiveWrapperMap
/*    */ {
/* 67 */   private static final Map<String, Class<?>> map = new HashMap(9);
/*    */ 
/*    */   static void replacePrimitivesWithWrappers(Class<?>[] paramArrayOfClass)
/*    */   {
/* 47 */     for (int i = 0; i < paramArrayOfClass.length; i++)
/* 48 */       if ((paramArrayOfClass[i] != null) && 
/* 49 */         (paramArrayOfClass[i].isPrimitive()))
/* 50 */         paramArrayOfClass[i] = getType(paramArrayOfClass[i].getName());
/*    */   }
/*    */ 
/*    */   public static Class<?> getType(String paramString)
/*    */   {
/* 64 */     return (Class)map.get(paramString);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 70 */     map.put(Boolean.TYPE.getName(), Boolean.class);
/* 71 */     map.put(Character.TYPE.getName(), Character.class);
/* 72 */     map.put(Byte.TYPE.getName(), Byte.class);
/* 73 */     map.put(Short.TYPE.getName(), Short.class);
/* 74 */     map.put(Integer.TYPE.getName(), Integer.class);
/* 75 */     map.put(Long.TYPE.getName(), Long.class);
/* 76 */     map.put(Float.TYPE.getName(), Float.class);
/* 77 */     map.put(Double.TYPE.getName(), Double.class);
/* 78 */     map.put(Void.TYPE.getName(), Void.class);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.PrimitiveWrapperMap
 * JD-Core Version:    0.6.2
 */