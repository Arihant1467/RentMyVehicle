<?php
if($_SERVER['REQUEST_METHOD']=='POST')
{
	if(isset($_FILES['image']))
	{
		include("connection.php");
		$vehicleNo=$_POST['vehicle_no'];
		$licenseNo=$_POST['license_no'];
		$vehicleName=$_POST['vehicle_name'];
		$city=$_POST['city'];
		$segment=$_POST['segment'];
		$email=$_POST['email'];
		$aadhar=$_POST['aadhar'];
		$price=$_POST['price'];
		$sql1="SELECT userId,name FROM userregistration WHERE email='$email';";
		$row=mysql_fetch_object(mysql_query($sql1));// to acquire userId of the user
		$userId=$row->userId;
		$userName=$row->name;
		$sql2="SELECT vehicleNo FROM vehicleregistration WHERE vehicleNo='$vehicleNo';";
		$exe_sql2=mysql_query($sql2);// to check if vehicle is registered or not
		if(mysql_num_rows($exe_sql2)==0)
		{
		$sql3="INSERT INTO vehicleregistration(userID,segment,vehicleNo,licenseNo,vehicleName,aadhar,city,PRICE) VALUES 
			('$userId','$segment','$vehicleNo','$licenseNo','$vehicleName',$aadhar,'$city','$price')";
			$exe_sql3=mysql_query($sql3);
			if(mysql_affected_rows()>0)
			{
				
		 		$upload_path = getcwd().'/';//upload folder 
		 		$server_ip = 'https://rent-my-vehicle.herokuapp.com/';// url address   
		 		$upload_url = $server_ip;// url address of the image 
		 		$name=intval(time());//timestamp
		 		$fileinfo=pathinfo($_FILES['image']['name']);// data
		 		$extension = $fileinfo['extension'];// correct extension for encoding
		 		$file_url = $upload_url .$name . '.' . $extension; // url of the file  
 				$file_path = $upload_path .$name. '.'. $extension; //file path to upload in the server where it is moved
 				$sql4="SELECT vehicleId FROM vehicleregistration WHERE vehicleNo='$vehicleNo';";
 				$row=mysql_fetch_object(mysql_query($sql4));
 				$vehicleId=$row->vehicleId;
 				$sql5="INSERT INTO vehicleimage(vehicleId,imageUrl,imageFile) VALUES('$vehicleId','$file_url','$file_path');";
 				mysql_query($sql5);// to insert image
 				if(mysql_affected_rows()>0)
 				{
 					try
 					{
	 					move_uploaded_file($_FILES['image']['tmp_name'],$file_path);
	 					$a=array();
	 					sendgrid_email($email,$userName);
						$a['status']="successful";
						$a['message']="Vehicle registered";
						echo json_encode($a);
 					}catch(Exception $e)
 					{
 						//move_uploaded_file($_FILES['image']['tmp_name'],$file_path);
 					$a=array();
					$a['status']="error1";
					$a['message']="File cannot be uploaded";
					mysql_query("DELETE FROM vehicleimage WHERE imageUrl='$file_url';");
					mysql_query("DELETE FROM vehicleregistration WHERE vehicleId='$vehicleId';");
					echo json_encode($a);
 					}
 					
 				}
 				else
 				{
 					$a=array();
					$a['status']="error2";
					$a['message']=mysql_error();
					//mysql_query("DELETE FROM vehicleimage WHERE imageUrl='$file_url';");
					echo json_encode($a);
 				}	
			}
			else
			{
				$a=array();
				$a['status']="error3";
				$a['message']=mysql_error();
				echo json_encode($a);
			}

		}
		else
		{
			$a=array();
			$a['status']="error4";
			$a['message']="Vehicle already registered";
			echo json_encode($a);
		}
	}
	else
	{
			$a['status']="error5";
			$a['message']="Request not satisfied";
			echo json_encode($a);
	}
		
}
else
{
	$a=array();
	$a['status']="error6";
	$a['message']="Bad Request";
	echo json_encode($a);
}

function sendgrid_email($to,$name)
{
$from = new SendGrid\Email("Rent-A-Vehicle","rent-my-vehicle@herokuapp.com");
$subject = "New Vehicle Registration";
$to = new SendGrid\Email($name,$to);
$msg=file_get_contents("vehicle-registration-email/index.html");
$content = new SendGrid\Content("text/html",$msg);
$mail = new SendGrid\Mail($from, $subject, $to, $content);

$apiKey = getenv('SENDGRID_API_KEY');
$sg = new \SendGrid($apiKey);
$response = $sg->client->mail()->send()->post($mail);

}
?>