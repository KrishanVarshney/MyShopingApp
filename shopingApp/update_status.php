<?php
require_once("database.php");
$orderid=$_REQUEST["orderid"];
$orderstatus=$_REQUEST["orderstatus"];
if($orderstatus=="CANCELED" || $orderstatus=="Delivered")
{
	$activitystatus="deactive";
}
else
{
	$activitystatus="active";
}
$sql="update orderdetails SET order_status='$orderstatus',activity_status='$activitystatus' where order_id='$orderid'";
mysql_query($sql,$cn);
$n=mysql_affected_rows();
if($n>0)
{
	echo "Updated";
}
else
{
	echo "invalid";
}
?>