<?php
// If you are using Composer
require 'vendor/autoload.php';

// If you are not using Composer (recommended)
// require("path/to/sendgrid-php/sendgrid-php.php");

//$msg=file_get_contents("email.php");
$name="ARihant";
$contact="1234567890";
//$msg='<!DOCTYPE html>
// <html>
// <head>
// 	<title>HTML</title>
// </head>
// <body>
// <h3 style="color: yellow">Your cab has been booked</h3>
// <p style="color: red;font-family:helvetica ">Yipee! Your cab has been booked and we these are some the credentials which are requeted by you.</p>
// <p style="color: red;font-family:helvetica ">
// Name:'.$name.'
// Contact :'.$contact.'
// </p>
// </body>
// </html>';

// $from = new SendGrid\Email("Arihant", "rent-my-vehicle@herokuapp.com");
// $subject = "Hello World from the SendGrid PHP Library!";
// $to = new SendGrid\Email("User", "arihant95@gmail.com");
// $content = new SendGrid\Content("text/html",$msg);
// $mail = new SendGrid\Mail($from, $subject, $to, $content);

// $apiKey = getenv('SENDGRID_API_KEY');
// $sg = new \SendGrid($apiKey);

// $response = $sg->client->mail()->send()->post($mail);
// echo $response->statusCode();
// echo $response->headers();
// echo $response->body();
// $from = new SendGrid\Email("Rent-A-Vehicle","rent-my-vehicle@herokuapp.com");
// $subject = "New Registration";
// $to = new SendGrid\Email("Arihant","arihant95@gmail.com");
//$msg=file_get_contents("http://localhost/RentMyVehicle/booking-owner-notification/index.php?name=Arihant&contact=1234567890&email=arihant95@gmail&vehicle_no=MP09&vehicle_name=Suzuki");
//echo $msg;

// $content = new SendGrid\Content("text/html",$msg);
// $mail = new SendGrid\Mail($from, $subject, $to, $content);

// $apiKey = getenv('SENDGRID_API_KEY');
// $sg = new \SendGrid($apiKey);
// $response = $sg->client->mail()->send()->post($mail);
$name="Arihant";$contact="9852";$vehicle_no="452";$vehicle_name="111";$to="arihnat95@gmail.com";
$get_request="https://rent-my-vehicle.herokuapp.com/booking-owner-notification/index.php?";
$get_request="http://localhost/RentMyVehicle/booking-owner-notification/index.php?";
$get_request=$get_request."name='$name'&";
$get_request=$get_request."contact='$contact'&";
$get_request=$get_request."email='$to'&";
$get_request=$get_request."vehicle_no='$vehicle_no'&";
$get_request=$get_request."vehicle_name='$vehicle_name'";
echo $get_request;
$msg=file_get_contents($get_request);
echo $msg;
?>