<?php

include '../include/dbconn.php';


if(isset($_POST['email'])&&isset($_POST['password'])&&isset($_POST['phone'])&&isset($_POST['name'])){
    $email = $_REQUEST['email'];
    $pwd = $_REQUEST['password'];
    $name = $_REQUEST['name'];
    $mob = $_REQUEST['phone'];

    $query = "INSERT INTO user (user_name, email, phone_no, password, cat_id) VALUES ('".$name."', '".$email."', '".$mob."', '".$pwd."', 2)";
    $response = array();
    if($conn->query($query)){
        $response['status']=1;
        echo json_encode($response);
    }else{
        $response['status']=2;
        echo json_encode($response);

    }



}