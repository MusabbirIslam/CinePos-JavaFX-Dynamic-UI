/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import Entity.Cart;
import Entity.CartForm;
import Entity.ConcessionProduct;
import Entity.ConcessionProductCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.prism.impl.Disposer.Record;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author CST
 */
public class CineConcessionController implements Initializable {
    
        //-----this temporary inner class serve as a demo database-----
    public static class Movie {
        private int id=0;
        private final SimpleStringProperty movieName;
        private final SimpleStringProperty qtyString;
        private final SimpleStringProperty totalString;
 
        private Movie(int id,String name, String qty, String total) {
            this.id = id;
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

    public static Cart cart;

    @FXML
    private TableView<Movie> salesTableView;

    @FXML
    private TabPane productsTabPane;

    @FXML
    private TableColumn<Movie, String> itemColumn;

    @FXML
    private TableColumn plusColumn;

    @FXML
    private TableColumn<Movie, String> qtyColumn;

    @FXML
    private TableColumn                minusColumn;
    //total column
    @FXML
    private TableColumn<Movie, String> totalColumn;

    @FXML
    private Label itemTotalLabel;

    @FXML
    private Label qtyTotalLabel;

    @FXML
    private Label priceTotalLabel;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cart = new Cart();
        getTab();
        getTableData();
    }

    // get data from database for order
    public static ObservableList<Movie> data = FXCollections.observableArrayList();

    private void getTableData() {

        //----some temporary value for table----

        //-----------pushing value to item column
        itemColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("movieName"));
        itemColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //if any value of the column get edited this block of code will run
        itemColumn.setOnEditCommit((CellEditEvent<Movie, String> t) -> {
            ((Movie) t.getTableView().getItems().get(t.getTablePosition().getRow())).setmovieName(t.getNewValue());
        });


        //-----pushing value to qty column
        qtyColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("qtyString"));
        qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //----if any value of the column get edited this block of code will run---
        qtyColumn.setOnEditCommit((CellEditEvent<Movie, String> t) -> {
            ((Movie) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQtyString(t.getNewValue());
        });

        //----pushing value to total column
        totalColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("totalString"));
        totalColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //----if any value of the column get edited this block of code will run---
        totalColumn.setOnEditCommit((CellEditEvent<Movie, String> t) -> {
            ((Movie) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTotalString(t.getNewValue());
        });

        salesTableView.setItems(data);

    }


