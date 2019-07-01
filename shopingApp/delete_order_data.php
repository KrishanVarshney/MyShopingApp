<?php 
require_once("database.php");
$orderid=$_REQUEST["orderid"];
$sql="delete from orderdetails where order_id='$orderid'";
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