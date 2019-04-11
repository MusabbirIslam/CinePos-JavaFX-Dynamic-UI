/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.*;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import Entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author CST
 */
public class CineBoxOfficeController implements Initializable {
    
    //-----this temporary inner class serve as a demo database-----
    public static class Movie {
        private int id;
        private final SimpleStringProperty movieName;
        private final SimpleStringProperty qtyString;
        private final SimpleStringProperty totalString;
 
        public Movie(int id,String name, String qty, String total) {
            this.id=id;
            this.movieName = new SimpleStringProperty(name);
            this.qtyString = new SimpleStringProperty(qty);
            this.totalString = new SimpleStringProperty(total);
        }
        
         public String getMovieName() {
            return movieName.get();
         }
        public void setmovieName(String fName) {
            movieName.set(fName);
         }
        
         public String getQtyString() {
            return qtyString.get();
        }
        public void setQtyString(String fName) {
            qtyString.set(fName);
        }
    
        public String getTotalString() {
            return totalString.get();
        }
        public void setTotalString(String fName) {
            totalString.set(fName);
        }
        
    }//end of inner demo database class
    
    //-----table view object-----
    @FXML
    private TableView<Movie> ordersTableView;
    //tree view for movie details show
    @FXML
    private TreeView<HBox> movieDetails;
    //main tree item root
    private TreeItem<HBox> rootNode;
    //---item column
    @FXML
    private TableColumn<Movie, String> itemColumn1;
    //---quantity column
    @FXML
    private TableColumn<Movie, String> qtyColumn1;
    //total column
    @FXML
    private TableColumn<Movie, String> totalColumn1;
    
    //---bottom label for total item
    @FXML
    private Label itemTotalLabel1;
    //---bottom label for total quantity
//    @FXML
//    private Label qtyTotalLabel1;
//    //---bottom label for total price
//    @FXML
//    private Label priceTotalLabel1;

    @FXML
    private ComboBox cb;

    @FXML
    private   Label totalQtyLabel;
    @FXML
    private  Label totalAmountLabel;

    private ArrayList<Film> allBoxOfficeFilmToday;

    private static int qty=1;
    private static float totalAmount =0;
    private static int totalQuantity =0;


    public static String totalQtyLabelString;
    public static String totalAmountLabelString;

    public static ArrayList<ArrayList<TicketSeat>> allTicketSeat = new ArrayList<ArrayList<TicketSeat>>();


/*    ObservableList<Movie> data = FXCollections.observableArrayList(
            new Movie(1,"Salle", "5", "2,500"),
            new Movie(2,"Salleaaaaaaaaaaaaaaa", "5", "2,500"),
            new Movie(3,"Salle", "5", "2,500"),
            new Movie(4,"Salle", "5", "2,500"),
            new Movie(5,"Salle", "5", "2,500")
                                                                  );*/

    public static ObservableList<Movie> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */

    public static int screenId;
    public static int filmTimeId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //setListData();

        totalQtyLabelString="0";
        totalAmountLabelString="0";

        java.util.List<Screen> allScreenList = this.getAllScreenName();

        ArrayList<String> screenName = new ArrayList<>();

       // allScreenList.forEach(x-> cb.setOnAction(event -> System.out.println("working !"+x.id)));

        allScreenList.forEach(screen -> screenName.add(screen.name));

        ObservableList<String> data = FXCollections.observableArrayList(screenName);
        cb.setItems(data);