    private void createPlusMin(int dataArrayIndex) {
        //--------------------------minus button in cell-------------

        minusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        minusColumn.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {

                return new minusButtonCell(dataArrayIndex);
            }

        });


        //--------------------------plus button in cell-------------

        plusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {
                                           @Override
                                           public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                                               return new SimpleBooleanProperty(p.getValue() != null);
                                           }
                                       });

        plusColumn.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

                                      @Override
                                      public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {

                                          return new plusButtonCell(dataArrayIndex);
                                      }

                                  });
    }

    //Define the minus button cell and onaction listener
    private class minusButtonCell extends TableCell<Record, Boolean> {

        Button cellButtonMinus = new Button("-");

        minusButtonCell(int dataArrayIndex){
            float productSellingPrice = Float.valueOf(data.get(dataArrayIndex).getTotalString()) ;
            float unitPrice = productSellingPrice/Integer.valueOf(data.get(dataArrayIndex).getQtyString());

            cellButtonMinus.setOnAction(new EventHandler<ActionEvent>(){
 
                @Override
                public void handle(ActionEvent t) {
                    /* int selectdIndex = getTableRow().getIndex();
                    //Create a new table show details of the selected item
                    Integer quantity=Integer.parseInt(salesTableView.getItems().get(selectdIndex).getQtyString());
                    if(quantity!=0)
                        quantity--;
                    System.out.println("selected index minus"+ selectdIndex);
                    System.out.println("data Index Array"+ dataArrayIndex);
                    salesTableView.getItems().get(selectdIndex).setQtyString(quantity.toString());
                    qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());*/

                    int quantity = Integer.valueOf(data.get(dataArrayIndex).getQtyString());

                    System.out.println("Qty of min :" + quantity);

                    if(quantity>1){

                        qty = Integer.valueOf(data.get(dataArrayIndex).getQtyString()) - 1;
                        int productId = data.get(dataArrayIndex).id;
                        String productName = data.get(dataArrayIndex).getMovieName();
                        data.remove(dataArrayIndex);
                        String newPrice = String.valueOf(unitPrice * qty);
                        data.add(dataArrayIndex, new Movie(productId, productName, String.valueOf(qty), newPrice));

                        cart.cartForms.remove(dataArrayIndex);
                        cart.cartForms.add(dataArrayIndex, new CartForm(productId, qty, 0, Double.valueOf(unitPrice), 1, 1, "product"));

                        totalQuantity = totalQuantity - 1;
                        totalAmount = totalAmount - unitPrice;

                        priceTotalLabel.setText(String.valueOf(totalAmount));
                        qtyTotalLabel.setText(String.valueOf(totalQuantity));
                    }
                    else if(quantity<=1){
                        data.remove(dataArrayIndex);
                        cart.cartForms.remove(dataArrayIndex);
                        totalQuantity = totalQuantity - 1;
                        totalAmount = totalAmount - unitPrice;

                        priceTotalLabel.setText(String.valueOf(totalAmount));
                        qtyTotalLabel.setText(String.valueOf(totalQuantity));
                    }
                }
            });
        }
 
        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButtonMinus);
                //System.out.println("minusButtonCell update is " + empty);
            }
        }
    }
    
     //Define the plus button cell and onaction listener
    private class plusButtonCell extends TableCell<Record, Boolean> {
         Button cellButtonPlus = new Button("+");

        plusButtonCell(int dataArrayIndex){
            float productSellingPrice = Float.valueOf(data.get(dataArrayIndex).getTotalString()) ;
            float unitPrice = productSellingPrice/Integer.valueOf(data.get(dataArrayIndex).getQtyString());

            cellButtonPlus.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    qty = Integer.valueOf(data.get(dataArrayIndex).getQtyString()) + 1;
                    int productId = data.get(dataArrayIndex).id;
                    String productName = data.get(dataArrayIndex).getMovieName();
                    data.remove(dataArrayIndex);
                    String newPrice = String.valueOf(unitPrice * qty);
                    data.add(dataArrayIndex, new Movie(productId, productName, String.valueOf(qty), newPrice));

                    cart.cartForms.remove(dataArrayIndex);
                    cart.cartForms.add(dataArrayIndex, new CartForm(productId, qty, 0, Double.valueOf(unitPrice), 1, 1, "product"));

                    totalQuantity = totalQuantity + 1;
                    totalAmount = totalAmount + unitPrice;

                    priceTotalLabel.setText(String.valueOf(totalAmount));
                    qtyTotalLabel.setText(String.valueOf(totalQuantity));

                   /* int selectdIndex = getTableRow().getIndex();
                    //Create a new table show details of the selected item
                    Integer quantity = Integer.parseInt(salesTableView.getItems().get(selectdIndex).getQtyString());
                    quantity++;
                    System.out.println("selected index plus" + selectdIndex + "\n");
                    System.out.println("data Index Array"+ dataArrayIndex);
                    salesTableView.getItems().get(selectdIndex).setQtyString(quantity.toString());
                    qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());*/
                }
            });
        }
 
        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                //System.out.println("minusButtonCell update is " + empty);
                setGraphic(cellButtonPlus);
           /*     cellButtonPlus.fire();

                int selectdIndex = getTableRow().getIndex();
                //Create a new table show details of the selected item
                Integer quantity=Integer.parseInt(salesTableView.getItems().get(selectdIndex).getQtyString());
                quantity--;
                salesTableView.getItems().get(selectdIndex).setQtyString(quantity.toString());
                qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());*/
            }
        }
    }
    
    //---get data from database for creating tab
    private void getTab()
    {
        ArrayList<ConcessionProduct> productList = this.getAllProduct();
        this.getAllCategory();
        createTab("All", productList);

        ArrayList<ConcessionProductCategory> concessionProductCategories = this.getAllCategory();

        for(ConcessionProductCategory category : concessionProductCategories){

            ArrayList<ConcessionProduct> productListByCategory= new ArrayList<>();
            productListByCategory = this.getByCategoryId(category.id);


            createTab(category.name, productListByCategory);
        }

        /*for(int i=0;i<3;i++)
        {
        //createtab(tab name,arrayList of products)
         createTab("Bar"+i,new ArrayList<String>(Arrays.asList("product"+i,"product"+i,"product"+i,"product"+i,"product"+i,"product"+i)));

        }*/
    }

    private ArrayList<ConcessionProduct> getAllProduct(){

        String ServiceUrl = CinePOS.serviceUrl+"api/app/concession-product/get/all";
        List<ConcessionProduct> concessionProductList  = null;
        ArrayList<ConcessionProduct> productList = new ArrayList<>();
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

                concessionProductList  = gson.fromJson(result.toString(),new TypeToken<List<ConcessionProduct>>(){}.getType());

                //ArrayList<String> productName = null;

                concessionProductList.forEach(x -> productList.add(x));

            }


        }catch (Exception e){
            System.out.println("Ex");
        }

        return productList;
    }


    private ArrayList<ConcessionProductCategory> getAllCategory(){

        String ServiceUrl = CinePOS.serviceUrl+"api/app/product-category/get/all";
        List<ConcessionProductCategory> concessionProductCategoryList = null;
        ArrayList<ConcessionProductCategory> concessionCategoryList = new ArrayList<>();
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

                concessionProductCategoryList  = gson.fromJson(result.toString(),new TypeToken<List<ConcessionProductCategory>>(){}.getType());

                //ArrayList<String> productName = null;

                concessionProductCategoryList.forEach(x -> concessionCategoryList.add(x));
                //categoryNameList.add(x.name)
            }


        }catch (Exception e){
            System.out.println("Ex");
        }

        return concessionCategoryList;
    }


    private ArrayList<ConcessionProduct> getByCategoryId(int categoryId){

        String ServiceUrl = CinePOS.serviceUrl+"api/app/concession-product/get/category-id/"+categoryId;
        List<ConcessionProduct> concessionProductList  = null;
        ArrayList<ConcessionProduct> productList = new ArrayList<>();
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

                concessionProductList  = gson.fromJson(result.toString(),new TypeToken<List<ConcessionProduct>>(){}.getType());

                //ArrayList<String> productName = null;

                concessionProductList.forEach(x -> productList.add(x));

            }


        }catch (Exception e){
            System.out.println("Ex");
        }

        return productList;
    }




    private static int qty=1;
    private static float totalAmount =0;
    private static int totalQuantity =0;

    private void createTab(String tabName,ArrayList<ConcessionProduct> products)
    {
        Tab tab = new Tab();
        tab.setText(tabName);
        FlowPane fPane = new FlowPane();
        fPane.setMaxWidth(Double.MAX_VALUE);
        fPane.setMaxHeight(Double.MAX_VALUE);
        fPane.getStyleClass().add("flowpane-button");
        byte i=1;//this variable for designing purpose


        for(ConcessionProduct pro : products)
        {
            Button b=new Button();
            //set movie name as button text
            b.setText(pro.name);
            //styling button with different color
            b.getStyleClass().add("color-button");

            if(pro.concessionProductImages != null){

                String productImage = CinePOS.imageUrl+pro.concessionProductImages.get(0).filePath;
                System.out.println("Image path : " + productImage);
                b.setStyle("-fx-background-image: url('" + productImage + "'); " +
                           "-fx-background-position: center center; " +
                           "-fx-background-repeat: no-repeat; " +
                           "-fx-background-size: 100%;");
            }

            //----------------------------setting an action listener for the product button------------
            b.setOnAction((ActionEvent event) -> {

                int indexOfTheArray = this.isDataExist(data, pro.id);

                if (indexOfTheArray != -1) {

                    qty = Integer.valueOf(data.get(indexOfTheArray).getQtyString()) + 1;
                    data.remove(indexOfTheArray);
                    String newPrice = String.valueOf(pro.sellingPrice * qty);
                    data.add(indexOfTheArray, new Movie(pro.id, pro.name, String.valueOf(qty), newPrice));

                    cart.cartForms.remove(indexOfTheArray);
                    cart.cartForms.add(indexOfTheArray, new CartForm(pro.id, qty, 0, pro.sellingPrice, 1, 1, "product"));

                    cart.terminalId = 1;

                    totalQuantity = totalQuantity + 1;
                    totalAmount = totalAmount + pro.sellingPrice;

                    priceTotalLabel.setText(String.valueOf(totalAmount));
                    qtyTotalLabel.setText(String.valueOf(totalQuantity));
                    System.out.println("index of the data array" + this.indexOfDataArray(pro.id));
                    this.createPlusMin(this.indexOfDataArray(pro.id));


                } else {
                    qty = 1;
                    data.add(new Movie(pro.id, pro.name, String.valueOf(qty), pro.sellingPrice.toString()));

                    cart.cartForms.add(new CartForm(pro.id, qty, 0, pro.sellingPrice, 1, 1, "product"));
                    cart.terminalId = 1;

                    totalQuantity = totalQuantity + 1;
                    totalAmount = totalAmount + pro.sellingPrice;

                    qtyTotalLabel.setText(String.valueOf(totalQuantity));
                    priceTotalLabel.setText(String.valueOf(totalAmount));

                    System.out.println("index of the data array" + this.indexOfDataArray(pro.id));
                    this.createPlusMin(this.indexOfDataArray(pro.id));
                    //System.out.println("cart will look like this :" + cart.cartForms.get().productQuantity);
                }

                //--get the id from database so that button click can show that product corresponding work"----
            });
            fPane.getChildren().add(b);
            b.setCursor(Cursor.OPEN_HAND);

        }
        tab.setContent(fPane);
        productsTabPane.getTabs().add(tab);

        productsTabPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    System.out.println("Double clicked !!");
                }
            }
        });

        //set cursor
        productsTabPane.setCursor(Cursor.CROSSHAIR);

    }

    public int isDataExist(ObservableList<Movie> dataToCheck,int productId){

        for(int j=0;j<dataToCheck.size();j++){
            if(dataToCheck.get(j).id ==productId){
               return j;
            }
        }
        return -1;

    }

    public int indexOfDataArray(int productId){

        for(int j=0;j<data.size();j++){
            if(data.get(j).id == productId){
                return j;
            }
        }

        return -1;
    }

    public static void removeDataAndCart(){

        data.clear();
        cart.cartForms.clear();
        System.out.println("Remove Here");

        /*for(int j=0;j<data.size();j++){
            data.remove(j);
        }

        for(int j=0;j<cart.cartForms.size();j++){
            cart.cartForms.remove(j);
        }*/
        totalQuantity = 0;
        totalAmount = 0;

    }
    
}
