<?php
require_once("database.php");
$orderid=$_REQUEST["orderid"];
$bemail=$_REQUEST["bemail"];
$sql="update orderdetails SET order_status='CANCELED',activity_status='deactive' where bemail='$bemail' AND order_id='$orderid'";
mysql_query($sql,$cn);
$n=mysql_affected_rows();
if($n>0)
{
	echo "CANCELED";
}
else
{
	echo "invalid";
}
?>