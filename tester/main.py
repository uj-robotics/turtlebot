import socket
import sys

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = ('192.168.0.5', 50000)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.bind(server_address)

sock.listen(1)

while True:
    # Wait for a connection
    connection, client_address = sock.accept()
    data = connection.recv(1024)
    print >>sys.stderr, 'received "%s"' % data
    connection.sendall(data)
connection.close()