        cb.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {

                for (Screen screen : allScreenList)

                    if (screen.name.equals(t1)) {

                        System.out.println("screen id: " + screen.id);
                        screenId = screen.id;
                        allBoxOfficeFilmToday = getAllBoxOfficeFilmToday(screen.id);
                        getListData();
                        getTableData();
                        return;
                    }
            }
        });

        cb.getSelectionModel().selectFirst();
    }

    public static java.util.List<Screen> getAllScreenName(){

        String ServiceUrl = CinePOS.serviceUrl+"api/app/screen/get/all";
        java.util.List<Screen> screenList  = null;
        try {

            StringBuilder result = new StringBuilder();
            URL url = new URL(ServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("Response code:" + conn.getResponseCode());

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                System.out.println(result.toString());

                Gson gson = new Gson();

                screenList  = gson.fromJson(result.toString(),new TypeToken<java.util.List<Screen>>(){}.getType());

                //ArrayList<String> productName = null;

                //screenList.forEach(x -> System.out.println(x.name));

            }


        }catch (Exception e){
            e.printStackTrace();
            //System.out.println("Ex on get all screen "+e.getMessage());
        }

        return screenList;
    }
    
    // get data from database for order 
    private void getTableData()
    {

        try {
            //----some temporary value for table----

            //-----------pushing value to item column
            itemColumn1.setCellValueFactory(new PropertyValueFactory<Movie, String>("movieName"));
            itemColumn1.setCellFactory(TextFieldTableCell.forTableColumn());

            //if any value of the column get edited this block of code will run
            itemColumn1.setOnEditCommit((CellEditEvent<Movie, String> t) -> {
                ((Movie) t.getTableView().getItems().get(t.getTablePosition().getRow())).setmovieName(t.getNewValue());
            });


            //-----pushing value to qty column
            qtyColumn1.setCellValueFactory(new PropertyValueFactory<Movie, String>("qtyString"));
            qtyColumn1.setCellFactory(TextFieldTableCell.forTableColumn());

            //----if any value of the column get edited this block of code will run---
            qtyColumn1.setOnEditCommit((CellEditEvent<Movie, String> t) -> {
                ((Movie) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQtyString(t.getNewValue());
            });

            //----pushing value to total column
            totalColumn1.setCellValueFactory(new PropertyValueFactory<Movie, String>("totalString"));
            totalColumn1.setCellFactory(TextFieldTableCell.forTableColumn());

            //----if any value of the column get edited this block of code will run---
            totalColumn1.setOnEditCommit((CellEditEvent<Movie, String> t) -> {
                ((Movie) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTotalString(t.getNewValue());
            });

            ordersTableView.setItems(data);
        }catch (NullPointerException ex){
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    //get data for lis from database
    private void getListData()
    {
        //Main root of the tree
        rootNode = new TreeItem<>(new HBox());
        rootNode.setExpanded(true);

       //loop for demo data
       /*for(int i=0;i<5;i++)
       {
       // function for setting data in list view
       //setListData(movie name,movie details string array,movie timing string array,movie available Ticket string array)
       setListData("Movie Name",new ArrayList<String>(Arrays.asList("name","genere","rating")),
                new ArrayList<String>(Arrays.asList("10.00-11.00","12.00-14.00")),
                new ArrayList<String>(Arrays.asList("adult 1000","Student 200","Child 400")));
       }*/

        if(allBoxOfficeFilmToday != null){
            for(Film film: allBoxOfficeFilmToday){
                setListData(film,new ArrayList<String>(Arrays.asList("adult 1000","Student 200","Child 400")));
            }
        }



      //setting tree item as main root of the tree view
        movieDetails.setRoot(rootNode);       
    }
    
    //---setting the data in list
   // private void setListData(String mName,ArrayList<String> details,ArrayList<String> timing,ArrayList<String> availableTicket)
    private void setListData(Film film,ArrayList<String> availableTicket)
    {

         HBox rootHBox = new HBox();
         HBox subRootHBox=new HBox();
         subRootHBox.setSpacing(15);
         //---label for the root
         Label rootTitle=new Label(film.name);
         rootHBox.getChildren().add(rootTitle);

         
         //VBox for the movie details
         VBox detailsVBox=new VBox();
         detailsVBox.getStyleClass().add("hbox-movie");

        /*for(String data:details)
        {
            Label label=new Label(data);
            detailsVBox.getChildren().add(label);
        }
        */

        Label label=new Label(film.name);
        detailsVBox.getChildren().add(label);

        Label label2=new Label(film.filmGenre.get(0).name);
        detailsVBox.getChildren().add(label2);

        Label label3=new Label(String.valueOf(film.rating));
        detailsVBox.getChildren().add(label3);

        
        VBox timingVBox=new VBox();
        timingVBox.getStyleClass().add("vbox-timing");

        /*for(String data:timing)
        {
            Label label=new Label(data);
            label.getStyleClass().add("label-border");
            timingVBox.getChildren().add(label);
        }*/

        Label label4=new Label(film.filmTimes.get(0).startTime);
        label4.getStyleClass().add("label-border");
        timingVBox.getChildren().add(label4);

        Label label5=new Label(film.filmTimes.get(0).endTime);
        label5.getStyleClass().add("label-border");
        timingVBox.getChildren().add(label5);
        
        FlowPane ticketVBox=new FlowPane();
        ticketVBox.setHgap(10);
        ticketVBox.setVgap(10);
        ticketVBox.getStyleClass().add("flowpane-ticket");
        ticketVBox.setPrefWrapLength(200);

        for(String data:availableTicket)
        {
            Label label7=new Label(data);
            label7.getStyleClass().add("label-border");
            ticketVBox.getChildren().add(label7);
        }
        
        //--ading all vbox in sub root hbox----
        subRootHBox.getChildren().addAll(detailsVBox, timingVBox, ticketVBox);

        subRootHBox.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                //show seatplan window
                filmTimeId = film.filmTimes.get(0).id;
                getAllTicketByFilmTimeId(film.filmTimes.get(0).id);
                showSeatPlan();
                setTotalQtyLabel();
                setAmountQtyLabel();
            }


        });

        //--adding sub root h box in root hbox
        rootHBox.getChildren().add(subRootHBox);
        HBox.setHgrow(detailsVBox, Priority.ALWAYS);        
        HBox.setHgrow(timingVBox, Priority.ALWAYS);
        //HBox.setHgrow(ticketVBox, Priority.ALWAYS);
        //---adding root hbox to the root tree item
        TreeItem<HBox> root = new TreeItem<>(rootHBox);
        root.setExpanded(true);
        //---adding sub roothbox to sub root tree item
        TreeItem<HBox> subRoot = new TreeItem<>(subRootHBox);
        root.getChildren().add(subRoot);
        
        //adding root tree item to the main root tree item of the tre view
        rootNode.getChildren().add(root);

    }  
    
// call this function when hbox get clicked    
    public void showSeatPlan() {
        
        try {
            Stage stage=new Stage();
            stage.setHeight(itemTotalLabel1.getScene().getWindow().getHeight());
            stage.setWidth(itemTotalLabel1.getScene().getWindow().getWidth());
            //--loading the cine table-----
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/seatPlan.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            //passing this class reference for switching scene----
            seatPlanController controller = loader.getController();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(itemTotalLabel1.getScene().getWindow());
            stage.showAndWait();
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Ex msg " + ex.getMessage());
        }
    }
    
  
    
    private ArrayList<Film> getAllBoxOfficeFilmToday(int screenId){

        allBoxOfficeFilmToday= null;

        try {

            String ServiceUrl = CinePOS.serviceUrl+"api/app/film-scheduling/get-for-box-office";

            URL obj = new URL(ServiceUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar calendar = Calendar.getInstance();

            Date today = calendar.getTime();

            System.out.println("Today " + dateFormat.format(today) +"Screen id :"+screenId);

            String urlParameter = "screen_id="+screenId+"&date="+dateFormat.format(today);
            //String urlParameter = "screen_id="+screenId+"&date=2017-02-10";

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

                allBoxOfficeFilmToday  = gson.fromJson(response.toString(),new TypeToken<java.util.List<Film>>(){}.getType());

                allBoxOfficeFilmToday.forEach(x -> System.out.println("Film id " + x.id));

                //setMovieButtons();

            } else {
                System.out.println("POST request not worked/No data found ");
            }

            con.disconnect();



        }catch (Exception e){

            e.printStackTrace();
            //System.out.println("Ex dsf" + e.getMessage());
        }finally {

        }

        return allBoxOfficeFilmToday;

    }

    public int isDataExist(ObservableList<Movie> dataToCheck,int productId){

        for(int j=0;j<dataToCheck.size();j++){
            if(dataToCheck.get(j).id ==productId){
                return j;
            }
        }
        return -1;

    }

    public static void getAllTicketByFilmTimeId(int filmTimeId){

        String ServiceUrl = CinePOS.serviceUrl+"api/app/ticket/seats/get-by-film-time/"+filmTimeId;

        try {

            StringBuilder result = new StringBuilder();
            URL url = new URL(ServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("Response code:" + conn.getResponseCode());

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                System.out.println(result.toString());

                Gson gson = new Gson();

                allTicketSeat  = gson.fromJson(result.toString(),new TypeToken<java.util.ArrayList<ArrayList<TicketSeat>>>(){}.getType());

                //ArrayList<String> productName = null;

                //allTicketSeat.forEach(x -> x.forEach(y-> System.out.println(y.currentState)));

            }


        }catch (Exception e){
            System.out.println("Ex");
        }

        return;

    }

    /*public void updateQtyAndTotal(CineBoxOfficeController c){
        System.out.print(label2.getText());
        try{
            label2.setText(c.getLabelText());
            label_qty.setText(c.getLabelText());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    public void setTotalQtyLabel(){
        this.totalQtyLabel.setText(totalQtyLabelString);
    }

    public void setAmountQtyLabel(){
        this.totalAmountLabel.setText(totalAmountLabelString);
    }

    public static void removeDataAndCart(){

        System.out.println("Here");

        CineBoxOfficeController.data.clear();

        CineConcessionController.cart.cartForms.clear();
        /*for(int j=0;j<CineConcessionController.cart.cartForms.size();j++){
            CineConcessionController.cart.cartForms.remove(j);
        }*/
        totalQtyLabelString ="0";
        totalAmountLabelString = "0";


    }

    //this action event for testing purpose
    @FXML
    void seatPlanButtonAction(ActionEvent event) {
       showSeatPlan();
    }
}
