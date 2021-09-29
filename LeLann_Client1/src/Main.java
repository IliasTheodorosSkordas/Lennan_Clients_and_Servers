//icsd13170 ilias theodoros skordas
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    //arxizei prwtos
    static ObjectInputStream main_server_InputStream;
    static ObjectOutputStream main_server_OutputStream;
    static boolean flag = false;
    static boolean token = true;

    static ArrayList<Eggrafi> arr1 = new ArrayList<Eggrafi>();

    public static void main(String[] args) throws IOException {

        String AM = "";
        String name;
        float grade;
        ServerSocket server = new ServerSocket(8081);
        Socket sock;
        Socket socketConnection2 = null;

        while (true) {
            //an exei to token
            if (token == true) {
                while (!AM.equals("OK")) {
                    System.out.println("Enter AM or hit \"OK\" if u want to terminate the proccess: ");
                    Scanner scanner = new Scanner(System.in);
                    AM = scanner.next();

                    if (!AM.equals("OK")) {
                        System.out.println("Enter Name: ");
                        name = scanner.next();
                        System.out.println("Enter Grade: ");
                        grade = Float.parseFloat(scanner.next());
                        arr1.add(new Eggrafi(AM, name, grade));
                        flag = true;

                    } else if (AM.equals("OK")) {
                        //establish connection with the main server at socket 8080
                        Socket socketConnection1;
                        try {
                            socketConnection1 = new Socket("127.0.0.1", 8080);
                            //ftiaxnw ta streams
                            main_server_InputStream = new ObjectInputStream(socketConnection1.getInputStream());
                            main_server_OutputStream = new ObjectOutputStream(socketConnection1.getOutputStream());
                            main_server_OutputStream.writeObject(new Message("START"));

                            main_server_OutputStream.flush();

                            Message m2 = (Message) main_server_InputStream.readObject();
                            if (m2.getType().equals("WAITING")) {
                                main_server_OutputStream.writeObject(new Message("INSERT", arr1));
                                main_server_OutputStream.flush();
                                arr1.clear();

                                Message m3 = (Message) main_server_InputStream.readObject();
                                if (m3.getType().equals("OK")) {
                                    main_server_OutputStream.close();
                                    main_server_InputStream.close();
                                    socketConnection1.close();
                                    if (socketConnection2 == null) {
                                        socketConnection2 = new Socket("127.0.0.1", 51840);

                                    }

                                    ObjectOutputStream OutPut_Stream_for_Token = new ObjectOutputStream(socketConnection2.getOutputStream());
                                    ObjectInputStream INPut_Stream_for_Token = new ObjectInputStream(socketConnection2.getInputStream());

                                    OutPut_Stream_for_Token.writeObject(new Message("START"));
                                    Message m4 = (Message) INPut_Stream_for_Token.readObject();
                                    if (m4.getType().equals("WAITING")) {
                                        OutPut_Stream_for_Token.writeObject(new Message("TOKEN"));

                                        token = false;

                                        OutPut_Stream_for_Token.close();
                                        INPut_Stream_for_Token.close();
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

            } else if (token == false) {
                try {

                    System.out.println("Waiting Incoming Connection...Wait for Token");
                    System.out.println("Local Address :" + server.getInetAddress() + " Port :" + server.getLocalPort());
                    sock = server.accept();
                    System.out.println("Connection adapted!");

                    //ftiaxnw ta streams
                    ObjectInputStream serverInputStream = new ObjectInputStream(sock.getInputStream());
                    ObjectOutputStream serverOutputStream = new ObjectOutputStream(sock.getOutputStream());
                    System.out.println("1");
                    Message m1 = (Message) serverInputStream.readObject();
                    System.out.println("2");
                    if (m1.getType().equals("START")) {
                        //apantaw WAITING me tin dimiourgia enos antikimenou tupou message
                        serverOutputStream.writeObject(new Message("WAITING"));

                        Message m2 = (Message) serverInputStream.readObject();
                        if (m2.getType().equals("TOKEN")) {
                            token = true;
                            serverOutputStream.close();
                            serverInputStream.close();
                            System.out.println("3");

                        }

                    }

                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
