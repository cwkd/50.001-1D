package expenses;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by John on 9/12/2017.
 */

public class CategoryReadJSON {


    private final Pattern foodPattern = Pattern.compile("food");
    private final Pattern travelPattern = Pattern.compile("taxi|uber|grabTaxi|grabShare|uberPool|uberX");
    private final Pattern emergencyPattern = Pattern.compile("Hospital|hospital");
    private final Pattern miscellaneousPattern = Pattern.compile("Louis Vuittion|Chanel|Sephora|Adidas|Nike|Aftershock");

    private boolean bFood;
    private boolean bTravel;
    private boolean bEmergency;
    private boolean bMiscellaneous;


    public CategoryReadJSON() throws JSONException {
    }

    public boolean bCategory(String category) throws JSONException {

        boolean boole = false;

        JSONObject obj = new JSONObject("templatereceipt.json");
        String address = obj.getString("address");

        Matcher mFood = foodPattern.matcher(address);
        Matcher mTravel = travelPattern.matcher(address);
        Matcher mEmergency = emergencyPattern.matcher(address);
        Matcher mMiscellaneous = miscellaneousPattern.matcher(address);

        bFood = mFood.matches();
        bTravel = mTravel.matches();
        bEmergency = mEmergency.matches();
        bMiscellaneous = mMiscellaneous.matches();


        if(category.equals("food")){
            boole=  bFood;
        }
        if(category.equals("travel")){
            boole = bTravel;
        }
        if(category.equals("emergency")){
            boole =  bEmergency;
        }
        if(category.equals("miscellaneous")){
            boole=  bMiscellaneous;
        }
        return boole;
    }

    public HashMap<String, Double> extractData() throws JSONException {
        HashMap<String, Double> Data = new HashMap<>();

        JSONObject obj = new JSONObject("templatereceipt.json");
        Iterator<?> keys = obj.keys();

        while(keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equals("address")) {
            } else {
                Double keyvalue = (Double) obj.get(key);
                Data.put(key, keyvalue);
            }
        }
        return Data;
    }


}