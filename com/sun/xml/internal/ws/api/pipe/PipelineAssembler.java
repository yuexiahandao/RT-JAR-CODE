package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;

public abstract interface PipelineAssembler
{
  @NotNull
  public abstract Pipe createClient(@NotNull ClientPipeAssemblerContext paramClientPipeAssemblerContext);

  @NotNull
  public abstract Pipe createServer(@NotNull ServerPipeAssemblerContext paramServerPipeAssemblerContext);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.PipelineAssembler
 * JD-Core Version:    0.6.2
 */