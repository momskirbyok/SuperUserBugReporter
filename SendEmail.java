/*
 * SendEmail
 * 1/11/17
 * Brandon Reid, Nathan Ingraham, Donovan Clofer, Austin Holford
 * Info: The objective of this class is to take in information from the UI to compose an 
 * email to send off to the developer team (us).
 */
package emaildeveloper;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.mail.*;
import javax.mail.internet.*;  
import java.util.Properties;  




public class SendEmail extends EmailDeveloperUI {
    
    String emailAddress;
    String emailContent;
    
    
    
    SendEmail(String messageRecipient, String messageContent) throws MessagingException{
       
       this.emailAddress = messageRecipient;
       this.emailContent = messageContent;
 
       
       }
    
    
    
    
    public void presentConfirmationBox(String emailAddress, String emailContent) throws MessagingException {
        System.out.println("This method has been called. " + " To: " + emailAddress + " With contents: " + emailContent);
        
        Stage confirmStage = new Stage();
                    
                    
        StackPane confirmPane = new StackPane();
                    
                    
                    
        Button confirmEmailButton = new Button("Confirm");
                    
                   
        confirmEmailButton.setTranslateY(50);
                    
        confirmEmailButton.setTranslateX(-30);
                    
        Button cancelButton = new Button("Cancel");
                    
        cancelButton.setTranslateX(40);
        cancelButton.setTranslateY(50);
                    
        Label confirmItLabel = new Label("Would you like to send the email?");
                    
        confirmItLabel.setTranslateX(0);
        confirmItLabel.setTranslateY(5);
                    
                    
                    
        ImageView questionMarkImageConfirm = new ImageView("resources/questionmark.png");
                    
        questionMarkImageConfirm.setLayoutX(0);
        questionMarkImageConfirm.setLayoutY(0);
        questionMarkImageConfirm.setTranslateY(-30);  
        confirmPane.getChildren().addAll(confirmEmailButton, confirmItLabel, questionMarkImageConfirm, cancelButton);
        Scene confirmScene = new Scene(confirmPane, 400,150);
        confirmStage.initStyle(StageStyle.UNDECORATED); //Prevents user from closing out of the confirmation box
        confirmStage.setResizable(false);
        confirmStage.setTitle("Confirm Email");
        confirmStage.setScene(confirmScene);
        confirmStage.show();
        
        confirmEmailButton.setOnAction(magicAction ->{
                        
                        
          try{
                    
               PrintWriter emailWriter = new PrintWriter("emailLog.txt");
                    
               sendEmailMessage(emailContent, emailAddress);

               emailWriter.println("\n Email with the contents of \"" +  emailContent + "\" was sent to " + emailAddress);
               emailWriter.close();
                    
               System.out.println("\n Email with the contents of \"" +  emailContent + "\" was sent to " + emailAddress);
                    } catch(FileNotFoundException e){
                        System.err.println("The log file for email was not found.");
                        System.out.println(e);
                        
                        }
                      catch(MessagingException e){
                          System.out.println(e);  
                      }
                    
                        
               System.exit(0);
                    
                    });
                    cancelButton.setOnAction(cancelAction ->{
                        System.out.println("Message confirmation dialog cancelled.");
                        confirmStage.close();
                        
                        
                    });
                    
                 }
       
        
    public void sendEmailMessage(String emailContent, String emailAddress) throws MessagingException, AddressException{
        
        
        
                final String emailAccount = "[removed]@gmail.com";
                final String password = "[removed]";
                Properties mailServerProperties;
                Session getMailSession;
                MimeMessage generateMailMessage;
               
		
		
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		
 
		
		/* MAIL SECTION
                * When a message is sent, a copy of it will be sent to the bug submitter
                * The message will be sent to the developer Gmail account &
                * cc'd to all the developers so that we for sure get the bug report.
                */
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAccount));
		generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("exampleemail@gmail.com"));
                
                 
		generateMailMessage.setSubject("New Bug Report COPY For SuperUser From: " + emailAddress);
		String emailBody = "Hello " + emailAddress + "," + " <BR> Thanks for your report. Your report has been sent to the developers of SuperUser. <BR>" + "For your records, this is the report you submitted: <BR><BR>" + emailContent +  "<BR><BR><BR> If you have anything to add to the bug report, feel free to reply to this email." + "<BR><BR> SENT FROM SUPERUSER BUG REPORTER";
                        
		generateMailMessage.setContent(emailBody, "text/html");
		
 
		
		Transport messageTransport = getMailSession.getTransport("smtp");
 
		
		messageTransport.connect("smtp.gmail.com", emailAccount, password);
		messageTransport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		messageTransport.close();
    
 }  
}  
        