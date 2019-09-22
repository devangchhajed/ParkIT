<?php

include '../include/dbconn.php';


if(isset($_POST['email'])&&isset($_POST['password'])){
    $email = $_REQUEST['email'];
    $pwd = $_REQUEST['password'];
    $query = "SELECT * FROM  user WHERE email= '".$email."' AND password='".$pwd."'";
    $result = $conn->query($query);
    $response = array();
    if($result->num_rows>0){
        $row = mysqli_fetch_row($result);

        $response['status']=1;
        $response['user_id']=$row[0];
        $response['user_name']=$row[1];
        $response['phone_no']=$row[2];
        echo json_encode($response);
    }else{
        $response['status']=2;
        $response['error']="Invalid Cred";
        echo json_encode($response);
    }



}