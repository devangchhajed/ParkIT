<?php include "commonheader.php";
include "include/dbconn.php";
?>

<?php
  $sql = "SELECT parking_lot_id FROM parking_lot_operator WHERE user_id = ".$_SESSION['user_id'];
  $result = $conn->query($sql);
  $row = mysqli_fetch_assoc($result);
  $pid = $row['parking_lot_id'];
  $sql = "SELECT current_general, current_differently_abled FROM parking_lot_details WHERE parking_lot_id = ".$pid;
  $result = $conn->query($sql);
  $row = mysqli_fetch_assoc($result);
  $gen_count = $row['current_general'];
  $da_count = $row['current_differently_abled'];
?>
    <div class="container-fluid mt--7">
      <!-- Table -->
      <div class="row">
        <div class="col">
          <div class="card shadow">
            <div class="card-header border-0">
              <h3 class="mb-0">Check-Out (Remaining Space: <?php echo $gen_count; ?>x)</h3>
            </div>
            <div class="table-responsive">
              <table class="table align-items-center table-flush">
                <thead class="thead-light">
                  <tr>
                    <th scope="col">Car Owner Name</th>
                    <th scope="col">Contact Number</th>
                    <th scope="col">Email-ID</th>
                    <th scope="col">Check-Out Status</th>
                    <th scope="col"></th>
                  </tr>
                </thead>
                <tbody>
                <?php
                    $sql = "SELECT parking_lot_id FROM parking_lot_operator WHERE user_id = ".$_SESSION['user_id'];
                    $result = $conn->query($sql);
                    $row = mysqli_fetch_assoc($result);
                    $pid = $row['parking_lot_id'];
                    $sql = "SELECT user_id from bookings WHERE check_in_time is not NULL and check_out_time is NULL and parking_lot_id = ".$pid;
                    $result = $conn->query($sql);
                    if($result->num_rows>0){
                      while($r = mysqli_fetch_assoc($result)){
                        $id = $r['user_id'];
                        $sql2 = "SELECT * FROM user WHERE user_id = ".$id;
                        $result2 = $conn->query($sql2);
                        $row2 = mysqli_fetch_assoc($result2);
                        $name = $row2['user_name'];
                        $number = $row2['phone_no'];
                        $email = $row2['email'];
                      ?>
                      
                          <form method="POST" action="checkOutt.php">
                        <tr>
                      <th scope="row">
                        <div class="media align-items-center">
                          <div class="media-body">
                            <?php echo "<span class='mb-0 text-sm'>".$name."</span>"; ?>
                            <input type='hidden' value='<?php echo (isset($id))?$id:'';?>' name="uname">
                          </div>
                        </div>
                      </th>
                      <td>
                      <?php echo $number;?>
                      <input type="hidden" value='<?php echo (isset($pid))?$pid:'';?>' name='pid'>
                      </td>
                      <td>
                        <span class="badge badge-dot mr-4">
                        <input type="hidden" value=<?php $email; ?>>
                        <?php echo $email;?>
                        </span>
                      </td>
                      <td>
                          <input type='submit' class='btn btn-success' value='Check Out'>
                          
                          
                      </td>
                    </tr>
                    </form>
                      <?php
                      }
                      
  
                    }
                ?>
                  
                </tbody>
              </table>
            </div>

          </div>
        </div>
      </div>

    </div>
  </div>
  <!--   Core   -->
  <script src="./assets/js/plugins/jquery/dist/jquery.min.js"></script>
  <script src="./assets/js/plugins/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
  <!--   Optional JS   -->
  <!--   Argon JS   -->
  <script src="./assets/js/argon-dashboard.min.js?v=1.1.0"></script>
  <script src="https://cdn.trackjs.com/agent/v3/latest/t.js"></script>
  <script>
    window.TrackJS &&
      TrackJS.install({
        token: "ee6fab19c5a04ac1a32a645abde4613a",
        application: "argon-dashboard-free"
      });
  </script>
  <?php 
    function ins($conn, $id){
      $t="wrk";
      var_dump($t);
      $sql = "INSERT INTO bookings (check_in_time) VALUES('".$_SERVER['REQUEST_TIME']."') WHERE user_id = ".$id;
      $conn->query($sql);
      echo "<script>window.location.href='checkIn.php'</script>";
    }
    
  ?>
</body>

</html>