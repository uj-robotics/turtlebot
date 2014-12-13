#!/usr/bin/env python

"""
A simple echo client
"""

import socket

host = '192.168.0.110'
port = 50000
size = 1024
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((host,port))
s.send('j')
data = s.recv(size)
s.close()
print 'Received:', data
