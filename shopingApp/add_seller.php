<?php 
require_once("database.php");
$name=$_REQUEST["name"];
$mob=$_REQUEST["mob"];
$email=$_POST["email"];
$usertype="seller";
$password=$_POST["password"];
$sql="insert into sellerdata values('$name','$mob','$email')";
$s2="insert into logindata values('$email','$password','$usertype')";
mysql_query($sql,$cn);
$n=mysql_affected_rows();
mysql_query($s2,$cn);
$m=mysql_affected_rows();
if($n==1 )
{
	if($m==1)
		echo "Data Saved and Login Created";
	else echo "Data Saved";
}
else
{
	if($m==1)	echo "Not Saved and Login created";
	else echo "Error : Try again";
}
?>