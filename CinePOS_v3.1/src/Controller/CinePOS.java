
package Controller;
import Entity.ConcessionProduct;
import Entity.FilmDetails;
import Entity.FilmSchedule;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.xml.ws.Response;
import java.io.*;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//--------main class for switching scene and holding stage---------
public class CinePOS extends Application {

    private Stage stage;
    private Scene scene;
    public static String serviceUrl = "http://163.53.151.2:8888/cinepos/";
    public static String imageUrl   = "http://163.53.151.2:8888/cinepos//product-image/";

    @FXML
    Button showingTodayButton;

    @FXML
    Button futurePerformanceButton;

    @FXML
    Button loginButton;

    @FXML
    Button manageBookingsButton;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        stage.setTitle("CinPos");
        stage.getIcons().add(new Image("icon.png"));
        stage.setFullScreen(true);
        home();
        stage.show();

    }

    public static void main(String[] args) {

        launch(args);

    }

    public void home()
    {
        try {

            //--loading the cine table-----
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CineFrontPage.fxml"));

            Parent root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);

            //passing this class reference for switching scene----
            CineFrontPageController controller = loader.getController();
            controller.setStage(this);


            stage.show();
        }catch(Exception ex){

        }

    }
        
    public void showTodayCineTableScene() {
        try {
            //--loading the cine table-----
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CineTable.fxml"));
        
        Parent root = (Parent)loader.load() ;
       //changing scene
        scene.setRoot(root);
        
        //passing this class reference for switching scene----
        CineTableController controller =loader.getController();      
        controller.setStage(this);  
        controller.setTodayPerformance("");
        }catch(Exception ex){

        }
    }
    

        
    public void showFutureCineTableScene()
    {
        try{
        //--loading the cine table-----
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CineTable.fxml"));
        
        Parent root = (Parent)loader.load() ;
       //changing scene
        scene.setRoot(root);
        
        //passing this class reference for switching scene----
        CineTableController controller =loader.getController();      
        controller.setStage(this);
        controller.setFuturePerformance();
        }catch(Exception ex){};
    }
    
    //---------------- for show movie details scene according to the button clicked-------------
    public void showMovieDetailsScene(FilmDetails movieDetails)
    {
        try{
            //-----loading the cine detail scene-----
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CineDetails.fxml"));
        
        Parent root = (Parent)loader.load() ;
           
        //changing scene
        scene.setRoot(root);
        
        //passing the movie id to movie details scene controller
        CineDetailsController controller =loader.getController();
            controller.setStage(this);
           // System.out.println("Film name 3: "+movieDetails.filmName);
        controller.setSelectedMovie(movieDetails);
        }catch(Exception ex){};
    }

    public void showManageBookingScene()
    {
        try{
            //-----loading the cine detail scene-----
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CineManageBooking.fxml"));
        
        Parent root = (Parent)loader.load() ;
           
        //changing scene
        scene.setRoot(root);
        
        //passing the movie id to movie details scene controller
        CineManageBookingController controller =loader.getController();      
        controller.setStage(this);
        }catch(Exception ex){System.out.println("Error in show manage booking scene"+ex.toString());};
    }

    public void showCineAdminScene()
    {
        try{
            //-----loading the cine detail scene-----
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/CinePosAdmin.fxml"));
        
        Parent root = (Parent)loader.load() ;
           
        //changing scene
        scene.setRoot(root);
        //passing the movie id to movie details scene controller
        CinePosAdminController controller =loader.getController(); 
        controller.setScene();
        controller.setStage(this);
        }catch(Exception ex){System.out.println("Error in CineBoxOfficeController"+ex.toString());};
    }

    public void setStage (Stage stage){
        stage.getIcons().add(new Image("file:icon.png"));
        this.stage = stage;
    }

    public void showStage(){
        this.stage.setTitle("Titel in der MainController.java geändert");
        this.stage.show();
    }
}
