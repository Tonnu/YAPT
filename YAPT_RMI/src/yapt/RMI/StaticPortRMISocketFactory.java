/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 *
 * @author Toon
 */
public class StaticPortRMISocketFactory extends RMISocketFactory {

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        System.out.format("Opening port %s on host: %s", host, port);
        return new Socket(host, port);
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        port = (port == 0 ? 1098 : port);
        return new ServerSocket(port);
    }

}
