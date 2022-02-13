package com.company;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        //BASE CONNETTION
        URL url = new URL("http://35.228.102.189/api/loginstatus");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        //COOKIE
        connection.setRequestProperty("Cookie","user=Lesha");

        //RESPONSE
        System.out.println(connection.getHeaderField("Set-Cookie"));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        System.out.println(in.readLine());
        in.close();
    }
}
