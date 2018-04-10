import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.*;
import java.security.spec.*;
import javax.crypto.*;

public class ServerWithSecurity{
    private static final String serverCert = "server.csr";
    private static final String serverDer = "privateServer.der";
    public static void main(String[] args){
        ServerSocket welcomeSocket = null;
		Socket connectionSocket = null;
		DataOutputStream toClient = null;
        DataInputStream fromClient = null;

		FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedFileOutputStream = null;
        String hsMessage = "Hello this is SecStore";
        
        try{
            System.out.println("Initialising...");
            welcomeSocket = new ServerSocket(4321);
            System.out.println("Waiting for Connection...");
            connectionSocket = welcomeSocket.accept();
            System.out.println("Connected");
            fromClient = new DataInputStream(connectionSocket.getInputStream());
            toClient = new DataOutputStream(connectionSocket.getOutputStream());          

            while(!connectionSocket.isClosed()){
                int messageCode = fromClient.readInt();

                //Transfer file name
                if (messageCode == 0){

                //Transfer file
                }else if (messageCode == 1){

                //Close connection
                }else if (messageCode == 2){
                    // fromClient.close();
                    // toClient.close();
                    // connectionSocket.close();
                //Handshake
                }else if (messageCode == 3){
                    System.out.println("Request for handshake");
                    byte[] signMessage = generateSignedMessage(serverDer, hsMessage);
                    toClient.writeInt(3);
                    toClient.writeInt(signMessage.length);
                    System.out.println("Signed Message Length: " + signMessage.length);
                    toClient.write(signMessage);
                    toClient.flush();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static byte[] generateSignedMessage(String privateKeyPath, String message) throws Exception{
        PrivateKey privateKey = loadPrivateKey(privateKeyPath);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        
        return encryptedBytes;
    }

    private static PrivateKey loadPrivateKey(String keyPath) throws Exception{
        KeyFactory kFactory = KeyFactory.getInstance("RSA");
        File privateKey = new File(keyPath);
        KeySpec ks = new PKCS8EncodedKeySpec(Files.readAllBytes(privateKey.toPath()));
        return kFactory.generatePrivate(ks);
    }


}