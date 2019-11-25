import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getProductIdFromUrl(String url){
        try{
            String s = null;
            if(url.contains("gp/product/")){
                s = "gp/product/";
            }else if(url.contains("dp/product/")){
                s = "dp/product/";
            }else if(url.contains("/gp/")){
                s = "/gp/";
            }else if(url.contains("/dp/")){
                s = "/dp/";
            }else{
                return null;
            }
            return url.split(s)[1].split("/", 0)[0];
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String parseMoneyFromStr(String strContainsDollar){
        if(!strContainsDollar.contains("$")){
            return null;
        }
        String pattern = "(\\$[0-9.]*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(strContainsDollar);
        if(!m.find()){
            return null;
        }
        String money = m.group(1);
        System.out.println(money);
        return money;
    }

    public static void main(String[] args) {
        parseMoneyFromStr("sdfsdgf$1.00sd$233.55210");
    }

}
