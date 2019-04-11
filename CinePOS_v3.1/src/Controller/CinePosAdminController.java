/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import Entity.ConcessionProduct;
import Entity.FilmSchedule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CST
 */
public class CinePosAdminController implements Initializable {
    
    private CinePOS stage;
    private Stage popUpStage;
    private Parent root;
    
     @FXML
    private Label dateLabel;

    @FXML
    private Label operatorNameLabel;

    @FXML
    private Label terminalIDLabel;

    @FXML
    private Label cinemaIDLabel;
    
    @FXML
    private Button filmProgrammingButton;

    @FXML
    private Button concessionButton;

    @FXML
    private Button financeButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button promotionsButton;

    @FXML
    private FlowPane buttomButtonPanels;
    
    @FXML
    private Label popUpTitleLabel;

    @FXML
    private Label popUpDataLabel;

    @FXML
    private Button popUpOkButton;

    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setScene() function serves as  initialize from CinePos class
    }
    //works as intialize
    public void setScene()
    {
          // TODO
        ArrayList<String> function=new ArrayList<String>(Arrays.asList("function","function","function","function"));   
        
        for(String pro : function)
        {
            Button b=new Button();
            //set movie name as button text
            b.setText(pro);
            b.setWrapText(true);
            b.getStyleClass().add("button-rectangale");
            //----------------------------setting an action listener for the product button------------
            b.setOnAction((ActionEvent event) -> {
                //--get the id from database so that button click can show that product corresponding work"---- 
            });
            buttomButtonPanels.getChildren().add(b);
        }

        Button reciptLastSale = new Button();
        reciptLastSale.setText("Recipt last sale");
        reciptLastSale.setWrapText(true);
        reciptLastSale.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(reciptLastSale);

        Button clearSale = new Button();
        clearSale.setText("Clear Sale");
        clearSale.setWrapText(true);
        clearSale.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(clearSale);

        Button cashTransaction = new Button();
        cashTransaction.setText("Cash Transaction");
        cashTransaction.setWrapText(true);
        cashTransaction.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(cashTransaction);

        cashTransaction.setOnAction((ActionEvent event) -> {
            //--get the id from database so that button click can show that product corresponding work"----
            Gson gson = new Gson();
            //gson.toJson(CineConcessionController.cart);
            String jsonInString;

            if(CineConcessionController.cart !=null){
                jsonInString = gson.toJson(CineConcessionController.cart);
            }if(seatPlanFromDetailsController.cartFromDetail != null){
                jsonInString = gson.toJson(seatPlanFromDetailsController.cartFromDetail);
            }else{
                jsonInString ="";
            }

            int saleServiceResponse =this.sendSaleOrder(jsonInString);

            System.out.println("return value from respomse : " + saleServiceResponse);
            if(saleServiceResponse==1){
                popUpDataLabel.setText("order placed successfully !");
            }if(saleServiceResponse==0){
                popUpDataLabel.setText("Error occurred !");
            }if(saleServiceResponse==-1){
                popUpDataLabel.setText("Quantity not available !");
            }

            CineConcessionController.removeDataAndCart();
            CineBoxOfficeController.removeDataAndCart();
            if(seatPlanFromDetailsController.cartFromDetail != null){
                seatPlanFromDetailsController.removeCartData();
            }


            System.out.println(jsonInString);
            //set massage for pop up window

            showPopUpWindow();
        });

        Button openDrawer = new Button();
        openDrawer.setText("Open Drawer");
        openDrawer.setWrapText(true);
        openDrawer.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(openDrawer);

        Button function1 = new Button();
        function1.setText("function1");
        function1.setWrapText(true);
        function1.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(function1);

        Button cardTransaction = new Button();
        cardTransaction.setText("Card Transaction");
        cardTransaction.setWrapText(true);
        cardTransaction.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(cardTransaction);

        Button subTotal = new Button();
        subTotal.setText("Sub Total");
        subTotal.setWrapText(true);
        subTotal.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(subTotal);

        Button cashingUp = new Button();
        cashingUp.setText("Cashing Up");
        cashingUp.setWrapText(true);
        cashingUp.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(cashingUp);

        Button reports = new Button();
        reports.setText("Reports");
        reports.setWrapText(true);
        reports.getStyleClass().add("button-right");
        buttomButtonPanels.getChildren().add(reports);


         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar   calendar = Calendar.getInstance();
        Date       today = calendar.getTime();

        String todayString = dateFormat.format(today);
        dateLabel.setText(todayString);      
    }
    
        //-----------loading the pop up window -----------
    private void loadPopUpWindow()
    {    
        try{
            popUpStage=new Stage();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/cashTransictionConfirmPopUp.fxml"));
            loader.setController(this);
            
            root =(Parent)loader.load();
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(buttomButtonPanels.getScene().getWindow());
            }catch(Exception ex){System.out.println("Error in cash transiction pop up scene "+ex.getMessage());};
    }
    
    private void showPopUpWindow()
    {
        popUpStage.showAndWait();
    }
    
    @FXML
    void concessionButtonAction(ActionEvent event) {

    }

    @FXML
    void filmProgrammingButtonAction(ActionEvent event) {

    }

    @FXML
    void financeButtonAction(ActionEvent event) {

    }

    @FXML
    void promotionsButtonAction(ActionEvent event) {

    }

    @FXML
    void reportsButtonAction(ActionEvent event) {

    }

    private int sendSaleOrder(String orderJson){
        try {

            String ServiceUrl = CinePOS.serviceUrl+"api/app/sells/create";

            URL obj = new URL(ServiceUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            System.out.println("JSON will look like this :" + orderJson);
            String urlParameter = "ordersJson="+orderJson+"&screenId=1";

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
                    //allFilmSchedules  = gson.fromJson(response.toString(),new TypeToken<List<FilmSchedule>>(){}.getType());

                    //allFilmSchedules.forEach(x -> System.out.println("File sc id " + x.id));

                }catch (Exception e){
                    System.out.println("Hello from L"+e.getMessage());
                }

                return 1;

                //setMovieButtons();


            }
            else if(responseCode == 422)
            {
                System.out.println("Quantity not available");
                return -1;
            }
            else
            {
                System.out.println("POST request not worked/No data found ");
            }

            con.disconnect();



        }catch (Exception e){

            e.printStackTrace();
            System.out.println("Ex Hello " + e.getMessage());
        }finally {

        }

        return 0;
    }
    
    public void setStage(CinePOS stage){
        this.stage = stage;           
        loadPopUpWindow();
    }

        
    @FXML
    void homeButtonClicked(ActionEvent event) {
        stage.home();
    }
    
    
    @FXML
    void popUpOkButtonAction(ActionEvent event) {
        popUpStage.close();
    }
}
