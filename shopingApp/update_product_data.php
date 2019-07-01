<?php
require_once("database.php");
$title=$_REQUEST["title"];
$newtitle=$_REQUEST["newtitle"];
$price=$_REQUEST["price"];
$details=$_REQUEST["details"];
$email=$_REQUEST["email"];
$sql="update productdata SET title='$newtitle',price='$price',details='$details' where email='$email' AND title='$title'";
mysql_query($sql,$cn);
$n=mysql_affected_rows();
if($n>0)
{
	echo "Update successfull";
}
else
{
	echo "invalid";
}
?>