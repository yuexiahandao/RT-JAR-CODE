/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ final class PropMap
/*     */   implements SortedMap<Object, Object>
/*     */ {
/*  51 */   private final TreeMap<Object, Object> theMap = new TreeMap();
/*  52 */   private final List<PropertyChangeListener> listenerList = new ArrayList(1);
/*     */ 
/* 144 */   private static Map<Object, Object> defaultProps = new HashMap(localProperties);
/*     */ 
/*     */   void addListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/*  55 */     this.listenerList.add(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   void removeListener(PropertyChangeListener paramPropertyChangeListener) {
/*  59 */     this.listenerList.remove(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   void addListeners(ArrayList<PropertyChangeListener> paramArrayList) {
/*  63 */     this.listenerList.addAll(paramArrayList);
/*     */   }
/*     */ 
/*     */   void removeListeners(ArrayList<PropertyChangeListener> paramArrayList) {
/*  67 */     this.listenerList.removeAll(paramArrayList);
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2)
/*     */   {
/*  72 */     Object localObject = this.theMap.put(paramObject1, paramObject2);
/*     */     PropertyChangeEvent localPropertyChangeEvent;
/*  73 */     if ((paramObject2 != localObject) && (!this.listenerList.isEmpty()))
/*     */     {
/*  75 */       localPropertyChangeEvent = new PropertyChangeEvent(this, (String)paramObject1, localObject, paramObject2);
/*     */ 
/*  78 */       for (PropertyChangeListener localPropertyChangeListener : this.listenerList) {
/*  79 */         localPropertyChangeListener.propertyChange(localPropertyChangeEvent);
/*     */       }
/*     */     }
/*  82 */     return localObject;
/*     */   }
/*     */ 
/*     */   PropMap()
/*     */   {
/* 148 */     this.theMap.putAll(defaultProps);
/*     */   }
/*     */ 
/*     */   SortedMap<Object, Object> prefixMap(String paramString)
/*     */   {
/* 155 */     int i = paramString.length();
/* 156 */     if (i == 0)
/* 157 */       return this;
/* 158 */     char c = (char)(paramString.charAt(i - 1) + '\001');
/* 159 */     String str = paramString.substring(0, i - 1) + c;
/*     */ 
/* 161 */     return subMap(paramString, str);
/*     */   }
/*     */ 
/*     */   String getProperty(String paramString) {
/* 165 */     return (String)get(paramString);
/*     */   }
/*     */   String getProperty(String paramString1, String paramString2) {
/* 168 */     String str = getProperty(paramString1);
/* 169 */     if (str == null)
/* 170 */       return paramString2;
/* 171 */     return str;
/*     */   }
/*     */   String setProperty(String paramString1, String paramString2) {
/* 174 */     return (String)put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   List getProperties(String paramString)
/*     */   {
/* 179 */     Collection localCollection = prefixMap(paramString).values();
/* 180 */     ArrayList localArrayList = new ArrayList(localCollection.size());
/* 181 */     localArrayList.addAll(localCollection);
/* 182 */     while (localArrayList.remove(null));
/* 183 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private boolean toBoolean(String paramString) {
/* 187 */     return Boolean.valueOf(paramString).booleanValue();
/*     */   }
/*     */   boolean getBoolean(String paramString) {
/* 190 */     return toBoolean(getProperty(paramString));
/*     */   }
/*     */   boolean setBoolean(String paramString, boolean paramBoolean) {
/* 193 */     return toBoolean(setProperty(paramString, String.valueOf(paramBoolean)));
/*     */   }
/*     */ 
/*     */   int toInteger(String paramString) {
/* 197 */     if (paramString == null) return 0;
/* 198 */     if ("true".equals(paramString)) return 1;
/* 199 */     if ("false".equals(paramString)) return 0;
/* 200 */     return Integer.parseInt(paramString);
/*     */   }
/*     */   int getInteger(String paramString) {
/* 203 */     return toInteger(getProperty(paramString));
/*     */   }
/*     */   int setInteger(String paramString, int paramInt) {
/* 206 */     return toInteger(setProperty(paramString, String.valueOf(paramInt)));
/*     */   }
/*     */ 
/*     */   long toLong(String paramString) {
/*     */     try {
/* 211 */       return paramString == null ? 0L : Long.parseLong(paramString); } catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 213 */     throw new IllegalArgumentException("Invalid value");
/*     */   }
/*     */ 
/*     */   long getLong(String paramString) {
/* 217 */     return toLong(getProperty(paramString));
/*     */   }
/*     */   long setLong(String paramString, long paramLong) {
/* 220 */     return toLong(setProperty(paramString, String.valueOf(paramLong)));
/*     */   }
/*     */ 
/*     */   int getTime(String paramString) {
/* 224 */     String str = getProperty(paramString, "0");
/* 225 */     if ("now".equals(str)) {
/* 226 */       return (int)((System.currentTimeMillis() + 500L) / 1000L);
/*     */     }
/* 228 */     long l = toLong(str);
/*     */ 
/* 231 */     if ((l < 10000000000L) && (!"0".equals(str))) {
/* 232 */       Utils.log.warning("Supplied modtime appears to be seconds rather than milliseconds: " + str);
/*     */     }
/* 234 */     return (int)((l + 500L) / 1000L);
/*     */   }
/*     */ 
/*     */   void list(PrintStream paramPrintStream) {
/* 238 */     PrintWriter localPrintWriter = new PrintWriter(paramPrintStream);
/* 239 */     list(localPrintWriter);
/* 240 */     localPrintWriter.flush();
/*     */   }
/*     */   void list(PrintWriter paramPrintWriter) {
/* 243 */     paramPrintWriter.println("#PACK200[");
/* 244 */     Set localSet = defaultProps.entrySet();
/* 245 */     for (Map.Entry localEntry : this.theMap.entrySet()) {
/* 246 */       if (!localSet.contains(localEntry))
/* 247 */         paramPrintWriter.println("  " + localEntry.getKey() + " = " + localEntry.getValue());
/*     */     }
/* 249 */     paramPrintWriter.println("#]");
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 254 */     return this.theMap.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 259 */     return this.theMap.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 264 */     return this.theMap.containsKey(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 269 */     return this.theMap.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 274 */     return this.theMap.get(paramObject);
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 279 */     return this.theMap.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public void putAll(Map paramMap)
/*     */   {
/* 285 */     this.theMap.putAll(paramMap);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 290 */     this.theMap.clear();
/*     */   }
/*     */ 
/*     */   public Set<Object> keySet()
/*     */   {
/* 295 */     return this.theMap.keySet();
/*     */   }
/*     */ 
/*     */   public Collection<Object> values()
/*     */   {
/* 300 */     return this.theMap.values();
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<Object, Object>> entrySet()
/*     */   {
/* 305 */     return this.theMap.entrySet();
/*     */   }
/*     */ 
/*     */   public Comparator<Object> comparator()
/*     */   {
/* 311 */     return this.theMap.comparator();
/*     */   }
/*     */ 
/*     */   public SortedMap<Object, Object> subMap(Object paramObject1, Object paramObject2)
/*     */   {
/* 316 */     return this.theMap.subMap(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public SortedMap<Object, Object> headMap(Object paramObject)
/*     */   {
/* 321 */     return this.theMap.headMap(paramObject);
/*     */   }
/*     */ 
/*     */   public SortedMap<Object, Object> tailMap(Object paramObject)
/*     */   {
/* 326 */     return this.theMap.tailMap(paramObject);
/*     */   }
/*     */ 
/*     */   public Object firstKey()
/*     */   {
/* 331 */     return this.theMap.firstKey();
/*     */   }
/*     */ 
/*     */   public Object lastKey()
/*     */   {
/* 336 */     return this.theMap.lastKey();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  90 */     Properties localProperties = new Properties();
/*     */ 
/*  93 */     localProperties.put("com.sun.java.util.jar.pack.disable.native", String.valueOf(Boolean.getBoolean("com.sun.java.util.jar.pack.disable.native")));
/*     */ 
/*  97 */     localProperties.put("com.sun.java.util.jar.pack.verbose", String.valueOf(Integer.getInteger("com.sun.java.util.jar.pack.verbose", 0)));
/*     */ 
/* 101 */     localProperties.put("com.sun.java.util.jar.pack.default.timezone", String.valueOf(Boolean.getBoolean("com.sun.java.util.jar.pack.default.timezone")));
/*     */ 
/* 105 */     localProperties.put("pack.segment.limit", "-1");
/*     */ 
/* 108 */     localProperties.put("pack.keep.file.order", "true");
/*     */ 
/* 111 */     localProperties.put("pack.modification.time", "keep");
/*     */ 
/* 114 */     localProperties.put("pack.deflate.hint", "keep");
/*     */ 
/* 117 */     localProperties.put("pack.unknown.attribute", "pass");
/*     */ 
/* 120 */     localProperties.put("pack.effort", "5");
/*     */ 
/* 125 */     String str1 = "intrinsic.properties";
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 127 */       InputStream localInputStream = PackerImpl.class.getResourceAsStream(str1); localObject1 = null;
/*     */       try { if (localInputStream == null) {
/* 129 */           throw new RuntimeException(str1 + " cannot be loaded");
/*     */         }
/* 131 */         localProperties.load(localInputStream);
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 127 */         localObject1 = localThrowable2; throw localThrowable2;
/*     */       }
/*     */       finally
/*     */       {
/* 132 */         if (localInputStream != null) if (localObject1 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { ((Throwable)localObject1).addSuppressed(localThrowable3); } else localInputStream.close();  
/*     */       } } catch (IOException localIOException) { throw new RuntimeException(localIOException); }
/*     */ 
/*     */ 
/* 136 */     for (Iterator localIterator = localProperties.entrySet().iterator(); localIterator.hasNext(); ) { localObject1 = (Map.Entry)localIterator.next();
/* 137 */       String str2 = (String)((Map.Entry)localObject1).getKey();
/* 138 */       String str3 = (String)((Map.Entry)localObject1).getValue();
/* 139 */       if (str2.startsWith("attribute."))
/* 140 */         ((Map.Entry)localObject1).setValue(Attribute.normalizeLayoutString(str3));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.PropMap
 * JD-Core Version:    0.6.2
 */