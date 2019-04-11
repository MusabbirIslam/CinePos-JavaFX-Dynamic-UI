  package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import Entity.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.REMAINING;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

  public class CineTableController implements Initializable {
    
    private CinePOS stage;
      //FilmDetails filmDetails = new FilmDetails();
    
    //---- this inner class is temporary it serving as a demo database
    private class Movie
    {
        public FilmDetails filmDetails;
        public String name;
        public float startingTime;
        public double durationTime;
        int day;
        Movie(FilmDetails filmDetails,String name,float startingTime,double durationTime,int day)
        {
            this.filmDetails = filmDetails;
            this.name=name;
            this.startingTime=startingTime;
            this.durationTime=durationTime;
            this.day=day;
        }
    }

    @FXML
    private GridPane cineGridPane;
    @FXML
    private Pane columnMark;
    @FXML
    private ComboBox cb;

      @FXML
      private Button prevBtn;

      @FXML
      private Button nextBtn;

    private ArrayList<Label> futureDays;

    private ArrayList<FilmSchedule> allFilmSchedules;
    private FilmSchedule allFilmTodaySchedules;

      private ArrayList<BoxOfficeScreen> boxOfficeScreen;


    public Button homeButton;


      private int columnSize =13;

    private ArrayList<Button> buttonToRemove;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //setColumnRow(row,column);
        cb.setDisable(true);
        cb.setStyle("-fx-opacity: 1;");
        setColumnRow(columnSize, 11);
        futureDays = new ArrayList<>();
        allFilmSchedules = new ArrayList<>();
        buttonToRemove = new ArrayList<>();
    }
    
    private void setColumnRow(int totalRow,int totalColumn)
    {
        totalColumn=totalColumn*4;
        for (int i = 1; i < totalColumn; i++) {
            //double minWidth, double prefWidth, double maxWidth, Priority hgrow, HPos halignment, boolean fillWidth
            ColumnConstraints column = new ColumnConstraints(10, 30, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.CENTER, true);
            cineGridPane.getColumnConstraints().add(column);           
            if(i%4==0)
            {
                Pane mark=new Pane();
                mark.setStyle("-fx-border-color:black;");
                cineGridPane.setRowIndex(mark,1);
                cineGridPane.setColumnIndex(mark,i-3);
                cineGridPane.setColumnSpan(mark,4);
                cineGridPane.setRowSpan(mark,REMAINING);
                cineGridPane.getChildren().add(mark);
            }            
        }

        for (int i = 1; i < totalRow ; i++) {
            RowConstraints row = new RowConstraints(10, 30, USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true);
            cineGridPane.getRowConstraints().add(row);
                Pane mark=new Pane();
                mark.setStyle("-fx-border-color:black;");
                cineGridPane.setRowIndex(mark,i);
                cineGridPane.setColumnIndex(mark,0);
                cineGridPane.setColumnSpan(mark,REMAINING);
                cineGridPane.setRowSpan(mark,1);
                cineGridPane.getChildren().add(mark);
        }
    }

      private int screenId;

      public void setFuturePerformance() {
          /*getAllScreen("future"); //for screen selection
          setFutureDays();
          setShowTimings();*/
          //setMovieButtons();

          cb.setDisable(false);
          setTodayPerformance("f");
      }

      public void setTodayPerformance(String dateFromParam) {
          //getAllScreen("today"); //for screen selection
          if(dateFromParam.length()>2){
              getAllScenesWithFilm(dateFromParam);
          }else {

              DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

              Calendar calendar = Calendar.getInstance();
              Date today = calendar.getTime();

              System.out.println("Today Formatted from future performance :" + dateFormat.format(today));
              getAllScenesWithFilm(dateFormat.format(today));
              getNextDays("none");
          }

          Label l = new Label("Today");
          futureDays.add(l);
          cineGridPane.setConstraints(l, 0, 1);

          //------placing in center of the gridpane
          cineGridPane.setHalignment(l, HPos.CENTER);
          cineGridPane.setValignment(l, VPos.CENTER);
          cineGridPane.getChildren().add(l);

          ArrayList<String> days=new ArrayList<>(Arrays.asList("10.00","11.00","12.00","13.00","14.00","15.00","16.00","17.00","18.00","19.00","20.00"));

          int i=0;
          for (String day: days)
          {
              Label l1=new Label(day);
              futureDays.add(l);
              cineGridPane.setConstraints(l1, (i*4)+1, 1); // column=i row= 0
              cineGridPane.setColumnSpan(l1,4);

              //------placing in center of the gridpane
              cineGridPane.setHalignment(l1,HPos.CENTER);
              cineGridPane.setValignment(l1,VPos.CENTER);
              cineGridPane.getChildren().add(l1);

              i++;
          }

          for(int k=0;k<boxOfficeScreen.size();k++){

              //System.out.println("Screen Name from func " + boxOfficeScreen.get(k).screen.name);
              Label screenNameLabel = new Label(boxOfficeScreen.get(k).screen.name);
              futureDays.add(screenNameLabel);
              cineGridPane.setConstraints(screenNameLabel, 0, k+2);

              //------placing in center of the gridpane
              cineGridPane.setHalignment(screenNameLabel, HPos.CENTER);
              cineGridPane.setValignment(screenNameLabel, VPos.CENTER);
              cineGridPane.getChildren().add(screenNameLabel);
          }

          ArrayList<Movie> movieList =new ArrayList<>();

          int count=0;

          try {


          for(BoxOfficeScreen boxOfficeScreenObj : boxOfficeScreen ){

              count++;
              //System.out.println("Film size "+filmSchedule.filmTimes.size());

              if(boxOfficeScreenObj.filmSchedule !=null) {

                  for (FilmTime filmTime : boxOfficeScreenObj.filmSchedule.filmTimes) {

                      String[] hourMin = filmTime.startTime.split(":");

                      String startTimeString = filmTime.startTime;
                      String endTimeString = filmTime.endTime;

                      String[] startTimeArray = startTimeString.split(":");
                      String[] endTimeArray = endTimeString.split(":");

                      double startTime = Double.parseDouble(startTimeArray[0]) + (Double.parseDouble(startTimeArray[1]) * 0.01);
                      double endTime = Double.parseDouble(endTimeArray[0]) + (Double.parseDouble(endTimeArray[1]) * 0.01);

                      double totalDuration = endTime - startTime;

                      /*System.out.println("duration " + totalDuration);

                      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                      Calendar calendar = Calendar.getInstance();
                      calendar.add(Calendar.DAY_OF_YEAR, 1);
                      Date tomorrow = calendar.getTime();

                      String tomorrowString = dateFormat.format(tomorrow);

                      String scheduleDate = boxOfficeScreenObj.filmSchedule.date;

                      String[] tomorrowDay = tomorrowString.split("-");
                      String[] scheduleDay = scheduleDate.split("-");*/

                      int day =count;



                      //int day = (Integer.valueOf(scheduleDay[2]) + 1) - Integer.valueOf(tomorrowDay[2]);

                      //System.out.println("Schedule date : " + (Integer.valueOf(tomorrowDay[2]) + 1) + " Tomorrow : " + (Integer.valueOf(scheduleDay[2])));
                 /*   String schFormat = String.valueOf(dateFormat.format(scheduleDate));

                    System.out.println("schFormat: "+schFormat +" fuck: " +tomorrow);

                    String[] tomorrowDay = schFormat.split("-");
                    String[] scheduleDay = filmTime.startTime.split(":");

                    System.out.println(tomorrowDay[0] + " dsafsdf sad " + scheduleDay[0]);*/
                      System.out.println("value of day:" + day);

                      double durationHourDetail = Double.valueOf(filmTime.film.durationHour);
                      double durationMinDetail = Double.valueOf(filmTime.film.durationMin);

                      int hourConverted = (int) durationHourDetail;
                      int minConverted = (int) durationMinDetail;

                      System.out.println("Film time from creation 2 " + filmTime.id);

                      FilmDetails filmDetails= new FilmDetails();


                      if (boxOfficeScreenObj.filmSchedule != null) {
                          filmDetails.screenName = boxOfficeScreenObj.filmSchedule.screen.name;
                      }

                      filmDetails.rating = filmTime.film.rating;

                      if (filmTime.film != null) {

                          filmDetails.filmName = filmTime.film.name;
                          filmDetails.filmTimeId = filmTime.id;

                          if (filmTime.film.filmGenre != null) {
                              filmDetails.genere = filmTime.film.filmGenre.get(0).name;
                          }

                          if (filmTime.film.filmTrailers != null) {
                              filmDetails.filmTrailer = filmTime.film.filmTrailers.get(0).trailerUrl;
                          }
                      }

                      filmDetails.startTime = filmTime.startTime;
                      filmDetails.endTime = filmTime.endTime;
                      filmDetails.duration = hourConverted + ":" + minConverted;

                      //System.out.println("Film name 1: "+filmDetails.filmName);

                      movieList.add(new Movie(filmDetails, filmTime.film.name, Integer.parseInt(hourMin[0]), totalDuration, day));
                      System.out.println("movie size " + movieList.size());
                  }
              }

          }
          }catch (Exception e){
              e.printStackTrace();
          }


          int firstShowingTime=10;
          Byte p=0;//this variable is for differenciating style between buttons
          for(Movie m : movieList) {

              System.out.println("movie name " + m.name);
              Button b = new Button();
              //set movie name as button text
              b.setText(m.name);

              //----------------------------setting an action listener for the movie button------------
              b.setOnAction((ActionEvent event) -> {
                  //--get the id from database so that button click can show that movie corresponding details in "MovieDetailsScene"----
                  String movieID = "wall-e";
                  //System.out.println("Film name 2: "+m.filmDetails.filmName);
                  stage.showMovieDetailsScene(m.filmDetails);
              });

              //set button in the movie showing day row
              System.out.println("day on loop " + m.day);
              cineGridPane.setRowIndex(b, m.day+1);

              //--------------------placing in grid pane -------------
              //generating column index according to movie starting time
              int columnIndex = (int) ((m.startingTime % firstShowingTime) * 4 + 1);
              //placing button in gridpane according to movie start time
              cineGridPane.setColumnIndex(b, columnIndex);
              //stretching button according to movie duration
              cineGridPane.setColumnSpan(b, (int) (m.durationTime * 4));

              //setting margin
              cineGridPane.setMargin(b, new Insets(20, 0, 20, 0));

              //stylyng button with different style
              if (p == 0) {
                  b.getStyleClass().add("button-orange");
              } else if (p == 1) {
                  b.getStyleClass().add("button-dark-blue");
              } else {
                  b.getStyleClass().add("button-green");
                  p = 0;
              }

              //button filling the width and hight
              b.setMaxWidth(Double.MAX_VALUE);
              b.setMaxHeight(Double.MAX_VALUE);
              //adding to gridpane
              buttonToRemove.add(b);
              cineGridPane.getChildren().add(b);
              p++;//this increment for styling button and differenciating between buttons


          }
      }


      public void getAllScreen(String calledFrom) {

          java.util.List<Screen> allScreenList = CineBoxOfficeController.getAllScreenName();

          ArrayList<String> screenName = new ArrayList<>();

          // allScreenList.forEach(x-> cb.setOnAction(event -> System.out.println("working !"+x.id)));

          allScreenList.forEach(x -> screenName.add(x.name));

          ObservableList<String> data = FXCollections.observableArrayList(screenName);
          cb.setItems(data);

          cb.valueProperty().addListener(new ChangeListener<String>() {
              @Override
              public void changed(ObservableValue ov, String t, String t1) {

                  for (Screen screen : allScreenList)

                      if (screen.name.equals(t1)) {

                          screenId = screen.id;
                          System.out.println("screen id: " + screen.id);
                          if (calledFrom.equals("today")) {
                              getAllTodaySchedule();
                          } else if (calledFrom.equals("future")) {
                              getAllFutureSchedule();
                          }

                      }
              }
          });
      }

      //---get future days from database and place them in gridpane 0 column starting from 1
      private void setFutureDays() {
          //---must be 4 days----

          Calendar calendar = Calendar.getInstance();
          int      today    = calendar.get(Calendar.DAY_OF_WEEK);

          ArrayList<String> days = null;

          if (today == 1) {
              days = new ArrayList<>(Arrays.asList("Mon", "Tue", "Wed", "Thu"));
          }
          if (today == 2) {
              days = new ArrayList<>(Arrays.asList("Tue", "Wed", "Thu","Fri"));
        }
        if(today==3){
            days = new ArrayList<>(Arrays.asList("Wed", "Thu", "Fri","Sat"));
        }
        if(today==4){
            days = new ArrayList<>(Arrays.asList("Thu", "Fri", "Sat","Sun"));
        }
        if(today ==5){
            days = new ArrayList<>(Arrays.asList("Fri", "Sat", "Sun","Mon"));
        }
        if(today==6){
            days = new ArrayList<>(Arrays.asList("Sat", "Sun", "Mon","Tue"));
        }
        if(today ==7){
            days = new ArrayList<>(Arrays.asList("Sun", "Mon", "Tue","Wed"));
        }

        int i = 1;
        for (String day : days)
        {
            Label l=new Label(day);
            futureDays.add(l);
            cineGridPane.setConstraints(l, 0, i); // column=0 row= i
            
            //------placing in center of the gridpane 
            cineGridPane.setHalignment(l,HPos.CENTER);
            cineGridPane.setValignment(l,VPos.CENTER);
            cineGridPane.getChildren().add(l);
           
            i++;
        }
    }
    
       //---get future days from database and place them in gridpane 0 column starting from 1
    private void setShowTimings()
    {
        
        //---first showing time must be given in setMovieButtons function
        //---must be 6 hours----
        ArrayList<String> days=new ArrayList<>(Arrays.asList("10.00","11.00","12.00","13.00","14.00","15.00","16.00"));

        int i=0;
        for (String day: days)
        {
            Label l=new Label(day);
            futureDays.add(l);
            cineGridPane.setConstraints(l, (i*4)+1, 0); // column=i row= 0
            cineGridPane.setColumnSpan(l,4);
            
            //------placing in center of the gridpane 
            cineGridPane.setHalignment(l,HPos.CENTER);
            cineGridPane.setValignment(l,VPos.CENTER);
            cineGridPane.getChildren().add(l);
           
            i++;
        }
    } 
    
    //------------------------for loading movie from database for button ---------------------
    
    private void setMovieButtons()
    {
       // cineGridPane.getChildren().removeAll(buttonToRemove);

        System.out.println("setMovieButtons called");

         //---first showing time must be given
        int firstShowingTime=10; // 10 means 10.00AM
        //duration 1 hour == 1, 1 hour and half ==1.50 , 1 hour and quarter half == 1.25 
        // starting time 10:00 == 10.00 , 10:30==10.50 , 10.15==10.25
        // tomorrow ==1,the day after tomorrow =2, the next day after tomorrow =3

        //ArrayList<FilmSchedule> filmSchedules = this.getAllFutureSchedule();

        ArrayList<Movie> movieList =new ArrayList<>();
        for(FilmSchedule filmSchedule : allFilmSchedules ){

            System.out.println("Film size "+filmSchedule.filmTimes.size());


            for(FilmTime filmTime : filmSchedule.filmTimes){

                String[] hourMin = filmTime.startTime.split(":");

                String startTimeString = filmTime.startTime;
                String endTimeString = filmTime.endTime;

                String[] startTimeArray = startTimeString.split(":");
                String[] endTimeArray = endTimeString.split(":");

                double startTime = Double.parseDouble(startTimeArray[0]) + (Double.parseDouble(startTimeArray[1])*0.01) ;
                double endTime = Double.parseDouble(endTimeArray[0]) + (Double.parseDouble(endTimeArray[1])*0.01) ;

                double totalDuration = endTime - startTime;

                System.out.println("duration " + totalDuration);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,1);
                Date tomorrow = calendar.getTime();

                String tomorrowString = dateFormat.format(tomorrow);

                String scheduleDate = filmSchedule.date;

                String[] tomorrowDay = tomorrowString.split("-");
                String[] scheduleDay = scheduleDate.split("-");

                int day = (Integer.valueOf(scheduleDay[2])+1)-Integer.valueOf(tomorrowDay[2]);

                System.out.println("Schedule date : " + (Integer.valueOf(tomorrowDay[2])+1) + " Tomorrow : "+(Integer.valueOf(scheduleDay[2])));
             /*   String schFormat = String.valueOf(dateFormat.format(scheduleDate));

                System.out.println("schFormat: "+schFormat +" fuck: " +tomorrow);

                String[] tomorrowDay = schFormat.split("-");
                String[] scheduleDay = filmTime.startTime.split(":");

                System.out.println(tomorrowDay[0] + " dsafsdf sad " + scheduleDay[0]);*/
                System.out.println("value of day:" + day);

                double durationHourDetail = Double.valueOf(filmTime.film.durationHour);
                double durationMinDetail = Double.valueOf(filmTime.film.durationMin);

                int hourConverted = (int) durationHourDetail;
                int minConverted = (int) durationMinDetail;

                FilmDetails filmDetails= new FilmDetails();

                filmDetails.filmName = filmTime.film.name;

                if(filmSchedule.screen != null){
                    filmDetails.screenName = filmSchedule.screen.name;
                }

                filmDetails.rating = filmTime.film.rating;

                filmDetails.filmTimeId = filmTime.id;

                System.out.println("Film time id From the creation " + filmTime.id);

                if(filmTime.film!= null){
                    if(filmTime.film.filmGenre!=null)
                    {
                        filmDetails.genere = filmTime.film.filmGenre.get(0).name;
                    }

                    if(filmTime.film.filmTrailers != null){
                        filmDetails.filmTrailer = filmTime.film.filmTrailers.get(0).trailerUrl;
                    }
                }

                filmDetails.startTime = filmTime.startTime;
                filmDetails.endTime = filmTime.endTime;
                filmDetails.duration = hourConverted+":"+minConverted;

                //System.out.println("Film name 1: "+filmDetails.filmName);

                movieList.add(new Movie(filmDetails,filmTime.film.name, Integer.parseInt(hourMin[0]), totalDuration, day));
            }

        }

       // Movie(name,startingTime,duration,day);
