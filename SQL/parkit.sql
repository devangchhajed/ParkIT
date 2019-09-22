-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 22, 2019 at 09:29 AM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parkit`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `booking_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `parking_lot_id` int(11) NOT NULL,
  `app_check_in` timestamp NOT NULL DEFAULT current_timestamp(),
  `app_check_out` text DEFAULT NULL,
  `check_in_time` datetime DEFAULT NULL,
  `check_out_time` datetime DEFAULT NULL,
  `booking_status` text NOT NULL,
  `payment_status` text NOT NULL,
  `booking_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`booking_id`, `user_id`, `vehicle_id`, `parking_lot_id`, `app_check_in`, `app_check_out`, `check_in_time`, `check_out_time`, `booking_status`, `payment_status`, `booking_date`) VALUES
(15, 12, 1, 3, '0000-00-00 00:00:00', NULL, '2019-09-22 11:53:48', '2019-09-22 11:54:15', 'Booked', 'Cash', NULL),
(17, 12, 1, 1, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(18, 12, 1, 1, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(19, 12, 1, 1, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(20, 12, 1, 1, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(21, 12, 1, 1, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(22, 12, 1, 1, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(23, 35, 1, 9, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(24, 35, 1, 9, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(25, 35, 1, 6, '0000-00-00 00:00:00', NULL, NULL, NULL, 'Booked', 'Cash', NULL),
(26, 35, 1, 3, '2019-09-22 06:20:14', NULL, '2019-09-22 11:53:48', '2019-09-22 11:54:15', 'Booked', 'Cash', NULL),
(27, 35, 1, 3, '2019-09-22 06:23:09', NULL, '2019-09-22 11:53:48', '2019-09-22 11:54:15', 'Booked', 'Cash', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `complaints`
--

CREATE TABLE `complaints` (
  `complaint_id` int(11) NOT NULL,
  `booking_id` int(11) NOT NULL,
  `reporter_id` int(11) NOT NULL,
  `reportee_id` int(11) NOT NULL,
  `comment` text NOT NULL,
  `decision` text NOT NULL,
  `status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `feedback_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `parking_lot_id` int(11) NOT NULL,
  `rating` text NOT NULL,
  `comment` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `parking_lot_details`
--

CREATE TABLE `parking_lot_details` (
  `parking_lot_id` int(11) NOT NULL,
  `parking_lot_name` text NOT NULL,
  `parking_lot_address` text NOT NULL,
  `parking_lot_latitude` text NOT NULL,
  `parking_lot_longitude` text NOT NULL,
  `ratings` text NOT NULL,
  `comments` text NOT NULL,
  `total_general` text NOT NULL,
  `total_differently_abled` text NOT NULL,
  `current_general` text NOT NULL,
  `current_differently_abled` text NOT NULL,
  `price_per_hour` text NOT NULL,
  `available` text NOT NULL,
  `valet_available` text NOT NULL DEFAULT 'No',
  `cctv_available` text NOT NULL DEFAULT 'No',
  `count` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parking_lot_details`
--

INSERT INTO `parking_lot_details` (`parking_lot_id`, `parking_lot_name`, `parking_lot_address`, `parking_lot_latitude`, `parking_lot_longitude`, `ratings`, `comments`, `total_general`, `total_differently_abled`, `current_general`, `current_differently_abled`, `price_per_hour`, `available`, `valet_available`, `cctv_available`, `count`, `user_id`) VALUES
(1, 'ABC PArking Solutions', 'Near Nagarro', '28.50167', '77.0742996', '4.5', 'Good', '100', '10', '100', '5', '10', 'Yes', '\'No\'', 'No', 0, 9),
(3, 'Pqr', 'Marine Drives, Mumbai', '19.090517770876716', '72.89470362447501', '', '', '100', '10', '100', '10', '10', 'True', 'True', 'True', 0, 4),
(4, 'Mehralui', 'Mehrauli-Gurgaon Rd, Indian Airlines Pilots Society, Sushant Lok Phase I, Sector 28, Gurugram, Haryana 122002', '28.626754669859', '76.59709620260001', '', '', '100', '20', '100', '20', '20', 'True', 'False', 'True', 0, 14),
(5, 'Kabra Parking Lot', 'random address', '19.119155', '72.828505', '', '', '100', '20', '100', '20', '5', 'True', 'False', 'False', 0, 15),
(6, 'White House', 'White House Residency, Gurugram', '28.442150', '77.057170', '3.5', 'NA', '100', '50', '100', '50', '10', '\'Yes\'', '\'No\'', '\'No\'', 0, 9),
(7, 'Azad Nagar', 'Azad Nagar Metro, Andheri(W), Mumbai', '19.127230', '72.838520', '4.8', 'Nice', '100', '25', '100', '25', '20', '\'Yes\'', '\'No\'', '\'No\'', 0, 4),
(8, 'IFFCO', 'IFFCO Chowk Metro Station', '28.486220', '72.838620', '4.5', 'NA', '100', '23', '100', '23', '23', '\'Yes\'', '\'No\'', '\'No\'', 0, 12),
(9, 'Versoa', 'Versoa Metro Station', '19.134199', '72.856880', '3.8', 'Fair', '100', '10', '100', '10', '10', '\'Yes\'', '\'No\'', '\'No\'', 0, 15);

