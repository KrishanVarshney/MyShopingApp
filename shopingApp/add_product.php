<?php 
require_once("database.php");
$title=$_REQUEST["title"];
$price=$_REQUEST["price"];
$email=$_POST["email"];
$details=$_POST["details"];
$sql="insert into productdata values('$title','$price','$details','$email')";
mysql_query($sql,$cn);
$n=mysql_affected_rows();

if($n==1 )
{
	 echo "Data Saved";
}
else
{
	echo "Error : Try again";
}
?>