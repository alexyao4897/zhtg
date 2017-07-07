package com.money.deep.tstock.data;

import com.money.deep.tstock.model.ShareEntry;

import java.util.ArrayList;

/**
 * Created by fengxg on 2016/8/25.
 */
public class ParseData {

    public ArrayList<ShareEntry> parseArrayData(String string){
        ArrayList<ShareEntry> shareEntrys = new ArrayList<ShareEntry>();
        String str_temp = string.trim().substring(1,string.length()-1);
        String[] str = str_temp.split("var");
        for(int i=0;i<str.length;i++){
            ShareEntry entry = new ShareEntry();
            String var_str = str[i];
//            System.out.println("var_str:" + var_str);
            String[] tempArr = var_str.split("\"");
            String key = tempArr[0].trim();
            int index = key.indexOf("=");
            String code = key.substring(index - 6, index);
            String q_code = key.substring(index - 8, index);
//            System.out.println("q_code="+q_code);

            String value = tempArr[1].trim();
            if(value.equals("FAILED")){
                break ;
            }else{
                String[] str_arr = value.split(",");
                entry.setName(str_arr[0]);
                entry.setOpen(str_arr[1]);
                entry.setClose(str_arr[2]);
                entry.setCurrent(str_arr[3]);
                entry.setHigh(str_arr[4]);
                entry.setLow(str_arr[5]);
                entry.setCode(code);
                entry.setQ_code(q_code);
            }



/*            for(int j = 0;j < str_arr.length;j++){
                String val = str_arr[j];
                System.out.println(val);
            }*/
            shareEntrys.add(entry);
        }

        return shareEntrys;
    }

}
