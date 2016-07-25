/*    */ package com.sun.beans.finder;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveTypeMap
/*    */ {
/* 51 */   private static final Map<String, Class<?>> map = new HashMap(9);
/*    */ 
/*    */   static Class<?> getType(String paramString)
/*    */   {
/* 48 */     return (Class)map.get(paramString);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 54 */     map.put(Boolean.TYPE.getName(), Boolean.TYPE);
/* 55 */     map.put(Character.TYPE.getName(), Character.TYPE);
/* 56 */     map.put(Byte.TYPE.getName(), Byte.TYPE);
/* 57 */     map.put(Short.TYPE.getName(), Short.TYPE);
/* 58 */     map.put(Integer.TYPE.getName(), Integer.TYPE);
/* 59 */     map.put(Long.TYPE.getName(), Long.TYPE);
/* 60 */     map.put(Float.TYPE.getName(), Float.TYPE);
/* 61 */     map.put(Double.TYPE.getName(), Double.TYPE);
/* 62 */     map.put(Void.TYPE.getName(), Void.TYPE);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.PrimitiveTypeMap
 * JD-Core Version:    0.6.2
 */