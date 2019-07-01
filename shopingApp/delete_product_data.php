<?php 
require_once("database.php");
$email=$_REQUEST["email"];
$title=$_REQUEST["title"];
$sql="delete from productdata where email='$email' AND title='$title'";
mysql_query($sql,$cn);
$n=mysql_affected_rows();
if($n==1 )
{
	echo "data deleted";
}
else
{
	 echo "Error : Try again";
}
?>