package com.example.ayush.rentalvehicles;

/**
 * Created by HP on 27-04-2017.
 */
public class vehicle
{
    String vehicleId,userId,vehicleName,imageUrl,imageId,price,vehicleNo;
    public vehicle()
    {

    }
    public vehicle(String vehicleId,String userId,String vehicleName,String imageUrl,String imageId)
    {
        this.vehicleId=vehicleId;
        this.userId=userId;
        this.vehicleName=vehicleName;
        this.imageUrl=imageUrl;
        this.imageId=imageId;
    }
    public vehicle(String vehicleId,String userId,String vehicleName,String imageUrl,String imageId,String price,String vehicleNo)
    {
        this.vehicleId=vehicleId;
        this.userId=userId;
        this.vehicleName=vehicleName;
        this.imageUrl=imageUrl;
        this.imageId=imageId;
        this.price=price;
        this.vehicleNo=vehicleNo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
