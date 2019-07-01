<?php 
$cn=mysql_connect("localhost","root","");
if(!$cn)
{
	echo "unable to connect";
	die();
}
$db=mysql_select_db("shoping_app",$cn);
if(!$db)
{
	echo "database does not exist";
	die();
}
?>