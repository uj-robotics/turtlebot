#!/usr/bin/env python

"""
A simple echo client
"""

import socket

host = '192.168.0.105'
port = 50000
size = 1024
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((host,port))
s.send('u')
data = s.recv(size)
s.close()
print 'Received:', data
