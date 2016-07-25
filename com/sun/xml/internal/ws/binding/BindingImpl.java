/*     */ package com.sun.xml.internal.ws.binding;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*     */ import com.sun.xml.internal.ws.developer.BindingTypeFeature;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.activation.CommandInfo;
/*     */ import javax.activation.CommandMap;
/*     */ import javax.activation.MailcapCommandMap;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ 
/*     */ public abstract class BindingImpl
/*     */   implements WSBinding
/*     */ {
/*     */   protected HandlerConfiguration handlerConfig;
/*     */   private final BindingID bindingId;
/*  70 */   protected final WebServiceFeatureList features = new WebServiceFeatureList();
/*     */ 
/*  72 */   protected Service.Mode serviceMode = Service.Mode.PAYLOAD;
/*     */ 
/*     */   protected BindingImpl(BindingID bindingId) {
/*  75 */     this.bindingId = bindingId;
/*  76 */     this.handlerConfig = new HandlerConfiguration(Collections.emptySet(), Collections.emptyList());
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public List<Handler> getHandlerChain()
/*     */   {
/*  82 */     return this.handlerConfig.getHandlerChain();
/*     */   }
/*     */ 
/*     */   public HandlerConfiguration getHandlerConfig() {
/*  86 */     return this.handlerConfig;
/*     */   }
/*     */ 
/*     */   public void setMode(@NotNull Service.Mode mode)
/*     */   {
/*  91 */     this.serviceMode = Service.Mode.MESSAGE;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public BindingID getBindingId()
/*     */   {
/*  97 */     return this.bindingId;
/*     */   }
/*     */ 
/*     */   public final SOAPVersion getSOAPVersion() {
/* 101 */     return this.bindingId.getSOAPVersion();
/*     */   }
/*     */ 
/*     */   public AddressingVersion getAddressingVersion()
/*     */   {
/*     */     AddressingVersion addressingVersion;
/*     */     AddressingVersion addressingVersion;
/* 106 */     if (this.features.isEnabled(AddressingFeature.class)) {
/* 107 */       addressingVersion = AddressingVersion.W3C;
/*     */     }
/*     */     else
/*     */     {
/*     */       AddressingVersion addressingVersion;
/* 108 */       if (this.features.isEnabled(MemberSubmissionAddressingFeature.class))
/* 109 */         addressingVersion = AddressingVersion.MEMBER;
/*     */       else
/* 111 */         addressingVersion = null; 
/*     */     }
/* 112 */     return addressingVersion;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final Codec createCodec()
/*     */   {
/* 122 */     initializeJavaActivationHandlers();
/*     */ 
/* 124 */     return this.bindingId.createEncoder(this);
/*     */   }
/*     */ 
/*     */   public static BindingImpl create(@NotNull BindingID bindingId) {
/* 128 */     if (bindingId.equals(BindingID.XML_HTTP)) {
/* 129 */       return new HTTPBindingImpl();
/*     */     }
/* 131 */     return new SOAPBindingImpl(bindingId);
/*     */   }
/*     */ 
/*     */   public static BindingImpl create(@NotNull BindingID bindingId, WebServiceFeature[] features)
/*     */   {
/* 136 */     for (WebServiceFeature feature : features) {
/* 137 */       if ((feature instanceof BindingTypeFeature)) {
/* 138 */         BindingTypeFeature f = (BindingTypeFeature)feature;
/* 139 */         bindingId = BindingID.parse(f.getBindingId());
/*     */       }
/*     */     }
/* 142 */     if (bindingId.equals(BindingID.XML_HTTP)) {
/* 143 */       return new HTTPBindingImpl();
/*     */     }
/* 145 */     return new SOAPBindingImpl(bindingId, features);
/*     */   }
/*     */ 
/*     */   public static WSBinding getDefaultBinding() {
/* 149 */     return new SOAPBindingImpl(BindingID.SOAP11_HTTP);
/*     */   }
/*     */ 
/*     */   public String getBindingID() {
/* 153 */     return this.bindingId.toString();
/*     */   }
/*     */   @Nullable
/*     */   public <F extends WebServiceFeature> F getFeature(@NotNull Class<F> featureType) {
/* 157 */     return this.features.get(featureType);
/*     */   }
/*     */ 
/*     */   public boolean isFeatureEnabled(@NotNull Class<? extends WebServiceFeature> feature) {
/* 161 */     return this.features.isEnabled(feature);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WebServiceFeatureList getFeatures() {
/* 166 */     return this.features;
/*     */   }
/*     */ 
/*     */   public void setFeatures(WebServiceFeature[] newFeatures)
/*     */   {
/* 171 */     if (newFeatures != null)
/* 172 */       for (WebServiceFeature f : newFeatures)
/* 173 */         this.features.add(f);
/*     */   }
/*     */ 
/*     */   public void addFeature(@NotNull WebServiceFeature newFeature)
/*     */   {
/* 179 */     this.features.add(newFeature);
/*     */   }
/*     */ 
/*     */   public static void initializeJavaActivationHandlers()
/*     */   {
/*     */     try {
/* 185 */       CommandMap map = CommandMap.getDefaultCommandMap();
/* 186 */       if ((map instanceof MailcapCommandMap)) {
/* 187 */         MailcapCommandMap mailMap = (MailcapCommandMap)map;
/*     */ 
/* 190 */         if (!cmdMapInitialized(mailMap)) {
/* 191 */           mailMap.addMailcap("text/xml;;x-java-content-handler=com.sun.xml.internal.ws.encoding.XmlDataContentHandler");
/* 192 */           mailMap.addMailcap("application/xml;;x-java-content-handler=com.sun.xml.internal.ws.encoding.XmlDataContentHandler");
/* 193 */           mailMap.addMailcap("image/*;;x-java-content-handler=com.sun.xml.internal.ws.encoding.ImageDataContentHandler");
/* 194 */           mailMap.addMailcap("text/plain;;x-java-content-handler=com.sun.xml.internal.ws.encoding.StringDataContentHandler");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean cmdMapInitialized(MailcapCommandMap mailMap) {
/* 203 */     CommandInfo[] commands = mailMap.getAllCommands("text/xml");
/* 204 */     if ((commands == null) || (commands.length == 0)) {
/* 205 */       return false;
/*     */     }
/*     */ 
/* 213 */     String saajClassName = "com.sun.xml.internal.messaging.saaj.soap.XmlDataContentHandler";
/* 214 */     String jaxwsClassName = "com.sun.xml.internal.ws.encoding.XmlDataContentHandler";
/* 215 */     for (CommandInfo command : commands) {
/* 216 */       String commandClass = command.getCommandClass();
/* 217 */       if ((saajClassName.equals(commandClass)) || (jaxwsClassName.equals(commandClass)))
/*     */       {
/* 219 */         return true;
/*     */       }
/*     */     }
/* 222 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.binding.BindingImpl
 * JD-Core Version:    0.6.2
 */