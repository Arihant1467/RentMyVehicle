<?php
if($_SERVER['REQUEST_METHOD']=="POST")
{
	include("connection.php");
	$email=$_POST['email'];
	$password=$_POST['password'];
	$sqlRetrieve="SELECT email,password FROM userregistration WHERE email='$email' AND password='$password';";
	mysql_query($sqlRetrieve);
	if(mysql_affected_rows()>0)
	{
		$a=array();
		$a['status']="successfull";
		$a['message']="valid user";
		echo json_encode($a);
	}
	else
	{
		$a=array();
		$a['status']="error";
		$a['message']=mysql_error();
		echo json_encode($a);
	}
}
else
{
	$a=array();
	$a['status']="error";
	$a['message']="Bad request";
	echo json_encode($a);
}

?>