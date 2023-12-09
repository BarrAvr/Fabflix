# General

#### Team #49 (based on Google Sheet):
    
#### Names: Mingjia Wang, Barr Avrahamov
    
#### Project 5 Video Demo Link: [https://drive.google.com/file/d/17iLn5o-xdSNnuAISpivErR0__ebgROlT/view?usp=sharing](https://drive.google.com/file/d/17iLn5o-xdSNnuAISpivErR0__ebgROlT/view?usp=sharing)

#### Instruction of deployment:

#### Collaborations and Work Distribution:
- Collaboration: Both of us worked on Tasks 2 & 3.
- Distribution: Barr did Task 1, Mingjia did Task 4


# Connection Pooling
#### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
/WebContent/META-INF/context.xml sets up the connection pooling configuration, and the SingleStarServlet, SingleMovieServlet, SearchServlet, PrefixesDisplayServlet, PaymentServlet, LoginServlet, EmployeeLoginServlet, GenreDisplayServlet, idToTitleServlet, SearchCountServlet, and AndroidLoginServlet servlets use PreparedStatements and JDBC connection pooling.
    
#### Explain how Connection Pooling is utilized in the Fabflix code, and how Connection Pooling works with two backend SQL:
Connection pooling is a mechanism that helps manage and reuse database connections to reduce the overhead caused by opening and closing new connections which reduces average run time. Two backend servers can be configured to share a connection pool of database connections that both servers can efficiently reuse. 

In this particular case, one of the servers is the primary/master and the second is the secondary/slave. The primary server returns reads and does insertions into the database while the secondary only does reads. If the primary inserts into the database that change gets replicated over to the secondary. The load balancer sends reads and inserts over to the appropriate primary/secondary instance.

# Master/Slave
#### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

/etc/apache2/sites-enabled/000-default.conf (only available on AWS.)

#### How read/write requests were routed to Master/Slave SQL?

We followed the instructions under Task 3 and modeled our configuration after the session example to set up a Proxy named "Fabflix_balancer" to act as a load balancer and added new rules in the ody of the VirtualHost tag.

This meant that all http requests that arrived at the load balancer instance's public IP on port 80 would be handled by the load balancer and redirected to either the master or slave SQL server, depending on which one was less busy at that moment. Load balancing prevents any one instance from getting overwhelmed by requests and allows for more availability to handle incoming queries. The provided examples and load balancer config files correctly routed the read/write requests to the appropriate Master or Slave SQL server, so we modeled our code off of them and let the Proxy setup in the apache2 config file handle the routing appropriately.
    
# JMeter TS/TJ Time Logs
#### Instructions of how to use the `log_processing.*` script to process the JMeter logs:
Run the command "python3 log_processing.py" followed by one or more arguments (where each argument is a file you would like to process).


# JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![/img/case-1.png](/img/case-1.png)   | 274                     | 86.69257212240184                   | 7.879062762124712         | Average query time is relatively fast as 1 user isn't enough to overwhelm the server.           |
| Case 2: HTTP/10 threads                        | ![/img/case-2.png](/img/case-2.png)   | 316                         | 108.26929885287994                                  | 17.72411082165163                        | Average query time slower than case 1 as expected, because a temfold increase in traffic is a lot for a single instance w/o optimizations.   |
| Case 3: HTTPS/10 threads                       | ![/img/case-3.png](/img/case-3.png)   | 413                         | 20.754675614134275            | 15.781300658303886                         | Avg query time is far slower than in cases 1 & 2 due to the encryption and certificate validation time needed to make HTTPS work.           |
| Case 4: HTTP/10 threads/No connection pooling  | ![/img/case-4.png](/img/case-4.png)   | 336                         | 104.10815614904246                                  | 21.11881252775465                        | There is an increase in response time compared to case 2 in the JDBC time column because getConnection() takes longer w/o pooling.           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![/img/case-5.png](/img/case-5.png)   | 266                         | 11.256315042156862                                  | 9.071428688235294                        | This case is identical to case 1 in terms of setup but avg query time is much faster because load balancer increases SQL server availability for better response time to reads           |
| Case 2: HTTP/10 threads                        | ![/img/case-6.png](/img/case-6.png)   | 266                         | 13.189380360321145                                  | 11.468891909266782                        | There is very little increase in avg query time here compared to case 5 because the load balancing offsets the jump in users by always choosing the most ideal instance to handle the requests quickly.           |
| Case 3: HTTP/10 threads/No connection pooling  | ![/img/case-7.png](/img/case-7.png)   | 263                         | 14.122508720702367                                  | 12.242715336340707                        | Much faster avg query time than in case 4 due to the advantages load balancing provides as described in cases 5 and 6.           |
