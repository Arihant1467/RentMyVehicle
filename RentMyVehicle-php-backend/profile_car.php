<?php
if($_SERVER['REQUEST_METHOD']=="POST")
{
	$email=$_POST['email'];
	$sql1="SELECT userId FROM userregistration WHERE email='$email';";
	$executeSql1=mysql_query($sql1);
	if(mysql_affected_rows()>0)
	{
		$row=mysql_fetch_object($executeSql1);
		$userId=$row->userId;
		
	}
	else
	{
		$response=array();
		$response['status']="error";
		$response['message']="There was an error in retrieving your info";
		echo json_encode($response);	
	}
}
else
{
	$response=array();
	$response['status']="error";
	$response['message']="Internal server error";
	echo json_encode($response);
}
?>