/*     */ package com.sun.org.glassfish.gmbal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.ModelMBeanInfo;
/*     */ 
/*     */ public class AMXClient
/*     */   implements AMXMBeanInterface
/*     */ {
/*  73 */   public static final ObjectName NULL_OBJECTNAME = makeObjectName("null:type=Null,name=Null");
/*     */   private MBeanServerConnection server;
/*     */   private ObjectName oname;
/*     */ 
/*     */   private static ObjectName makeObjectName(String str)
/*     */   {
/*     */     try
/*     */     {
/*  65 */       return new ObjectName(str); } catch (MalformedObjectNameException ex) {
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  81 */     if (this == obj) {
/*  82 */       return true;
/*     */     }
/*     */ 
/*  85 */     if (!(obj instanceof AMXClient)) {
/*  86 */       return false;
/*     */     }
/*     */ 
/*  89 */     AMXClient other = (AMXClient)obj;
/*     */ 
/*  91 */     return this.oname.equals(other.oname);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  96 */     int hash = 5;
/*  97 */     hash = 47 * hash + (this.oname != null ? this.oname.hashCode() : 0);
/*  98 */     return hash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return "AMXClient[" + this.oname + "]";
/*     */   }
/*     */ 
/*     */   private <T> T fetchAttribute(String name, Class<T> type) {
/*     */     try {
/* 108 */       Object obj = this.server.getAttribute(this.oname, name);
/* 109 */       if (NULL_OBJECTNAME.equals(obj)) {
/* 110 */         return null;
/*     */       }
/* 112 */       return type.cast(obj);
/*     */     }
/*     */     catch (JMException exc) {
/* 115 */       throw new GmbalException("Exception in fetchAttribute", exc);
/*     */     } catch (IOException exc) {
/* 117 */       throw new GmbalException("Exception in fetchAttribute", exc);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AMXClient(MBeanServerConnection server, ObjectName oname)
/*     */   {
/* 123 */     this.server = server;
/* 124 */     this.oname = oname;
/*     */   }
/*     */ 
/*     */   private AMXClient makeAMX(ObjectName on) {
/* 128 */     if (on == null) {
/* 129 */       return null;
/*     */     }
/* 131 */     return new AMXClient(this.server, on);
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 135 */     return (String)fetchAttribute("Name", String.class);
/*     */   }
/*     */ 
/*     */   public Map<String, ?> getMeta() {
/*     */     try {
/* 140 */       ModelMBeanInfo mbi = (ModelMBeanInfo)this.server.getMBeanInfo(this.oname);
/* 141 */       Descriptor desc = mbi.getMBeanDescriptor();
/* 142 */       Map result = new HashMap();
/* 143 */       for (String str : desc.getFieldNames()) {
/* 144 */         result.put(str, desc.getFieldValue(str));
/*     */       }
/* 146 */       return result;
/*     */     } catch (MBeanException ex) {
/* 148 */       throw new GmbalException("Exception in getMeta", ex);
/*     */     } catch (RuntimeOperationsException ex) {
/* 150 */       throw new GmbalException("Exception in getMeta", ex);
/*     */     } catch (InstanceNotFoundException ex) {
/* 152 */       throw new GmbalException("Exception in getMeta", ex);
/*     */     } catch (IntrospectionException ex) {
/* 154 */       throw new GmbalException("Exception in getMeta", ex);
/*     */     } catch (ReflectionException ex) {
/* 156 */       throw new GmbalException("Exception in getMeta", ex);
/*     */     } catch (IOException ex) {
/* 158 */       throw new GmbalException("Exception in getMeta", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AMXClient getParent() {
/* 163 */     ObjectName res = (ObjectName)fetchAttribute("Parent", ObjectName.class);
/* 164 */     return makeAMX(res);
/*     */   }
/*     */ 
/*     */   public AMXClient[] getChildren() {
/* 168 */     ObjectName[] onames = (ObjectName[])fetchAttribute("Children", [Ljavax.management.ObjectName.class);
/*     */ 
/* 170 */     return makeAMXArray(onames);
/*     */   }
/*     */ 
/*     */   private AMXClient[] makeAMXArray(ObjectName[] onames) {
/* 174 */     AMXClient[] result = new AMXClient[onames.length];
/* 175 */     int ctr = 0;
/* 176 */     for (ObjectName on : onames) {
/* 177 */       result[(ctr++)] = makeAMX(on);
/*     */     }
/*     */ 
/* 180 */     return result;
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String attribute) {
/*     */     try {
/* 185 */       return this.server.getAttribute(this.oname, attribute);
/*     */     } catch (MBeanException ex) {
/* 187 */       throw new GmbalException("Exception in getAttribute", ex);
/*     */     } catch (AttributeNotFoundException ex) {
/* 189 */       throw new GmbalException("Exception in getAttribute", ex);
/*     */     } catch (ReflectionException ex) {
/* 191 */       throw new GmbalException("Exception in getAttribute", ex);
/*     */     } catch (InstanceNotFoundException ex) {
/* 193 */       throw new GmbalException("Exception in getAttribute", ex);
/*     */     } catch (IOException ex) {
/* 195 */       throw new GmbalException("Exception in getAttribute", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, Object value) {
/* 200 */     Attribute attr = new Attribute(name, value);
/* 201 */     setAttribute(attr);
/*     */   }
/*     */ 
/*     */   public void setAttribute(Attribute attribute) {
/*     */     try {
/* 206 */       this.server.setAttribute(this.oname, attribute);
/*     */     } catch (InstanceNotFoundException ex) {
/* 208 */       throw new GmbalException("Exception in setAttribute", ex);
/*     */     } catch (AttributeNotFoundException ex) {
/* 210 */       throw new GmbalException("Exception in setAttribute", ex);
/*     */     } catch (InvalidAttributeValueException ex) {
/* 212 */       throw new GmbalException("Exception in setAttribute", ex);
/*     */     } catch (MBeanException ex) {
/* 214 */       throw new GmbalException("Exception in setAttribute", ex);
/*     */     } catch (ReflectionException ex) {
/* 216 */       throw new GmbalException("Exception in setAttribute", ex);
/*     */     } catch (IOException ex) {
/* 218 */       throw new GmbalException("Exception in setAttribute", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AttributeList getAttributes(String[] attributes) {
/*     */     try {
/* 224 */       return this.server.getAttributes(this.oname, attributes);
/*     */     } catch (InstanceNotFoundException ex) {
/* 226 */       throw new GmbalException("Exception in getAttributes", ex);
/*     */     } catch (ReflectionException ex) {
/* 228 */       throw new GmbalException("Exception in getAttributes", ex);
/*     */     } catch (IOException ex) {
/* 230 */       throw new GmbalException("Exception in getAttributes", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AttributeList setAttributes(AttributeList attributes) {
/*     */     try {
/* 236 */       return this.server.setAttributes(this.oname, attributes);
/*     */     } catch (InstanceNotFoundException ex) {
/* 238 */       throw new GmbalException("Exception in setAttributes", ex);
/*     */     } catch (ReflectionException ex) {
/* 240 */       throw new GmbalException("Exception in setAttributes", ex);
/*     */     } catch (IOException ex) {
/* 242 */       throw new GmbalException("Exception in setAttributes", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException
/*     */   {
/*     */     try {
/* 249 */       return this.server.invoke(this.oname, actionName, params, signature);
/*     */     } catch (InstanceNotFoundException ex) {
/* 251 */       throw new GmbalException("Exception in invoke", ex);
/*     */     } catch (IOException ex) {
/* 253 */       throw new GmbalException("Exception in invoke", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MBeanInfo getMBeanInfo() {
/*     */     try {
/* 259 */       return this.server.getMBeanInfo(this.oname);
/*     */     } catch (InstanceNotFoundException ex) {
/* 261 */       throw new GmbalException("Exception in invoke", ex);
/*     */     } catch (IntrospectionException ex) {
/* 263 */       throw new GmbalException("Exception in invoke", ex);
/*     */     } catch (ReflectionException ex) {
/* 265 */       throw new GmbalException("Exception in invoke", ex);
/*     */     } catch (IOException ex) {
/* 267 */       throw new GmbalException("Exception in invoke", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ObjectName objectName() {
/* 272 */     return this.oname;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.AMXClient
 * JD-Core Version:    0.6.2
 */