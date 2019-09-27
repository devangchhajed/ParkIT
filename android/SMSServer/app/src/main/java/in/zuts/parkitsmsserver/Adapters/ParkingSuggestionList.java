package in.zuts.parkitsmsserver.Adapters;

public class ParkingSuggestionList {

    String parking_lot_id, name,time,cost,address,lat,lng,ratings,comments,total_general,total_differently_abled,current_general,current_differently_abled,
            price_per_hour, available ,valet_available,cctv_available,count,distance,user_id ;

    public ParkingSuggestionList(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParking_lot_id() {
        return parking_lot_id;
    }

    public void setParking_lot_id(String parking_lot_id) {
        this.parking_lot_id = parking_lot_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTotal_general() {
        return total_general;
    }

    public void setTotal_general(String total_general) {
        this.total_general = total_general;
    }

    public String getTotal_differently_abled() {
        return total_differently_abled;
    }

    public void setTotal_differently_abled(String total_differently_abled) {
        this.total_differently_abled = total_differently_abled;
    }

    public String getCurrent_general() {
        return current_general;
    }

    public void setCurrent_general(String current_general) {
        this.current_general = current_general;
    }

    public String getCurrent_differently_abled() {
        return current_differently_abled;
    }

    public void setCurrent_differently_abled(String current_differently_abled) {
        this.current_differently_abled = current_differently_abled;
    }

    public String getPrice_per_hour() {
        return price_per_hour;
    }

    public void setPrice_per_hour(String price_per_hour) {
        this.price_per_hour = price_per_hour;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getValet_available() {
        return valet_available;
    }

    public void setValet_available(String valet_available) {
        this.valet_available = valet_available;
    }

    public String getCctv_available() {
        return cctv_available;
    }

    public void setCctv_available(String cctv_available) {
        this.cctv_available = cctv_available;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
