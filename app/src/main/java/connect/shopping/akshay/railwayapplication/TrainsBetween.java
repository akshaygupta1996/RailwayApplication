package connect.shopping.akshay.railwayapplication;

/**
 * Created by Akshay on 18-04-2017.
 */

public class TrainsBetween {

    private String train_name;
    private String arrival_time, departure_time, travel_time;
    private int train_no;
    private int no;

    public TrainsBetween(String train_name, String arrival_time, String departure_time, String travel_time, int train_no, int no) {
        this.train_name = train_name;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.travel_time = travel_time;
        this.train_no = train_no;
        this.no = no;
    }

    public TrainsBetween() {
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(String travel_time) {
        this.travel_time = travel_time;
    }

    public int getTrain_no() {
        return train_no;
    }

    public void setTrain_no(int train_no) {
        this.train_no = train_no;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
