<?php
if($_SERVER['REQUEST_METHOD']=="POST")
{
	include("connection.php");
	$imageId=$_POST['image_id'];
	$booking=intval($_POST['booking']);
	$sql1="UPDATE vehicleimage SET booking='$booking' WHERE imageId='$imageId';";
	$executeSql1=mysql_query($sql1);
	if(mysql_affected_rows()>0)
	{
		$response=array();
		$response['status']="successful";
		$response['message']="Updated successfully";
		echo json_encode($response);
	}
	else
	{
		$response=array();
		$response['status']="error";
		$response['message']="Request could not be satisfied";
		echo json_encode($response);
	}
}
else
{
	$response=array();
	$response['status']="error";
	$response['message']="Bad Request";
	echo json_encode($response);
}
?>
