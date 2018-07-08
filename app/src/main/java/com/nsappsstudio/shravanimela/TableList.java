package com.nsappsstudio.shravanimela;

public class TableList {

    public String tableTitle;
    public String cell1;
    public String cell2;
    public String cell3;
    public String cell4;
    public int type;

    public TableList(String tableTitle, String cell1, String cell2, String cell3, String cell4, int type) {
        this.tableTitle = tableTitle;
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
        this.cell4 = cell4;
        this.type = type;
    }

    public String getTableTitle() {
        return tableTitle;
    }

    public String getCell1() {
        return cell1;
    }

    public String getCell2() {
        return cell2;
    }

    public String getCell3() {
        return cell3;
    }

    public String getCell4() {
        return cell4;
    }

    public int getType() {
        return type;
    }
}
