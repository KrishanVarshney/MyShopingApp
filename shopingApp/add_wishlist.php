<?php 
require_once("database.php");
$title=$_REQUEST["title"];
$price=$_REQUEST["price"];
$semail=$_POST["semail"];
$bemail=$_POST["bemail"];
$details=$_POST["details"];
$sql="insert into wishlist values('$title','$price','$details','$semail','$bemail')";
mysql_query($sql,$cn);
$n=mysql_affected_rows();

if($n==1 )
{
	 echo "Successfull";
}
else
{
	echo "Error : Try again";
}
?>