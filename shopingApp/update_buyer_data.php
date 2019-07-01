<?php
require_once("database.php");
$name=$_REQUEST["name"];
$mob=$_REQUEST["mob"];
$email=$_REQUEST["email"];
$sql="update buyerdata SET name='$name',mob='$mob' where email='$email' ";
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