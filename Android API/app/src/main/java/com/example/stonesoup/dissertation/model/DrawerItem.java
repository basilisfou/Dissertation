package com.example.stonesoup.dissertation.model;

import android.support.v4.widget.DrawerLayout;

import java.io.Serializable;

/*
******************************
*  8         8    888888888  *
*   8       8     8          *
*    8     8      888888888  *
*     8   8       8          *
*      8 8        8          *
*       8     .   8         .*
******************************
* vassilis Fouroulis
 */
public class DrawerItem implements Serializable {
    private String mTitle;
    private int mIcom;
    private static final long serialVersionUID = 1L;

    /********************************************** Constructor ****************************************************************/
    public DrawerItem(String mTitle, int mIcom) {
        this.mTitle = mTitle;
        this.mIcom = mIcom;
    }

    /****************************      Getters and Setters  **********************************************************************/
    public int getIcom() {
        return mIcom;
    }

    public void setIcoc(int mIcom) {
        this.mIcom = mIcom;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
