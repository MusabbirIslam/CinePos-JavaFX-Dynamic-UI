import Entity.CartProduct;
import Entity.OrderJson;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Application Name : CinePOS
 * Package Name     : PACKAGE_NAME
 * Author           : Abu Bakar Siddique
 * Email            : absiddique.live@gmail.com
 * Created Date     : 2/1/17
 */
public class Test {

    public static void main(String[] args){
        /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 4);
        Date tomorrow = calendar.getTime();

        System.out.println("Today" +dateFormat.format(today));
        System.out.println("Tomorrow :"+dateFormat.format(tomorrow));



        Gson gson = new Gson();

        OrderJson orderJson = new OrderJson();

        CartProduct cartProduct = new CartProduct();

        cartProduct.id=1;
        cartProduct.price=502.415;
        cartProduct.quantity=5;
        cartProduct.sellingType ="combo";

        orderJson.cart.add(cartProduct);

        CartProduct cartProduct2 = new CartProduct();

        cartProduct2.id=2;
        cartProduct2.price=212.45;
        cartProduct2.quantity=4;
        cartProduct2.sellingType ="combo";

        orderJson.cart.add(cartProduct2);

        orderJson.terminalId =1;

        System.out.println(gson.toJson(orderJson));
*/

        int i;
        for(i=0; i<10; i++)
            switch(i) {
                case 0:
                    System.out.println("i is zero");
                    break;
                case 1:
                    System.out.println("i is 1");
                    break;
                case 2:
                    System.out.println("i is 2");
                    break;
                case 3:
                    System.out.println("i is 3");
                    break;
                case 4:
                    System.out.println("i is 4");
                    break;
                default:
                    System.out.println("i is five or more");
            }



    }


}
