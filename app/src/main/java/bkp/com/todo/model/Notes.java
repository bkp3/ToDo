package bkp.com.todo.model;

public class Notes {

    private String tag, detail, nid, date, time;

    public Notes() {
    }

    public Notes(String tag, String detail, String nid, String date, String time) {
        this.tag = tag;
        this.detail = detail;
        this.nid = nid;
        this.date = date;
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
