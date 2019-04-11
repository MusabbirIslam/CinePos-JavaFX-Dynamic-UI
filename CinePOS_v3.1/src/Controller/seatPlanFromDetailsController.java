/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Cart;
import Entity.CartForm;
import Entity.Ticket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author CST
 */
public class seatPlanFromDetailsController implements Initializable {

    public static Cart cartFromDetail;

    @FXML
    private GridPane seatGridPane;

    @FXML
    private ImageView image1;

    @FXML
    private Label label1;

    @FXML
    private ScrollPane seatScroll;

    @FXML
    private Button doneButton;

    private Bloom bloom;

    ArrayList<Integer> selectedSeats = new ArrayList<Integer>();
    //HashMap<Long, Double>selectedTickets=new HashMap<Long, Double>();

    ArrayList<Ticket> selectedTickets = new ArrayList<Ticket>();

    Ticket ticket;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cartFromDetail = new Cart();
        //CineConcessionController.cart = new Cart();
        //total number of row and column
        int totalRow    = 5;
        int totalColumn = 5;

        //setting bloop object for click event
        bloom = new Bloom();
        bloom.setThreshold(0);

        totalRow++;
        totalColumn++;

        for (int i = 0; i < CineBoxOfficeController.allTicketSeat.size(); i++) {
            ColumnConstraints column = new ColumnConstraints(30, 40, USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.CENTER, true);
            seatGridPane.getColumnConstraints().add(column);
        }

        for (int i = 0; i < CineBoxOfficeController.allTicketSeat.size(); i++) {
            RowConstraints row = new RowConstraints(30, 40, USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true);
            seatGridPane.getRowConstraints().add(row);
        }

        //naming the rows
        for (int row = 1; row <= CineBoxOfficeController.allTicketSeat.size(); row++) {
            //making the label
            String id = Integer.toString(row);
            Label seatId = new Label(id);
            seatId.setFont(new Font(12));

            seatGridPane.setRowIndex(seatId, row);
            seatGridPane.setColumnIndex(seatId, 0);
            seatGridPane.getChildren().add(seatId);
        }

        //naming the column
        for (int column = 1; column <= CineBoxOfficeController.allTicketSeat.size(); column++) {
            //making the label
                String id=Integer.toString(column);
                Label seatId=new Label(id);
                seatId.setFont(new Font(12));
                
                seatGridPane.setRowIndex(seatId, 0);
                seatGridPane.setColumnIndex(seatId,column);                              
                seatGridPane.getChildren().add(seatId);
        }
        //creatng image objects and click able event for them and setting them in gridpane

        /*for (int row = 1; row < totalRow; row++) {
            for (int column = 1; column < totalColumn; column++) {
                //loading in image view
                ImageView image = new ImageView();
                Image image2= new Image("/Design/Image/seat_available.png");

                image.setImage(image2);
                image.setFitHeight(40d);
                image.setFitWidth(30d);
                image.setPreserveRatio(false);
                image.setSmooth(true);
                
                //------------image mouse clicked EVENT
                image.setOnMouseClicked((MouseEvent event) -> {
                    if (image.getEffect() == null) {
                        image.setEffect(bloom);
                    } else {
                        image.setEffect(null);
                    }
                }
                );
                

                
                //placing the image and level on gridpane
                seatGridPane.setRowIndex(image, row);
                seatGridPane.setColumnIndex(image,column);  
                seatGridPane.getChildren().add(image);
            }
        }*/

