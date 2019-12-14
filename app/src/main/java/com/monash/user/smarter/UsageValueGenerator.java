package com.monash.user.smarter;

import java.util.HashMap;
import java.util.Random;

public class UsageValueGenerator {
    int current_hour;
    int current_temp;
    HashMap<Integer,Boolean> wmuse = new HashMap<Integer, Boolean>();
    HashMap<Integer,Boolean> acuse = new HashMap<Integer, Boolean>();
    boolean WMalreadyused = false;


    UsageValueGenerator(int current_hr,int current_tmp) {
        this.current_temp = current_tmp;
        this.current_hour =current_hr;
    }
    public double getrandomnumber(double min, double max) {
        Random r = new Random();
        double x = (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
        double rounded_x = Math.round(x * 100.0) / 100.0;
        return rounded_x;
    }

    public Double getfridgeusage() {
        //Fridge runs all the time
        return getrandomnumber(0.3,0.8);

    }

    public Double getWMusage() {
       // Washingmachine only runs between 6am to 9pm
        if(current_hour < 6 || current_hour > 21) {
            return 0.0;
        }
        //Check for continouos 3 hours
        if(wmuse.containsKey(current_hour-1)) {
            if(wmuse.containsKey(current_hour-2)) {
                if(wmuse.containsKey(current_hour-3)) {
                    WMalreadyused = true;
                    return 0.0;
                }
                else {
                    wmuse.put(current_hour,true);
                    return getrandomnumber(0.4,1.3);

                }
            }
            else {
                wmuse.put(current_hour,true);
                return getrandomnumber(0.4, 1.3);

                }
        }

        else {
            wmuse.put(current_hour, true);
            return getrandomnumber(0.4, 1.3);

        }

    }

    public Double getacusage() {

        //Temperature < 20
        // hour greater than 9 and lesser than 23
        if(current_temp < 20 || current_hour < 9 || current_hour > 23) {
            return 0.0;
        }

        if(acuse.size() == 10 ) {
            return 0.0;
        }
        else {
            acuse.put(current_hour, true);
            if(current_temp < 20)
            return getrandomnumber(1, 5);
            else
                return 0.0;
        }
    }

   public String[] getcurrenthour_data() {
        String fridgeusage = getfridgeusage().toString();
        String acusage = getacusage().toString();
        String wmusage = getWMusage().toString();
        String [] appliance_usage = {fridgeusage,acusage,wmusage};
        return appliance_usage;


    }
}
