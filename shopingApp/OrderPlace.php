<?php 
require_once("database.php");
$name=$_REQUEST["name"];
$address=$_REQUEST["adress"];
$semail=$_POST["semail"];
$bemail=$_POST["bemail"];
$title=$_POST["title"];
$mob=$_POST["mob"];
$order_status="Preparing for Dispatch";
$activity="active";
$sql="insert into orderdetails values(0,'$name','$address','$mob','$semail','$bemail','$order_status','$activity','$title')";
mysql_query($sql,$cn);
$n=mysql_affected_rows();

if($n==1 )
{
	 echo "Order Placed";
}
else
{
	echo "Error : Try again";
}
?>