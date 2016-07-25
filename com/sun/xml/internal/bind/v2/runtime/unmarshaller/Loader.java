/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.xml.bind.Unmarshaller.Listener;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Loader
/*     */ {
/*     */   protected boolean expectText;
/*     */ 
/*     */   protected Loader(boolean expectText)
/*     */   {
/*  49 */     this.expectText = expectText;
/*     */   }
/*     */ 
/*     */   protected Loader()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void childElement(UnmarshallingContext.State state, TagName ea)
/*     */     throws SAXException
/*     */   {
/*  90 */     reportUnexpectedChildElement(ea, true);
/*  91 */     state.setLoader(Discarder.INSTANCE);
/*  92 */     state.setReceiver(null);
/*     */   }
/*     */ 
/*     */   protected final void reportUnexpectedChildElement(TagName ea, boolean canRecover) throws SAXException
/*     */   {
/*  97 */     if ((canRecover) && (!UnmarshallingContext.getInstance().parent.hasEventHandler()))
/*     */     {
/* 101 */       return;
/* 102 */     }if ((ea.uri != ea.uri.intern()) || (ea.local != ea.local.intern()))
/* 103 */       reportError(Messages.UNINTERNED_STRINGS.format(new Object[0]), canRecover);
/*     */     else
/* 105 */       reportError(Messages.UNEXPECTED_ELEMENT.format(new Object[] { ea.uri, ea.local, computeExpectedElements() }), canRecover);
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedChildElements()
/*     */   {
/* 112 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedAttributes()
/*     */   {
/* 119 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   public void text(UnmarshallingContext.State state, CharSequence text)
/*     */     throws SAXException
/*     */   {
/* 131 */     text = text.toString().replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').trim();
/* 132 */     reportError(Messages.UNEXPECTED_TEXT.format(new Object[] { text }), true);
/*     */   }
/*     */ 
/*     */   public final boolean expectText()
/*     */   {
/* 140 */     return this.expectText;
/*     */   }
/*     */ 
/*     */   public void leaveElement(UnmarshallingContext.State state, TagName ea)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   private String computeExpectedElements()
/*     */   {
/* 170 */     StringBuilder r = new StringBuilder();
/*     */ 
/* 172 */     for (QName n : getExpectedChildElements()) {
/* 173 */       if (r.length() != 0) r.append(',');
/* 174 */       r.append("<{").append(n.getNamespaceURI()).append('}').append(n.getLocalPart()).append('>');
/*     */     }
/* 176 */     if (r.length() == 0) {
/* 177 */       return "(none)";
/*     */     }
/*     */ 
/* 180 */     return r.toString();
/*     */   }
/*     */ 
/*     */   protected final void fireBeforeUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state)
/*     */     throws SAXException
/*     */   {
/* 190 */     if (beanInfo.lookForLifecycleMethods()) {
/* 191 */       UnmarshallingContext context = state.getContext();
/* 192 */       Unmarshaller.Listener listener = context.parent.getListener();
/* 193 */       if (beanInfo.hasBeforeUnmarshalMethod()) {
/* 194 */         beanInfo.invokeBeforeUnmarshalMethod(context.parent, child, state.getPrev().getTarget());
/*     */       }
/* 196 */       if (listener != null)
/* 197 */         listener.beforeUnmarshal(child, state.getPrev().getTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void fireAfterUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state)
/*     */     throws SAXException
/*     */   {
/* 210 */     if (beanInfo.lookForLifecycleMethods()) {
/* 211 */       UnmarshallingContext context = state.getContext();
/* 212 */       Unmarshaller.Listener listener = context.parent.getListener();
/* 213 */       if (beanInfo.hasAfterUnmarshalMethod()) {
/* 214 */         beanInfo.invokeAfterUnmarshalMethod(context.parent, child, state.getTarget());
/*     */       }
/* 216 */       if (listener != null)
/* 217 */         listener.afterUnmarshal(child, state.getTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static void handleGenericException(Exception e)
/*     */     throws SAXException
/*     */   {
/* 226 */     handleGenericException(e, false);
/*     */   }
/*     */ 
/*     */   public static void handleGenericException(Exception e, boolean canRecover) throws SAXException {
/* 230 */     reportError(e.getMessage(), e, canRecover);
/*     */   }
/*     */ 
/*     */   public static void handleGenericError(Error e) throws SAXException {
/* 234 */     reportError(e.getMessage(), false);
/*     */   }
/*     */ 
/*     */   protected static void reportError(String msg, boolean canRecover) throws SAXException {
/* 238 */     reportError(msg, null, canRecover);
/*     */   }
/*     */ 
/*     */   public static void reportError(String msg, Exception nested, boolean canRecover) throws SAXException {
/* 242 */     UnmarshallingContext context = UnmarshallingContext.getInstance();
/* 243 */     context.handleEvent(new ValidationEventImpl(canRecover ? 1 : 2, msg, context.getLocator().getLocation(), nested), canRecover);
/*     */   }
/*     */ 
/*     */   protected static void handleParseConversionException(UnmarshallingContext.State state, Exception e)
/*     */     throws SAXException
/*     */   {
/* 256 */     state.getContext().handleError(e);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
 * JD-Core Version:    0.6.2
 */