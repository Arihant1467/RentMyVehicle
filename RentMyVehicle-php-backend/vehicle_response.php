<?php

if($_SERVER['REQUEST_METHOD']=="POST")
{
	
	include("connection.php");
	$segment=$_POST['segment'];
	$dateStart=$_POST['date_start'];
	$dateEnd=$_POST['date_end'];
	$city=$_POST['city'];
    $sql1="SELECT a.vehicleId,a.userId,a.vehicleName,a.price,a.vehicleNo,b.imageUrl,b.imageId FROM vehicleregistration a,vehicleimage b WHERE a.vehicleId=b.vehicleId AND a.city='$city' AND a.segment='$segment' AND b.booking=0;";
	$a=array();
	$executeSql1=mysql_query($sql1);
	// to check if that car has been booked during the tenure;
	//$sql2="SELECT imageId FROM bookings WHERE date_start<='$dateStart' AND date_end>='$dateEnd';";
	$sql2="SELECT imageId, IF('$dateStart' BETWEEN date_start AND date_end
		OR '$dateEnd' BETWEEN date_start AND date_end
		OR date_start BETWEEN '$dateStart' AND '$dateEnd'
		OR date_end BETWEEN '$dateStart' AND '$dateEnd',0,1)
		AS canbe FROM bookings;";
	$imID=array();
	$executeSql2=mysql_query($sql2);
	while($resultset=mysql_fetch_object($executeSql2))
	{
		if(intval($resultset->canbe)==0)
		{
			array_push($imID,$resultset->imageId);
		}
		
	}
// sql1 and slq2 combination will give us all the vehicles which are free during the tenure entered by the user

	while($row=mysql_fetch_object($executeSql1))
	{
		
		if(!in_array($row->imageId,$imID))
		{
		$b=array();
		$b['user_id']=$row->userId;
		$b['vehicle_id']=$row->vehicleId;
		$b['vehicle_name']=$row->vehicleName;
		$b['image_url']=$row->imageUrl;
		$b['image_id']=$row->imageId;
		$b['price']=$row->price;
		$b['vehicle_no']=$row->vehicleNo;
		array_push($a,$b);
		}		
		
	}
	//echo json_encode($a);
	$response=array();
	$response['status']="successful";
	$response['count']=count($a);
	$response['result']=$a;
	echo json_encode($response);
}
else
{
	$response=array();
	$response['status']="error";
	$response['message']="Bad request";
	echo json_encode($reponse);

}
?>
