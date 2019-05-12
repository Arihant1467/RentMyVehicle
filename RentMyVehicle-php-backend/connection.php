<?php
require 'vendor/autoload.php';
//$url = parse_url("mysql://b9b57cd93508b2:98ec4699@us-cdbr-iron-east-03.cleardb.net/heroku_2aeda49eaf1fda2?reconnect=true");
$url=parse_url(getenv("CLEARDB_DATABASE_URL"));
$server =$url["host"];
$username =$url["user"];
$password = $url["pass"];
$db = substr($url["path"], 1);

$conn=mysql_connect($server,$username,$password);
if($conn)
{
	mysql_select_db($db);
	
}
?>