-- --------------------------------------------------------

--
-- Table structure for table `parking_lot_offers`
--

CREATE TABLE `parking_lot_offers` (
  `offer_id` int(11) NOT NULL,
  `percent_discount` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `parking_lot_operator`
--

CREATE TABLE `parking_lot_operator` (
  `operator_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `parking_lot_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parking_lot_operator`
--

INSERT INTO `parking_lot_operator` (`operator_id`, `user_id`, `parking_lot_id`) VALUES
(2, 17, 3),
(4, 34, 3);

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `booking_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `rewards`
--

CREATE TABLE `rewards` (
  `reward_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `points` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `rules`
--

CREATE TABLE `rules` (
  `rule_id` int(11) NOT NULL,
  `rule` text NOT NULL,
  `reward_points` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `user_name` text NOT NULL,
  `phone_no` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `cat_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_name`, `phone_no`, `email`, `password`, `cat_id`) VALUES
(4, 'Devang', '9887722857', 'devang.chhajed@spit.ac.in', '123', 3),
(5, 'Devang', '9887722857', 'devang.chhajed@spit.ac.in', '123', 3),
(6, 'Devang', '9887722857', 'devang.chhajed@spit.ac.in', '123', 3),
(7, 'Devang', '9887722857', 'devang.chhajed@spit.ac.in', '123', 3),
(8, 'Devang', '9887722857', 'devang.chhajed@spit.ac.in', '123', 3),
(9, 'Devang', '9887722857', 'devang.chhajed@spit.ac.in', '123', 3),
(10, 'Test1', '123', 'test1@t.com', '123', 2),
(12, 'Aakash Niwane', '8087055149', 'aakash.niwane9@gmail.com', '1234', 2),
(14, 'Aakash', '9876543210', 'a@hi2.in', '123', 3),
(15, 'Akshay', '9087564312', 'ak@hi2.in', '123', 3),
(34, '', '', 'def@p2.in', '123', 4),
(35, 'abc', '9876543120', 'abc', '123', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_category`
--

CREATE TABLE `user_category` (
  `cat_id` int(11) NOT NULL,
  `cat_name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_category`
--

INSERT INTO `user_category` (`cat_id`, `cat_name`) VALUES
(1, 'Admin'),
(2, 'Vehicle Owner'),
(3, 'Parking Lot Owner'),
(4, 'Pay And Park Operator');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle_details`
--

CREATE TABLE `vehicle_details` (
  `vehicle_id` int(11) NOT NULL,
  `vechicle_type` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `vehicle_name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `watchlist`
--

CREATE TABLE `watchlist` (
  `watchlist_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `parking_lot_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`booking_id`);

--
-- Indexes for table `complaints`
--
ALTER TABLE `complaints`
  ADD PRIMARY KEY (`complaint_id`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`);

--
-- Indexes for table `parking_lot_details`
--
ALTER TABLE `parking_lot_details`
  ADD PRIMARY KEY (`parking_lot_id`);

--
-- Indexes for table `parking_lot_offers`
--
ALTER TABLE `parking_lot_offers`
  ADD PRIMARY KEY (`offer_id`);

--
-- Indexes for table `parking_lot_operator`
--
ALTER TABLE `parking_lot_operator`
  ADD PRIMARY KEY (`operator_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`);

--
-- Indexes for table `rewards`
--
ALTER TABLE `rewards`
  ADD PRIMARY KEY (`reward_id`);

--
-- Indexes for table `rules`
--
ALTER TABLE `rules`
  ADD PRIMARY KEY (`rule_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `user_category`
--
ALTER TABLE `user_category`
  ADD PRIMARY KEY (`cat_id`);

--
-- Indexes for table `vehicle_details`
--
ALTER TABLE `vehicle_details`
  ADD PRIMARY KEY (`vehicle_id`);

--
-- Indexes for table `watchlist`
--
ALTER TABLE `watchlist`
  ADD PRIMARY KEY (`watchlist_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `complaints`
--
ALTER TABLE `complaints`
  MODIFY `complaint_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `parking_lot_details`
--
ALTER TABLE `parking_lot_details`
  MODIFY `parking_lot_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `parking_lot_offers`
--
ALTER TABLE `parking_lot_offers`
  MODIFY `offer_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `parking_lot_operator`
--
ALTER TABLE `parking_lot_operator`
  MODIFY `operator_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rewards`
--
ALTER TABLE `rewards`
  MODIFY `reward_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rules`
--
ALTER TABLE `rules`
  MODIFY `rule_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `user_category`
--
ALTER TABLE `user_category`
  MODIFY `cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `vehicle_details`
--
ALTER TABLE `vehicle_details`
  MODIFY `vehicle_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `watchlist`
--
ALTER TABLE `watchlist`
  MODIFY `watchlist_id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
