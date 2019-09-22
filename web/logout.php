<?php include "include/dbconn.php";
session_start();
if (isset($_SESSION['user_id'])) {
    session_destroy();
    echo "<script>alert('Logout Success...!!!');
        window.location.href='login.php'
        </script>";

    exit();
}
?>