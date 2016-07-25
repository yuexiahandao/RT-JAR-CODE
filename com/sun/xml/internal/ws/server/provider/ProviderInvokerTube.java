/*    */ package com.sun.xml.internal.ws.server.provider;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.server.Invoker;
/*    */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*    */ import com.sun.xml.internal.ws.server.InvokerTube;
/*    */ import javax.xml.ws.Provider;
/*    */ 
/*    */ public abstract class ProviderInvokerTube<T> extends InvokerTube<Provider<T>>
/*    */ {
/*    */   protected ProviderArgumentsBuilder<T> argsBuilder;
/*    */ 
/*    */   ProviderInvokerTube(Invoker invoker, ProviderArgumentsBuilder<T> argsBuilder)
/*    */   {
/* 47 */     super(invoker);
/* 48 */     this.argsBuilder = argsBuilder;
/*    */   }
/*    */ 
/*    */   public static <T> ProviderInvokerTube<T> create(Class<T> implType, WSBinding binding, Invoker invoker)
/*    */   {
/* 54 */     ProviderEndpointModel model = new ProviderEndpointModel(implType, binding);
/* 55 */     ProviderArgumentsBuilder argsBuilder = ProviderArgumentsBuilder.create(model, binding);
/* 56 */     if ((binding instanceof SOAPBindingImpl))
/*    */     {
/* 58 */       ((SOAPBindingImpl)binding).setMode(model.mode);
/*    */     }
/*    */ 
/* 61 */     return model.isAsync ? new AsyncProviderInvokerTube(invoker, argsBuilder) : new SyncProviderInvokerTube(invoker, argsBuilder);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.ProviderInvokerTube
 * JD-Core Version:    0.6.2
 */