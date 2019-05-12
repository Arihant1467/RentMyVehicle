package com.example.ayush.rentalvehicles;

/**
 * Created by Arihant on 30-04-2017.
 */
public class RentingVehicle
{
    String vehicle_id,vehicle_name,vehicle_no,image_id,image_url,booking,segment;

    public RentingVehicle()
    {

    }
    public RentingVehicle(String vehicle_id,String vehicle_name,String vehicle_no,String image_id,String image_url,
                          String booking,String segment)
    {
        this.vehicle_id=vehicle_id;
        this.vehicle_name=vehicle_name;
        this.vehicle_no=vehicle_no;
        this.image_id=image_id;
        this.image_url=image_url;
        this.booking=booking;
        this.segment=segment;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }
}
