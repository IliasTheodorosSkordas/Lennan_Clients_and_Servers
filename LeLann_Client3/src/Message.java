//icsd13170 ilias theodoros skordas
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {

    private String type;
    private String AM;
    private String name;
    private float vathmologia;
    private String roomtype;
    private Boolean proino;
    private Boolean TOKEN;

    private String date_of_booking;
    private ArrayList<Eggrafi> arr;

    //typos munhmatos gia eggrafi mias vathmologias
    public Message(String type, String AM, String name, float vathmologia) {
        this.type = type;
        this.AM = AM;
        this.name = name;
        this.vathmologia = vathmologia;
    }

    public Message(String type, ArrayList<Eggrafi> arr) {
        this.type = type;
        this.arr = arr;

    }

    public Message(String type) {

        this.type = type;

    }

    public Message(boolean TOKEN) {

        this.TOKEN = TOKEN;

    }

    public String getType() {
        return this.type;
    }

    public ArrayList getList() {
        return this.arr;
    }

    public String getname() {
        return this.name;
    }

    public String getAM() {
        return this.AM;
    }

    public float getVathmo() {
        return this.vathmologia;
    }
}
