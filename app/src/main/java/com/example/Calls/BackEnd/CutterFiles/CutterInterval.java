package com.example.Calls.BackEnd.CutterFiles;

public class CutterInterval {
    private int id;
    private int start;
    private int end;

    public CutterInterval(int id, int start) {
        this.setStart(start);
        this.setId(id);
    }

    public int getStart() {
        return start;
    }
    public int getId(){return id;}

    private void setStart(int start) {
        this.start = start;
    }
    private void setId(int id){this.id = id;}

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
