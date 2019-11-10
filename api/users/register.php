<?php

include '../include/dbconn.php';


if(isset($_POST['email'])&&isset($_POST['password'])&&isset($_POST['phone'])&&isset($_POST['name'])&&isset($_POST['carnum'])){
    $email = $_REQUEST['email'];
    $pwd = $_REQUEST['password'];
    $name = $_REQUEST['name'];
    $mob = $_REQUEST['phone'];
    $carnum = $_REQUEST['carnum'];

    $query = "INSERT INTO user (user_name, email, phone_no, password, cat_id, carnumber) VALUES ('".$name."', '".$email."', '".$mob."', '".$pwd."', 2,'".$carnum."')";
    $response = array();
	if($conn->query($query)){
		$last_id = $conn->insert_id;
		$query = "INSERT INTO `vehicle_details` (`vehicle_id`, `user_id`, `vehicle_name`) VALUES (NULL, '".$last_id."', '".$carnum."');";
		$response = array();
		if($conn->query($query)){
			$response['status']=1;
			echo json_encode($response);
		}else{
			$response['status']=2;
			echo json_encode($response);

		}
    }else{
        $response['status']=2;
        echo json_encode($response);

    }



}