#!/usr/bin/env python

"""
A simple echo client
"""

import socket
import sys
import time


host = '192.168.0.110'
port = 50000
size = 1024

print "Movement\nu i o \nj k l \nm , .\nspeed q/z\nlinear speed w/x\nangular speed e/c\nSTOP-> SPACE KEY/K"
prev = ''


class Client(object):
    def __init__(self):
        self.prev = ""

    def send(self, c):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((host, port))
        s.send(c)
        data = s.recv(1)
        # print 'Received:', data
        s.close()

    def run(self):
        while True:
            c = sys.stdin.read(1)
            if c != '\n':
                if self.prev == c or self.prev == '':
                    self.send(c)
                else:
                    for i in range(22):
                        self.send(c)
                self.prev = c

    def run(self, c):
        if c != '\n':
            if self.prev == c or self.prev == '':
                self.send(c)
            else:
                for i in range(22):
                    self.send(c)
            self.prev = c

