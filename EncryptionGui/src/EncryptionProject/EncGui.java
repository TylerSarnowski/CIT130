package EncryptionProject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tyler_sarnowski
 */
public class EncGui extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        //Declare Var
        Label encPublicKey,encPrivateKey,encString,encryption;
        TextField encPublicKeyTF,encPrivateKeyTF,encStringTF;
        TextArea encryptionTA;
        Button encryptionButton,exitButton;
        TabPane mainPane;
        Tab enc,dec;
        //Enstatiate Gui Elements - Add to GridPane
        //Labels
        encPublicKey = new Label("Encryption Public Key: ");    root.add(encPublicKey, 0, 1, 1, 1);
        encPrivateKey = new Label("Encryption Private Key: ");  root.add(encPrivateKey, 0, 2, 1, 1);    
        encString = new Label("Message to be Encrypted: ");     root.add(encString,0,3,1,1);
        encryption = new Label("Encrypted Message: ");          root.add(encryption,0,4,1,1);
        //Text Fields
        encPublicKeyTF = new TextField("");                     root.add(encPublicKeyTF,1,1,1,1);
        encPrivateKeyTF = new TextField("");                    root.add(encPrivateKeyTF,1,2,1,1);      encPrivateKeyTF.setEditable(false);
        encStringTF = new TextField("");                        root.add(encStringTF,1,3,1,1);
        //TextArea
        encryptionTA = new TextArea();                          root.add(encryptionTA,0,5,2,1);         encryptionTA.setEditable(false);
        //Buttons
        encryptionButton = new Button("Encrypt");               root.add(encryptionButton,0,6,1,1);     
        exitButton = new Button("Exit");                        root.add(exitButton,1,6,1,1);
        //Tabs
        enc = new Tab("Encryption");                            //enc.setTooltip(new ToolTip("Encyrption Mode"));
        dec = new Tab("Decryption");
        //Tab Pane
        //mainPane = new TabPane();                               mainPane.getTabs().addAll(enc,dec);     root.add(mainPane, 0, 0, 2, 1);         mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        //Stage
        Scene scene = new Scene(root, 500, 350);
        primaryStage.setTitle("Encryption ");
        root.setBackground(new Background(new BackgroundFill(Color.web("#d3d4d3"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10,10,10,10));
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //ActionListeners
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        
        encryptionButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                encryptionTA.setText("");
                //setKey(encPublicKey.getText());
                encryptionTA.setText(encString(encPublicKey.getText(),encStringTF.getText()));
                encPrivateKeyTF.setText(Base64.getEncoder().encodeToString(setKey(encPublicKeyTF.getText()).getEncoded()));
            }
        });
        
    }
    
    
        
    public static void main(String[] args){ 
        launch();
    }
    
    //encrypts a string through magic and mystery
    public static String encString(String encString, String encKey ){
        
        try {
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE,setKey(encKey));
            return Base64.getEncoder().encodeToString(c.doFinal(encString.getBytes("UTF-8")));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    //imagine a meat grinder when you put a full steak in and it spits out a jumbled mess
    public static SecretKeySpec setKey(String myKey){
        SecretKeySpec secretKey = null;
        byte[] key;
        MessageDigest sha = null;
        
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key,"AES");
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(EncGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return secretKey;
        
    }
    
}
