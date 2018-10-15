/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tyler
 */
public class EncryptionGui extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        /////Var Decs
        Label encType, encKey, encString, result;
        TextField encKeyTf, encStringTf;
        TextArea encResult;
        ComboBox encTypeCb;
        Button encText;
        String[] encTypes = {
            "AES/CBC/NoPadding",
            "AES/CBC/PKCS5Padding",
            "AES/ECB/NoPadding",
            "AES/ECB/PKCS5Padding",
            /*"DES/CBC/NoPadding",
            "DES/CBC/PKCS5Padding",
            "DES/ECB/NoPadding",
            "DES/ECB/PKCS5Padding",
            "DESede/CBC/NoPadding",
            "DESede/CBC/PKCS5Padding",
            "DESede/ECB/NoPadding",
            "DESede/ECB/PKCS5Padding",
            "RSA/ECB/PKCS1Padding",
            "RSA/ECB/OAEPWithSHA-1AndMGF1Padding",
            "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"*/};
        
        //Labels
        encType = new Label("Encryption Type: ");           root.add(encType, 0, 0, 1, 1);
        encKey = new Label("Encryption Key: ");             root.add(encKey, 0, 1, 1, 1);
        encString = new Label("Text To be Encrypted: ");    root.add(encString, 0, 2, 1, 1);
        result = new Label("Result: ");                     root.add(result, 0, 3, 1, 1);
        
        //Button
        encText = new Button("Encrypt");                    root.add(encText, 0, 5, 1, 1);
        
        //Text Fields
        encKeyTf = new TextField("");                       root.add(encKeyTf, 1, 1, 1, 1);         encKeyTf.setMaxWidth(300);
        encStringTf = new TextField("");                    root.add(encStringTf, 1, 2, 1, 1);      encStringTf.setMaxWidth(300);
        
        //Text Area
        encResult = new TextArea("");                       root.add(encResult, 0, 4, 2, 1);        encResult.setMaxWidth(413);
        
        //Combo Box
        encTypeCb = new ComboBox();                         root.add(encTypeCb, 1, 0, 1, 1);        encTypeCb.setMaxWidth(300);     encTypeCb.getItems().addAll((Object[]) encTypes);
        
        //Stage
        Scene scene = new Scene(root, 435, 294);
        primaryStage.setTitle("Encryption ");
        root.setBackground(new Background(new BackgroundFill(Color.web("#d3d4d3"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10,10,10,10));
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Actions
        encText.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                setKey(encKeyTf.getText());
                encResult.setText(encString(encTypeCb.getValue().toString(),encKeyTf.getText(),encStringTf.getText()));
            }
        });
    }
    
    public static void main(String[] args){ 
        launchGui();
    }

    public static void launchGui(){
        launch();
    }
    
    public static String encString(String encType, String encString, String encKey ){
        
        try {
            setKey(encString);
            Cipher c = Cipher.getInstance(encType);
            c.init(Cipher.ENCRYPT_MODE,setKey(encString));
            return Base64.getEncoder().encodeToString(c.doFinal(encString.getBytes("UTF-8")));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
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
            Logger.getLogger(EncryptionGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return secretKey;
        
    }
}
