#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import cv2
import video
import socket
import time


global old_fx
global old_fy
global it
it = 0
host = '192.168.0.107'
port = 50000
size = 1024

cmd_list = [0, 0, 0, 0]

LEFT = 0
RIGHT = 1
HEAD = 2
TAIL = 3

RIGHT_THRESHOLD = 10.0
LEFT_THRESHOLD = -10.0
UP_THRESHOLD = 10.0
DOWN_THRESHOLD = -10.0

MOTION = 4

def send(cmd):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))
    s.send(cmd)
    s.recv(size)
    s.close()


def _process(cmd):
    c = 'k'  # stop
    if cmd == HEAD:
        print "UP"
        c = 'i'
    elif cmd == TAIL:
        print "DOWN"
        c = '.'
    elif cmd == LEFT:
        print "LEFT"
        c = 'j'
    elif cmd == RIGHT:
        print "RIGHT"
        c = 'm'
        # send(c)


def calculate_percentage(list):
    greater = 0
    lower = 0
    for i in list:
        if i < 0.0:
            lower += 1
        elif i > 0.0:
            greater += 1
    statistic = float(greater) / float(len(list))
    statistic1 = float(lower) / float(len(list))
    return statistic, statistic1


def draw_flow(img, flow, step=16):
    h, w = img.shape[:2]  # zwraca rozmiar obrazka
    y, x = np.mgrid[step / 2:h:step, step / 2:w:step].reshape(2, -1)
    fx, fy = flow[y, x].T
    global cmd_list,it
    if (max(fx) > RIGHT_THRESHOLD):
        cmd_list[RIGHT] += 1
    if (min(fx) < LEFT_THRESHOLD):
        cmd_list[LEFT] += 1
    if (max(fy) > UP_THRESHOLD):
        cmd_list[HEAD] += 1
    if (min(fy) < DOWN_THRESHOLD):
        cmd_list[TAIL] += 1

    _max = cmd_list[0]
    index = 0
    for i in range(len(cmd_list)):
        if _max < cmd_list[i]:
            _max = cmd_list[i]
            index = i
    if _max > MOTION:
        #print cmd_list
        _process(index)
        cmd_list = [0,0,0,0]
        #cmd_list[index] = 0
    lines = np.vstack([x, y, x + fx, y + fy]).T.reshape(-1, 2, 2)

    lines = np.int32(lines + 0.5)
    vis = cv2.cvtColor(img, cv2.COLOR_GRAY2BGR)
    cv2.polylines(vis, lines, 0, (0, 255, 0))
    for (x1, y1), (x2, y2) in lines:  # rysowanie kropek
        cv2.circle(vis, (x1, y1), 1, (0, 255, 0), -1)

    it += 1
    return vis


if __name__ == '__main__':
    import sys

    try:
        fn = sys.argv[1]
    except:
        fn = 0

    cam = video.create_capture(fn)
    ret, prev = cam.read()
    prevgray = cv2.cvtColor(prev, cv2.COLOR_BGR2GRAY)
    cur_glitch = prev.copy()
    old_fx = []
    old_fy = []
    step = 1
    while True:
        ret, img = cam.read()
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        #
        # prev – first 8-bit single-channel input image
        # next – second input image of the same size and the same type as prev.
        # flow – computed flow image that has the same size as prev and type CV_32FC2.
        # pyr_scale – parameter, specifying the image scale (<1) to build pyramids for each image; pyr_scale=0.5 means a classical pyramid, where each next layer is twice smaller than the previous one.
        # levels – number of pyramid layers including the initial image; levels=1 means that no extra layers are created and only the original images are used.
        # winsize – averaging window size; larger values increase the algorithm robustness to image noise and give more chances for fast motion detection, but yield more blurred motion field.
        # iterations – number of iterations the algorithm does at each pyramid level.
        # poly_n – size of the pixel neighborhood used to find polynomial expansion in each pixel; larger values mean that the image will be approximated with smoother surfaces, yielding more robust algorithm and more blurred motion field, typically poly_n =5 or 7.
        # poly_sigma – standard deviation of the Gaussian that is used to smooth derivatives used as a basis for the polynomial expansion; for poly_n=5, you can set poly_sigma=1.1, for poly_n=7, a good value would be poly_sigma=1.5.
        # flags –
        flow = cv2.calcOpticalFlowFarneback(prevgray, gray, 0.5, 3, 15, 3, 5, 1.2, 0)
        prevgray = gray
        cv2.imshow('flow', draw_flow(gray, flow))
        if step == 1:
            step += 1
        ch = 0xFF & cv2.waitKey(5)
        if ch == 27:
            break
    # time.sleep(1)
    cv2.destroyAllWindows()
