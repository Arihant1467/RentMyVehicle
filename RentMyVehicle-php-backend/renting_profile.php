<?php
if($_SERVER["REQUEST_METHOD"]=="POST")
{
	include("connection.php");
	$email=$_POST['email'];
	$sql1="SELECT userId,name,contact FROM userregistration WHERE email='$email' LIMIT 1;";
	$executeSql1=mysql_query($sql1);
	$row=mysql_fetch_object($executeSql1);
	$userId=$row->userId;
	$name=$row->name;
	$contact=$row->contact;
	$sql2="SELECT a.vehicleId,a.vehicleName,a.vehicleNo,a.segment,
			b.imageId,b.imageUrl,b.booking 
			FROM vehicleregistration a,vehicleimage b WHERE a.userId='$userId' AND b.vehicleId=a.vehicleId;";
	$executeSql2=mysql_query($sql2);
	if(mysql_affected_rows()>0)
	{
		$a=array();
		while($row=mysql_fetch_object($executeSql2))
		{
			$b=array();
			$b['vehicle_id']=$row->vehicleId;
			$b['vehicle_name']=$row->vehicleName;
			$b['vehicle_no']=$row->vehicleNo;
			$b['image_id']=$row->imageId;
			$b['image_url']=$row->imageUrl;
			$b['booking']=$row->booking;
			$b['segment']=$row->segment;
			$b['email']=$email;
			array_push($a,$b);
		}
		$response=array();
		$response['status']="successful";
		$response["message"]="Your history has been successfully retreived";
		$response['count']=count($a);
		$response['vehicles']=$a;
		echo json_encode($response);
	}
	else
	{
		$response=array();
    	$response['status']='Error';
   		$response['message']="Sorry there was nothing to retrieve for you";
   		echo json_encode($response);
	}
	
}
else
{
	$response=array();
    $response['status']='Error';
   	$response['message']="Bad request";
   	echo json_encode($response);
}
?>