<?php 
require_once("database.php");
$semail=$_REQUEST["semail"];
$astatus="active";
$sql="select * from orderdetails where semail='$semail' AND activity_status='$astatus'";
$result=mysql_query($sql,$cn);
$n=mysql_num_rows($result);
if($n>0)
{
	$rows=array();
	while($rw=mysql_fetch_assoc($result))
	{
		$rows[]=$rw;
	}
	print json_encode($rows);
}
?>