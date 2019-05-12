<?php
if($_SERVER['REQUEST_METHOD']=="POST")
{
	include("connection.php");
	$email=$_POST['email'];
	$imageId=$_POST['image_id'];
	$ownerUserId=$_POST['user_id'];
	$vehicleBookedId=$_POST['vehicle_id'];
	$dateStart=$_POST['date_start'];
	$dateEnd=$_POST['date_end'];
	$sql2="SELECT userId,name,contact FROM userregistration WHERE email='$email';";
	$executeSql2=mysql_query($sql2);	
	if(mysql_affected_rows()>0)
	{
		//$sql2="SELECT userId,name,contact FROM userregistration WHERE email='$email';"
		$row=mysql_fetch_object($executeSql2);
		$renterUserId=$row->userId;
		$renterEmail=$email;
		$renterName=$row->name;
		$renterContact=$row->contact;
		$sql3="INSERT INTO bookings(ownerUserId,renterUserId,vehicleBookedId,imageId,date_start,date_end) VALUES(
		'$ownerUserId','$renterUserId','$vehicleBookedId','$imageId','$dateStart','$dateEnd');";
		$executeSql3=mysql_query($sql3);
		
		if(mysql_affected_rows()>0)
		{
			$sql4="SELECT bookingId FROM bookings ORDER BY bookingId DESC LIMIT 1;";
			$executeSql4=mysql_query($sql4);
			if(mysql_affected_rows()>0)
			{
				
			$sqlVehicle="SELECT vehicleName,vehicleNo FROM vehicleregistration WHERE vehicleId='$vehicleBookedId';";
			$exeSqlVehicle=mysql_query($sqlVehicle);
			$vehicleRow=mysql_fetch_object($exeSqlVehicle);
				$row=mysql_fetch_object($executeSql4);
				$bookingId=$row->bookingId;
				$to=$email;


				$sql5="SELECT email,name,contact FROM userregistration WHERE userId='$ownerUserId';";
				$row=mysql_fetch_object(mysql_query($sql5));
				$ownerEmailId=$row->email;
				$ownerName=$row->name;
				$onwerContact=$row->contact;
				$to=$ownerEmailId;
				sendgrid_email_owner($ownerEmailId,$renterName,$renterContact,$renterEmail,$vehicleRow->vehicleNo,$vehicleRow->vehicleName);
				sendgrid_email_renter($renterEmail,$ownerName,$onwerContact,$ownerEmailId,$vehicleRow->vehicleNo,$vehicleRow->vehicleName);
    			$response=array();
    			$response['status']='successful';
    			$response['message']="Your vehicle has been booked and a mail has been sent to you.";
    			echo json_encode($response);		   

			}
			else
			{
				$response=array();
    			$response['status']='Error';
    			$response['message']="Booking could not be retrieved";
    			echo json_encode($response);
			}
		}
		else
		{
				$response=array();
    			$response['status']='Error';
    			$response['message']="Could not confirm booking. Try again.";
    			echo json_encode($response);		   

		}

	}
}
else
{
	$response=array();
    $response['status']='Error';
   	$response['message']="Bad request";
   	echo json_encode($response);
}
function sendgrid_email_owner($to,$name,$contact,$renteremail,$vehicle_no,$vehicle_name)
{
$from = new SendGrid\Email("Rent-A-Vehicle","rent-my-vehicle@herokuapp.com");
$subject = "Your vehicle has been booked";
$to = new SendGrid\Email($name,$to);
$get_request="https://rent-my-vehicle.herokuapp.com/booking-owner-notification/index.php?";
//$get_request="http://localhost/RentMyVehicle/booking-owner-notification/index.php?";
$get_request=$get_request."name='$name'&";
$get_request=$get_request."contact='$contact'&";
$get_request=$get_request."email='$renteremail'&";
$get_request=$get_request."vehicle_no='$vehicle_no'&";
$get_request=$get_request."vehicle_name='$vehicle_name'";
$msg=file_get_contents($get_request);
$content = new SendGrid\Content("text/html",$msg);
$mail = new SendGrid\Mail($from, $subject, $to, $content);

$apiKey = getenv('SENDGRID_API_KEY');
$sg = new \SendGrid($apiKey);
$response = $sg->client->mail()->send()->post($mail);

}
function sendgrid_email_renter($to,$name,$contact,$owneremail,$vehicle_no,$vehicle_name)
{
$from = new SendGrid\Email("Rent-A-Vehicle","rent-my-vehicle@herokuapp.com");
$subject = "Your vehicle has been booked";
$to = new SendGrid\Email($name,$to);
$get_request="https://rent-my-vehicle.herokuapp.com/booking-renter-notification/index.php?";
$get_request=$get_request."name='$name'&";
$get_request=$get_request."contact='$contact'&";
$get_request=$get_request."email='$owneremail'&";
$get_request=$get_request."vehicle_no='$vehicle_no'&";
$get_request=$get_request."vehicle_name='$vehicle_name'";
$msg=file_get_contents($get_request);
$content = new SendGrid\Content("text/html",$msg);
$mail = new SendGrid\Mail($from, $subject, $to, $content);

$apiKey = getenv('SENDGRID_API_KEY');
$sg = new \SendGrid($apiKey);
$response = $sg->client->mail()->send()->post($mail);


}
?>