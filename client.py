#!/usr/bin/env python

"""
A simple echo client
"""

import socket
import sys

host = '192.168.0.110'
port = 50000
size = 1024
print "Movement\nu i o \nj k l \nm , .\nspeed q/z\nlinear speed w/x\nangular speed e/c\nSTOP-> SPACE KEY/K"

while True:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host,port))
    c = sys.stdin.read(1)
    s.send(c)
    data = s.recv(size)
    s.close()
    print 'Received:', data
