package com.practicaljava.lesson22;
// Class EmployeeList displays Employees from the table EMP 
// using JDBC drivers of type 4

import java.sql.*;

class EmployeeList {

  public static void main(String argv[]) {
   Connection conn=null;
   Statement stmt=null;
   ResultSet rs=null;
   PreparedStatement pr_stmt = null;
   

   try {
    // Load the JDBC driver  
	// This can be skipped for Derby, but derbyclient.jar has to be in the CLASSPATH   
    // Class.forName("org.apache.derby.jdbc.ClientDriver");
 
    conn = DriverManager.getConnection( "jdbc:derby://localhost:1527/Ex2;create=true"); 

    // Build an SQL String 
    String sqlQuery = "SELECT * from Employee";
    
    String sqlCreateEmployeeQuery = "create table Employee (" +
    		"EmpNo int not null," +
    		"EName varchar (50) not null," +
    		"JobTitle varchar (150) not null, " +
    		"DeptNo int not null)";
    
    String sqlFillEmployeeQuery = "insert into Employee values " +
    		"(1,'John Smith', 'Clerk', 1), " +
    		"(2,'Joe Allen','Salesman', 1), " +
    		"(3,'Mary Lou','Director', 1), " +
    		"(4,'Robin Williams', 'Clerk', 2), " +
    		"(5,'Will Smith','Salesman', 2), " +
    		"(6,'Morgan Freeman','Salesman', 2), " +
    		"(7,'Eddie Murphy', 'Clerk', 3), " +
    		"(8,'Bruce Willis','Director', 2), " +
    		"(9,'Tom Cruise','Director', 3), " +
    		"(10,'Orlando Bloom','Salesman', 3), " +
    		"(11,'Harrison Ford','Salesman', 1), " +
    		"(12,'Tom Hanks','Salesman', 3)";
		
    // Create a Statement object
    stmt = conn.createStatement(); 

    // Execute SQL and get obtain the ResultSet object
    stmt.execute(sqlCreateEmployeeQuery);
    stmt.execute(sqlFillEmployeeQuery);
    rs = stmt.executeQuery(sqlQuery);  

    System.out.println("----- First Part ------");
    
    // Process the result set - print Employees
    while (rs.next()){ 
    	 int empNo = rs.getInt("EmpNo");
       	 String eName = rs.getString("EName");
         String job = rs.getString("JobTitle");
         String dept = rs.getString("DeptNo");
         System.out.println(""+ empNo + ", " + eName + ", " + job + ", " + dept); 
    }

    
    System.out.println("----- Second Part ------");
    
    pr_stmt = conn.prepareStatement("SELECT * from Employee WHERE EmpNo=?");
    ResultSet pr_rs = null;
    
    int employees[] = {7, 5, 9, 12};   
    
    for (int i = 0; i < employees.length; i++){
    	pr_stmt.setInt(1,employees[i]);
    	pr_rs = pr_stmt.executeQuery();
    
 // Process the result set - print Employees
	    while (pr_rs.next()){ 
	    	 int empNo = pr_rs.getInt("EmpNo");
	       	 String eName = pr_rs.getString("EName");
	         String job = pr_rs.getString("JobTitle");
	         String dept = pr_rs.getString("DeptNo");
		     System.out.println(""+ empNo + ", " + eName + ", " + job + ", " + dept); 
	    }
    }
    
    
    System.out.println("----- Third Part ------");
    
    String sqlCreateDeptQuery = "create table Department (" +
    		"DeptNo int not null," +
    		"DeptName varchar (50) not null," +
    		"AccessLevel varchar (15) not null)";
    
    String sqlFillDeptQuery = "insert into Department values " +
    		"(1, 'Control department', 'High'), " +
    		"(2, 'Department of management', 'Medium'), " +
    		"(3, 'Sales department', 'Low'), ";
    
    String sqlDeleteDeptQuery = "delete from Department where DeptNo = 3";
    String sqlDeleteEmployeeQuery = "delete from Employee where DeptNo = 3";
    
    stmt.execute(sqlCreateDeptQuery);
    stmt.execute(sqlFillDeptQuery);
    
    //removal of sales department
    
    try{ 
    	conn.setAutoCommit(false); 
    	 
    	stmt.execute(sqlDeleteDeptQuery);
    	stmt.execute(sqlDeleteEmployeeQuery); 
    	conn.commit();// Transaction succeeded
    	
    	}catch(SQLException e){ 
    		conn.rollback();// Transaction failed 
    		e.printStackTrace();
    	} 
    
    rs = stmt.executeQuery(sqlQuery);  
    
    while (rs.next()){ 
    	 int empNo = rs.getInt("EmpNo");
       	 String eName = rs.getString("EName");
         String job = rs.getString("JobTitle");
         String dept = rs.getString("DeptNo");
 	     System.out.println(""+ empNo + ", " + eName + ", " + job + ", " + dept); 
    }
    
   } catch( SQLException se ) {
      System.out.println ("SQLError: " + se.getMessage ()
           + " code: " + se.getErrorCode ());

   } catch( Exception e ) {
      System.out.println(e.getMessage()); 
      e.printStackTrace(); 
   } finally{
       // clean up the system resources
       try{
	   rs.close();     
	   stmt.close(); 
	   conn.close();  
       } catch(Exception e){
           e.printStackTrace();
       } 
   }
}
}
