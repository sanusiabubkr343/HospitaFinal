package com.example.hospitalwaka.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkChecker {
    public  void  hasNetwork(final AsynchronousResponse callBack)
    {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 600);
            sock.close();
            callBack.processFinished(true);
        } catch (IOException e) {
            callBack.processFinished(false); }
    }
}
