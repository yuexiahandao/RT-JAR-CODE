/*    */ package com.sun.corba.se.spi.orb;
/*    */ 
/*    */ import com.sun.corba.se.impl.orb.ParserAction;
/*    */ import com.sun.corba.se.impl.orb.ParserActionFactory;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class PropertyParser
/*    */ {
/*    */   private List actions;
/*    */ 
/*    */   public PropertyParser()
/*    */   {
/* 42 */     this.actions = new LinkedList();
/*    */   }
/*    */ 
/*    */   public PropertyParser add(String paramString1, Operation paramOperation, String paramString2)
/*    */   {
/* 48 */     this.actions.add(ParserActionFactory.makeNormalAction(paramString1, paramOperation, paramString2));
/*    */ 
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   public PropertyParser addPrefix(String paramString1, Operation paramOperation, String paramString2, Class paramClass)
/*    */   {
/* 56 */     this.actions.add(ParserActionFactory.makePrefixAction(paramString1, paramOperation, paramString2, paramClass));
/*    */ 
/* 58 */     return this;
/*    */   }
/*    */ 
/*    */   public Map parse(Properties paramProperties)
/*    */   {
/* 65 */     HashMap localHashMap = new HashMap();
/* 66 */     Iterator localIterator = this.actions.iterator();
/* 67 */     while (localIterator.hasNext()) {
/* 68 */       ParserAction localParserAction = (ParserAction)localIterator.next();
/*    */ 
/* 70 */       Object localObject = localParserAction.apply(paramProperties);
/*    */ 
/* 74 */       if (localObject != null) {
/* 75 */         localHashMap.put(localParserAction.getFieldName(), localObject);
/*    */       }
/*    */     }
/* 78 */     return localHashMap;
/*    */   }
/*    */ 
/*    */   public Iterator iterator()
/*    */   {
/* 83 */     return this.actions.iterator();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.PropertyParser
 * JD-Core Version:    0.6.2
 */