/*     */ package com.sun.xml.internal.ws.api;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.util.ReadOnlyPropertyException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class PropertySet
/*     */ {
/*     */   Set<Map.Entry<String, Object>> mapViewCore;
/*     */ 
/*     */   protected abstract PropertyMap getPropertyMap();
/*     */ 
/*     */   protected static PropertyMap parse(Class clazz)
/*     */   {
/* 148 */     return (PropertyMap)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public PropertySet.PropertyMap run() {
/* 150 */         PropertySet.PropertyMap props = new PropertySet.PropertyMap();
/* 151 */         for (Class c = this.val$clazz; c != null; c = c.getSuperclass()) {
/* 152 */           for (Field f : c.getDeclaredFields()) {
/* 153 */             PropertySet.Property cp = (PropertySet.Property)f.getAnnotation(PropertySet.Property.class);
/* 154 */             if (cp != null) {
/* 155 */               for (String value : cp.value()) {
/* 156 */                 props.put(value, new PropertySet.FieldAccessor(f, value));
/*     */               }
/*     */             }
/*     */           }
/* 160 */           for (Method m : c.getDeclaredMethods()) {
/* 161 */             PropertySet.Property cp = (PropertySet.Property)m.getAnnotation(PropertySet.Property.class);
/* 162 */             if (cp != null) { String name = m.getName();
/* 164 */               assert (name.startsWith("get"));
/*     */ 
/* 166 */               String setName = 's' + name.substring(1);
/*     */               Method setter;
/*     */               try { setter = this.val$clazz.getMethod(setName, new Class[] { m.getReturnType() });
/*     */               } catch (NoSuchMethodException e) {
/* 171 */                 setter = null;
/*     */               }
/* 173 */               for (String value : cp.value()) {
/* 174 */                 props.put(value, new PropertySet.MethodAccessor(m, setter, value));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 180 */         return props;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public final boolean containsKey(Object key)
/*     */   {
/* 310 */     return get(key) != null;
/*     */   }
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 322 */     Accessor sp = (Accessor)getPropertyMap().get(key);
/* 323 */     if (sp != null)
/* 324 */       return sp.get(this);
/* 325 */     throw new IllegalArgumentException("Undefined property " + key);
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/* 342 */     Accessor sp = (Accessor)getPropertyMap().get(key);
/* 343 */     if (sp != null) {
/* 344 */       Object old = sp.get(this);
/* 345 */       sp.set(this, value);
/* 346 */       return old;
/*     */     }
/* 348 */     throw new IllegalArgumentException("Undefined property " + key);
/*     */   }
/*     */ 
/*     */   public boolean supports(Object key)
/*     */   {
/* 356 */     return getPropertyMap().containsKey(key);
/*     */   }
/*     */ 
/*     */   public Object remove(Object key) {
/* 360 */     Accessor sp = (Accessor)getPropertyMap().get(key);
/* 361 */     if (sp != null) {
/* 362 */       Object old = sp.get(this);
/* 363 */       sp.set(this, null);
/* 364 */       return old;
/*     */     }
/* 366 */     throw new IllegalArgumentException("Undefined property " + key);
/*     */   }
/*     */ 
/*     */   public final Map<String, Object> createMapView()
/*     */   {
/* 392 */     final Set core = new HashSet();
/* 393 */     createEntrySet(core);
/*     */ 
/* 395 */     return new AbstractMap() {
/*     */       public Set<Map.Entry<String, Object>> entrySet() {
/* 397 */         return core;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   void createEntrySet(Set<Map.Entry<String, Object>> core) {
/* 403 */     for (final Map.Entry e : getPropertyMap().entrySet())
/* 404 */       core.add(new Map.Entry() {
/*     */         public String getKey() {
/* 406 */           return (String)e.getKey();
/*     */         }
/*     */ 
/*     */         public Object getValue() {
/* 410 */           return ((PropertySet.Accessor)e.getValue()).get(PropertySet.this);
/*     */         }
/*     */ 
/*     */         public Object setValue(Object value) {
/* 414 */           PropertySet.Accessor acc = (PropertySet.Accessor)e.getValue();
/* 415 */           Object old = acc.get(PropertySet.this);
/* 416 */           acc.set(PropertySet.this, value);
/* 417 */           return old;
/*     */         }
/*     */       });
/*     */   }
/*     */ 
/*     */   protected static abstract interface Accessor
/*     */   {
/*     */     public abstract String getName();
/*     */ 
/*     */     public abstract boolean hasValue(PropertySet paramPropertySet);
/*     */ 
/*     */     public abstract Object get(PropertySet paramPropertySet);
/*     */ 
/*     */     public abstract void set(PropertySet paramPropertySet, Object paramObject);
/*     */   }
/*     */ 
/*     */   static final class FieldAccessor
/*     */     implements PropertySet.Accessor
/*     */   {
/*     */     private final Field f;
/*     */     private final String name;
/*     */ 
/*     */     protected FieldAccessor(Field f, String name)
/*     */     {
/* 207 */       this.f = f;
/* 208 */       f.setAccessible(true);
/* 209 */       this.name = name;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 213 */       return this.name;
/*     */     }
/*     */ 
/*     */     public boolean hasValue(PropertySet props) {
/* 217 */       return get(props) != null;
/*     */     }
/*     */ 
/*     */     public Object get(PropertySet props) {
/*     */       try {
/* 222 */         return this.f.get(props); } catch (IllegalAccessException e) {
/*     */       }
/* 224 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     public void set(PropertySet props, Object value)
/*     */     {
/*     */       try {
/* 230 */         this.f.set(props, value);
/*     */       } catch (IllegalAccessException e) {
/* 232 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class MethodAccessor
/*     */     implements PropertySet.Accessor
/*     */   {
/*     */ 
/*     */     @NotNull
/*     */     private final Method getter;
/*     */ 
/*     */     @Nullable
/*     */     private final Method setter;
/*     */     private final String name;
/*     */ 
/*     */     protected MethodAccessor(Method getter, Method setter, String value)
/*     */     {
/* 254 */       this.getter = getter;
/* 255 */       this.setter = setter;
/* 256 */       this.name = value;
/* 257 */       getter.setAccessible(true);
/* 258 */       if (setter != null)
/* 259 */         setter.setAccessible(true);
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 263 */       return this.name;
/*     */     }
/*     */ 
/*     */     public boolean hasValue(PropertySet props) {
/* 267 */       return get(props) != null;
/*     */     }
/*     */ 
/*     */     public Object get(PropertySet props) {
/*     */       try {
/* 272 */         return this.getter.invoke(props, new Object[0]);
/*     */       } catch (IllegalAccessException e) {
/* 274 */         throw new AssertionError();
/*     */       } catch (InvocationTargetException e) {
/* 276 */         handle(e);
/* 277 */       }return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public void set(PropertySet props, Object value)
/*     */     {
/* 282 */       if (this.setter == null)
/* 283 */         throw new ReadOnlyPropertyException(getName());
/*     */       try {
/* 285 */         this.setter.invoke(props, new Object[] { value });
/*     */       } catch (IllegalAccessException e) {
/* 287 */         throw new AssertionError();
/*     */       } catch (InvocationTargetException e) {
/* 289 */         handle(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     private Exception handle(InvocationTargetException e)
/*     */     {
/* 299 */       Throwable t = e.getTargetException();
/* 300 */       if ((t instanceof Error))
/* 301 */         throw ((Error)t);
/* 302 */       if ((t instanceof RuntimeException))
/* 303 */         throw ((RuntimeException)t);
/* 304 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Inherited
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
/*     */   public static @interface Property
/*     */   {
/*     */     public abstract String[] value();
/*     */   }
/*     */ 
/*     */   protected static final class PropertyMap extends HashMap<String, PropertySet.Accessor>
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.PropertySet
 * JD-Core Version:    0.6.2
 */