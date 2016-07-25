package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;

public abstract interface TubelineAssembler
{
  @NotNull
  public abstract Tube createClient(@NotNull ClientTubeAssemblerContext paramClientTubeAssemblerContext);

  @NotNull
  public abstract Tube createServer(@NotNull ServerTubeAssemblerContext paramServerTubeAssemblerContext);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.TubelineAssembler
 * JD-Core Version:    0.6.2
 */