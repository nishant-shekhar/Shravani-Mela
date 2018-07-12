package com.nsappsstudio.shravanimela;

public class TableList {

    public boolean tableTitle;
    public String cell1;
    public String cell2;
    public String cell3;
    public String cell4;
    public String cell5;
    public boolean even;
    public int type;

    public TableList(boolean tableTitle, String cell1, String cell2, String cell3, String cell4, String cell5, boolean even, int type) {
        this.tableTitle = tableTitle;
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
        this.cell4 = cell4;
        this.cell5 = cell5;
        this.even = even;
        this.type = type;
    }

    public boolean isTableTitle() {
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

    public String getCell5() {
        return cell5;
    }

    public boolean isEven() {
        return even;
    }

    public int getType() {
        return type;
    }
}
