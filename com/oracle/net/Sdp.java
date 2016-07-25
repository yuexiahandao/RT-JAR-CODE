package com.oracle.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

import sun.net.sdp.SdpSupport;
import sun.nio.ch.Secrets;

public final class Sdp {
    private static final Constructor<ServerSocket> serverSocketCtor;
    private static final Constructor<SocketImpl> socketImplCtor;

    private static void setAccessible(AccessibleObject paramAccessibleObject) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Void run() {
                this.val$o.setAccessible(true);
                return null;
            }
        });
    }

    private static SocketImpl createSocketImpl() {
        try {
            return (SocketImpl) socketImplCtor.newInstance(new Object[0]);
        } catch (InstantiationException localInstantiationException) {
            throw new AssertionError(localInstantiationException);
        } catch (IllegalAccessException localIllegalAccessException) {
            throw new AssertionError(localIllegalAccessException);
        } catch (InvocationTargetException localInvocationTargetException) {
            throw new AssertionError(localInvocationTargetException);
        }
    }

    public static Socket openSocket()
            throws IOException {
        SocketImpl localSocketImpl = createSocketImpl();
        return new SdpSocket(localSocketImpl);
    }

    public static ServerSocket openServerSocket()
            throws IOException {
        SocketImpl localSocketImpl = createSocketImpl();
        try {
            return (ServerSocket) serverSocketCtor.newInstance(new Object[]{localSocketImpl});
        } catch (IllegalAccessException localIllegalAccessException) {
            throw new AssertionError(localIllegalAccessException);
        } catch (InstantiationException localInstantiationException) {
            throw new AssertionError(localInstantiationException);
        } catch (InvocationTargetException localInvocationTargetException) {
            Throwable localThrowable = localInvocationTargetException.getCause();
            if ((localThrowable instanceof IOException))
                throw ((IOException) localThrowable);
            if ((localThrowable instanceof RuntimeException))
                throw ((RuntimeException) localThrowable);
            throw new RuntimeException(localInvocationTargetException);
        }
    }

    public static SocketChannel openSocketChannel()
            throws IOException {
        FileDescriptor localFileDescriptor = SdpSupport.createSocket();
        return Secrets.newSocketChannel(localFileDescriptor);
    }

    public static ServerSocketChannel openServerSocketChannel()
            throws IOException {
        FileDescriptor localFileDescriptor = SdpSupport.createSocket();
        return Secrets.newServerSocketChannel(localFileDescriptor);
    }

    static {
        try {
            serverSocketCtor = ServerSocket.class.getDeclaredConstructor(new Class[]{SocketImpl.class});

            setAccessible(serverSocketCtor);
        } catch (NoSuchMethodException localNoSuchMethodException1) {
            throw new AssertionError(localNoSuchMethodException1);
        }

        try {
            Class localClass = Class.forName("java.net.SdpSocketImpl", true, null);
            socketImplCtor = localClass.getDeclaredConstructor(new Class[0]);
            setAccessible(socketImplCtor);
        } catch (ClassNotFoundException localClassNotFoundException) {
            throw new AssertionError(localClassNotFoundException);
        } catch (NoSuchMethodException localNoSuchMethodException2) {
            throw new AssertionError(localNoSuchMethodException2);
        }
    }

    private static class SdpSocket extends Socket {
        SdpSocket(SocketImpl paramSocketImpl)
                throws SocketException {
            super();
        }
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.oracle.net.Sdp
 * JD-Core Version:    0.6.2
 */