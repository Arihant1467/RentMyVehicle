<?php
	if($_SERVER['REQUEST_METHOD']=="POST")
	{	
		include("connection.php");
		// $url = parse_url(getenv("CLEARDB_DATABASE_URL"));
		// $db = substr($url["path"], 1);
		// mysql_select_db($db);
		$name=$_POST['name'];
		$email=$_POST['email'];
		$city=$_POST['city'];
		$mobile=$_POST['mobile'];
		$password=$_POST['password'];
		$sqlInsert="INSERT INTO userregistration(name,email,password,city,contact) VALUES('$name','$email','$password','$city','$mobile');";
		mysql_query($sqlInsert);
		if(mysql_affected_rows()>0)
		{
		
			$sqlRetrieve="SELECT userID FROM userregistration WHERE email='$email';";
			$s=mysql_query($sqlRetrieve);
			if(mysql_num_rows($s)==1)
			{				
				$row=mysql_fetch_object($s);
				$a=array();
				$a['id']=$row->userID;
				sendgrid_email($email,$name);
				$a['status']='successfull';
				$a['message']='Query completed';
				echo json_encode($a);
			}
			else
			{
				$a=array();
				$a['status']="error1";
				$a['message']=mysql_error();
				echo json_encode($a);	
			}
		}
		else
		{
			$a=array();
			$a['status']="error2";
			$a['message']=mysql_error();
			echo json_encode($a);
		}
	}
	else
	{
		$a=array();
		$a['status']="error3";
		$a['message']="Bad request";
		echo json_encode($a);
	}
function sendgrid_email($to,$name)
{
$from = new SendGrid\Email("Rent-A-Vehicle","rent-my-vehicle@herokuapp.com");
$subject = "New Registration";
$to = new SendGrid\Email($name,$to);
$msg=file_get_contents("user-registration-email/index.html");
$content = new SendGrid\Content("text/html",$msg);
$mail = new SendGrid\Mail($from, $subject, $to, $content);

$apiKey = getenv('SENDGRID_API_KEY');
$sg = new \SendGrid($apiKey);
$response = $sg->client->mail()->send()->post($mail);

}	
?>