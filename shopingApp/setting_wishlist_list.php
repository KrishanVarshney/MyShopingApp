<?php 
require_once("database.php");
$bemail=$_REQUEST["bemail"];
$sql="select * from wishlist where bemail='$bemail'";
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