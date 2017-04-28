package com.example.sqisoft.step4video.Activity.Socket;

/**
 * Created by SQISOFT on 2017-04-26.
 */

import com.example.sqisoft.step4video.Activity.Activity.MainActivity;
import com.example.sqisoft.step4video.Activity.Manager.DataManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

    public static final int ServerPort = 8118;
    public static String ServerIP = "xxx.xxx.xxx.xxxx";

    public TCPServer(String IP){

        ServerIP = IP;
        System.out.println("S: Android Server IP = "+ServerIP+"  *************************************************************************");
    }

    @Override
    public void run() {

        try {
            System.out.println("S: Connecting...***************************************************************************************");
            ServerSocket serverSocket = new ServerSocket(); // <-- create an unbound socket first
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(ServerPort)); // <-- now bind it


            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("S: Receiving...");
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
                    String str = in.readLine();
                    str = str.substring(8, str.length());
                    System.out.println("S: Received: '" + str + "'");

                    ((MainActivity)DataManager.getInstance().getActivity()).playVideo(str);

                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                    out.println("Server Received " + str);
                } catch (Exception e) {
                    System.out.println("S: Error");
                    e.printStackTrace();
                } finally {
                    client.close();
                    System.out.println("S: Done.");
                }
            }
        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
    }




}


