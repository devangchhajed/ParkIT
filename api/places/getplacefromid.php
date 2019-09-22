<?php

include '../include/dbconn.php';


if(isset($_GET['pid'])){
    $id = $_GET['pid'];
    $query = "SELECT * FROM parking_lot_details where parking_lot_id = ".$id;
    $result = $conn->query($query);
    $res = array();
    if($result->num_rows>0){
        while($row = $result->fetch_assoc()) {
            $res['status']=11;
            $res['cg']=$row['current_general'];
            $res['tg']=$row['total_general'];
            $res['cd']=$row['current_differently_abled'];
            $res['pr']=$row['price_per_hour'];
            echo json_encode($res);
            break;
        }

    }else{
        $response['status']=2;
        $response['error']="No Result Found";
        echo json_encode($response);
    }



}