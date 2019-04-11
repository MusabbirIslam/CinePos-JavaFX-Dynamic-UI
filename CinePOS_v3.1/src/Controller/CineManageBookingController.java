package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class CineManageBookingController implements Initializable{
    
    
    private CinePOS stage;
    private Stage popUpStage;
    private Parent root;
    
    @FXML
    private TextField bookingCodeTextField;

    @FXML
    private Button resendCodeButton;

    @FXML
    private Button printTicketButton;
    
    @FXML
    private Label popUpTitleLabel;

    @FXML
    private Label popUpDataLabel;

    @FXML
    private Button popUpPrintButton;

    @FXML
    private Button popUpCancelButton;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    
    
    public void setStage(CinePOS stage)
    {
        this.stage=stage;
        loadPopUpWindow();
    }
    
    //-----------loading the pop up window -----------
    private void loadPopUpWindow()
    {    
        try{
            popUpStage=new Stage();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CineManageBookingPopUp.fxml"));
            loader.setController(this);
            
            root =(Parent)loader.load();
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(bookingCodeTextField.getScene().getWindow());
            }catch(Exception ex){System.out.println("Error in resend pop up manage booking scene "+ex.getMessage());};
    }
   
    //-----showing the pop up window----------
    private void showPopUpWindow()
    {
        popUpStage.showAndWait();        
    }
    
    //-----changing the text for resend code button click------ 
    private void initializingComponentForResendCode()
    {
        popUpTitleLabel.setText("Sending Code");
        popUpDataLabel.setText("Sending Code ..... 12345567890");
        popUpPrintButton.setText("Ok");    
    }
    
    //-----changing the text for resend code button click------ 
    private void initializingComponentForPrint()
    {
        popUpTitleLabel.setText("Booking Details");
        popUpDataLabel.setText("Booking code 123456789"); 
        popUpPrintButton.setText("Print"); 
    }
    //-------Action listener for the FXML Button of cine manage booking pop up---
        @FXML
    void printTicketButtonAction(ActionEvent event) {
        initializingComponentForPrint();
        showPopUpWindow();
    }
    
    @FXML
    void resendCodeButtonAction(ActionEvent event) {
        initializingComponentForResendCode();
        showPopUpWindow();
    }
    
        @FXML
    void popUpCancelButtonAction(ActionEvent event) {
        popUpStage.close();
    }

    @FXML
    void popUpPrintButtonAction(ActionEvent event) {
        
        //---for detecting which pop up showed and which action to perform---
        if(popUpPrintButton.getText().equals("Ok"))
        {
            //---code for resend pop up windows "OK" button
            popUpStage.close();
            
        }
        else
        {
            //---code for print pop up windows "Print" button
            popUpStage.close();
        }
    }

    public void toHome(ActionEvent event){

        System.out.println("working !");
        stage.home();
    }
    
}
