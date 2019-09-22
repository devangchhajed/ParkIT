<?php

include '../include/dbconn.php';

    $query = "SELECT *,(0) as  distance FROM parking_lot_details";
    $result = $conn->query($query);
    $res = array();
    if($result->num_rows>0){
        $response = array();
        while($row = $result->fetch_assoc()) {
            array_push($res, $row);
        }

        echo json_encode($res);



}