//        movieList.add(new Movie("wall e",11.75f,2.5f,2));
//        movieList.add(new Movie("Avengers",10.00f,1.50f,1));
//        movieList.add(new Movie("XXX",13.50f,1f,3));
//        movieList.add(new Movie("F&F",12.00f,1.50f,4));
        
        Byte i=0;//this variable is for differenciating style between buttons
        for(Movie m : movieList)
        {
            Button b=new Button();
            //set movie name as button text
            b.setText(m.name);  
            
            //----------------------------setting an action listener for the movie button------------
            b.setOnAction((ActionEvent event) -> {
                //--get the id from database so that button click can show that movie corresponding details in "MovieDetailsScene"----
                String movieID="wall-e";
                //System.out.println("Film name 2: "+m.filmDetails.filmName);
                stage.showMovieDetailsScene(m.filmDetails);
            });
            
            //set button in the movie showing day row
            cineGridPane.setRowIndex(b,m.day);
            
            //--------------------placing in grid pane -------------
            //generating column index according to movie starting time
            int columnIndex=(int) ((m.startingTime%firstShowingTime)*4 +1);
            //placing button in gridpane according to movie start time
            cineGridPane.setColumnIndex(b,columnIndex);
            //stretching button according to movie duration
            cineGridPane.setColumnSpan(b,(int)(m.durationTime*4));
            
            //setting margin
            cineGridPane.setMargin(b,new Insets(20, 0, 20, 0));
            
            //stylyng button with different style
            if(i==0)
            {
                b.getStyleClass().add("button-orange");
            }
            else if(i==1)
            {
                b.getStyleClass().add("button-dark-blue");
            }
            else
            {
                b.getStyleClass().add("button-green");
                i=0;
            }
            
            //button filling the width and hight
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            //adding to gridpane
            buttonToRemove.add(b);
            cineGridPane.getChildren().add(b);
            i++;//this increment for styling button and differenciating between buttons 
        }
        
        
        
    }


      private void setTodayMovieButtons()
      {
          // cineGridPane.getChildren().removeAll(buttonToRemove);

          System.out.println("setMovieButtons called");

          //---first showing time must be given
          int firstShowingTime=10; // 10 means 10.00AM
          //duration 1 hour == 1, 1 hour and half ==1.50 , 1 hour and quarter half == 1.25
          // starting time 10:00 == 10.00 , 10:30==10.50 , 10.15==10.25
          // tomorrow ==1,the day after tomorrow =2, the next day after tomorrow =3

          //ArrayList<FilmSchedule> filmSchedules = this.getAllFutureSchedule();

          ArrayList<Movie> movieList =new ArrayList<>();


          //System.out.println("Film size " + allFilmTodaySchedules.filmTimes.size());


          for (FilmTime filmTime : allFilmTodaySchedules.filmTimes) {

              String[] hourMin = filmTime.startTime.split(":");

              String startTimeString = filmTime.startTime;
              String endTimeString = filmTime.endTime;

              String[] startTimeArray = startTimeString.split(":");
              String[] endTimeArray = endTimeString.split(":");

              double startTime = Double.parseDouble(startTimeArray[0]) + (Double.parseDouble(startTimeArray[1])*0.01) ;
              double endTime = Double.parseDouble(endTimeArray[0]) + (Double.parseDouble(endTimeArray[1])*0.01) ;

              double totalDuration = endTime - startTime;

              System.out.println("duration " + totalDuration);

              DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

              Calendar calendar = Calendar.getInstance();
              calendar.add(Calendar.DAY_OF_YEAR, 1);
              Date tomorrow = calendar.getTime();

              String tomorrowString = dateFormat.format(tomorrow);

              String scheduleDate = allFilmTodaySchedules.date;

              String[] tomorrowDay = tomorrowString.split("-");
              String[] scheduleDay = scheduleDate.split("-");

              //int day = (Integer.valueOf(scheduleDay[2]) + 1) - Integer.valueOf(tomorrowDay[2]);

              System.out.println("Schedule date : " + (Integer.valueOf(tomorrowDay[2]) + 1) + " Tomorrow : " + (Integer.valueOf(scheduleDay[2])));
             /*   String schFormat = String.valueOf(dateFormat.format(scheduleDate));

                System.out.println("schFormat: "+schFormat +" fuck: " +tomorrow);

                String[] tomorrowDay = schFormat.split("-");
                String[] scheduleDay = filmTime.startTime.split(":");

                System.out.println(tomorrowDay[0] + " dsafsdf sad " + scheduleDay[0]);*/
              //System.out.println("value of day:" + day);

              FilmDetails filmDetails = new FilmDetails();

              double durationHourDetail = Double.valueOf(filmTime.film.durationHour);
              double durationMinDetail = Double.valueOf(filmTime.film.durationMin);

              int hourConverted = (int) durationHourDetail;
              int minConverted = (int) durationMinDetail;

              filmDetails.filmName = filmTime.film.name;

              if (allFilmTodaySchedules.screen != null) {
                  filmDetails.screenName = allFilmTodaySchedules.screen.name;
              }

              filmDetails.rating = filmTime.film.rating;

              if (filmTime.film != null) {
                  if (filmTime.film.filmGenre != null) {
                      filmDetails.genere = filmTime.film.filmGenre.get(0).name;
                  }

                  if (filmTime.film.filmTrailers != null) {
                      filmDetails.filmTrailer = filmTime.film.filmTrailers.get(0).trailerUrl;
                  }
              }

              filmDetails.startTime = filmTime.startTime;
              filmDetails.endTime = filmTime.endTime;
              filmDetails.duration = hourConverted + ":" + minConverted;

              //System.out.println("Film name 1: "+filmDetails.filmName);

              movieList.add(new Movie(filmDetails, filmTime.film.name, Integer.parseInt(hourMin[0]), totalDuration, 2));
          }



          //Movie(name,starting time,duration,day)
       /* movieList.add(new Movie("wall e",11.75f,2.5f,2));
        movieList.add(new Movie("Avengers",10.00f,1.50f,1));
        movieList.add(new Movie("XXX",13.50f,1f,3));
        movieList.add(new Movie("F&F",12.00f,1.50f,4));*/

          Byte i=0;//this variable is for differenciating style between buttons
          for(Movie m : movieList)
          {
              Button b=new Button();
              //set movie name as button text
              b.setText(m.name);

              //----------------------------setting an action listener for the movie button------------
              b.setOnAction((ActionEvent event) -> {
                  //--get the id from database so that button click can show that movie corresponding details in "MovieDetailsScene"----
                  String movieID="wall-e";
                  //System.out.println("Film name 2: "+m.filmDetails.filmName);
                  stage.showMovieDetailsScene(m.filmDetails);
              });

              //set button in the movie showing day row
              cineGridPane.setRowIndex(b,m.day);

              //--------------------placing in grid pane -------------
              //generating column index according to movie starting time
              int columnIndex=(int) ((m.startingTime%firstShowingTime)*4 +1);
              //placing button in gridpane according to movie start time
              cineGridPane.setColumnIndex(b,columnIndex);
              //stretching button according to movie duration
              cineGridPane.setColumnSpan(b,(int)(m.durationTime*4));

              //setting margin
              cineGridPane.setMargin(b,new Insets(20, 0, 20, 0));

              //stylyng button with different style
              if(i==0)
              {
                  b.getStyleClass().add("button-orange");
              }
              else if(i==1)
              {
                  b.getStyleClass().add("button-dark-blue");
              }
              else
              {
                  b.getStyleClass().add("button-green");
                  i=0;
              }

              //button filling the width and hight
              b.setMaxWidth(Double.MAX_VALUE);
              b.setMaxHeight(Double.MAX_VALUE);
              //adding to gridpane
              buttonToRemove.add(b);
              cineGridPane.getChildren().add(b);
              i++;//this increment for styling button and differenciating between buttons
          }
      }

    public ArrayList<FilmSchedule> getAllFutureSchedule(){

        allFilmSchedules= null;

        try {
            for(Button btn: buttonToRemove){

                btn.setVisible(false);
                System.out.println("button block "+buttonToRemove.size());
            }
            String ServiceUrl = CinePOS.serviceUrl+"api/app/film-scheduling/get-all-in-date-range/"+screenId;

            URL obj = new URL(ServiceUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 4);
            Date tomorrow = calendar.getTime();

            System.out.println("Today" + dateFormat.format(today));
            System.out.println("Tomorrow :" + dateFormat.format(tomorrow));

            String urlParameter = "startDate="+dateFormat.format(today)+"&endDate="+dateFormat.format(tomorrow);

            con.setDoOutput(true);
            OutputStream os =  con.getOutputStream();
            os.write(urlParameter.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            StringBuffer response = new StringBuffer();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(response.toString());

                Gson gson = new Gson();
                try{
                    allFilmSchedules  = gson.fromJson(response.toString(),new TypeToken<List<FilmSchedule>>(){}.getType());

                    allFilmSchedules.forEach(x -> System.out.println("File sc id " + x.id));

                }catch (Exception e){
                    System.out.println("Hello from L"+e.getMessage());
                }


                setMovieButtons();

            } else {
                System.out.println("POST request not worked/No data found ");
            }

            con.disconnect();



        }catch (Exception e){

            e.printStackTrace();
            System.out.println("Ex Hello " + e.getMessage());
        }finally {

        }

        return allFilmSchedules;
    }

      public FilmSchedule getAllTodaySchedule(){

          allFilmTodaySchedules= null;

          System.out.println("Today called !");

          try {
              for(Button btn: buttonToRemove){

                  btn.setVisible(false);
                  System.out.println("button block "+buttonToRemove.size());
              }
              String ServiceUrl = CinePOS.serviceUrl+"api/app/film-scheduling/get-by-date/"+screenId;

              URL obj = new URL(ServiceUrl);
              HttpURLConnection con = (HttpURLConnection) obj.openConnection();

              // optional default is GET
              con.setRequestMethod("POST");

              //add request header
              con.setRequestProperty("User-Agent", "Mozilla/5.0");

              DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

              Calendar calendar = Calendar.getInstance();
              Date today = calendar.getTime();

              System.out.println("Today" + dateFormat.format(today));

              String urlParameter = "startDate="+dateFormat.format(today);

              con.setDoOutput(true);
              OutputStream os =  con.getOutputStream();
              os.write(urlParameter.getBytes());
              os.flush();
              os.close();

              int responseCode = con.getResponseCode();
              StringBuffer response = new StringBuffer();

              if (responseCode == HttpURLConnection.HTTP_OK) { //success
                  BufferedReader in = new BufferedReader(new InputStreamReader(
                          con.getInputStream()));
                  String inputLine;

                  while ((inputLine = in.readLine()) != null) {
                      response.append(inputLine);
                  }
                  in.close();
                  System.out.println(response.toString());

                  Gson gson = new Gson();
                  try{
                      allFilmTodaySchedules  = gson.fromJson(response.toString(),new TypeToken<FilmSchedule>(){}.getType());

                      System.out.println("All today film schedule id :" + allFilmTodaySchedules.id);

                  }catch (Exception e){
                      System.out.println("Hello from getAllTodaySchedule service"+e.getMessage());
                  }


                  setTodayMovieButtons();

              } else {
                  System.out.println("POST request not worked/No data found ");
              }

              con.disconnect();



          }catch (Exception e){

              e.printStackTrace();
              System.out.println("Ex Hello " + e.getMessage());
          }finally {

          }

          return allFilmTodaySchedules;
      }

      public void toHome(ActionEvent event){

          System.out.println("working !");
          stage.home();
      }

      public void getAllScenesWithFilm(String dateParam){

          try {
              if(buttonToRemove.size()>0)
              {
                  for(Button btn: buttonToRemove){

                      btn.setVisible(false);
                  }
              }
          }catch (Exception e){
              e.printStackTrace();
          }


          try {

              String ServiceUrl = CinePOS.serviceUrl+"api/app/film-scheduling/get-for-all-screen/film-time-wise";

              URL obj = new URL(ServiceUrl);
              HttpURLConnection con = (HttpURLConnection) obj.openConnection();

              // optional default is GET
              con.setRequestMethod("POST");

              //add request header
              con.setRequestProperty("User-Agent", "Mozilla/5.0");

              DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

              Calendar calendar = Calendar.getInstance();
              Date today = calendar.getTime();

              System.out.println("\n\n New Service Response :");
              System.out.println("Today" + dateFormat.format(today));

              String urlParameter ;

              if(dateParam.length()>0){
                  System.out.print("from param ");
                  urlParameter = "date="+dateParam;
              }else{
                  System.out.print("from else");
                  urlParameter= "date="+dateFormat.format(today);
              }


              con.setDoOutput(true);
              OutputStream os =  con.getOutputStream();
              os.write(urlParameter.getBytes());
              os.flush();
              os.close();

              int responseCode = con.getResponseCode();
              StringBuffer response = new StringBuffer();

              if (responseCode == HttpURLConnection.HTTP_OK) { //success
                  BufferedReader in = new BufferedReader(new InputStreamReader(
                          con.getInputStream()));
                  String inputLine;

                  while ((inputLine = in.readLine()) != null) {
                      response.append(inputLine);
                  }
                  in.close();
                  System.out.println(response.toString());

                  Gson gson = new Gson();
                  try{
                      boxOfficeScreen  = gson.fromJson(response.toString(),new TypeToken<List<BoxOfficeScreen>>(){}.getType());

                      boxOfficeScreen.forEach(x -> System.out.println("Screen Id: " + x.screen.id));
                      columnSize = boxOfficeScreen.size()+1;

                      System.out.println("Size of screen array " + boxOfficeScreen.size());

                  }catch (Exception e){
                      System.out.println("Hello from getAllScenesWithFilm service"+e.getMessage());
                  }


                  //setTodayMovieButtons();

              } else {
                  System.out.println("POST request not worked/No data found ");
              }

              con.disconnect();



          }catch (Exception e){

              e.printStackTrace();
              System.out.println("Ex Hello " + e.getMessage());
          }finally {

          }
      }


      int countHit=0;
      public void getNextDays(String calledFrom) {

          Format formatter = new SimpleDateFormat("EEEE,dd MMMM yyyy");
          String today = formatter.format(new Date());
          //System.out.println("Today : " + today);

          Date dayTwo = new Date();
          Calendar c = Calendar.getInstance();
          c.setTime(dayTwo);
          c.add(Calendar.DATE, 1);
          dayTwo = c.getTime();

          String dayTwoString = formatter.format(dayTwo);

          Date dayThree = new Date();
          c = Calendar.getInstance();
          c.setTime(dayThree);
          c.add(Calendar.DATE, 2);
          dayThree = c.getTime();

          String daythreeString = formatter.format(dayThree);

          Date dayFour = new Date();
          c = Calendar.getInstance();
          c.setTime(dayFour);
          c.add(Calendar.DATE, 3);
          dayFour = c.getTime();

          String dayFourString = formatter.format(dayFour);

          ArrayList<String> dateArray=new ArrayList<>(Arrays.asList(today,dayTwoString,daythreeString,dayFourString));

          ObservableList<String> data = FXCollections.observableArrayList(dateArray);
          cb.setItems(data);
          cb.getSelectionModel().selectFirst();

          //To disable combo box ::

          /*cb.setDisable(true);
          cb.setStyle("-fx-opacity: 1;");
*/
          DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

          Calendar calendar = Calendar.getInstance();
          Date todayFormatted = calendar.getTime();

          Calendar calendar2 = Calendar.getInstance();
          calendar2.add(Calendar.DAY_OF_YEAR, 1);
          Date dayTwoFormatted = calendar2.getTime();

          Calendar calendar3 = Calendar.getInstance();
          calendar3.add(Calendar.DAY_OF_YEAR, 2);
          Date dayThreeFormatted = calendar3.getTime();

          Calendar calendar4 = Calendar.getInstance();
          calendar4.add(Calendar.DAY_OF_YEAR, 3);
          Date dayFourFormatted = calendar4.getTime();


          ArrayList<String> dateArrayFormatted=new ArrayList<>(Arrays.asList(dateFormat.format(todayFormatted),dateFormat.format(dayTwoFormatted),dateFormat.format(dayThreeFormatted),dateFormat.format(dayFourFormatted)));


          cb.valueProperty().addListener(new ChangeListener<String>() {
              @Override
              public void changed(ObservableValue ov, String t, String t1) {

                  countHit++;
                  System.out.println("change event called "+countHit+" times");
                  for(String date : dateArrayFormatted){

                      String string = t1;

                      String[] fullParamDate = string.split(",");
                      String[] dateFromParam = fullParamDate[1].split(" ");

                      String[] dateFromLoop = date.split("-");

                      if(dateFromParam[0].equals(dateFromLoop[2])){

                          setTodayPerformance(date);
                          boxOfficeScreen.clear();
                      }
                  }

                  /*for (Screen screen : allScreenList)

                      if (screen.name.equals(t1)) {

                          screenId = screen.id;
                          System.out.println("screen id: " + screen.id);
                          if (calledFrom.equals("today")) {
                              getAllTodaySchedule();
                          } else if (calledFrom.equals("future")) {
                              getAllFutureSchedule();
                          }

                      }*/
              }
          });
      }

      public void nextButtonAction(){
          cb.getSelectionModel().selectNext();
      }

      public void prevButtonAction(){
          cb.getSelectionModel().selectPrevious();
      }

    public void setStage(CinePOS stage)
    {
        this.stage=stage;
    }

}

