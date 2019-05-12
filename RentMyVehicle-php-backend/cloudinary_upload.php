<?php

require "vendor/autoload.php";
require "vendor/cloudinary/cloudinary_php/src/Cloudinary.php";
require "vendor/cloudinary/cloudinary_php/src/Uploader.php";
require "vendor/cloudinary/cloudinary_php/src/Api.php";
include("connection.php");
$url=parse_url(getenv("CLOUDINARY_URL"));
$cloud_name=$url["host"];
$api_key=$url["user"];
$api_secret=$url["pass"];
$config=\Cloudinary::config(array( 
  "cloud_name" => $cloud_name, 
  "api_key" => $api_key, 
  "api_secret" => $api_secret 
));

// we will start uploading images to our cloudinary cloud using the api

$sql="SELECT imageId,imageUrl FROM vehicleimage WHERE cloudinary=0 ORDER BY imageId ASC LIMIT 5";
$execute=mysql_query($sql);
if(mysql_affected_rows()>0)
{
	while($row=mysql_fetch_object($execute))
	{
		$imageId=$row->imageId;
		$imageUrl=$row->imageUrl;
		$response=\Cloudinary\Uploader::upload($imageUrl);
		$returnUrl=$response["url"];
		$sql1="UPDATE vehicleimage SET imageUrl='$returnUrl',cloudinary=1 WHERE imageId=$imageId;";
		mysql_query($sql1);

	}
}


?>