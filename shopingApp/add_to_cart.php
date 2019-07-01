<?php 
require_once("database.php");
$title=$_REQUEST["title"];
$price=$_REQUEST["price"];
$semail=$_POST["semail"];
$bemail=$_POST["bemail"];
$sql="insert into cart values('$title','$price','$semail','$bemail')";
mysql_query($sql,$cn);
$n=mysql_affected_rows();
if($n==1 )
{
	 echo "Product added in Cart";
}
else
{
	echo "Error : Try again";
}
?>