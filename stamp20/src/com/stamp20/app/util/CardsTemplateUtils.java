package com.stamp20.app.util;

import com.stamp20.app.R;

public class CardsTemplateUtils {

    public static final String ACTIVITY_CHANGE_TEMPLATE = "change_template";

    private static int sTransTemplateList[] = { R.drawable.x_christmas2_front, R.drawable.x_christmas3_front, R.drawable.x_christmas4_front,
            R.drawable.x_christmas5_front, R.drawable.x_congrats1_front, R.drawable.x_greeting1_front, R.drawable.x_holiday1_front,
            R.drawable.x_holiday2_front, R.drawable.x_holiday3_front, R.drawable.x_holiday4_front, R.drawable.x_invitation1_front,
            R.drawable.x_invitation2_front, R.drawable.x_love1_front, R.drawable.x_love3_front, R.drawable.x_love4_front, R.drawable.x_new_year2_front,
            R.drawable.x_new_year4_front, R.drawable.x_new_year5_front, R.drawable.x_new_year6_front, R.drawable.x_new_year7_front,
            R.drawable.x_save_the_date1_front, R.drawable.x_thanks1_front, R.drawable.x_thanks2_front, R.drawable.x_thanks3_front, };
    private static int sTemplateList[] = { R.drawable.xx_christmas2_front, R.drawable.xx_christmas3_front, R.drawable.xx_christmas4_front,
            R.drawable.xx_christmas5_front, R.drawable.xx_congrats1_front, R.drawable.xx_greeting1_front, R.drawable.xx_holiday1_front,
            R.drawable.xx_holiday2_front, R.drawable.xx_holiday3_front, R.drawable.xx_holiday4_front, R.drawable.xx_invitation1_front,
            R.drawable.xx_invitation2_front, R.drawable.xx_love1_front, R.drawable.xx_love3_front, R.drawable.xx_love4_front, R.drawable.xx_new_year2_front,
            R.drawable.xx_new_year4_front, R.drawable.xx_new_year5_front, R.drawable.xx_new_year6_front, R.drawable.xx_new_year7_front,
            R.drawable.xx_save_the_date1_front, R.drawable.xx_thanks1_front, R.drawable.xx_thanks2_front, R.drawable.xx_thanks3_front, };

    public static int getTransTemplateId(int position) {
        int templateId = 0;
        if (sTransTemplateList != null) {
            templateId = sTransTemplateList[position];
        }

        return templateId;
    }

    public static int getTemplateId(int position) {
        int templateId = 0;
        if (sTransTemplateList != null) {
            templateId = sTemplateList[position];
        }

        return templateId;
    }

    public static int getTemplateSize() {
        return sTemplateList.length;
    }
}
