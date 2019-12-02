package Utils;

import Entity.Product;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static HashMap<String, Integer> month2Int = new HashMap<String, Integer>(){
            {
                put("january", 1);
                put("february", 2);
                put("march", 3);
                put("april", 4);
                put("may", 5);
                put("june", 6);
                put("july", 7);
                put("august", 8);
                put("september", 9);
                put("october", 10);
                put("november", 11);
                put("december", 12);
            }
    };

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
        //System.out.println(money);
        return money.trim();
    }

    public static String transformTitle(String originTitle){
        if(originTitle == null){
            return null;
        }
        originTitle = originTitle.replaceAll("\\(.*\\)", "");
        return originTitle.replaceAll("&", "and").trim();
    }

    public static Integer parseNormalRunTime(String originRunTime){
        if(originRunTime == null){
            return null;
        }
        if(!originRunTime.contains(" minutes")){
            return null;
        }
        try{
            return Integer.parseInt(originRunTime.split(" minutes")[0]);
        }catch (Exception e){
            return null;
        }
    }

    public static Integer parsePrimeRunTime(String originRunTime){
        if(originRunTime == null){
            return null;
        }
        String p1Str = "^ *(\\d*) *h *(\\d*) *min *$";
        String p2Str = "^ *(\\d*) *min *$";
        Pattern p = Pattern.compile(p1Str);
        Matcher m = p.matcher(originRunTime);
        Integer res = null;
        try{
            if(m.find()){
                ////System.out.println(m.group(1));
                ////System.out.println(m.group(2));
                return Integer.parseInt(m.group(1)) * 60 + Integer.parseInt(m.group(2));
            }
            p = Pattern.compile(p2Str);
            m = p.matcher(originRunTime);
            if(m.find()){
                return Integer.parseInt(m.group(1));
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static int[] parseYearMonthDayWeekDay(String releaseDate){
        String pStr = "^.*?(\\w*) +(\\d+) *, *(\\d+).*$";
        Pattern p = Pattern.compile(pStr);
        Matcher m = p.matcher(releaseDate);
        try{
            if(m.find()){
                String year = m.group(3);
                String month = m.group(1);
                String day = m.group(2);
                //System.out.println("year = " + year);
                //System.out.println("month = " + month);
                //System.out.println("day = " + day);
                int[] res = new int[4];
                res[0] = Integer.parseInt(year);
                if(!month2Int.containsKey(month.toLowerCase())){
                    throw new Exception();
                }
                res[1] = month2Int.get(month.toLowerCase());
                res[2] = Integer.parseInt(day);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy mm dd");
                Date d = null;
                try{
                    d = sdf.parse(res[0] +
                            " " +
                            (String.valueOf(res[1]).length() > 1 ? res[1] : "0" + res[1]) +
                            " " +
                            (String.valueOf(res[1]).length() > 1 ? res[1] : "0" + res[2]));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(d != null){
                    c.setTime(d);
                    res[3] = c.get(Calendar.DAY_OF_WEEK);
                }else{
                    res[3] = 0;
                }
                return res;
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static boolean isSame(String word1, String word2) {
        //dp[i][j]表示源串A位置i到目标串B位置j处最低需要操作的次数
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for(int i = 0; i< word1.length() + 1; i++){
            dp[i][0] = i;
        }
        for(int j = 0; j< word2.length() + 1; j++){
            dp[0][j] = j;
        }
        for(int i = 1; i< word1.length() + 1; i++){
            for(int j = 1; j< word2.length() + 1; j++){
                if(word1.charAt(i - 1) == word2.charAt(j - 1)){  // 第i个字符是字符串下标为i-1第哪个
                    dp[i][j] = dp[i - 1][j - 1];
                }else{
                    dp[i][j] = (Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1])) + 1;
                }
            }
        }
        return dp[word1.length()][word2.length()] < 2;
    }

    public static boolean hasSameInArray(String word, ArrayList<String> a){
        if(word == null || a == null){
            return false;
        }
        for (String s : a) {
            if(s == null)
                continue;
            if(isSame(word, s)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasSameInArray(Product p, ArrayList<Product> a){
        if(p == null || a == null){
            return false;
        }
        for (Product s : a) {
            if(s == null || p.getProductId() == null || s.getProductId() == null){
                continue;
            }
            if(p.getProductId().equals(s.getProductId())){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        //System.out.println(parsePrimeRunTime("1 h 65min"));
//        //System.out.println(parsePrimeRunTime("aff 1h 65 min"));
//        //System.out.println(parsePrimeRunTime("  1  h 65  min  "));
//        //System.out.println(parsePrimeRunTime("1h65min"));
//        //System.out.println(parsePrimeRunTime(" 1hour 65minutes "));
        //parseMoneyFromStr("sdfsdgf$1.00sd$233.55210");
        //System.out.println("transformTitle(\"asdasd (fuck)\") = " + transformTitle("asdasd (fuck)"));
    }

}
