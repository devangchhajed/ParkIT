<?php include "commonheader.php"; 
  include "include/dbconn.php";
?>
    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-12 order-xl-1">
          <div class="card bg-secondary shadow">
            <div class="card-header bg-white border-0">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">Add Operator</h3>
                </div>
              </div>
            </div>
            <div class="card-body">
              <form method="POST" action="addoperator.php">
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="input-first-name">Email</label>
                        <input type="email" id="input-first-name" class="form-control form-control-alternative" placeholder="Email" name="op_name" required>
                      </div>
                    </div>
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="input-last-name">Password</label>
                        <input type="text" id="input-last-name" class="form-control form-control-alternative" placeholder="Password" name="op_pwd" required>
                      </div>
                    </div>
                  </div>
                </div>


                  <div class="col-4 text-right">
                      <input type="submit" class="btn btn-sm btn-primary" value="Save"/>
                  </div>

              </form>
            </div>
          </div>
        </div>
<?php
  if(isset($_POST['op_name'])){
    $name = $_REQUEST['op_name'];
    $pwd = $_REQUEST['op_pwd'];
    $query = "INSERT INTO user (email, password, cat_id) VALUES ('".$name."', '".$pwd."', 4)";
    $result2 = $conn->query($query);
    
    //echo "<script>alert(\"Works\")";
    if($result2){
      $res = $conn->query("SELECT user_id FROM user WHERE email = '".$name."' AND password = '".$pwd."'");
      $r = mysqli_fetch_assoc($res);
      $last_id = $r['user_id'];
      $sql = "SELECT parking_lot_id FROM parking_lot_details WHERE user_id = ".$_SESSION['user_id'];
      $result = $conn->query($sql);
      $row = mysqli_fetch_assoc($result);
      var_dump($_SESSION['user_id']);
      $pid = $row['parking_lot_id'];
      $sql = "INSERT INTO parking_lot_operator (user_id, parking_lot_id) VALUES(".$last_id.", ".$pid.")";
      $result = $conn->query($sql);
      if($result){
        echo "<script>alert(\"Registered Successfully...!!!\")";
      }else{
        $conn->query("DELETE FROM user WHERE user_id = ".$last_id);
      }
    }else{
      echo mysqli_error($conn);
    }
  }
?>

<?php include "commonfooter.php"; ?>