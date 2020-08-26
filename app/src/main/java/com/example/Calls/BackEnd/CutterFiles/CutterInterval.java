package com.example.Calls.BackEnd.CutterFiles;

public class CutterInterval {
    private int start;
    private int end;

    public CutterInterval(int start) {
        this.setStart(start);
    }

    public int getStart() {
        return start;
    }

    private void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
