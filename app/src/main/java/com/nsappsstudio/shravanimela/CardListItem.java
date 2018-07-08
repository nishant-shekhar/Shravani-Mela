package com.nsappsstudio.shravanimela;

import android.widget.TextView;

public class CardListItem {

    public String cTitle;
    public String cSubtitle;
    public String cBody;
    public String cType;
    public int loadType;

    public CardListItem(String cTitle, String cSubtitle, String cBody, String cType, int loadType) {
        this.cTitle = cTitle;
        this.cSubtitle = cSubtitle;
        this.cBody = cBody;
        this.cType = cType;
        this.loadType = loadType;
    }

    public String getcTitle() {
        return cTitle;
    }

    public String getcSubtitle() {
        return cSubtitle;
    }

    public String getcBody() {
        return cBody;
    }

    public String getcType() {
        return cType;
    }

    public int getLoadType() {
        return loadType;
    }
}
