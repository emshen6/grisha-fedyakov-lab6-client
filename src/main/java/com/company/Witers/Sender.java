package com.company.Witers;


import com.company.Command;
import com.company.Commands.Empty;
import com.company.Helpes.Parser;
import com.company.Writer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Sender {

    private static Sender instance;
    public static DatagramChannel socket;
    public static InetAddress address_server;
    public static int port_server;

    private Sender(InetAddress address, int port) {
        try {
            address_server = address;
            socket = DatagramChannel.open();
            port_server = port;
            socket.socket().bind(new InetSocketAddress(port));
        }
        catch (Exception ignored){
            System.out.println("1");
        }
    }

    public static Sender getInstance() {
        if(instance == null){
            try { Init(InetAddress.getByName("192.168.5.1"), 1111); }
            catch(UnknownHostException ignored){} catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    public static void Init(InetAddress address_server, int port_server) throws IOException {
        instance = new Sender(address_server ,port_server);
    }
    public Writer Recieve(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            socket.receive(buffer);
            return Parser.GetResponce(buffer.array());
        }
        catch (IOException e){
            Printer.getInstance().WriteLine("ошибка принятия сообщения" + e.getMessage());
            return null;
        }
    }

    public void Send(Empty o) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(Parser.GetCommand(o));
        socket.send(buffer, new InetSocketAddress(address_server, port_server + 1));
    }
}
