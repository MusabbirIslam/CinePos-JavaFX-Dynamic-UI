package Controller;


import Entity.FilmDetails;
import Entity.FilmSchedule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CineDetailsController implements Initializable  {

    private CinePOS stage;

    @FXML
    private WebView trailerWebView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label screenLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private TextField durationTimeField;

    @FXML
    private Label synopsisLabel;

    @FXML
    private Label castLabel;

    @FXML
    private Label directorLabel;

    @FXML
    private TableView<?> nextShowTableView;

    //--------------------------------------button action listener methods----------------------------------
    @FXML
    void adultButtonClicked(ActionEvent event) {

    }

    @FXML
    void buyTicketButtonClicked(ActionEvent event) {

        showSeatPlan();
    }

    @FXML
    void childButtonClicked(ActionEvent event) {

    }
    @FXML
    private Label eroorLavel;
    
    @FXML
    void studentButtonClicked(ActionEvent event) {

    }

    @FXML
    void watchTrailerHereButtonClicked(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    //get selectd movie name or id for gettiong details from database
    public void setSelectedMovie(FilmDetails filmDetails)
    {
        CineBoxOfficeController.allTicketSeat.clear();
        //demo data for a movie 
        
        //get embed code of movie trailer from database
        //String trailer="https://www.youtube.com/embed/9pyBKj5-jVk";//"http://www.youtube.com/embed/utUPth77L_o?autoplay=1";
        String trailer=filmDetails.filmTrailer;

        System.out.println("Film trailer url"+trailer);

        try{
            trailerWebView.getEngine().load(trailer);
        }catch(Exception e)
        {
            eroorLavel.setVisible(true);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        String tomorrowString = dateFormat.format(today);

        titleLabel.setText(filmDetails.filmName) ;

        timeLabel.setText(tomorrowString);
        
        screenLabel.setText(screenLabel.getText()+filmDetails.screenName);

        ratingLabel.setText(ratingLabel.getText()+filmDetails.rating);
        
        genreLabel.setText(genreLabel.getText()+filmDetails.genere);

        startTimeField.setText(filmDetails.startTime);

        endTimeField.setText(filmDetails.endTime);

        durationTimeField.setText(filmDetails.duration);

        synopsisLabel.setText("sypnosis");

        castLabel.setText("castLabel");

        directorLabel.setText("directorLabel");

        System.out.println("Film time Id from CineDetails " + filmDetails.filmTimeId);

        CineBoxOfficeController.getAllTicketByFilmTimeId(filmDetails.filmTimeId);

        CineBoxOfficeController.filmTimeId = filmDetails.filmTimeId;
        System.out.println("size of box office ticket array " +CineBoxOfficeController.allTicketSeat.size());
    }
    public void toHome(ActionEvent event){

        stage.home();
    }



    public void showSeatPlan() {

        try {
            Stage stage=new Stage();
            stage.setHeight(titleLabel.getScene().getWindow().getHeight());
            stage.setWidth(titleLabel.getScene().getWindow().getWidth());
            //--loading the cine table-----
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/seatPlanFromDetails.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            //passing this class reference for switching scene----
            seatPlanFromDetailsController controller = loader.getController();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(titleLabel.getScene().getWindow());
            stage.showAndWait();
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Ex msg " + ex.getMessage());
        }
    }

    public void setStage(CinePOS stage)
    {
        this.stage=stage;
    }
}