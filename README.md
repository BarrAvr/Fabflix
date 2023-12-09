# General

#### Team #:
    
#### Names:
    
#### Project 5 Video Demo Link: [https://drive.google.com/file/d/1VtulH0Kd2tZyOflrhqeHBs_WfzPNTDoj/view?usp=sharing](https://drive.google.com/file/d/1VtulH0Kd2tZyOflrhqeHBs_WfzPNTDoj/view?usp=sharing)

#### Instruction of deployment:

#### Collaborations and Work Distribution:


# Connection Pooling
#### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    /WebContent/META-INF/context.xml sets up the connection pooling configuration, and the SingleStarServlet, SingleMovieServlet, SearchServlet, PrefixesDisplayServlet, PaymentServlet, LoginServlet, EmployeeLoginServlet, GenreDisplayServlet, idToTitleServlet, SearchCountServlet, and AndroidLoginServlet servlets use PreparedStatements and JDBC connection pooling.
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code, and how Connection Pooling works with two backend SQL:
    Connection pooling is a mechanism that helps manage and reuse database connections to reduce the overhead caused by opening and closing new connections which reduces average run time. Two backend servers can be configured to share a connection pool of database connections that both servers can efficiently reuse. 
    In this particular case, one of the servers is the primary/master and the second is the secondary/slave. The primary server returns reads and does insertions into the database while the secondary only does reads. If the primary inserts into the database that change gets replicated over to the secondary. The load balancer that sends reads and inserts over to the primary/secondary.

# Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

    - #### How read/write requests were routed to Master/Slave SQL?
    
# JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.


# JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![/img/case-1.png](/img/case-1.png)   | 274                     | 86.69257212240184                   | 7.879062762124712         | ??           |
| Case 2: HTTP/10 threads                        | ![/img/case-2.png](/img/case-2.png)   | 316                         | 108.26929885287994                                  | 17.72411082165163                        | ??           |
| Case 3: HTTPS/10 threads                       | ![/img/case-3.png](/img/case-3.png)   | 413                         | 20.754675614134275            |                         | 15.781300658303886           |
| Case 4: HTTP/10 threads/No connection pooling  | ![/img/case-4.png](/img/case-4.png)   | 336                         | 104.10815614904246                                  | 21.11881252775465                        | ??           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![/img/case-5.png](/img/case-5.png)   | 266                         | 11.256315042156862                                  | 9.071428688235294                        | ??           |
| Case 2: HTTP/10 threads                        | ![/img/case-6.png](/img/case-6.png)   | 266                         | 13.189380360321145                                  | 11.468891909266782                        | ??           |
| Case 3: HTTP/10 threads/No connection pooling  | ![/img/case-7.png](/img/case-7.png)   | 263                         | 14.122508720702367                                  | 12.242715336340707                        | ??           |
