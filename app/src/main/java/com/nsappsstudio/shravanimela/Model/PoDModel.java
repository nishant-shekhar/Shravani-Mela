package com.nsappsstudio.shravanimela.Model;

public class PoDModel {
    public String url;
    public String winner;
    public String winnerLocation;
    public String winnerNo;
    public String date;
    public Long ts;

    public PoDModel() {
    }

    public PoDModel(String url, String winner, String winnerLocation, String winnerNo, String date) {
        this.url = url;
        this.winner = winner;
        this.winnerLocation = winnerLocation;
        this.winnerNo = winnerNo;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public String getWinner() {
        return winner;
    }

    public String getWinnerLocation() {
        return winnerLocation;
    }

    public String getWinnerNo() {
        return winnerNo;
    }

    public String getDate() {
        return date;
    }

    public Long getTs() {
        return ts;
    }
}
