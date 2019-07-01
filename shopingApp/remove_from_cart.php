<?php 
require_once("database.php");
$title=$_REQUEST["title"];
$bemail=$_POST["bemail"];
$sql="delete from cart where bemail='$bemail' AND title='$title'";
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