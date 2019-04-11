/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author CST
 */
public class CineFrontPageController implements Initializable {
    
     private CinePOS stage;
     
        
        
    @FXML
    private Button showingTodayButton;

    @FXML
    private Button futurePerformanceButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button manageBookingsButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
        @FXML
    void futurePerformanceButtonAction(ActionEvent event) {
        stage.showFutureCineTableScene();
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        stage.showCineAdminScene();
    }

    @FXML
    void manageBookingsButtonAction(ActionEvent event) {
        stage.showManageBookingScene();
    }

    @FXML
    void showingTodayButtonAction(ActionEvent event) {
        stage.showTodayCineTableScene();
    }
    
    //---SAVING STAGE FRO CHANGING SCENE
    public void setStage(CinePOS stage)
    {
        this.stage=stage;
    }
    
}
