//icsd13170 ilias skordas

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        ArrayList<Eggrafi> arr = new ArrayList<Eggrafi>();

        try {
            ServerSocket server = new ServerSocket(8080);
            ObjectOutputStream out_file = new ObjectOutputStream(new FileOutputStream("Eggrafes.txt", true));

            while (true) {

                System.out.println("Waiting Incoming Connection...");
                System.out.println("Local Address :" + server.getInetAddress() + " Port :" + server.getLocalPort());
                Socket sock = server.accept();
                System.out.println("Connection adapted!");

                //ftiaxnw ta streams
                ObjectOutputStream serverOutputStream = new ObjectOutputStream(sock.getOutputStream());
                ObjectInputStream serverInputStream = new ObjectInputStream(sock.getInputStream());

                Message m1 = (Message) serverInputStream.readObject();

                if (m1.getType().equals("START")) {
                    //apantaw WAITING me tin dimiourgia enos antikimenou tupou message
                    serverOutputStream.writeObject(new Message("WAITING"));
                    serverOutputStream.flush();
                    Message m2 = (Message) serverInputStream.readObject();
                    if (m2.getType().equals("INSERT")) {
                        serverOutputStream.writeObject(new Message("OK"));
                        serverOutputStream.flush();
                        if (!m2.getList().isEmpty()) {
                            out_file.writeObject((ArrayList) m2.getList());
                        }

                    }

                }

            }

            

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
