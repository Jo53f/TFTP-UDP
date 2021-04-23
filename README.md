# TFTP
 TFTP Client and Server
 
It's possible to read data from the server, using the client. The client still can't properly detect when the last packet has been sent, as it still appears to be a fill packet. 

To do list:

Client
- Multi-Packet transfer
- Handle recieving error packets
- Send error packets
- Send files

Server
- Multi-Packet transfer
- Send acknowledgments for packets
- Recieve files
- Handle recieving error packets
