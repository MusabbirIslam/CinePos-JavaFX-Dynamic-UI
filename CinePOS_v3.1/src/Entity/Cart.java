package Entity;

import java.util.ArrayList;

/**
 * Application Name : CinePOS
 * Package Name     : Entity
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 2/20/17
 */
public class Cart {

    public ArrayList<CartForm> cartForms;
    public int terminalId;
    public int screenId;
    public String sellingComment;

    public Cart(){
        cartForms = new ArrayList<>();
        terminalId = 1;
        screenId=1;
        sellingComment= "selling comment";
    }
}