        System.out.println("size of all ticket " + CineBoxOfficeController.allTicketSeat.size());
        for (int row=1;row<=CineBoxOfficeController.allTicketSeat.size();row++) {
            for (int column=1;column<=CineBoxOfficeController.allTicketSeat.size();column++) {

                final int rowIndex = row-1;
                final int columnIndex = column-1;

                //loading in image view
                ImageView image = new ImageView();
                Image image2;

                if(CineBoxOfficeController.allTicketSeat.get(rowIndex).get(columnIndex).currentState==null){
                    image2 = new Image("/Design/Image/seat_sold.png");
                    image.setImage(image2);
                    image.setFitHeight(40d);
                    image.setFitWidth(30d);
                    image.setPreserveRatio(false);
                    image.setSmooth(true);

                }else if(CineBoxOfficeController.allTicketSeat.get(rowIndex).get(columnIndex).currentState.equals("AVAILABLE"))
                {
                        image2 = new Image("/Design/Image/seat_available.png");
                        image.setImage(image2);
                        image.setFitHeight(40d);
                        image.setFitWidth(30d);
                        image.setPreserveRatio(false);
                        image.setSmooth(true);

                        //------------image mouse clicked EVENT
                        image.setOnMouseClicked((MouseEvent event) -> {
                            if (image.getEffect() == null) {
                                selectedSeats.add(CineBoxOfficeController.allTicketSeat.get(rowIndex).get(columnIndex).id);
                                //CineConcessionController.cart.cartForms.add(CineBoxOfficeController.allTicketSeat.get(rowIndex).get(columnIndex).id, 1, 0, pro.sellingPrice, 1, 1, "product");
                                System.out.println("Seat Status " + CineBoxOfficeController.allTicketSeat.get(rowIndex).get(columnIndex).currentState);
                                //System.out.println("Seat ID "+CineBoxOfficeController.allTicketSeat.get(rowIndex).get(columnIndex).id);
                                image.setEffect(bloom);
                            } else {
                                image.setEffect(null);
                            }
                        });




                }else{
                    image2 = new Image("/Design/Image/seat_booked.png");image.setImage(image2);
                    image.setFitHeight(40d);
                    image.setFitWidth(30d);
                    image.setPreserveRatio(false);
                    image.setSmooth(true);

                }

                //placing the image and level on gridpane
                seatGridPane.setRowIndex(image, row);
                seatGridPane.setColumnIndex(image, column);
                seatGridPane.getChildren().add(image);

                //System.out.print("if ex");

            }
        }

    }

   @FXML
    void doneButtonClicked(ActionEvent event) {

     /*  selectedSeats.clear();
       selectedTickets.clear();*/
       System.out.println("Selected seat size " +selectedSeats.size());

       System.out.println("Selected ticket size " +selectedTickets.size());

       for (int i=0;i<selectedSeats.size();i++){
           getTicketByFilmTime(selectedSeats.get(i));
       }

       int qty = 0;
       double priceTotal = 0;
       for(Ticket ticket:selectedTickets){
           priceTotal+=ticket.printedPrice;
           qty++;
           CineBoxOfficeController.data.add(new CineBoxOfficeController.Movie(1, ticket.screenSeat.name, "1", ticket.printedPrice.toString()));
           System.out.println("price :"+ticket.printedPrice);
       }
       Node  source = (Node)  event.getSource();
       Stage stageForSeat = (Stage) source.getScene().getWindow();
       stageForSeat.close();

       CineBoxOfficeController.totalQtyLabelString=String.valueOf(qty);
       CineBoxOfficeController.totalAmountLabelString= String.valueOf(priceTotal);

       this.clearSelectedSeats();



   }

    private void clearSelectedSeats(){

        selectedSeats.clear();
        /*for (int i=0;i<selectedSeats.size();i++){
            selectedSeats.remove(i);
        }*/
    }

    private void getTicketByFilmTime(int seatId){
        //selectedTickets =null;
        String ServiceUrl = CinePOS.serviceUrl+"api/app/ticket/get-by-film-time/"+CineBoxOfficeController.filmTimeId+"/"+seatId;

        try {

            StringBuilder result = new StringBuilder();
            URL url = new URL(ServiceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("Response code:"+conn.getResponseCode());

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

                ticket =gson.fromJson(result.toString(), new TypeToken<Ticket>() {}.getType());
                System.out.println(ticket.printedPrice);
                if(ticket !=null)
                {
                    System.out.println("ticket printed price " + ticket.printedPrice);
                    System.out.println("ticket id " + ticket.id);
                    selectedTickets.add(ticket);
                    try{

                        cartFromDetail.cartForms.add(new CartForm(ticket.id, 1, 0, ticket.printedPrice, 1, 1, "ticket"));
                        String cartForm;

                        cartForm = gson.toJson(cartFromDetail.cartForms);

                        System.out.println(cartForm);

                    }catch (NullPointerException e){
                        System.out.println("Null pointer exception " + e.getMessage());
                    }

                }

                //System.out.print(ticket.printedPrice);


            }


        }catch (Exception e){
            e.printStackTrace();
            //System.out.println("Ex");
        }

        return;
    }

    public static void removeCartData(){
        cartFromDetail.cartForms.clear();
    }

}
