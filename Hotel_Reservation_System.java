package my_Package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Hotel_Reservation_System {
	
	private static final String url ="jdbc:mysql://localhost:3306/hotel_db";
	private static final String username ="root";
	private static final String password ="root";

	public static void main(String[] args) throws Exception {
		 try {
			 Connection connection =DriverManager.getConnection(url, username, password);
			 
			 while(true) {
				System.out.println("===================================");
				 System.out.println("HOTEL MANAGEMENT SYSTEM");	
				 Scanner sc= new Scanner(System.in);
				 System.out.println("1. Reserve a room");
	             System.out.println("2. View Reservations");
	             System.out.println("3. Get Room Number");
	             System.out.println("4. Update Reservations");
	             System.out.println("5. Delete Reservations");
	             System.out.println("0. Exit");
	             System.out.print("Choose an option: ");
	             int choice = sc.nextInt();
	             
	             switch(choice) {
	             case 1 :
	            	 reserveRoom(connection,sc);
	            	 break;
	            	 
	             case 2 :
	            	 viewReservations(connection);
	            	 break;
	            	 
	             case 3 :
	            	 getRoomNumber(connection,sc);
	            	 break;
	            	 
	             case 4 :
	            	 updateReservation(connection,sc);
	            	 break;
	            	 
	             case 5 :
	            	 deleteReservation(connection,sc);
	            	 break;
	            	 
	             case 0 :
	            	 exit();
	            	 sc.close();
	            	 return;
	            	 
	             default :
	            	 System.out.println("Invalid choice !. Please Try Again.");
	            
	             }
			 }
			 
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	}
		 
		private static void reserveRoom(Connection connection, Scanner sc) {
			
			System.out.println("Enter Guest Name:");
			String guestName=sc.next();
			System.out.println("Enter Room Number:");
			int roomNumber=sc.nextInt();
			System.out.println("Enter Contact Number:");
			String contactNumber=sc.next();
			
			String query="insert into reservations(guest_name,room_no,contact_no)"
					+ "values('"+ guestName +"',"+ roomNumber +",'"+ contactNumber +"')";
			
			try {
				Statement statement = connection.createStatement();
				int affectedRows =statement.executeUpdate(query);
				
				if(affectedRows>0) {
					System.out.println("Reservation Successful");
				}
				else {
					System.out.println("Reservation Failed");

				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
				
		}
		
		
		private static void viewReservations(Connection connection) {
			String query="select * from reservations";
			try {
				Statement statement = connection.createStatement();
				ResultSet rs=statement.executeQuery(query);
				
				System.out.println("Current Reservations:");
				
				while(rs.next()) {
					int reservationId=rs.getInt("reservation_id");
					String guestName=rs.getString("guest_name");
					int roomNo=rs.getInt("room_no");
					String contactNo=rs.getString("contact_no");
					String reservationDate=rs.getTimestamp("reservation_date").toString();
					
					System.out.println("Reservation ID: "+reservationId);
					System.out.println("Guest Name: "+guestName);
					System.out.println("Room No: "+roomNo);
					System.out.println("Contact No: "+contactNo);
					System.out.println("Reservation Date: "+reservationDate);
					System.out.println("***************************");
					
				}
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

		
		private static void getRoomNumber(Connection connection,Scanner sc) {
			
				System.out.println("Enter Reservation ID: ");
				int reservationID=sc.nextInt();
				System.out.println("Enter Guest Name: ");
				String guestName=sc.next();
				
				String query="SELECT room_no FROM reservations " +
	                    "WHERE reservation_id = " + reservationID +
	                    " AND guest_name = '" + guestName + "'";
				
				try {
					Statement statement = connection.createStatement();
					ResultSet rs=statement.executeQuery(query);
					
					if(rs.next()) {
					int roomNum=rs.getInt("room_no");
					System.out.println("RoomNumber for ReservationId: "+reservationID+" and Guest: "
							+guestName+" is: "+roomNum);
					}
					else {
						System.out.println("Reservation Not Found For Given Id and GuestName");
					}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		
		private static void updateReservation(Connection connection,Scanner sc) {
			System.out.println("Enter Reservation ID to Update:");
			int reservationID=sc.nextInt();
			sc.nextLine();
			
			if(! reservationExists(connection,reservationID)) {
				System.out.println("Reservation not found for the given ID");
				return;
			}
			
			System.out.println("Enter New Guest Name:");
			String new_GuestName=sc.next();
			System.out.println("Enter New Room Number:");
			int new_RoomNum=sc.nextInt();
			System.out.println("Enter New Contact Number:");
			String new_ContactNum=sc.next();
			
			String query="UPDATE reservations SET guest_name = '" + new_GuestName + "', " +
                    "room_no = " + new_RoomNum + ", " +
                    "contact_no = '" + new_ContactNum + "' " +
                    "WHERE reservation_id = " + reservationID ;
			
			try {
				Statement statement = connection.createStatement();
				int affectedrow=statement.executeUpdate(query);
				
				if(affectedrow>0) {
					System.out.println("Reservation Updated Successfully");
				}
				else {
					System.out.println("Reservation Update Failed");
				}
			}
			catch(Exception e) {
				System.out.println("hiiiiii");
				System.out.println(e.getMessage());
			}
			
					
		}
		
		
		private static void deleteReservation(Connection connection,Scanner sc) {
			System.out.println("Enter Reservation Id to delete: ");
			int reservationId=sc.nextInt();
			
			if(! reservationExists(connection,reservationId)) {
				System.out.println("Reservation not found for the given ID");
				return;
			}
			
			String query="delete from reservations where reservation_id="+reservationId;
			
			try {
				Statement statement = connection.createStatement();
				int affectedrow=statement.executeUpdate(query);
				
				if(affectedrow>0) {
					System.out.println("Reservation Deleted Successfully");
				}
				else {
					System.out.println("Reservation Deletion Failed");
				}
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

		private static boolean reservationExists(Connection connection, int reservationId) {
			String query="select reservation_id from reservations where reservation_id= "
		    +reservationId;
			
			try {
				Statement statement= connection.createStatement();
				ResultSet rs =statement.executeQuery(query);
				
				return rs.next();
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		
		private static void exit() throws Exception{
			System.out.print("Exiting System");
			
			for(int i=5;i>0;i--) {
				System.out.print(".");
				Thread.sleep(450);
			}
			System.out.println();
			System.err.print("ThankYou For Using Hotel Reservation System");
		}

}

